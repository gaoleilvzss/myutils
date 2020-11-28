package com.winter.library.compress;

import java.io.File;

/**
 * @Author: 高磊
 * @Time: 2020/11/23 5:34 PM
 * @Description:
 */
public interface OnCompressListener {
    /**
     * 当开始压缩图片时候的回调
     */
    void onStart();

    /**
     * 完成压缩时的回调
     * @param file
     */
    void onSuccess(File file);

    /**
     * 发生异常的回调
     * @param e
     */
    void onError(Throwable e);
}
