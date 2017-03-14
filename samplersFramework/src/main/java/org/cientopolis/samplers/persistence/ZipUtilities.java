package org.cientopolis.samplers.persistence;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by Xavier on 11/03/2017.
 * Abstract class to provide file compresion (zip) methods
 * It uses the standar package java.util.zip
 */
public abstract class ZipUtilities {

    private static final int BUFFER_SIZE = 1024;

    /**
     * Zip a list of files into one zip file
     *
     * @param files Array with full path of the files you want to put in the zip file.
     * @param zipFile Full path of the zip file to create.
     * @throws IOException
     */
    public static void zipFiles(String[] files, String zipFile) throws IOException {
        BufferedInputStream origin;
        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile)));
        try {
            byte data[] = new byte[BUFFER_SIZE];

            for (int i = 0; i < files.length; i++) {

                FileInputStream fi = new FileInputStream(files[i]);
                origin = new BufferedInputStream(fi, BUFFER_SIZE);
                try {
                    ZipEntry entry = new ZipEntry(files[i].substring(files[i].lastIndexOf("/") + 1));
                    out.putNextEntry(entry);
                    int count;
                    while ((count = origin.read(data, 0, BUFFER_SIZE)) != -1) {
                        out.write(data, 0, count);
                    }
                }
                finally {
                    origin.close();
                }
            }
        }
        finally {
            out.close();
        }
    }


    /**
     * Zip all the files in the given directory into a zip file.
     * It calls to {@link ZipUtilities#zipFiles}  to do the work
     *
     * @param directory The directory where the files to zip are.
     * @param zipFile Full path of the zip file to create.
     * @throws IOException
     */
    public static void zipFilesInDirectory(File directory, String zipFile) throws IOException {
        String[] fileNames = directory.list();
        int length = fileNames.length;
        String[] fullPathList = new String[length];

        // iterate through the fileNames to get the full path of each file
        for (int i = 0; i < length; i++) {
            File file = new File(directory, fileNames[i]);
            fullPathList[i] = file.getAbsolutePath();
        }

        // call zipFiles to do the work
        zipFiles(fullPathList, zipFile);
    }


}
