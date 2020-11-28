package com.winter.library.compress;

/**
 * @Author: 高磊
 * @Time: 2020/11/23 5:23 PM
 * @Description:
 */
public interface FilterCondition {
    /**
     * 过滤条件
     * @param path
     * @Description: 过滤
     * @return: 是否过滤
     */
    boolean filter(String path);
}
