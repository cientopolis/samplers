package org.cientopolis.samplers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Xavier on 08/10/2017.
 */

public class RawFilesManagement {

    private static String RAW_PATH;


    public static void setRawPath(String rawPath) {
        RAW_PATH = rawPath;
    }


    public static String copyRawResourceFile(String fileName){
        String resourseName = "";

        // source file
        File source = new File(fileName);
        if (source.exists()) {
            // raw dir
            File rawDir = new File(RAW_PATH);
            rawDir.mkdirs();

            // destination file
            File dest = new File(rawDir, source.getName());

            // Resource name = file name without the extension
            resourseName = dest.getName();

            // clear the extension
            int pos = resourseName.lastIndexOf(".");
            if (pos >= 0) {
                resourseName = resourseName.substring(0, pos);
            }

            // add R.raw prefix
            resourseName = "R.raw." + resourseName;

            // copy the file
            try {
                //System.out.println("source:" + source.getCanonicalPath());
                //System.out.println("dest:" + dest.getCanonicalPath());

                copyFiles(source, dest);
                //Files.copy(source.toPath(),dest.toPath());
            } catch (IOException e) {
                resourseName = "";
                e.printStackTrace();
            }
        }
        else {
            try {
                System.out.println("FILE NOT FOUND: " + source.getCanonicalPath());
            } catch (IOException e) {
                resourseName = "";
                e.printStackTrace();
            }
        }

        return resourseName;
    }


    private static void copyFiles(File source, File dest) throws IOException {
        // Copy source file to dest file
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }
    }
}
