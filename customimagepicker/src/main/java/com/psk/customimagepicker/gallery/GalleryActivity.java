package com.psk.customimagepicker.gallery;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader;
import com.bumptech.glide.util.FixedPreloadSizeProvider;
import com.psk.customimagepicker.R;
import com.psk.customimagepicker.filePicker.DocScannerTask;
import com.psk.customimagepicker.filePicker.FileResultCallback;
import com.psk.customimagepicker.gallery.adapters.GalleryAdapter;
import com.psk.customimagepicker.gallery.callbacks.GalleryAdapterCallback;
import com.psk.customimagepicker.models.DocumentModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by psk on 9/30/2018.
 */

public class GalleryActivity extends AppCompatActivity implements GalleryAdapterCallback {
    private RecyclerView rvGallery;
    private final int imageWidthPixels = 1024;
    private final int imageHeightPixels = 768;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        rvGallery = findViewById(R.id.rvGallery);
//        rvGallery.setLayoutManager(new GridLayoutManager(this, 3));
        rvGallery.setLayoutManager(new LinearLayoutManager(this));
        new DocScannerTask(this, new FileResultCallback<DocumentModel>() {
            @Override
            public void onResultCallback(List<DocumentModel> files) {
                if (files == null || files.size() == 0) {
                    Log.e("ERROR", "No FIles Found");
                    return;
                }
                init((ArrayList<DocumentModel>) files);
//                rvGallery.setAdapter(new GalleryAdapter((ArrayList<DocumentModel>) files, GalleryActivity.this));
            }
        }, true).execute();
    }

    private void init(ArrayList<DocumentModel> documentModels) {
        ListPreloader.PreloadSizeProvider sizeProvider =
                new FixedPreloadSizeProvider(imageWidthPixels, imageHeightPixels);
        ListPreloader.PreloadModelProvider modelProvider = new MyPreloadModelProvider(documentModels, this);
        RecyclerViewPreloader<DocumentModel> preloader =
                new RecyclerViewPreloader<DocumentModel>(
                        Glide.with(this), modelProvider, sizeProvider, 10 /*maxPreload*/);
        rvGallery.addOnScrollListener(preloader);
        rvGallery.setAdapter(new GalleryAdapter(documentModels, GalleryActivity.this));
    }

    @Override
    public void onListItemClicked(DocumentModel documentModel) {
        Log.e("File", documentModel.getPath());
    }
}
