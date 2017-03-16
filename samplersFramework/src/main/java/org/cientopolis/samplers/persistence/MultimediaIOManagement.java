package org.cientopolis.samplers.persistence;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Xavier on 15/03/2017.
 */

public class MultimediaIOManagement {

    public static final String PHOTO_EXTENSION = "jpg";

    private static final String TEMP_DIR = "tmp";

    public static File getTempDir(Context context) {

        File tmpDir = new File(context.getFilesDir(), TEMP_DIR);
        tmpDir.mkdirs();

        return tmpDir;
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
}
