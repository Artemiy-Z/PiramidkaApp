package com.example.study4child.Tools;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.study4child.R;

public class MyActivity extends AppCompatActivity {

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(MyApplication.getPoolInstance() != null) {
            MyApplication.getPoolInstance().pause();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();

        if(MyApplication.getPoolInstance() != null) {
            MyApplication.getPoolInstance().resume();
        }
    }
}