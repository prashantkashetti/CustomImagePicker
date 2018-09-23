package com.psk.customimagepicker.listeners;

/**
 * Created by psk on 9/23/2018.
 */

public interface CustomImagePickerListeners {
    void onImageOrFileSelected(String path, boolean isPdfFile);

    void onAppPermissionDenied();
}
