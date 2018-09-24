package com.psk.customimagepicker.filePicker;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import com.psk.customimagepicker.models.DocumentModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by psk on 9/23/2018.
 */

public class DocScannerTask extends AsyncTask<Void, Void, List<DocumentModel>> {

    final String[] DOC_PROJECTION = {
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA,
            MediaStore.Files.FileColumns.MIME_TYPE,
            MediaStore.Files.FileColumns.SIZE,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Files.FileColumns.TITLE
    };
    private final FileResultCallback<DocumentModel> resultCallback;
    private final Context context;
    private String[] selectionArgs = new String[]{".pdf", ".ppt", ".pptx", ".xlsx", ".xls", ".doc", ".docx", ".txt"};
    private boolean isForImage;

    public DocScannerTask(Context context, FileResultCallback<DocumentModel> fileResultCallback, boolean isForImage) {
        this.context = context;
        this.resultCallback = fileResultCallback;
        this.isForImage = isForImage;
    }

    @Override
    protected List<DocumentModel> doInBackground(Void... voids) {
        ArrayList<DocumentModel> documentModels = new ArrayList<>();
        final String[] projection = DOC_PROJECTION;
        Uri uri;
        String selection = null;
        if (isForImage) {
            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        } else {
            uri = MediaStore.Files.getContentUri("external");
            String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension("pdf");
            selection = MediaStore.Files.FileColumns.MIME_TYPE + "='" + mimeType + "'";
        }
        final Cursor cursor = context.getContentResolver().query(uri,
                projection,
                selection,
                null,
                MediaStore.Files.FileColumns.DATE_ADDED + " DESC");
        if (cursor != null) {
            documentModels = getDocumentFromCursor(cursor);
            cursor.close();
        }
        return documentModels;
    }

    @Override
    protected void onPostExecute(List<DocumentModel> documentModels) {
        super.onPostExecute(documentModels);
        if (resultCallback != null) {
            resultCallback.onResultCallback(documentModels);
        }
    }

    private ArrayList<DocumentModel> getDocumentFromCursor(Cursor data) {
        ArrayList<DocumentModel> documentModels = new ArrayList<>();
        while (data.moveToNext()) {

            int imageId = data.getInt(data.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID));
            String path = data.getString(data.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA));
            String title = data.getString(data.getColumnIndexOrThrow(MediaStore.Files.FileColumns.TITLE));

            if (path != null) {
                DocumentModel documentModel = new DocumentModel(imageId, title, path);

                String mimeType = data.getString(data.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE));
                if (mimeType != null && !TextUtils.isEmpty(mimeType))
                    documentModel.setMimeType(mimeType);
                else {
                    documentModel.setMimeType("");
                }

                documentModel.setSize(data.getString(data.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE)));

                if (!documentModels.contains(documentModel))
                    documentModels.add(documentModel);
            }
        }

        return documentModels;
    }

    boolean contains(String[] types, String path) {
        for (String string : types) {
            if (path.endsWith(string)) return true;
        }
        return false;
    }
}
