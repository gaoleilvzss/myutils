package com.winter.library.network;

/**
 * @Author: 高磊
 * @Time: 2020/11/18 1:53 PM
 * @Description:
 */
public interface DisposeDownloadListener extends DisposeDataListener {
    void onProgress(int progress);

}
