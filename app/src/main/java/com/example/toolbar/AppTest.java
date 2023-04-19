package com.example.toolbar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

public class AppTest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_test);

        List<PackageInfo> mPackageInfo = getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < mPackageInfo.size(); i++) {
            Log.d("lzx", "onCreate: mPackageInfo"+mPackageInfo.get(i).packageName);
        }

    }
}