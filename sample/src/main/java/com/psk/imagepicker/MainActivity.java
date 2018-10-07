package com.psk.imagepicker;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.psk.customimagepicker.CustomImagePicker;
import com.psk.customimagepicker.gallery.GalleryActivity;
import com.psk.customimagepicker.listeners.CustomImagePickerListeners;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CustomImagePickerListeners {
    private String path = Environment.getExternalStorageDirectory() + File.separator + "SamplePath";
    private CustomImagePicker customImagePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView imageView = findViewById(R.id.ivImage);
        customImagePicker = CustomImagePicker.getInstance(getSupportFragmentManager());
    }

    public void pickImage(View view) {
        /*customImagePicker
                .path(path)
                .fileName("xyz")
                .build();*/
        /*File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        File[] files = f.listFiles();
        for (File inFile : files) {
            if (inFile.isDirectory()) {
                // is directory
                Log.e("Folder", inFile.getName());
            }
        }*/
//        ArrayList<File> files = new ArrayList<>();
//        listFiles(Environment.getExternalStorageDirectory().getAbsolutePath(), files);
       /* if (files.size() > 0) {
            for (File file : files)
                Log.e("Folder", file.getAbsolutePath());
        }*/

        ///storage/6238-6330/DCIM/Camera/IMG_20161027_184522017_BURST011.jpg
        Intent intent = new Intent(this, GalleryActivity.class);
        startActivity(intent);
    }

    public void listFiles(String directoryName, List<File> files) {
        File directory = new File(directoryName);
        File[] fList = directory.listFiles();
        if (fList != null) {
            for (File file : fList) {
                String fileName = file.getName();
                if (file.isDirectory() && !fileName.equalsIgnoreCase("android") && !fileName.startsWith(".")) {
                    search(file, files);
                    listFiles(file.getAbsolutePath(), files);
                }
            }
        }
    }

    /*    public void searchImageFromSpecificDirectory(String directoryName) {

            String path = null;

            String uri = MediaStore.Images.Media.DATA;
            // if GetImageFromThisDirectory is the name of the directory from which image will be retrieved
            String condition = uri + " like '%/"+directoryName+"/%'";
            String[] projection = {uri, MediaStore.Images.Media.DATE_ADDED,
                    MediaStore.Images.Media.SIZE};
            Vector additionalFiles = null;
            try {
                if (additionalFiles == null) {
                    additionalFiles = new Vector<String>();
                }
                Cursor cursor = managedQuery(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
                        condition, null, null);
                if (cursor != null) {

                    boolean isDataPresent = cursor.moveToFirst();

                    if (isDataPresent) {

                        do {

                            path = cursor.getString(cursor.getColumnIndex(uri));
                            Log.e("...path...",path);
                            additionalFiles.add(path);
                        } while (cursor.moveToNext());
                    }
                    if (cursor != null) {
                        cursor.close();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
    private void search(File folder, List<File> files) {
        File[] allFiles = folder.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return (name.endsWith(".jpg") || name.endsWith(".jpeg"));
            }
        });
        if (allFiles != null && allFiles.length > 0) {
            files.add(folder);
            for (File file : allFiles) {
                Log.e("Image Path", file.getAbsolutePath());
            }
        }
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
