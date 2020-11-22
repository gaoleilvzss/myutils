package com.winter.library.permission;

import java.util.List;

/**
 * create by 高 (｡◕‿◕｡) 磊
 * 2020/11/14
 * desc :
 */
public interface PermissionCallBack {
    void onResult(boolean allGranted, List<String> grantedList, List<String> deniedList);
}
