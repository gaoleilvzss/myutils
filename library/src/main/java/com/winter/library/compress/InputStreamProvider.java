package com.winter.library.compress;

import java.io.IOException;
import java.io.InputStream;

/**
 * 通过此接口获取输入流，以兼容文件、FileProvider方式获取到的图片
 * @author gaolei
 */
public interface InputStreamProvider {

  InputStream open() throws IOException;

  String getPath();
}
