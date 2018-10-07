package com.psk.customimagepicker.gallery;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.psk.customimagepicker.models.DocumentModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by psk on 10/2/2018.
 */

public class MyPreloadModelProvider implements ListPreloader.PreloadModelProvider {
    private ArrayList<DocumentModel> documentModels;
    private final int imageWidthPixels = 1024;
    private final int imageHeightPixels = 768;
    private Context context;

    public MyPreloadModelProvider(ArrayList<DocumentModel> documentModels, Context context) {
        this.documentModels = documentModels;
        this.context = context;
    }

    @Override
    @NonNull
    public List<DocumentModel> getPreloadItems(int position) {
        DocumentModel documentModel = documentModels.get(position);
        if (documentModel == null) {
            return Collections.emptyList();
        }
        return Collections.singletonList(documentModel);
    }

    @Nullable
    @Override
    public RequestBuilder<?> getPreloadRequestBuilder(@NonNull Object item) {
        DocumentModel documentModel = (DocumentModel) item;
        return GlideApp.with(context)
                .load(documentModel.getPath())
//                .override(imageWidthPixels, imageHeightPixels)
                .transform(new CenterCrop());
    }
}
