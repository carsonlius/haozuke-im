package com.carsonlius.haoke.im;

import com.carsonlius.haoke.im.pojo.Member;
import org.apache.commons.lang3.builder.ToStringExclude;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestSpringBootES {
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Test
    public void testSave(){
        Member member = new Member();
        member.setAge(29);
        member.setId(1001L);
        member.setName("张三");
        member.setHobby("足球 篮球 听音乐");
        IndexQuery indexQuery = new IndexQueryBuilder().withObject(member).build();

        String index = this.elasticsearchTemplate.index(indexQuery);

        System.out.println(index);
    }

    @Test
    public void testBulk(){
        List<IndexQuery> list = new ArrayList<>();
        for (int i = 0; i < 5000; i++) {
            Member member = new Member();
            member.setAge(i%50 + 10);
            member.setId(1001L + i);
            member.setName("张三" + i);
            member.setHobby("足球 篮球 听音乐");

            IndexQuery indexQuery = new IndexQueryBuilder().withObject(member).build();
            list.add(indexQuery);
        }

        Long start = System.currentTimeMillis();
        this.elasticsearchTemplate.bulkIndex(list);

        System.out.println("耗时:" + (System.currentTimeMillis() - start));
    }

    @Test
    public void testUpdate(){
        IndexRequest indexRequest = new IndexRequest();
        indexRequest.source("age", 30);

        UpdateQuery updateQuery = new UpdateQueryBuilder()
                .withId("1001")
                .withClass(Member.class)
                .withIndexRequest(indexRequest)
                .build();
        elasticsearchTemplate.update(updateQuery);
    }

    @Test
    public void testDelete(){
        elasticsearchTemplate.delete(Member.class,"1002");
    }

    @Test
    public void testSearch(){
        PageRequest pageRequest = PageRequest.of(1, 10);

        QueryBuilder queryBuilder = QueryBuilders.matchQuery("name", "张三");

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .withPageable(pageRequest)
                .build();

        AggregatedPage<Member> memberAggregatedPage = elasticsearchTemplate
                .queryForPage(searchQuery, Member.class);

        // 总页
        for (Member member : memberAggregatedPage.getContent()) {
            System.out.println(member);
        }
        System.out.println("总页数:" + memberAggregatedPage.getTotalPages());

    }
}
