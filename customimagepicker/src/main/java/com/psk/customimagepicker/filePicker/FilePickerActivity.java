package com.psk.customimagepicker.filePicker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.psk.customimagepicker.R;
import com.psk.customimagepicker.models.DocumentModel;

import java.io.File;
import java.util.List;

/**
 * Created by psk on 9/23/2018.
 */

public class FilePickerActivity extends AppCompatActivity {

    public static String INTENT_EXTRA_PDF_PATH = "pdfPath";
    private RecyclerView recyclerView;
    private LinearLayout llNoRecords;
    private TextView tvNoRecords;
    private ImageView ivSad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_picker);
        recyclerView = findViewById(R.id.recyclerView);
        tvNoRecords = findViewById(R.id.tvNoRecords);
        llNoRecords = findViewById(R.id.llNoRecords);
        ivSad = findViewById(R.id.ivSad);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        new DocScannerTask(this, new FileResultCallback<DocumentModel>() {
            @Override
            public void onResultCallback(List<DocumentModel> files) {
                if (files == null || files.size() == 0) {
                    llNoRecords.setVisibility(View.VISIBLE);
                    tvNoRecords.setText("No Files Found");
                    tvNoRecords.setTextColor(ContextCompat.getColor(FilePickerActivity.this, android.R.color.darker_gray));
                    ivSad.setColorFilter(ContextCompat.getColor(FilePickerActivity.this, android.R.color.darker_gray));
                    return;
                }
                recyclerView.setAdapter(new FileListAdapter(FilePickerActivity.this, files));
            }
        }, false).execute();
    }

    public void onFileSelected(DocumentModel documentModel) {
        File f = new File(documentModel.getPath());
        float sizeInMb = f.length() / (1024f * 1024f);
        /*if (sizeInMb > 2) {
            toastUtils.show(getString(R.string.file_size_error));
        } else {*/
        Intent intent = new Intent();
        intent.putExtra(INTENT_EXTRA_PDF_PATH, documentModel.getPath());
        setResult(RESULT_OK, intent);
        finish();
//        }
    }
}