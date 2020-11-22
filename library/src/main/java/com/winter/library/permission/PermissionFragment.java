package com.winter.library.permission;

import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

/**
 * create by 高 (｡◕‿◕｡) 磊
 * 2020/11/14
 * desc :
 */
public class PermissionFragment extends Fragment {
    private PermissionBuilder pb;
    private static int REQUEST_NORMAL_PERMISSIONS = 1;
    private List<String> granted_list = new ArrayList<>();
    private List<String> denied_list = new ArrayList<>();

    void requestNow(PermissionBuilder permissionBuilder, List<String> permissions) {
        this.pb = permissionBuilder;
        requestPermissions(permissions.toArray(new String[0]), REQUEST_NORMAL_PERMISSIONS);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_NORMAL_PERMISSIONS) {
            boolean isAllGrant = false;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    granted_list.add(permissions[i]);
                } else {
                    denied_list.add(permissions[i]);
                }
            }
            isAllGrant = denied_list.size() == 0;
            pb.callBack.onResult(isAllGrant, granted_list, denied_list);
        }
    }

}
