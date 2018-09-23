package com.psk.customimagepicker.filePicker;

import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.psk.customimagepicker.R;
import com.psk.customimagepicker.models.PdfDocumentModel;

import java.util.List;

/**
 * Created by psk on 9/23/2018.
 */

public class FileListAdapter extends RecyclerView.Adapter<FileListAdapter.FileViewHolder> {


    private final FilePickerActivity activity;
    private List<PdfDocumentModel> pdfDocumentModels;

    public FileListAdapter(FilePickerActivity activity, List<PdfDocumentModel> pdfDocumentModels) {
        this.activity = activity;
        this.pdfDocumentModels = pdfDocumentModels;
    }

    @Override
    public FileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(activity).inflate(R.layout.item_doc_layout, parent, false);

        return new FileViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final FileViewHolder holder, int position) {
        final PdfDocumentModel pdfDocumentModel = pdfDocumentModels.get(position);
        holder.fileNameTextView.setText(pdfDocumentModel.getTitle());
        holder.fileSizeTextView.setText(Formatter.formatShortFileSize(activity, Long.parseLong(pdfDocumentModel.getSize())));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.onFileSelected(pdfDocumentModel);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pdfDocumentModels.size();
    }

    public static class FileViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        TextView fileNameTextView;

        TextView fileSizeTextView;

        public FileViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.file_iv);
            fileNameTextView = itemView.findViewById(R.id.file_name_tv);
            fileSizeTextView = itemView.findViewById(R.id.file_size_tv);
        }
    }
}
