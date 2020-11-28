package com.winter.library.compress;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.text.TextUtils;

import com.winter.library.ThreadUtils;
import com.winter.library.log.LogX;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.winter.library.compress.ImageXTypeUtils.ImageType.*;


/**
 * @Author: 高磊
 * @Time: 2020/11/23 5:12 PM
 * @Description:
 */
public class ImageX {

    /**
     * 邻近采样
     **/
    public static final int NEAREST_NEIGHBOUR_RESAMPLING = 1;
    /**
     * 双线性采样
     **/
    public static final int BILINEAR_RESAMPLING = 2;
    /**
     * 双立方／双三次采样
     **/
    public static final int BICUBIC_RESAMPLING = 3;


    private Context context;
    private int quality;
    private int strategy;
    private FilterCondition filterCondition;
    private OnCompressListener onCompressListener;
    private RenameListener renameListener;
    private List<InputStreamProvider> mStreamProviders;
    private String outputPath;

    public ImageX(Builder builder) {
        this.context = builder.context;
        this.mStreamProviders = builder.mStreamProviders;
        this.outputPath = builder.outPutPath;
        this.quality = builder.quality;
        this.strategy = builder.strategy;
        this.filterCondition = builder.filterCondition;
        this.onCompressListener = builder.onCompressListener;
        this.renameListener = builder.renameListener;
    }

    public static Builder init(Context context) {
        return new Builder(context);
    }

    public void launch(Context context) {
        if (mStreamProviders == null || mStreamProviders.size() == 0 && onCompressListener != null) {
            onCompressListener.onError(new NullPointerException("image file cannot be null"));
        }
        Iterator<InputStreamProvider> iterator = mStreamProviders.iterator();
        while (iterator.hasNext()) {
            final InputStreamProvider path = iterator.next();
            ThreadUtils.compress(() -> {
                try {
                    ThreadUtils.runOnUiThread(() -> onCompressListener.onStart());
                    File result = compress(context, path);
                    ThreadUtils.runOnUiThread(() -> onCompressListener.onSuccess(result));
                } catch (IOException e) {
                    ThreadUtils.runOnUiThread(() -> onCompressListener.onError(e));
                }
            });
        }
    }

    private File compress(Context context, InputStreamProvider stream) throws IOException {
        ImageXTypeUtils.ImageType type = ImageXTypeUtils.getFormat(stream.open());
        if (type == null) {
            throw new IOException("tpye == null");
        }
        LogX.e(type.toString());
        File outFile = getImageCacheFile(context, type.toString());
        LogX.e(outFile.getAbsolutePath());
        if (renameListener != null) {
            String filename = renameListener.rename(stream.getPath()) + "." + type.toString();
            outFile = getImageCustomFile(context, filename);
        }
        LogX.e(outFile.getAbsolutePath());
        File outPutFile = null;
        if (strategy == ImageX.NEAREST_NEIGHBOUR_RESAMPLING) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            Bitmap compress = BitmapFactory.decodeFile(stream.getPath(), options);
            if (compress != null) {
                outPutFile = getFile(quality, compress, outFile.getAbsolutePath(), type);
                compress.recycle();
            } else {
                throw new IOException("compress failed");
            }
        } else if (strategy == ImageX.BILINEAR_RESAMPLING) {
            Bitmap bitmap = BitmapFactory.decodeFile(stream.getPath());
            Matrix matrix = new Matrix();
            matrix.setScale(0.5f, 0.5f);
            Bitmap compress = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            if (compress != null) {
                outPutFile = getFile(quality, compress, outFile.getAbsolutePath(), type);
                compress.recycle();
            } else {
                bitmap.recycle();
                throw new IOException("compress failed");
            }
        } else if (strategy == ImageX.BICUBIC_RESAMPLING) {
            throw new IOException("not support");
        }
        return outPutFile;
    }

    private File getImageCustomFile(Context context, String filename) {
        if (TextUtils.isEmpty(outputPath)) {
            outputPath = context.getCacheDir().getAbsolutePath();
        }
        String cacheBuilder = outputPath + "/" + filename;
        return new File(cacheBuilder);
    }

    private File getImageCacheFile(Context context, String suffix) {
        if (TextUtils.isEmpty(outputPath)) {
            outputPath = context.getCacheDir().getAbsolutePath();
        }

        String cacheBuilder = outputPath + "/" +
                System.currentTimeMillis() +
                (int) (Math.random() * 1000) +
                (TextUtils.isEmpty(suffix) ? ".jpg" : "." + suffix);

        return new File(cacheBuilder);
    }

    public static class Builder {
        private Context context;
        private int quality;
        private int strategy;
        private String outPutPath;
        private FilterCondition filterCondition;
        private OnCompressListener onCompressListener;
        private RenameListener renameListener;
        private List<InputStreamProvider> mStreamProviders;


        public Builder(Context context) {
            this.context = context;
            mStreamProviders = new ArrayList<>();
        }

        public Builder load(InputStreamProvider inputStreamProvider) {
            mStreamProviders.add(inputStreamProvider);
            return this;
        }

        public Builder load(final File file) {
            mStreamProviders.add(new InputStreamProvider() {
                @Override
                public InputStream open() throws IOException {
                    return new FileInputStream(file);
                }

                @Override
                public String getPath() {
                    return file.getAbsolutePath();
                }
            });
            return this;
        }

        public Builder load(final String filePath) {
            mStreamProviders.add(new InputStreamProvider() {
                @Override
                public InputStream open() throws IOException {
                    return new FileInputStream(filePath);
                }

                @Override
                public String getPath() {
                    return filePath;
                }
            });
            return this;
        }

        public <T> Builder load(List<T> list) {
            for (T src : list) {
                if (src instanceof String) {
                    load((String) src);
                } else if (src instanceof File) {
                    load((File) src);
                } else {
                    throw new IllegalArgumentException("Incoming data type exception, it must be String, File or Bitmap");
                }
            }
            return this;
        }

        public Builder setCompressListener(OnCompressListener compressListener) {
            this.onCompressListener = compressListener;
            return this;
        }

        public Builder filter(FilterCondition filterCondition) {
            this.filterCondition = filterCondition;
            return this;
        }


        public Builder quality(int quality) {
            this.quality = quality;
            return this;
        }

        public Builder strategy(int strategy) {
            this.strategy = strategy;
            return this;
        }

        private ImageX build() {
            return new ImageX(this);
        }

        public Builder setRenameListener(RenameListener renameListener) {
            this.renameListener = renameListener;
            return this;
        }

        public Builder setOutputFilePath(String outputFilePath) {
            this.outPutPath = outputFilePath;
            return this;
        }

        public void launch() {
            build().launch(context);
        }
    }

    public File getFile(int quality, Bitmap bitmap, String fileName, ImageXTypeUtils.ImageType type) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap.CompressFormat format;
        if (type == JPG) {
            format = Bitmap.CompressFormat.JPEG;
        } else if (type == PNG) {
            format = Bitmap.CompressFormat.PNG;
        } else if (type == WEBP) {
            format = Bitmap.CompressFormat.WEBP;
        } else {
            format = Bitmap.CompressFormat.JPEG;
        }
        bitmap.compress(format, quality, baos);
        File file = new File(fileName);
        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            InputStream is = new ByteArrayInputStream(baos.toByteArray());
            int x = 0;
            byte[] b = new byte[1024 * 100];
            while ((x = is.read(b)) != -1) {
                fos.write(b, 0, x);
            }
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }


}
