package com.winter.library.compress;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.winter.library.compress.ImageXTypeUtils.ImageType.GIF;
import static com.winter.library.compress.ImageXTypeUtils.ImageType.JPG;
import static com.winter.library.compress.ImageXTypeUtils.ImageType.PNG;
import static com.winter.library.compress.ImageXTypeUtils.ImageType.WEBP;

/**
 * @Author: 高磊
 * @Time: 2020/11/26 10:50 AM
 * @Description:
 */
public class ImageXTypeUtils {
    public enum ImageType {
        JPG("jpg"), PNG("png"), WEBP("webp"), GIF("gif");
        public String extension;

        ImageType(String extension) {
            this.extension = extension;
        }
    }

    public static ImageType getFormat(File file) {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            return getFormat(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ImageType getFormat(InputStream inputStream) {
        try {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            bufferedInputStream.mark(Integer.MAX_VALUE);
            byte[] start;
            byte[] end;

            start = new byte[2];
            bufferedInputStream.read(start);
            if (start[0] == (byte) 0xFF &&
                    start[1] == (byte) 0xD8
            ) {
                return JPG;
            }
            bufferedInputStream.reset();

            start = new byte[8];
            bufferedInputStream.read(start);
            if (start[0] == (byte) 0x89 &&
                    start[1] == (byte) 0x50 &&
                    start[2] == (byte) 0x4E &&
                    start[3] == (byte) 0x47 &&
                    start[4] == (byte) 0x0D &&
                    start[5] == (byte) 0x0A &&
                    start[6] == (byte) 0x1A &&
                    start[7] == (byte) 0x0A) {
                return PNG;
            }
            bufferedInputStream.reset();

            // https://developers.google.com/speed/webp/docs/riff_container
            // https://en.wikipedia.org/wiki/WebP
            start = new byte[4];
            end = new byte[4];
            bufferedInputStream.read(start);
            bufferedInputStream.skip(4);
            bufferedInputStream.read(end);
            if (start[0] == (byte) 0x52 &&
                    start[1] == (byte) 0x49 &&
                    start[2] == (byte) 0x46 &&
                    start[3] == (byte) 0x46 &&
                    end[0] == (byte) 0x57 &&
                    end[1] == (byte) 0x45 &&
                    end[2] == (byte) 0x42 &&
                    end[3] == (byte) 0x50) {
                return WEBP;
            }
            bufferedInputStream.reset();

            start = new byte[6];
            bufferedInputStream.read(start);
            if (start[0] == (byte) 0x47 &&
                    start[1] == (byte) 0x49 &&
                    start[2] == (byte) 0x46 &&
                    start[3] == (byte) 0x38 &&
                    start[4] == (byte) 0x39 &&
                    start[5] == (byte) 0x61) {
                return GIF;
            }

            bufferedInputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
