package com.psk.customimagepicker.exceptions;

/**
 * Created by psk on 9/23/2018.
 */

public enum ErrorMessages {
    INVALID_PATH("Invalid Path "), INVALID_FILE_NAME("Invalid File Name "), NULL_FRAGMENT_MANAGER("FragmentManager can not be null");
    private String message;

    ErrorMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
