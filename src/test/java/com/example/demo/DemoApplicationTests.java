package com.example.demo;

import com.alibaba.fastjson.JSON;
import com.example.demo.entity.SingleTableQueryParam;
import com.example.demo.service.ElasticsearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

    @Autowired
    private ElasticsearchService elasticsearchService;

    @Test
    public void contextLoads() {
        SingleTableQueryParam param = new SingleTableQueryParam("student", "中国人", 1, 10);

        param.addFilterCol("level", "3")
             .addSortCols("id", false);


        System.out.println(JSON.toJSONString(elasticsearchService.searchSingleTable(param)));
    }

}
