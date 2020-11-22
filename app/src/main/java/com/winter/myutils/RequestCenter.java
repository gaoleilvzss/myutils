package com.winter.myutils;


import com.winter.library.network.CommonOkHttpClient;
import com.winter.library.network.CommonRequest;
import com.winter.library.network.DisposeDataHandle;
import com.winter.library.network.DisposeDataListener;
import com.winter.library.network.DisposeDownloadListener;
import com.winter.library.network.RequestParams;

/**
 * 请求中心
 */
public class RequestCenter {

    public static class HttpConstants {
        public static final String ROOT_URL = "https://www.wanandroid.com";
        public static final String TEST_URL = ROOT_URL + "/list/1/json";

    }

    private static void getRequest(String url, RequestParams params, DisposeDataListener listener, Class<?> clazz) {
        CommonOkHttpClient.get(CommonRequest.createGetRequest(url, params), new DisposeDataHandle(listener, clazz));
    }


    public static void requestTestGet(DisposeDataListener listener) {
        RequestParams requestParams = new RequestParams();
        getRequest(HttpConstants.TEST_URL, requestParams, listener, TestModel.class);
    }
}
