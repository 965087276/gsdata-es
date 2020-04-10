package com.example.demo.entity;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangjinhao
 * @date 2020/4/9 13:39
 */
@Data
public class SingleTableQueryParam {
    /**
     * 表名
     */
    private String tableName;

    /**
     * 待查询文本
     */
    private String word;

    /**
     * 第几页（从1开始）
     */
    private int curPage;

    /**
     * 每页多少条
     */
    private int pageSize;

    /**
     * 根据哪些字段对结果进行过滤（如要根据 ORGID 字段过滤）。
     * map中key、value分别为 字段名（必须用小写）、字段值（字段值可以有多个，会匹配这这多个中的任意一个）
     */
    private Map<String, List<String>> filterCols;

    /**
     * 根据哪些字段对结果进行排序（如要根据 date 字段过滤）。
     * map中key、value分别为 字段名（必须用小写）、升序还是降序（升序用True，降序用False）
     */
    private Map<String, Boolean> sortCols;

    /**
     * 构造函数
     * @param tableName
     * @param word
     * @param curPage
     * @param pageSize
     */
    public SingleTableQueryParam(String tableName, String word, int curPage, int pageSize) {
        this.tableName = tableName;
        this.word = word;
        this.curPage = curPage;
        this.pageSize = pageSize;
    }

    /**
     * 添加过滤字段 （字段值只有一个的情况）
     * @param column 字段名（必须为小写）
     * @param value  字段值
     * @return
     */
    public SingleTableQueryParam addFilterCol(String column, String value) {
        // 忽略空字段
        if (StrUtil.isBlank(column) || StrUtil.isBlank(value)) {
            return this;
        }
        if (filterCols == null) filterCols = new HashMap<>();
        filterCols.put(column.toLowerCase(), Arrays.asList(value));
        return this;
    }

    /**
     * 添加过滤字段 （字段值有多个的情况）
     * @param column 字段名（必须为小写）
     * @param values  字段值列表
     * @return
     */
    public SingleTableQueryParam addFilterCol(String column, List<String> values) {
        // 忽略空字段
        if (StrUtil.isBlank(column) || values == null || values.size() == 0) {
            return this;
        }
        if (filterCols == null) filterCols = new HashMap<>();
        filterCols.put(column.toLowerCase(), values);
        return this;
    }

    /**
     * 添加排序字段
     * @param column 字段名（必须为小写）
     * @param asc 排序方式（true为升序，false为降序）
     * @return
     */
    public SingleTableQueryParam addSortCols(String column, boolean asc) {
        // 忽略空字段
        if (StrUtil.isBlank(column)) {
            return this;
        }
        if (sortCols == null) sortCols = new HashMap<>();
        sortCols.put(column.toLowerCase(), asc);
        return this;
    }

}
