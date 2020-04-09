package com.example.demo.utils;

import cn.hutool.core.util.StrUtil;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author wangjinhao
 * @date 2020/4/9 12:20
 */
public class ElasticsearchUtils {
    /**
     * 规范化搜索词
     * 去除标点字符
     * 将所有词用 AND 连接起来
     * @param word
     * @return
     */
    public static String wordRegular(String word) {
        word = word.replaceAll("[\\pP\\p{Punct}]"," ");
        String[] words = StrUtil.trimToEmpty(word).split("\\s+");;
        return Arrays.stream(words).collect(Collectors.joining(" AND "));
    }
}
