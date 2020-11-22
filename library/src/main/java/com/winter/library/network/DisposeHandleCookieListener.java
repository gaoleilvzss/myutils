package com.winter.library.network;

import java.util.ArrayList;

/**
 * @Author: 高磊
 * @Time: 2020/11/18 1:53 PM
 * @Description:
 */
public interface DisposeHandleCookieListener extends DisposeDataListener{
    public void onCookie(ArrayList<String> cookieStrLists);
}
