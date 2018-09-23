package com.psk.imagepicker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.psk.customimagepicker.CaptureImageFragment;

public class MainActivity extends AppCompatActivity implements CaptureImageFragment.CaptureImageListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void pickImage(View view) {
        CaptureImageFragment fragment = CaptureImageFragment.getInstance("ABC.jpg");
        getSupportFragmentManager().beginTransaction().add(fragment, fragment.getClass().getName()).commit();
    }

    @Override
    public void onImageSelected(String path, boolean isPdfFile) {
        Log.e("Path", path);
    }
}
