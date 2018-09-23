package com.psk.customimagepicker;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;

import com.psk.customimagepicker.exceptions.CustomImagePickerException;
import com.psk.customimagepicker.exceptions.ErrorMessages;

import java.io.File;

/**
 * Created by psk on 9/23/2018.
 */

public class CustomImagePicker {
    private String path = Environment.getExternalStorageDirectory() + File.separator + "CustomImagePicker";
    private String fileName = "CustomImagePicker";
    private FragmentManager fragmentManager;

    public static CustomImagePicker getInstance(@NonNull FragmentManager fragmentManager) {
        return new CustomImagePicker(fragmentManager);
    }

    private CustomImagePicker(@NonNull FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public CustomImagePicker path(@NonNull String path) {
        this.path = path;
        return this;
    }

    public CustomImagePicker fileName(@NonNull String fileName) {
        this.fileName = fileName;
        return this;
    }

    public void build() {
        if (isValidData()) {
            CaptureImageFragment fragment = CaptureImageFragment.getInstance(path, fileName + ".jpg");
            fragmentManager.beginTransaction().add(fragment, fragment.getClass().getName()).commit();
        }
    }

    private boolean isValidData() {
        if (TextUtils.isEmpty(path)) {
            throw new CustomImagePickerException(ErrorMessages.INVALID_PATH.getMessage() + path);
        }
        if (TextUtils.isEmpty(fileName)) {
            throw new CustomImagePickerException(ErrorMessages.INVALID_FILE_NAME.getMessage() + fileName);
        }
        if (fragmentManager == null) {
            throw new CustomImagePickerException(ErrorMessages.NULL_FRAGMENT_MANAGER.getMessage());
        }
        return true;
    }
}
