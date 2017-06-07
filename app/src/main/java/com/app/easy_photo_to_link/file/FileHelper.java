package com.app.easy_photo_to_link.file;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Владислав on 16.05.2017.
 */

public class FileHelper {
    private final Context context;

    public FileHelper(Context context) {
        this.context = context;
    }

    public File getFileFromUriByIS(Uri photoPath) {
        InputStream inputStream;
        File finishFile = null;
        Bitmap bit;
        try {
            inputStream = context.getContentResolver().openInputStream(photoPath);
            bit = BitmapFactory.decodeStream(inputStream);
            finishFile = persistImage(bit, "tratata");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return finishFile;
    }

    private File persistImage(Bitmap bitmap, String name) {
        File filesDir = context.getFilesDir();
        File imageFile = new File(filesDir, name + ".jpg");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
        }
        return imageFile;
    }
    public File getFileFromUri(Uri photoPath) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(photoPath,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return new File(cursor.getString(column_index));
        }catch (Exception ex)
        {
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
        }

    }
}
