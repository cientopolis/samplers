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

    private Context myContext;

    SampleDAOImpl(Context context) {
        myContext = context;
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

    @Override
    public Long save(Sample object) {
        object.setId(object.getStartDateTime().getTime());

        String filename = SAMPLES_PREFIX + String.valueOf(object.getId());

        Gson gson = new Gson();
        String jsonObject = gson.toJson(object);

        FileOutputStream outputStream;

        try {
            File fileSample = new File(getSamplesDir(myContext),filename);

            outputStream = new FileOutputStream(fileSample);
            //outputStream = myContext.openFileOutput(fileDir.getAbsolutePath()+"/"+filename, Context.MODE_PRIVATE);
            outputStream.write(jsonObject.getBytes());
            outputStream.close();
            Log.e("SampleDAOImpl", "sample saved");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SampleDAOImpl", "cant save sample");
        }

        return object.getId();
    }

    @Override
    public Sample find(Long key) {
        String filename = SAMPLES_PREFIX + String.valueOf(key);
        Sample sample = null;

        //StringBuilder text = new StringBuilder();
        try {
            File fileSample = new File(getSamplesDir(myContext),filename);

            BufferedReader br = new BufferedReader(new FileReader(fileSample));
/*            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
            }
            br.close();
*/
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
        List<Sample> result = new ArrayList<Sample>();

        Sample sample;
        File fileSample;
        BufferedReader br;
        Gson gson = new Gson();

        try {
            File fileDir = getSamplesDir(myContext);
            String[] samples = fileDir.list();

            for (String sampleName: samples) {
                fileSample = new File(fileDir, sampleName);

                br = new BufferedReader(new FileReader(fileSample));

                sample = gson.fromJson(br, Sample.class);

                result.add(sample);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return result;
    }
}
