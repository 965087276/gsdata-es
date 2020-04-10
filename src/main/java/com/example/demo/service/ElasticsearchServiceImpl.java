package com.example.demo.service;

import cn.hutool.core.util.StrUtil;
import com.example.demo.entity.SingleTableQueryParam;
import com.example.demo.entity.SingleTableQueryResult;
import com.example.demo.utils.ElasticsearchUtils;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author wangjinhao
 * @date 2020/4/9 12:14
 */
@Service
@Slf4j
public class ElasticsearchServiceImpl implements ElasticsearchService {
    @Autowired
    private RestHighLevelClient client;

    /**
     * 单表查询
     *
     * @param paramBody 待传的参数，见SingleTableQueryParam注释
     * @return
     */
    @Override
    public SingleTableQueryResult searchSingleTable(SingleTableQueryParam paramBody) {

        String word = ElasticsearchUtils.wordRegular(paramBody.getWord());

        // 待搜索文本为空字符串，直接返回
        if (StrUtil.isBlank(word)) {
            return SingleTableQueryResult.NO_RESULT;
        }

        // query string查询体
        QueryBuilder queryStringQueryBuilder = this.
                getQueryStringQuery(paramBody.getTableName(), word);

        // filter 查询体
        QueryBuilder filterQueryBuilder = this.getFilterQuery(paramBody.getFilterCols());

        // 主查询体，由query string查询体和filter查询体组成
        BoolQueryBuilder totalQuery = QueryBuilders.boolQuery()
                .must(queryStringQueryBuilder);
        if (filterQueryBuilder != null)
            totalQuery.filter(filterQueryBuilder);

        // 设置结果高亮显示
        HighlightBuilder highLight = new HighlightBuilder()
                // * 表示所有搜索字段都设置高亮
                .field("*");

        // 这是总的请求体（包含分页、排序以及上面的查询）
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder()
                .from((paramBody.getCurPage() - 1) * paramBody.getPageSize()) // 从第几条开始
                .size(paramBody.getPageSize()) // 显示多少条
                .query(totalQuery) // 上面的查询
                .highlighter(highLight);
        // 设置排序字段
        this.setSortFields(sourceBuilder, paramBody.getSortCols());

        // 最终的搜索请求
        SearchRequest request = new SearchRequest(paramBody.getTableName()).source(sourceBuilder);

        // 搜索返回
        SearchResponse response = null;
        try {
            response = client.search(request);
        } catch (IOException e) {
            log.error("Single table search error! searchParam is {}, error message is {}", paramBody.toString(), e.getMessage());
        }
        if (response == null) {
            return SingleTableQueryResult.NO_RESULT;
        }

        // 填充搜索结果
        SingleTableQueryResult result = new SingleTableQueryResult();
        // 总命中数
        result.setTotalHits(response.getHits().totalHits);

        // 遍历每条搜索结果，并设置搜索结果高亮显示
        response.getHits().forEach(searchHit -> {
            Map<String, Object> row = searchHit.getSourceAsMap();
            // 用有高亮内容的字段值替换原来的字段值
            searchHit.getHighlightFields().forEach((col, highlightContent) -> {
                row.replace(col, highlightContent.fragments()[0].toString());
            });
            result.addRow(row);
        });

        return result;
    }

    /**
     * 设置待排序的字段
     * @param sourceBuilder
     * @param sortCols key, value 分别表示 字段名、是否升序排序
     */
    private void setSortFields(SearchSourceBuilder sourceBuilder, Map<String, Boolean> sortCols) {
        if (sortCols == null) return;
        sortCols.forEach((col, isAsc) -> {
            sourceBuilder.sort(col.toLowerCase(), isAsc ? SortOrder.ASC : SortOrder.DESC);
        });
    }

    /**
     * 构造 query_string query
     * @param tableName 表名
     * @param word 待查询文本
     * @return
     */
    private QueryBuilder getQueryStringQuery(String tableName, String word) {
        return QueryBuilders
                .queryStringQuery(word)
                .defaultOperator(Operator.AND)
                .fuzzyTranspositions(false);
    }

    /**
     * 构造 filter query
     * @param filterColumnMap 待搜索的字段及其字段值
     * @return
     */
    private QueryBuilder getFilterQuery(Map<String, List<String>> filterColumnMap) {
        if (filterColumnMap == null) return null;
        BoolQueryBuilder filterQueryBuilder = new BoolQueryBuilder();
        filterColumnMap.forEach((column, values) -> {
            if (values.size() == 1) {
                filterQueryBuilder.must(QueryBuilders.termQuery(column, values.get(0)));
            }
            else {
                filterQueryBuilder.must(QueryBuilders.termsQuery(column, values));
            }
        });
        return filterQueryBuilder;
    }

}
