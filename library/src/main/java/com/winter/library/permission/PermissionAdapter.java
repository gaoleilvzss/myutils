package com.winter.library.permission;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * create by 高 (｡◕‿◕｡) 磊
 * 2020/11/14
 * desc :
 */
public class PermissionAdapter {

    private Fragment fragment;
    private FragmentActivity activity;

    public PermissionAdapter(Fragment fragment) {
        this.fragment = fragment;
    }

    public PermissionAdapter(FragmentActivity activity) {
        this.activity = activity;
    }

    public PermissionBuilder permission(String... permissions) {
        return permission(new ArrayList<>(Arrays.asList(permissions)));
    }

    private PermissionBuilder permission(List<String> permissions) {
        return new PermissionBuilder(activity,fragment,permissions);
    }


}
