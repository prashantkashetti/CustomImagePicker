package com.psk.customimagepicker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.widget.Toast;

import com.psk.customimagepicker.filePicker.FilePickerActivity;
import com.psk.customimagepicker.listeners.CustomImagePickerListeners;
import com.psk.customimagepicker.utils.GetPathUtils;
import com.psk.customimagepicker.utils.ImageCompressionUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by psk on 9/23/2018.
 */

public class CaptureImageFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST_CODE = 100;
    private CustomImagePickerListeners listener;
    private File finalFile;
    private String path;

    public static CaptureImageFragment getInstance(String path, String fileName) {
        CaptureImageFragment fragment = new CaptureImageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("fileName", fileName);
        bundle.putString("path", path);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            if (getParentFragment() != null)
                listener = (CustomImagePickerListeners) getParentFragment();
            else
                listener = (CustomImagePickerListeners) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement " + CustomImagePickerListeners.class.getName());
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String fileName = getArguments().getString("fileName");
        path = getArguments().getString("path");
        captureImage(fileName);
    }

    public void captureImage(String fileName) {
        List<Intent> pickFileIntents = new ArrayList<Intent>();


        File storagePath = new File(path);
        if (!storagePath.exists()) {
            if (!storagePath.mkdirs()) {
                Toast.makeText(getContext(), "Storage Full", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        finalFile = new File(storagePath, fileName);

        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        galleryIntent.putExtra("crop", "true");
        galleryIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(finalFile));

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(finalFile));

        Intent filePickerIntent = new Intent(getContext(), FilePickerActivity.class);

        pickFileIntents.add(galleryIntent);
        pickFileIntents.add(cameraIntent);
        pickFileIntents.add(filePickerIntent);

        Intent chooserIntent = Intent.createChooser(pickFileIntents.remove(0), "Pick Image");

        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, pickFileIntents.toArray(new Parcelable[]{}));

        startActivityForResult(chooserIntent, PICK_IMAGE_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;
        Uri originalUri;
        if (requestCode == PICK_IMAGE_REQUEST_CODE) {
            if (data != null) {
                originalUri = data.getData();
                if (originalUri != null) {
                    String path = GetPathUtils.getPath(getContext(), originalUri);
                    compressImage();
                    if (!finalFile.getAbsolutePath().equals(path)) {
                        try {
                            if (path != null)
                                copy(new File(path), finalFile);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    String pdfPath = data.getStringExtra(FilePickerActivity.INTENT_EXTRA_PDF_PATH);
                    if (!TextUtils.isEmpty(pdfPath)) {
                        try {
                            File originalFile = new File(pdfPath);
                            File newFile = new File(path, finalFile.getName().split("\\.")[0] + ".pdf");
                            copy(originalFile, newFile);
                            listener.onImageOrFileSelected(newFile.getAbsolutePath(), true);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        compressImage();
                    }
                }
            } else {
                compressImage();
            }
        }
    }

    public void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    /**
     * Compressing output image
     */
    private void compressImage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean result = ImageCompressionUtils.compressImage(finalFile.getAbsolutePath());
                if (result) {
                    final String path = finalFile.getAbsolutePath();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listener.onImageOrFileSelected(path, false);
                        }
                    });
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "Capture Image Again", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }
}
