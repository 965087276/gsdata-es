package com.example.demo.entity;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

import java.util.HashMap;
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
     * map中key、value分别为 字段名（必须用小写）、字段值
     */
    private Map<String, String> filterCols;

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
     * 添加过滤字段
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
        filterCols.put(column.toLowerCase(), value);
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
