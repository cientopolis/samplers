package org.cientopolis.samplers.persistence;

import android.content.Context;
import android.util.Log;
import com.google.gson.Gson;

import org.cientopolis.samplers.framework.photo.PhotoStepResult;
import org.cientopolis.samplers.framework.Sample;
import org.cientopolis.samplers.framework.StepResult;
import org.cientopolis.samplers.framework.soundRecord.SoundRecordStepResult;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xavier on 09/02/2017.
 */

class SampleDAOImpl implements SampleDAO {

    private static final  String SAMPLES_DIR = "samples";
    private static final  String SAMPLES_PREFIX = "sample_";
    private static final  String SAMPLES_EXTENSION = ".json";

    private Context myContext;

    SampleDAOImpl(Context context) {
        myContext = context;
    }


    private String getSampleDirFileName(Long id) {
        return SAMPLES_PREFIX + String.valueOf(id);
    }

    private String getSampleFileName(Long id) {
        return SAMPLES_PREFIX + String.valueOf(id) + SAMPLES_EXTENSION;
    }

    @Override
    public File getSamplesDir() {
        File samplesDir = null;
        try {
            samplesDir = getSamplesDir(myContext);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return samplesDir;
    }

    private File getSamplesDir(Context context) throws IOException {

        File fileDir = new File(context.getFilesDir(),SAMPLES_DIR);


        if (!fileDir.exists()) {
            if (!fileDir.mkdirs()) {
                throw new IOException("cant create samples dir");
            }
        }

        return fileDir;

    }

    private File getSampleDir(Context context, Sample sample) throws IOException {

        String filename = getSampleDirFileName(sample.getId());
        File fileDir = new File(getSamplesDir(context),filename);


        if (!fileDir.exists()) {
            if (!fileDir.mkdirs()) {
                throw new IOException("cant create samples dir");
            }
        }

        return fileDir;

    }

    @Override
    public Long save(Sample sample) {
        sample.setId(sample.getStartDateTime().getTime());

        String filename = getSampleFileName(sample.getId());

        Gson gson = new Gson();
        String jsonObject = gson.toJson(sample);

        FileOutputStream outputStream;

        try {
            // Create the sample directory
            File sampleDir = getSampleDir(myContext,sample);

            // Create the sample file
            File fileSample = new File(sampleDir,filename);

            // Move the multimedia files to the sample directory
            List<StepResult> results = sample.getStepResults();
            for (StepResult stepResult: results) {
                if (stepResult.hasMultimediaFile()) {
                    if (!moveMediaToSampleDirectory(stepResult.getMultimediaFileName(),sampleDir)) {
                        throw new Exception("Cant move multimedia file");
                    }
                }
            }

            outputStream = new FileOutputStream(fileSample);
            outputStream.write(jsonObject.getBytes());
            outputStream.close();
            Log.e("SampleDAOImpl", "sample saved");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SampleDAOImpl", "cant save sample");
        }

        return sample.getId();
    }

    private boolean moveMediaToSampleDirectory(String mediaFileName, File sampleDirectory) {
        boolean ok;

        File mediaTempDir = MultimediaIOManagement.getTempDir(myContext);
        File mediaFileFrom = new File(mediaTempDir, mediaFileName);

        if (mediaFileFrom.exists()) {
            String fileName = mediaFileFrom.getName();
            File photoFileTo = new File(sampleDirectory, fileName);

            // Move the file
            ok = mediaFileFrom.renameTo(photoFileTo);

            if (!ok)
                Log.e("SampleDAOImpl", "renameTo failed");
        }
        else {
            ok = true; // Assume already moved
            Log.e("SampleDAOImpl", "media file dont exists: " + mediaFileFrom.getAbsolutePath());
        }

        return ok;
    }

    @Override
    public Sample find(Long key) {
        String sampleDirectoryName = getSampleDirFileName(key);
        Sample sample = null;

        try {
            //recovers sample directory
            File sampleDirectory = new File(getSamplesDir(myContext),sampleDirectoryName);
            //list directory
            String[] files = sampleDirectory.list();
            for (String fileName: files) {
                //iterates the directory until it finds a file with .json extension
                if (fileName.endsWith(SAMPLES_EXTENSION)) {
                    File sampleFile = new File(sampleDirectory, fileName);
                    BufferedReader br = new BufferedReader(new FileReader(sampleFile));
                    Gson gson = new Gson();
                    sample = gson.fromJson(br, Sample.class);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return sample;
    }


    @Override
    public boolean delete(Sample sample) {
        Boolean ok = false;
        try {
            File sampleDir = getSampleDir(myContext,sample);
            //deletes all contents before deleting folder
            deleteRecursive(sampleDir);
            ok = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e("SampleDAOImpl", "delete sample complete + ok = "+String.valueOf(ok));
        return ok;
    }

    //helper function
    private void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);

        fileOrDirectory.delete();
    }

    @Override
    public List<Sample> list() {
        List<Sample> result = new ArrayList<>();

        Sample sample;
        File dirSample;
        File fileSample;
        BufferedReader br;
        Gson gson = new Gson();

        try {
            File fileDir = getSamplesDir(myContext);
            String[] samplesDirs = fileDir.list();

            for (String sampleName: samplesDirs) {
                Log.e("samplesDirs",sampleName);

                dirSample = new File(fileDir, sampleName);

                // Check if it is a sample directory (could be a zipped sample)
                if (dirSample.isDirectory()) {

                    String[] files = dirSample.list();
                    for (String fileName : files) {
                        if (fileName.endsWith(SAMPLES_EXTENSION)) {
                            fileSample = new File(dirSample, fileName);

                            br = new BufferedReader(new FileReader(fileSample));

                            sample = gson.fromJson(br, Sample.class);

                            result.add(sample);
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return result;
    }

    @Override
    public File getSampleDir(Sample sample) {
        File sampleDir = null;
        try {
            sampleDir = getSampleDir(myContext,sample);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sampleDir;
    }
}
