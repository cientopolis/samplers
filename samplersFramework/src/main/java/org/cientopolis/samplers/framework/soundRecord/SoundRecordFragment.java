package org.cientopolis.samplers.framework.soundRecord;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import org.cientopolis.samplers.R;
import org.cientopolis.samplers.framework.StepResult;
import org.cientopolis.samplers.framework.StepFragment;
import org.cientopolis.samplers.framework.StepFragmentInteractionListener;
import org.cientopolis.samplers.framework.soundRecord.service.RecordingItem;
import org.cientopolis.samplers.framework.soundRecord.service.RecordingService;
import org.cientopolis.samplers.ui.ErrorMessaging;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * Created by laura on 06/09/17.
 * A simple {@link StepFragment} subclass to complete a SoundRecordStep and record a sound.
 * Activities that contain this fragment must implement the {@link StepFragmentInteractionListener}
 * interface to handle interaction events.
 * Use the {@link StepFragment#newInstance} factory method to create an instance of this fragment.
 *
 *
 * */
public class SoundRecordFragment extends StepFragment {

    private static final String KEY_RECORDING_ITEM = "org.cientopolis.samplers.SoundRecordFragment_RecordingItem";
    private static final String KEY_STATE = "org.cientopolis.samplers.SoundRecordFragment_State";

    private static final int START_RECORDING_RESOURSE_ID = R.drawable.ic_mic_black_36dp;
    private static final int STOP_RECORDING_RESOURSE_ID = R.drawable.ic_stop_black_36dp;

    private static final int PLAY_RESOURSE_ID = R.drawable.ic_play_arrow_black_36dp;
    private static final int PAUSE_RESOURSE_ID = R.drawable.ic_pause_black_36dp;

    private static final int REQUEST_RECORDING_PERMISSION = 10;

    private UIState mUIState;

    // FOR RECORDING ------------------
    private Chronometer mChronometer;
    private TextView lb_recording_status;
    private ImageButton bt_recordStop;

    private MyServiceConnection mConnection;
    private RecordingService mRecordingService;
    boolean mBound = false;
    private RecordingItem mRecordingItem;

    private ProgressBar progressBar;
    private Drawable progressBarDraw;

    // FOR PLAYBACK ------------------------------
    private Handler mHandler = new Handler();
    private MediaPlayer mMediaPlayer = null;
    private ImageButton bt_playStop;
    private SeekBar mSeekBar = null;
    private TextView lb_currentProgress = null;
    private TextView lb_fileLength = null;

    //stores whether or not the mediaplayer is currently playing audio
    //private boolean isPlaying = false;
    //private long timeWhenPaused = 0; //stores time when user clicks pause button



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mRecordingItem = (RecordingItem) savedInstanceState.getSerializable(KEY_RECORDING_ITEM);
            mUIState = (UIState) savedInstanceState.getSerializable(KEY_STATE);
            //mConnection = (MyServiceConnection) savedInstanceState.getSerializable(KEY_CONNECTION);


        }
        else {
            mUIState = new IdleUIState();

        }
        mConnection = new MyServiceConnection();
    }

    @Override
    public void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e("SoundRecordFragment", "onSaveInstanceState: IN");
        outState.putSerializable(KEY_RECORDING_ITEM,mRecordingItem);
        //outState.putSerializable(KEY_STATE,mUIState);
        outState.putSerializable(KEY_STATE, mUIState);
        //outState.putSerializable(KEY_CONNECTION, mConnection);
        Log.e("SoundRecordFragment", "onSaveInstanceState: OUT");

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_sound_record;
    }

    @Override
    protected void onCreateViewStepFragment(View rootView, Bundle savedInstanceState) {

        // Record ----------------------
        bt_recordStop = (ImageButton) rootView.findViewById(R.id.bt_recordStop);
        bt_recordStop.setImageAlpha(getResources().getInteger(R.integer.image_buttons_alpha));
        mChronometer = (Chronometer) rootView.findViewById(R.id.chronometer);
        lb_recording_status = (TextView) rootView.findViewById(R.id.lb_recording_status);

        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        progressBarDraw = getResources().getDrawable(R.drawable.record_progress_bar);
        // Get the Drawable custom_progressbar

        // set the drawable as progress drawable
        //progressBar.setProgressDrawable(draw);
        //progressBar.setIndeterminateDrawable(progressBarDraw);
        //progressBar.setIndeterminateDrawable(progressBarDraw);

        progressBar.setIndeterminate(true);
        progressBar.setIndeterminateDrawable(null);

        // Playback -----------------------
        lb_fileLength = (TextView) rootView.findViewById(R.id.lb_fileLength);
        lb_currentProgress = (TextView) rootView.findViewById(R.id.lb_currentProgress);
        bt_playStop = (ImageButton) rootView.findViewById(R.id.bt_playStop);
        bt_playStop.setImageAlpha(getResources().getInteger(R.integer.image_buttons_alpha));

        mSeekBar = (SeekBar) rootView.findViewById(R.id.seekbar);
        // TODO: rename color name and set it in layout xml file
        ColorFilter filter = new LightingColorFilter(getResources().getColor(R.color.progress_bar_primary), getResources().getColor(R.color.progress_bar_primary));
        mSeekBar.getProgressDrawable().setColorFilter(filter);
        mSeekBar.getThumb().setColorFilter(filter);
        mSeekBar.setOnSeekBarChangeListener(new SeekBarChangeListener());
    }

    @Override
    protected SoundRecordStep getStep() {
        return (SoundRecordStep) step;
    }

    @Override
    protected boolean validate() {
        boolean ok = true;

        if (mRecordingItem == null) {
            ok = false;
            ErrorMessaging.showValidationErrorMessage(getActivity(),  getResources().getString(R.string.error_must_record_audio));
        }

        return ok;
    }

    @Override
    protected StepResult getStepResult() {
        String fileName = mRecordingItem.getFilePath();
        int cut = fileName.lastIndexOf('/');
        if (cut != -1) {
            fileName = fileName.substring(cut + 1);
        }
        return new SoundRecordStepResult(getStep().getId(),fileName);
    }


    @Override
    public void onPause() {
        super.onPause();

        if (mMediaPlayer != null) {
            stopPlaying();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mMediaPlayer != null) {
            stopPlaying();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Intent intent = new Intent(getActivity(), RecordingService.class);
        getActivity().startService(intent);
        getActivity().bindService(intent, mConnection, Activity.BIND_AUTO_CREATE);
    }

    @Override
    public void onStop() {
        super.onStop();
        // Unbind from the service
        if (mBound) {
            getActivity().unbindService(mConnection);
            mBound = false;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("SoundRecordFragment", "onResume()");
        //start RecordingService

        mUIState.setUp(this);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void requestRecordingPermission() {
        if (getActivity().checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORDING_PERMISSION);
            //  result on onRequestPermissionsResult()

        } else { //we already have permission, instantiate the fragment

            startRecording();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == REQUEST_RECORDING_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startRecording();
            } else {
                // Send a message to the user that we need permissions to access the location to get the gps position
                ErrorMessaging.showValidationErrorMessage(getActivity().getApplicationContext(),getResources().getString(R.string.recording_permissions_needed));
            }
        }
    }


    private void startRecording() {
        mRecordingService.startRecording();

        mUIState = new RecordingUIState();
        mUIState.setUp(this);
    }

    private void stopRecording() {
        //if(mBound) {
        mRecordingService.stopRecording();

        mRecordingItem = mRecordingService.getRecordingItem();

        mUIState = new IdleUIState();
        mUIState.setUp(this);

    }

    private void startPlaying() {

        mMediaPlayer = new MediaPlayer();

        try {
            mMediaPlayer.setDataSource(mRecordingItem.getFilePath());
            mMediaPlayer.prepare();
            mSeekBar.setMax(mMediaPlayer.getDuration());

            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mMediaPlayer.start();
                }
            });
        } catch (IOException e) {
            Log.e("SoundRecordFragment", "prepare() failed");
        }

        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopPlaying();
            }
        });

        updateSeekBar();

        mUIState = new PlayingUIState();
        mUIState.setUp(this);


    }

    private void pausePlaying() {

        mHandler.removeCallbacks(mRunnable);
        mMediaPlayer.pause();

        mUIState = new PlayingPausedUIState();
        mUIState.setUp(this);
    }

    private void resumePlaying() {

        mHandler.removeCallbacks(mRunnable);
        mMediaPlayer.start();
        updateSeekBar();

        mUIState = new PlayingUIState();
        mUIState.setUp(this);
    }

    private void stopPlaying() {

        mHandler.removeCallbacks(mRunnable);
        mMediaPlayer.stop();
        mMediaPlayer.reset();
        mMediaPlayer.release();
        mMediaPlayer = null;

        mUIState = new IdleUIState();
        mUIState.setUp(this);
    }

    private void prepareMediaPlayerFromPoint(int progress) {
        //set mediaPlayer to start from middle of the audio file

        mMediaPlayer = new MediaPlayer();

        try {
            mMediaPlayer.setDataSource(mRecordingItem.getFilePath());
            mMediaPlayer.prepare();
            mSeekBar.setMax(mMediaPlayer.getDuration());
            mMediaPlayer.seekTo(progress);

            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopPlaying();
                }
            });

        } catch (IOException e) {
            Log.e("SoundRecordFragment", "prepare() failed");
        }

        //keep screen on while playing audio
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }




    private class SeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if(mMediaPlayer != null && fromUser) {
                mMediaPlayer.seekTo(progress);
                mHandler.removeCallbacks(mRunnable);

                long minutes = TimeUnit.MILLISECONDS.toMinutes(mMediaPlayer.getCurrentPosition());
                long seconds = TimeUnit.MILLISECONDS.toSeconds(mMediaPlayer.getCurrentPosition())
                        - TimeUnit.MINUTES.toSeconds(minutes);
                lb_currentProgress.setText(String.format("%02d:%02d", minutes,seconds));

                updateSeekBar();

            } else if (mMediaPlayer == null && fromUser) {
                prepareMediaPlayerFromPoint(progress);
                updateSeekBar();
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            if(mMediaPlayer != null) {
                // remove message Handler from updating progress bar
                mHandler.removeCallbacks(mRunnable);
            }
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            if (mMediaPlayer != null) {
                mHandler.removeCallbacks(mRunnable);
                mMediaPlayer.seekTo(seekBar.getProgress());

                long minutes = TimeUnit.MILLISECONDS.toMinutes(mMediaPlayer.getCurrentPosition());
                long seconds = TimeUnit.MILLISECONDS.toSeconds(mMediaPlayer.getCurrentPosition())
                        - TimeUnit.MINUTES.toSeconds(minutes);
                lb_currentProgress.setText(String.format("%02d:%02d", minutes,seconds));
                updateSeekBar();
            }
        }
    }

    //updating mSeekBar
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if(mMediaPlayer != null){

                int mCurrentPosition = mMediaPlayer.getCurrentPosition();
                mSeekBar.setProgress(mCurrentPosition);

                long minutes = TimeUnit.MILLISECONDS.toMinutes(mCurrentPosition);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(mCurrentPosition)
                        - TimeUnit.MINUTES.toSeconds(minutes);
                lb_currentProgress.setText(String.format("%02d:%02d", minutes, seconds));

                updateSeekBar();
            }
        }
    };

    private void updateSeekBar() {
        mHandler.postDelayed(mRunnable, 1000);
    }

    private class RecordClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // Need to request permission at run time
                requestRecordingPermission();
            }
            else {
                startRecording();
            }
        }
    }

    private class StopRecordClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            stopRecording();
        }
    }

    private class PlayClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Log.e("SoundRecordFragment", "Play click");
            if (mRecordingItem != null) {
                startPlaying(); //start from beginning

            }
        }
    }

    private class PausePlayingClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Log.e("SoundRecordFragment", "Pause click");
            if (mMediaPlayer != null) {
                pausePlaying();
            }
        }
    }

    private class ResumeClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Log.e("SoundRecordFragment", "Resume click");
            if (mRecordingItem != null) {
                resumePlaying(); //resume the currently paused MediaPlayer

            }
        }
    }

    private class MyServiceConnection implements ServiceConnection, Serializable {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            RecordingService.LocalBinder binder = (RecordingService.LocalBinder) service;
            mRecordingService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mRecordingService = null;
            mBound = false;
        }
    }

    private interface UIState extends Serializable {
        void setUp(SoundRecordFragment fragment);
    }

    private class IdleUIState implements UIState {

        @Override
        public void setUp(final SoundRecordFragment fragment) {
            fragment.bt_recordStop.setImageResource(START_RECORDING_RESOURSE_ID);

            fragment.bt_recordStop.setOnClickListener(fragment.new RecordClickListener());
            fragment.bt_recordStop.setEnabled(true);

            fragment.progressBar.setIndeterminateDrawable(null);

            fragment.mChronometer.stop();
            fragment.mChronometer.setBase(SystemClock.elapsedRealtime());
            //timeWhenPaused = 0;
            fragment.lb_recording_status.setText(fragment.getString(R.string.record_prompt));

            // enable playback button
            fragment.bt_playStop.setImageResource(PLAY_RESOURSE_ID);

            fragment.bt_playStop.setEnabled(true);
            fragment.bt_playStop.setOnClickListener(fragment.new PlayClickListener());
                /*and seekbar callbacks*/

            fragment.lb_currentProgress.setText(String.format("%02d:%02d",0,0));
            if (fragment.mRecordingItem != null)
                fragment.lb_fileLength.setText(String.format("%02d:%02d", fragment.mRecordingItem.getMinutes(),fragment.mRecordingItem.getSeconds()));
            fragment.mSeekBar.setProgress(0);

            //allow the screen to turn off again once recording is finished
            if (fragment.getActivity() != null)
                fragment.getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            Log.e("SoundRecordFragment", "IDLE");
        }


        private void writeObject(java.io.ObjectOutputStream out) throws IOException{
            // Needed, to override the default one that tries to serialize the SoundRecordFragment class
        }

    }

    private class RecordingUIState extends IdleUIState { // implements UIState {
        private long startingTime = 0;

        @Override
        public void setUp(SoundRecordFragment fragment) {
            /* this change image from "start" to "stop" */
            fragment.bt_recordStop.setImageResource(STOP_RECORDING_RESOURSE_ID);
            fragment.bt_recordStop.setOnClickListener(fragment.new StopRecordClickListener());

            fragment.progressBar.setIndeterminateDrawable(progressBarDraw);

            /*disable playback button*/
            fragment.bt_playStop.setEnabled(false);


            // start Chronometer
            if (startingTime == 0)
                startingTime = SystemClock.elapsedRealtime();
            fragment.mChronometer.setBase(startingTime);
            fragment.mChronometer.start();

            //keep screen on while recording
            if (fragment.getActivity() != null)
                fragment.getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            fragment.lb_recording_status.setText(fragment.getString(R.string.record_in_progress));

            Log.e("SoundRecordFragment", "RECORDING");
        }

        private void writeObject(java.io.ObjectOutputStream out) throws IOException{
            // Needed, to override the default one that tries to serialize the SoundRecordFragment class
        }
    }

    private class PlayingUIState extends IdleUIState { //  implements UIState {


        @Override
        public void setUp(SoundRecordFragment fragment) {
            fragment.bt_playStop.setImageResource(PAUSE_RESOURSE_ID);
            fragment.bt_playStop.setOnClickListener(fragment.new PausePlayingClickListener());

            fragment.bt_recordStop.setEnabled(false);

            //keep screen on while playing audio
            if (fragment.getActivity() != null)
                fragment.getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            Log.e("SoundRecordFragment", "PLAYING");
        }

        private void writeObject(java.io.ObjectOutputStream out) throws IOException{
            // Needed, to override the default one that tries to serialize the SoundRecordFragment class
        }
    }

    private class PlayingPausedUIState extends IdleUIState { //  implements UIState {


        @Override
        public void setUp(SoundRecordFragment fragment) {
            fragment.bt_playStop.setImageResource(PLAY_RESOURSE_ID);
            fragment.bt_playStop.setOnClickListener(fragment.new ResumeClickListener());

            fragment.bt_recordStop.setEnabled(true);

            //allow the screen to turn off again once audio is not playing
            if (fragment.getActivity() != null)
                fragment.getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            Log.e("SoundRecordFragment", "PLAYINGPAUSED");
        }

        private void writeObject(java.io.ObjectOutputStream out) throws IOException{
            // Needed, to override the default one that tries to serialize the SoundRecordFragment class
        }
    }

}
