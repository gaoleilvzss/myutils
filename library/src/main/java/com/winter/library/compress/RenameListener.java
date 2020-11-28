package com.winter.library.compress;

/**
 * @Author: 高磊
 * @Time: 2020/11/23 5:37 PM
 * @Description:
 */
public interface RenameListener {
    /**
     * 修改图片名字
     * @param filePath
     * @return
     */
    String rename(String filePath);
}
