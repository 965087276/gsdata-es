package com.example.demo.service;

import com.example.demo.entity.SingleTableQueryParam;
import com.example.demo.entity.SingleTableQueryResult;


/**
 * @author wangjinhao
 * @date 2020/4/9 12:14
 */
public interface ElasticsearchService {
    /**
     * 单表查询
     * @param paramBody 待传的参数，见SingleTableQueryParam注释
     * @return
     */
    SingleTableQueryResult searchSingleTable(SingleTableQueryParam paramBody);

}
