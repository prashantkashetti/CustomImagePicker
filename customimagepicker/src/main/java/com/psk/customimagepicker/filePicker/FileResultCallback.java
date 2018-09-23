package com.psk.customimagepicker.filePicker;

import java.util.List;

/**
 * Created by psk on 9/23/2018.
 */

public interface FileResultCallback<T> {
    void onResultCallback(List<T> files);
}
