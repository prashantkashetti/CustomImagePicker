package com.psk.imagepicker;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.psk.customimagepicker.CustomImagePicker;
import com.psk.customimagepicker.listeners.CustomImagePickerListeners;

import java.io.File;

public class MainActivity extends AppCompatActivity implements CustomImagePickerListeners {
    private String path = Environment.getExternalStorageDirectory() + File.separator + "SamplePath";
    private CustomImagePicker customImagePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        customImagePicker = CustomImagePicker.getInstance(getSupportFragmentManager());
    }

    public void pickImage(View view) {
        customImagePicker
                .path(path)
                .fileName("xyz")
                .build();
    }

    @Override
    public void onImageOrFileSelected(String path, boolean isPdfFile) {
        Log.e("Path", path);
    }

    @Override
    public void onAppPermissionDenied() {
        Log.e("onAppPermissionDenied", "App Permission Denied");
    }
}
