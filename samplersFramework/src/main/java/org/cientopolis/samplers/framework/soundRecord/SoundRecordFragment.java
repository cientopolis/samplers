package org.cientopolis.samplers.framework.soundRecord;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import org.cientopolis.samplers.R;
import org.cientopolis.samplers.framework.StepResult;
import org.cientopolis.samplers.framework.base.StepFragment;
import org.cientopolis.samplers.framework.soundRecord.service.RecordingItem;
import org.cientopolis.samplers.framework.soundRecord.service.RecordingService;

/**
 * Created by laura on 06/09/17.
 */

public class SoundRecordFragment extends StepFragment {

    private Chronometer mChronometer;
    private TextView mRecordingPrompt;
   // private Button mRecordButton;
    private ImageButton mPlayButton;
    private ImageButton mRecordButton;
    private String fileName;
    boolean mBound = false;
    private RecordingItem mRecordingItem;

    private int mRecordPromptCount = 0;
    long timeWhenPaused = 0; //stores time when user clicks pause button

    private boolean mStartRecording = true;
    private boolean mPauseRecording = true;
    private RecordingService mRecordingService;

    //for playback
    private MediaPlayer mMediaPlayer = null;
    private SeekBar mSeekBar = null;

    private ServiceConnection mConnection = new ServiceConnection(){
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            RecordingService.LocalBinder binder = (RecordingService.LocalBinder) service;
            mRecordingService = binder.getService();
            mRecordingService.startRecording();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //
        }
    };


    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_sound_record;
    }

    @Override
    protected void onCreateViewStepFragment(View rootView, Bundle savedInstanceState) {
        mChronometer = (Chronometer) rootView.findViewById(R.id.chronometer);
        mRecordingPrompt = (TextView) rootView.findViewById(R.id.recording_status_text);
        //assign listeners
        mRecordButton = (ImageButton) rootView.findViewById(R.id.btnStart);
        mRecordButton.setImageResource(R.drawable.ic_launcher);
      /**  mRecordButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    //
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    //
                }
                return false;
            }

        });*/
        mRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRecord(mStartRecording);
                mStartRecording = !mStartRecording;
            }
        });
        mPlayButton = (ImageButton) rootView.findViewById(R.id.btn_play);
        mPlayButton.setImageResource(R.drawable.ic_media_play);
     /*   mPlayButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                try {*/

                    /*android.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();*/
                  //  PlaybackFragment playbackFragment = new PlaybackFragment().newInstance(mRecordingItem);
                    /*FragmentTransaction transaction = getFragmentManager().beginTransaction();*/
                 //   playbackFragment.show(getFragmentManager(),"");


                            /*((FragmentActivity) getActivity())
                            .getSupportFragmentManager()
                            .beginTransaction();*/

                    /*playbackFragment.show(transaction, "dialog_playback");*/

              /*  } catch (Exception e) {
                    Log.e("playback exc", "exception", e);
                }
            }
        });*/
        mSeekBar = (SeekBar) rootView.findViewById(R.id.seekbar);
        ColorFilter filter = new LightingColorFilter
                (getResources().getColor(R.color.primary), getResources().getColor(R.color.primary));
        mSeekBar.getProgressDrawable().setColorFilter(filter);
        mSeekBar.getThumb().setColorFilter(filter);
    }

    @Override
    protected SoundRecordStep getStep() {
        return (SoundRecordStep) step;
    }

    @Override
    protected boolean validate() {
        return true;
    }

    @Override
    protected StepResult getStepResult() {
        return new SoundRecordStepResult(getStep().getId(),fileName);
    }

    /*functionality*/
    private void onRecord(boolean start){

        Intent intent = new Intent(getActivity(), RecordingService.class);
        /** Permission for audio source*/
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, 10);

        } else {

            /**comment to exclude permission request*/

            if (start) {
                // start recording
                /* this change image from "start" to "stop" */
                mRecordButton.setImageResource(R.drawable.ic_media_stop);
                /*disable playback button*/
                mPlayButton.setEnabled(false);
                // start Chronometer
                mChronometer.setBase(SystemClock.elapsedRealtime());
                mChronometer.start();
                mChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                    @Override
                    public void onChronometerTick(Chronometer chronometer) {
                        if (mRecordPromptCount == 0) {
                            mRecordingPrompt.setText(getString(R.string.record_in_progress) + ".");
                        } else if (mRecordPromptCount == 1) {
                            mRecordingPrompt.setText(getString(R.string.record_in_progress) + "..");
                        } else if (mRecordPromptCount == 2) {
                            mRecordingPrompt.setText(getString(R.string.record_in_progress) + "...");
                            mRecordPromptCount = -1;
                        }

                        mRecordPromptCount++;
                    }
                });
                //start RecordingService
                getActivity().bindService(intent, mConnection, getActivity().BIND_AUTO_CREATE);
                //keep screen on while recording
                getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

                mRecordingPrompt.setText(getString(R.string.record_in_progress) + ".");
                mRecordPromptCount++;

            } else {
                //stop recording
                mRecordButton.setImageResource(R.drawable.ic_launcher);
                mChronometer.stop();
                mChronometer.setBase(SystemClock.elapsedRealtime());
                timeWhenPaused = 0;
                mRecordingPrompt.setText(getString(R.string.record_prompt));
                /*enable playback button*/
                mPlayButton.setEnabled(true);
                /*and seekbar callbacks*/
                //getActivity().stopService(intent);
                if(mBound) {
                    fileName = mRecordingService.getFileName();
                    mRecordingItem = new RecordingItem();
                    mRecordingItem.setFilePath(fileName);
                    mRecordingItem.setName("sound recorded");
                    mRecordingService.stopRecording();
                    /*get Duration*/
                    int millSecond = mRecordingService.getElapsedMillis();
                    mRecordingItem.setLength(millSecond);

                    getActivity().unbindService(mConnection);
                    mBound = false;
                }
                //allow the screen to turn off again once recording is finished
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }

        }
    }

    /*test for playing recorded audio*/

    private void startPlaying() {
        //
    }

}
