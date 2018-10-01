package com.psk.customimagepicker.gallery.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.psk.customimagepicker.R;
import com.psk.customimagepicker.gallery.callbacks.GalleryAdapterCallback;
import com.psk.customimagepicker.models.DocumentModel;

import java.util.ArrayList;

/**
 * Created by psk on 9/30/2018.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {
    private ArrayList<DocumentModel> documentModels;
    private GalleryAdapterCallback callback;

    public GalleryAdapter(ArrayList<DocumentModel> documentModels, GalleryAdapterCallback callback) {
        this.documentModels = documentModels;
        this.callback = callback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_gallery_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final DocumentModel documentModel = documentModels.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onListItemClicked(documentModel);
            }
        });
        Glide.with(holder.ivImage.getContext()).load(documentModel.getPath()).into(holder.ivImage);
    }

    @Override
    public int getItemCount() {
        return documentModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;

        ViewHolder(View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImage);
        }
    }
}
