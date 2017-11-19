package org.cientopolis.samplers.framework.soundRecord.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import org.cientopolis.samplers.persistence.MultimediaIOManagement;

import java.io.File;
import java.io.IOException;
import java.util.TimerTask;

/**
 * Created by laura on 06/09/17.
 */

public class RecordingService extends Service {
    private MediaRecorder mRecorder;
    private long mStartingTimeMillis = 0;
    private long mElapsedMillis = 0;
    private TimerTask mIncrementTimerTask = null;

    /*MultimediaIOManagement future responsibility*/
    private String mFileName = null;
    private String mFilePath = null;

    /**/
    private static final String LOG_TAG = "RecordingService";
    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void setFileNameAndPath(){
        try {
            File f = MultimediaIOManagement.saveTempAudioFile(getApplicationContext(), MultimediaIOManagement.SOUND_EXTENSION);
            mFileName = f.toString();
            mFilePath = f.getAbsolutePath();

        }
        catch (IOException e){
            e.printStackTrace();
            Log.e("rec service", "error creating temp sound file");
        }
    }

    @Override
    public void onDestroy() {
        if (mRecorder != null) {
            stopRecording();
        }

        super.onDestroy();
    }

    public void startRecording(){
        setFileNameAndPath();
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mRecorder.setOutputFile(mFilePath);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mRecorder.setAudioChannels(1);
        if (MySharedPreferences.getPrefHighQuality(this)) {
            mRecorder.setAudioSamplingRate(44100);
            mRecorder.setAudioEncodingBitRate(192000);
        }

        try {
            mRecorder.prepare();
            mRecorder.start();
            mStartingTimeMillis = System.currentTimeMillis();

            //startTimer();
            //startForeground(1, createNotification());

        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    public String getFileName(){
        return mFileName;
    }

    public int getElapsedMillis(){
        return (int)mElapsedMillis;
    }


    public void stopRecording() {
        mRecorder.stop();
        mElapsedMillis = (System.currentTimeMillis() - mStartingTimeMillis);
        mRecorder.release();
        //Toast.makeText(this, "recording saved to" + mFilePath, Toast.LENGTH_LONG).show();

        //remove notification
        if (mIncrementTimerTask != null) {
            mIncrementTimerTask.cancel();
            mIncrementTimerTask = null;
        }

        mRecorder = null;
    }

    public class LocalBinder extends Binder {
        public RecordingService getService() {
            // Return this instance of LocalService so clients can call public methods
            return RecordingService.this;
        }
    }


}
