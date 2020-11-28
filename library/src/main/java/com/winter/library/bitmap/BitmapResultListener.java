package com.winter.library.bitmap;

import android.graphics.Bitmap;

/**
 * @Author: 高磊
 * @Time: 2020/11/26 5:20 PM
 * @Description:
 */
public interface BitmapResultListener {
    void onResult(Bitmap newBitmap);
    void onError(Exception e);
}
