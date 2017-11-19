package org.cientopolis.samplers.persistence;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Xavier on 15/03/2017.
 */

public class MultimediaIOManagement {

    public static final String PHOTO_EXTENSION = "jpg";
    public static final String SOUND_EXTENSION = "mp4";

    private static final String TEMP_DIR = "tmp";

    public static File getTempDir(Context context) {

        File tmpDir = new File(context.getFilesDir(), TEMP_DIR);
        tmpDir.mkdirs();

        return tmpDir;
    }

    public static File saveTempAudioFile(Context context, String fileExtension) throws IOException{
        // Generate the file name with the system current date/time
        String filename = String.format("%d."+fileExtension, System.currentTimeMillis());

        // Get the temp directory
        File directory = MultimediaIOManagement.getTempDir(context);

        // create the file
        return new File(directory,filename);
    }

    public static File saveTempFile(Context context, String fileExtension, byte[] data) throws IOException {

        // Generate the file name with the system current date/time
        String filename = String.format("%d."+fileExtension, System.currentTimeMillis());

        // Get the temp directory
        File directory = MultimediaIOManagement.getTempDir(context);

        // create the file
        File file = new File(directory,filename);

        // file output stream (to write on the file)
        FileOutputStream fos =  new FileOutputStream(file);

        // write data to output stream and close
        fos.write(data);
        fos.close();

        return file;
    }

    /**
     * for testing purposes if needed, add to Manifest permission for writing external storage
     * and let MultimediaIOManagement save file to Galery
     *
     * */
    @SuppressWarnings("unused")
    public static File savePublicTempFile(Context context, String fileExtension, byte[] data) throws IOException {
        ContentResolver cr = context.getContentResolver();

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, String.format("%d."+fileExtension, System.currentTimeMillis()));
        values.put(MediaStore.Images.Media.DISPLAY_NAME, String.format("%d."+fileExtension, System.currentTimeMillis()));
        values.put(MediaStore.Images.Media.DESCRIPTION, String.format("%d."+fileExtension, System.currentTimeMillis()));
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        // Add the date meta data to ensure the image is added at the front of the gallery
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        // Generate the file name with the system current date/time
        Uri url = null;
        String stringUrl = null;    /* value to be returned */

        try {
            url = cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            if (bitmap != null) {
                OutputStream imageOut = cr.openOutputStream(url);
                try {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, imageOut);
                } finally {
                    imageOut.close();
                }

            } else {
                cr.delete(url, null, null);
                url = null;
            }
        } catch (Exception e) {
            if (url != null) {
                cr.delete(url, null, null);
                url = null;
            }
        }

        if (url != null) {
            stringUrl = url.toString();
        }

        return new File(stringUrl);
    }
}
