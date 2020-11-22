package com.winter.library.network;

/**
 * @Author: 高磊
 * @Time: 2020/11/18 1:52 PM
 * @Description:
 */
public interface DisposeDataListener {
    /**
     * 请求成功回调事件处理
     */
    void onSuccess(Object responseObj);

    /**
     * 请求失败回调事件处理
     */
    void onFailure(Object reasonObj);
}
