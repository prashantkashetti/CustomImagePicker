package com.psk.customimagepicker.models;

/**
 * Created by psk on 9/23/2018.
 */

public class DocumentModel {
    private int id;
    private String title;
    private String path;
    private String mimeType;
    private String size;
    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public DocumentModel(int id, String title, String path) {
        this.id = id;
        this.title = title;
        this.path = path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DocumentModel)) return false;

        DocumentModel documentModel = (DocumentModel) o;

        return id == documentModel.id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return path;
    }
}