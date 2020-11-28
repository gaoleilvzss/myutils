package com.winter.myutils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.winter.library.log.LogX;
import com.winter.library.network.DisposeDataListener;
import com.winter.library.permission.PermissionUtils;
import com.winter.myutils.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        PermissionUtils.init(this).permission(Manifest.permission.CAMERA)
                .request((allGranted, grantedList, deniedList) -> Toast.makeText(this, "success", Toast.LENGTH_SHORT).show());
        LogX.d("test log");
        binding.btn.setOnClickListener(v -> RequestCenter.requestTestGet(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                TestModel model = (TestModel) responseObj;
                binding.tv.setText(model.toString());
            }

            @Override
            public void onFailure(Object reasonObj) {
                LogX.e("failed");
            }
        }));


    }
}