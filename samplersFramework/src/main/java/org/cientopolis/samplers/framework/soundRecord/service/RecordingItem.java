package org.cientopolis.samplers.framework.soundRecord.service;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * Created by laura on 01/10/17.
 */

public class RecordingItem implements Serializable {

    private String mFilePath; //file path
    private int mLength = 0; // length of recording in seconds
    private long minutes = 0;
    private long seconds = 0;

    private long mTime; // date/time of the recording

    public RecordingItem()
    {
    }

    public String getFilePath() {
        return mFilePath;
    }

    public void setFilePath(String filePath) {
        mFilePath = filePath;
    }

    public int getLength() {
        return mLength;
    }

    public void setLength(int length) {
        mLength = length;

        minutes = TimeUnit.MILLISECONDS.toMinutes(mLength);
        seconds = TimeUnit.MILLISECONDS.toSeconds(mLength)
                - TimeUnit.MINUTES.toSeconds(minutes);
    }

    public long getMinutes() {
        return minutes;
    }

    public long getSeconds() {
        return seconds;
    }

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        mTime = time;
    }


}
