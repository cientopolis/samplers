package org.cientopolis.samplers.persistence;

import android.content.Context;
import android.util.Log;
import com.google.gson.Gson;
import org.cientopolis.samplers.model.Sample;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xavier on 09/02/2017.
 */

class SampleDAOImpl implements DAO<Sample, Long> {

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

    private File getSamplesDir(Context context) throws Exception {

        File fileDir = new File(context.getFilesDir(),SAMPLES_DIR);


        if (!fileDir.exists()) {
            if (!fileDir.mkdirs()) {
                throw new Exception("cant create samples dir");
            }
        }

        return fileDir;

    }

    private File getSampleDir(Context context, Sample sample) throws Exception {

        String filename = getSampleDirFileName(sample.getId());
        File fileDir = new File(getSamplesDir(context),filename);


        if (!fileDir.exists()) {
            if (!fileDir.mkdirs()) {
                throw new Exception("cant create samples dir");
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
            File fileSample = new File(getSampleDir(myContext,sample),filename);

            outputStream = new FileOutputStream(fileSample);
            //outputStream = myContext.openFileOutput(fileDir.getAbsolutePath()+"/"+filename, Context.MODE_PRIVATE);
            outputStream.write(jsonObject.getBytes());
            outputStream.close();
            Log.e("SampleDAOImpl", "sample saved");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SampleDAOImpl", "cant save sample");
        }

        return sample.getId();
    }

    @Override
    public Sample find(Long key) {
        String filename = getSampleFileName(key);
        Sample sample = null;

        try {
            File fileSample = new File(getSamplesDir(myContext),filename);

            BufferedReader br = new BufferedReader(new FileReader(fileSample));

            Gson gson = new Gson();
            sample = gson.fromJson(br,Sample.class);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return sample;
    }

    @Override
    public boolean delete(Sample object) {
        // // TODO: 28/02/2017 implement delete sample
        return false;
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
                dirSample = new File(fileDir, sampleName);

                String[] files = dirSample.list();
                for (String fileName: files) {
                    if (fileName.endsWith(SAMPLES_EXTENSION)) {
                        fileSample = new File(dirSample, fileName);

                        br = new BufferedReader(new FileReader(fileSample));

                        sample = gson.fromJson(br, Sample.class);

                        result.add(sample);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return result;
    }
}
