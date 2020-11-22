package com.winter.library.permission;

import android.content.Context;
import android.content.pm.PackageManager;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

/**
 * create by 高 (｡◕‿◕｡) 磊
 * 2020/11/14
 * desc :
 */
public class PermissionUtils {
    public static PermissionAdapter init(FragmentActivity activity) {
        return new PermissionAdapter(activity);
    }

    public static PermissionAdapter init(Fragment fragment) {
        return new PermissionAdapter(fragment);
    }

}
