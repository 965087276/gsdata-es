package com.example.demo.entity;

import cn.hutool.json.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author wangjinhao
 * @date 2020/4/9 15:24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SingleTableQueryResult {

    /**
     * 总结果数
     */
    private long totalHits;

    /**
     * 所有结果（字段名均为小写）
     * 匹配到的值会用标签对 "<br></br>" 包起来
     */
    private List<Map<String, Object>> rows;

    public static final SingleTableQueryResult NO_RESULT = new SingleTableQueryResult(0L, new ArrayList<>());

    public void addRow(Map<String, Object> row) {
        if (rows == null) rows = new ArrayList<>();
        rows.add(row);
    }
}
