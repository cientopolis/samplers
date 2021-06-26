package org.cientopolis.samplers.persistence;

import android.content.Context;
import android.util.Log;
import com.google.gson.Gson;

import org.cientopolis.samplers.bus.BusProvider;
import org.cientopolis.samplers.bus.NewSampleSavedEvent;
import org.cientopolis.samplers.framework.Sample;
import org.cientopolis.samplers.framework.StepResult;
import org.cientopolis.samplers.persistence.gson.GsonProvider;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xavier on 09/02/2017.
 * Implementation of {@link  SampleDAO}
 * It stores each sample as a directory which contains a .json file of the sample object and the multimedia files.
 * The samples are saved in the samples directory in the internal storage (in the app's internal storage directory)
 * For example:
 * Sample id:123456 with 2 photos is saved like this:
 *      /samples/                                        // Samples directory
 *                sample_123456/                         // Sample id:123456 directory
 *                              sample_123456.json       // Sample object as a JSON object
 *                              1524437599776.jpg        // Photo file 1
 *                              1524441664170.jpg        // Photo file 2
 *
 *
 * From Android documentation:
 * "By default, files saved to the internal storage are private to your app, and other apps cannot access them (nor can the user, unless they have root access)"
 * "When the user uninstalls your app, the files saved on the internal storage are removed"
 * See Android documentation for more details about the app's internal storage
 */

class SampleDAOImpl implements SampleDAO {

    // The name of the directory where the samples will be saved
    private static final  String SAMPLES_DIR = "samples";
    // The prefix of the sample file
    private static final  String SAMPLES_PREFIX = "sample_";
    // The extension of the sample file
    private static final  String SAMPLES_EXTENSION = ".json";

    // The Context to get access to the app's internal storage.
    private Context myContext;

    /**
     * Constructor.
     *
     * @param context The {@link Context} to get access to the app's internal storage.
     */
    SampleDAOImpl(Context context) {
        myContext = context;
    }


    /**
     * The name of the directory where the sample is saved
     * It consist of the sample prefix plus the id of the sample
     *
     * @param id The id of the Sample
     * @return The name of the directory where the sample is saved
     */
    private String getSampleDirFileName(Long id) {
        return SAMPLES_PREFIX + String.valueOf(id);
    }

    /**
     * The name of the file that stores the sample object
     * It consist of the sample prefix plus the id of the sample
     *
     * @param id The id of the Sample
     * @return The name of the file that stores the sample object
     */
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

    public Long save(Sample sample) {
        Log.e("SampleDAOImpl", "Saving sample: "+sample.toString());

        boolean succeeded = false;
        boolean newSample = (sample.getId() == null);
        if (newSample) {
            sample.setId(sample.getStartDateTime().getTime());
        }

        String filename = getSampleFileName(sample.getId());

        Gson gson = GsonProvider.newGsonInstance(); //new Gson();
        String jsonObject = gson.toJson(sample);

        Log.e("SampleDAOImpl", "Sample JSON: "+jsonObject);

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

            succeeded = true;

            Log.e("SampleDAOImpl", "sample saved. Id: "+sample.getId());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SampleDAOImpl", "cant save sample");
        }

        if (succeeded && newSample) {
            BusProvider.getInstance().post(new NewSampleSavedEvent(sample));
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
                    Gson gson = GsonProvider.newGsonInstance(); //new Gson();
                    sample = gson.fromJson(br, Sample.class);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return sample;
    }


    @Override
    public Long insert(Sample sample) {
        return save(sample);
    }

    @Override
    public Long update(Sample sample) {
        return save(sample);
    }

    @Override
    public void delete(Sample sample) {

        try {
            File sampleDir = getSampleDir(myContext,sample);
            //deletes all contents before deleting folder
            deleteRecursive(sampleDir);

        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e("SampleDAOImpl", "delete sample complete");

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
        Gson gson = GsonProvider.newGsonInstance(); //new Gson();

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
    public List<Sample> getUnsentSamples() {
        List<Sample> samples = list();
        List<Sample> unsentSamples = new ArrayList<>();

        for (Sample sample: samples) {
            if (!sample.isSent()) {
                unsentSamples.add(sample);
            }
        }

        return unsentSamples;
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
