package com.winter.library.permission;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import java.util.List;

/**
 * create by 高 (｡◕‿◕｡) 磊
 * 2020/11/14
 * desc :
 */
public class PermissionBuilder {
    private static final String FRAGMENT_TAG = "permission_fragment";
    private FragmentActivity activity;
    private Fragment fragment;
    private List<String> permissions ;
    PermissionCallBack callBack;


    public PermissionBuilder(FragmentActivity activity, Fragment fragment, List<String> permissions) {
        this.activity = activity;
        this.fragment = fragment;
        if (activity == null && fragment != null) {
            this.activity = fragment.getActivity();
        }
        this.permissions = permissions;
    }

    public void request(PermissionCallBack callBack) {
        this.callBack = callBack;
        getPermissionFragment().requestNow(this, permissions);
    }
    private PermissionFragment getPermissionFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        Fragment existedFragment = fragmentManager.findFragmentByTag(FRAGMENT_TAG);
        if (existedFragment != null) {
            return (PermissionFragment) existedFragment;
        } else {
            PermissionFragment permissionFragment = new PermissionFragment();
            fragmentManager.beginTransaction().add(permissionFragment, FRAGMENT_TAG).commitNowAllowingStateLoss();
            return permissionFragment;
        }
    }

    FragmentManager getFragmentManager() {
        FragmentManager fragmentManager;
        if (fragment != null) {
            fragmentManager = fragment.getChildFragmentManager();
        } else {
            fragmentManager = activity.getSupportFragmentManager();
        }
        return fragmentManager;
    }
}
