package com.winter.library.bitmap;

/**
 * @Author: 高磊
 * @Time: 2020/11/26 5:11 PM
 * @Description:
 */


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Build;
import android.text.TextUtils;

import com.winter.library.ThreadUtils;


/**
 * 对于bitmap的操作 暂时只支持传入bitmap
 * 1.bitmap旋转(0-360)
 * 2.bitmap的缩放
 * 3.bitmap添加水印，水印位置等
 * 4.bitmap压缩
 */
public class BitmapX {

    private Bitmap bitmap;
    private BitmapResultListener bitmapResultListener;


    private int rotate;
    private float scale;
    private String message;
    private PointF pointF;
    private int targetSizeKb;
    private int newWidth;
    private int newHeight;
    private boolean isWaterTextFirst;
    private int paintSize;
    private int paintColor;

    public BitmapX(Builder builder) {
        this.newHeight = builder.newHeight;
        this.newWidth = builder.newWidth;
        this.bitmap = builder.bitmap;
        this.bitmapResultListener = builder.bitmapResultListener;
        this.rotate = builder.rotate;
        this.scale = builder.scale;
        this.message = builder.message;
        this.pointF = builder.pointF;
        this.targetSizeKb = builder.targetSizeKb;
        this.isWaterTextFirst = builder.isWaterTextFirst;
        this.paintSize = builder.paintSize;
        this.paintColor = builder.paintColor;
    }


    public static Builder init(Context context) {
        return new Builder(context);
    }

    public static class Builder {
        private Bitmap bitmap;
        private Context context;
        private BitmapResultListener bitmapResultListener;


        //旋转
        private int rotate;
        private float scale;
        private String message;
        private PointF pointF;
        private int targetSizeKb;
        private int newWidth;
        private int newHeight;
        private boolean isWaterTextFirst;
        private int paintSize;
        private int paintColor;


        public Builder(Context context) {
            this.context = context;
        }

        public Builder setRotate(int rotate) {
            this.rotate = rotate;
            return this;
        }


        public Builder setBitmap(Bitmap bmp) {
            this.bitmap = bmp;
            return this;
        }

        public BitmapX build() {
            return new BitmapX(this);
        }

        public void launch() {
            build().launch();
        }

        public Builder setResult(BitmapResultListener bitmapResultListener) {
            this.bitmapResultListener = bitmapResultListener;
            return this;
        }

        public Builder setScale(float scale) {
            this.scale = scale;
            return this;
        }

        public Builder addWaterText(String message) {
            this.message = message;

            return this;
        }

        public Builder setWaterTextGravity(PointF pointF) {
            this.pointF = pointF;
            return this;
        }

        public Builder setWaterTextGravity(int x, int y) {
            this.pointF = new PointF(x, y);
            return this;
        }

        public Builder setCompressTargetSize(int targetSizeKb) {
            this.targetSizeKb = targetSizeKb;
            return this;
        }

        public Builder setScaleWidth(int width) {
            this.newWidth = width;
            return this;
        }

        public Builder setScaleHeight(int height) {
            this.newHeight = height;
            return this;
        }

        public Builder setWaterTextFirst(boolean isWaterTextFirst) {
            this.isWaterTextFirst = isWaterTextFirst;
            return this;
        }

        public Builder setWaterTextSize(int paintSize) {
            this.paintSize = paintSize;
            return this;
        }

        public Builder setWaterTextColor(int paintColor) {
            this.paintColor = paintColor;
            return this;
        }
    }

    private void launch() {
        ThreadUtils.operate(new Runnable() {
            Bitmap newBitmap = null;

            @Override
            public void run() {
                if (bitmap == null) {
                    ThreadUtils.runOnUiThread(() -> bitmapResultListener.onError(new NullPointerException("bitmap can not be null")));
                    return;
                }
                try {
                    if (isWaterTextFirst) {
                        if (!TextUtils.isEmpty(message)) {
                            //todo 添加水印
                            newBitmap = addTextToBitmap(BitmapX.this.bitmap, message, pointF);
                            if (!bitmap.isRecycled()) {
                                bitmap.recycle();
                            }
                        }
                    }
                    if (rotate != 0) {
                        int newWidth = newBitmap == null ? bitmap.getWidth() : newBitmap.getWidth();
                        int newHeight = newBitmap == null ? bitmap.getHeight() : newBitmap.getHeight();
                        Matrix matrix = new Matrix();
                        matrix.setRotate(rotate);
                        // 围绕原地进行旋转
                        newBitmap = Bitmap.createBitmap(newBitmap == null ? bitmap : newBitmap, 0, 0, newWidth, newHeight, matrix, false);
                        if (!bitmap.isRecycled()) {
                            bitmap.recycle();
                        }
                    }

                    if (newWidth != 0 && newHeight != 0) {
                        int oldHeight = newBitmap.getHeight();
                        int oldWidth = newBitmap.getWidth();
                        float scaleWidth = ((float) newWidth) / oldWidth;
                        float scaleHeight = ((float) newHeight) / oldHeight;
                        Matrix matrix = new Matrix();
                        matrix.postScale(scaleWidth, scaleHeight);// 使用后乘
                        newBitmap = Bitmap.createBitmap(newBitmap, 0, 0, oldWidth, oldHeight, matrix, false);
                    } else if (scale != 1f) {
                        //todo 缩放
                        int oldWidth = newBitmap.getWidth();
                        int oldHeight = newBitmap.getHeight();
                        Matrix matrix = new Matrix();
                        matrix.preScale(scale, scale);
                        newBitmap = Bitmap.createBitmap(newBitmap, 0, 0, oldWidth, oldHeight, matrix, false);
                    }

                    if ((newBitmap.getByteCount() / 1024) - 10 > targetSizeKb) {
                        //todo 进行压缩
                    }
                    if (!isWaterTextFirst) {
                        if (!TextUtils.isEmpty(message)) {
                            //todo 添加水印
                            newBitmap = addTextToBitmap(BitmapX.this.bitmap, message, pointF);
                        }
                    }
                    ThreadUtils.runOnUiThread(() -> bitmapResultListener.onResult(newBitmap));
                } catch (Exception e) {
                    e.printStackTrace();
                    ThreadUtils.runOnUiThread(() -> bitmapResultListener.onError(e));

                }
            }
        });

    }

    private Bitmap addTextToBitmap(Bitmap mBitmap, String msg, PointF pointF) {
        int mBitmapWidth = mBitmap.getWidth();
        int mBitmapHeight = mBitmap.getHeight();
        Bitmap mNewBitmap = Bitmap.createBitmap(mBitmapWidth, mBitmapHeight, Bitmap.Config.ARGB_8888);
        Canvas mCanvas = new Canvas(mNewBitmap);
        mCanvas.drawBitmap(mBitmap, 0, 0, null);
        //添加文字
        Paint mPaint = new Paint();
        mPaint.setColor(paintColor);
        mPaint.setTextSize(paintSize);
        //水印的位置坐标
        if (pointF.x > mBitmapWidth || pointF.y > mBitmapHeight) {
            pointF.x = 0;
            pointF.y = 0;
        }
        mCanvas.drawText(msg, pointF.x, pointF.y, mPaint);
        mCanvas.save();
        mCanvas.restore();
        return mNewBitmap;
    }


}
