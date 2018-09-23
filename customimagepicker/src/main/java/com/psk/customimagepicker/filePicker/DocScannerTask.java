package com.psk.customimagepicker.filePicker;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.psk.customimagepicker.models.PdfDocumentModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by psk on 9/23/2018.
 */

public class DocScannerTask extends AsyncTask<Void, Void, List<PdfDocumentModel>> {

    final String[] DOC_PROJECTION = {
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA,
            MediaStore.Files.FileColumns.MIME_TYPE,
            MediaStore.Files.FileColumns.SIZE,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Files.FileColumns.TITLE
    };
    private final FileResultCallback<PdfDocumentModel> resultCallback;
    private final Context context;
    String[] selectionArgs = new String[]{".pdf", ".ppt", ".pptx", ".xlsx", ".xls", ".doc", ".docx", ".txt"};

    public DocScannerTask(Context context, FileResultCallback<PdfDocumentModel> fileResultCallback) {
        this.context = context;
        this.resultCallback = fileResultCallback;
    }

    @Override
    protected List<PdfDocumentModel> doInBackground(Void... voids) {
        ArrayList<PdfDocumentModel> pdfDocumentModels = new ArrayList<>();
        final String[] projection = DOC_PROJECTION;
        final Cursor cursor = context.getContentResolver().query(MediaStore.Files.getContentUri("external"),
                projection,
                MediaStore.Files.FileColumns.MIME_TYPE + "='application/pdf'",
                null,
                MediaStore.Files.FileColumns.DATE_ADDED + " DESC");
        if (cursor != null) {
            pdfDocumentModels = getDocumentFromCursor(cursor);
            cursor.close();
        }


        return pdfDocumentModels;
    }

    @Override
    protected void onPostExecute(List<PdfDocumentModel> pdfDocumentModels) {
        super.onPostExecute(pdfDocumentModels);
        if (resultCallback != null) {
            resultCallback.onResultCallback(pdfDocumentModels);
        }
    }

    private ArrayList<PdfDocumentModel> getDocumentFromCursor(Cursor data) {
        ArrayList<PdfDocumentModel> pdfDocumentModels = new ArrayList<>();
        while (data.moveToNext()) {

            int imageId = data.getInt(data.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID));
            String path = data.getString(data.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA));
            String title = data.getString(data.getColumnIndexOrThrow(MediaStore.Files.FileColumns.TITLE));

            if (path != null && contains(selectionArgs, path)) {
                PdfDocumentModel pdfDocumentModel = new PdfDocumentModel(imageId, title, path);

                String mimeType = data.getString(data.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE));
                if (mimeType != null && !TextUtils.isEmpty(mimeType))
                    pdfDocumentModel.setMimeType(mimeType);
                else {
                    pdfDocumentModel.setMimeType("");
                }

                pdfDocumentModel.setSize(data.getString(data.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE)));

                if (!pdfDocumentModels.contains(pdfDocumentModel))
                    pdfDocumentModels.add(pdfDocumentModel);
            }
        }

        return pdfDocumentModels;
    }

    boolean contains(String[] types, String path) {
        for (String string : types) {
            if (path.endsWith(string)) return true;
        }
        return false;
    }
}
