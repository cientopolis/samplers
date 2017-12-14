package org.cientopolis.samplers.framework.soundRecord;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
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
import org.cientopolis.samplers.framework.base.StepFragment;
import org.cientopolis.samplers.framework.soundRecord.service.RecordingItem;
import org.cientopolis.samplers.framework.soundRecord.service.RecordingService;
import org.cientopolis.samplers.ui.ErrorMessaging;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * Created by laura on 06/09/17.
 * TODO:
 * - Aplicar pattern de estado
 * - No perder contacto (bound) con el servicio de grabacion cuando se rota
 * - Al rotar aparece la ayuda (revisar la TakeSampleActivity
 * - refactor onRecord() en startRecording() y stopRecording()
 * - Cambiar iconos por los sin el circulo
 * - Sacar MySharedPreferences del Service
 * - Progress bar (opcional)
 */

public class SoundRecordFragment extends StepFragment {

    private static final String KEY_RECORDING_ITEM = "org.cientopolis.samplers.SoundRecordFragment_RecordingItem";
    private static final String KEY_STATE = "org.cientopolis.samplers.SoundRecordFragment_State";
    private static final String KEY_CONNECTION = "org.cientopolis.samplers.SoundRecordFragment_Connection";
    private static final String KEY_BOOLEAN = "org.cientopolis.samplers.SoundRecordFragment_Boolean";


    private static final int PLAY_RESOURSE_ID = R.drawable.ic_play_circle_outline_black_36dp;
    private static final int PAUSE_RESOURSE_ID = R.drawable.ic_pause_circle_outline_black_36dp;

    private State mState;

    // FOR RECORDING ------------------
    private Chronometer mChronometer;
    private TextView lb_recording_status;
    private ImageButton bt_recordStop;

    private boolean mStartRecording = true;

    private MyServiceConnection mConnection;
    private RecordingService mRecordingService;
    boolean mBound = false;
    private RecordingItem mRecordingItem;

    private ProgressBar progressBar;
    private Drawable progressBarDraw;

    // FOR PLAYBACK ------------------------------
    private Handler mHandler = new Handler();

    private ImageButton bt_playStop;
    private MediaPlayer mMediaPlayer = null;
    private SeekBar mSeekBar = null;
    private TextView lb_currentProgress = null;
    private TextView lb_fileLength = null;

    //stores whether or not the mediaplayer is currently playing audio
    private boolean isPlaying = false;
    //private long timeWhenPaused = 0; //stores time when user clicks pause button



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mRecordingItem = (RecordingItem) savedInstanceState.getSerializable(KEY_RECORDING_ITEM);
            mState = (State) savedInstanceState.getSerializable(KEY_STATE);
            mState.setFragment(this);
            //mConnection = (MyServiceConnection) savedInstanceState.getSerializable(KEY_CONNECTION);
            mStartRecording =  savedInstanceState.getBoolean(KEY_BOOLEAN);

        }
        else {
            mState = new IdleState(this);

        }
        mConnection = new MyServiceConnection();
    }

    @Override
    public void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_RECORDING_ITEM,mRecordingItem);
        outState.putSerializable(KEY_STATE,mState);
        outState.putSerializable(KEY_CONNECTION, mConnection);
        outState.putBoolean(KEY_BOOLEAN, mStartRecording);


    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_sound_record;
    }

    @Override
    protected void onCreateViewStepFragment(View rootView, Bundle savedInstanceState) {
        mChronometer = (Chronometer) rootView.findViewById(R.id.chronometer);
        lb_recording_status = (TextView) rootView.findViewById(R.id.lb_recording_status);

        bt_recordStop = (ImageButton) rootView.findViewById(R.id.bt_recordStop);
        bt_recordStop.setOnClickListener(new RecordClickListener());

        // Playback -----------------------
        lb_fileLength = (TextView) rootView.findViewById(R.id.lb_fileLength);
        if (mRecordingItem != null)
            lb_fileLength.setText(String.format("%02d:%02d", mRecordingItem.getMinutes(),mRecordingItem.getSeconds()));

        lb_currentProgress = (TextView) rootView.findViewById(R.id.lb_currentProgress);


        bt_playStop = (ImageButton) rootView.findViewById(R.id.bt_playStop);
        bt_playStop.setOnClickListener(new PlayClickListener());

        mSeekBar = (SeekBar) rootView.findViewById(R.id.seekbar);
        // TODO: rename color name and set it in layout xml file
        ColorFilter filter = new LightingColorFilter(getResources().getColor(R.color.progress_bar_primary), getResources().getColor(R.color.progress_bar_primary));
        mSeekBar.getProgressDrawable().setColorFilter(filter);
        mSeekBar.getThumb().setColorFilter(filter);
        mSeekBar.setOnSeekBarChangeListener(new SeekBarChangeListener());

        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        progressBarDraw = getResources().getDrawable(R.drawable.record_progress_bar);
        // Get the Drawable custom_progressbar

        // set the drawable as progress drawable
        //progressBar.setProgressDrawable(draw);
        //progressBar.setIndeterminateDrawable(progressBarDraw);
        //progressBar.setIndeterminateDrawable(progressBarDraw);

        progressBar.setIndeterminate(true);
        progressBar.setIndeterminateDrawable(null);

        //mState.setUp();
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

        mState.setUp();
    }


    /*functionality*/
    private void onRecord(boolean start){


        /* Permission for audio source*/
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, 10);

        } else {



            if (start) {
                // start recording
                mRecordingService.startRecording();

                mState = new RecordingState(this);
                mState.setUp();




            } else {
                //stop recording
                mState = new IdleState(this);
                mState.setUp();

                //if(mBound) {
                    mRecordingService.stopRecording();

                    mRecordingItem = mRecordingService.getRecordingItem();
                    lb_fileLength.setText(String.format("%02d:%02d", mRecordingItem.getMinutes(),mRecordingItem.getSeconds()));

                    //getActivity().unbindService(mConnection);
                //    mBound = false;
                //}

            }

        }
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
                bt_playStop.setImageResource(PLAY_RESOURSE_ID);
            }
        });

        updateSeekBar();

        //keep screen on while playing audio
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
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

    private void pausePlaying() {
        //bt_playStop.setImageResource(R.drawable.ic_media_play);
        mHandler.removeCallbacks(mRunnable);
        mMediaPlayer.pause();
    }

    private void resumePlaying() {
        //bt_playStop.setImageResource(R.drawable.ic_media_pause);
        mHandler.removeCallbacks(mRunnable);
        mMediaPlayer.start();
        updateSeekBar();
    }

    private void stopPlaying() {
        //bt_playStop.setImageResource(R.drawable.ic_media_play);
        mHandler.removeCallbacks(mRunnable);
        mMediaPlayer.stop();
        mMediaPlayer.reset();
        mMediaPlayer.release();
        mMediaPlayer = null;

        mSeekBar.setProgress(mSeekBar.getMax());
        isPlaying = !isPlaying;

        lb_currentProgress.setText(lb_fileLength.getText());
        mSeekBar.setProgress(mSeekBar.getMax());

        //allow the screen to turn off again once audio is finished playing
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
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
            onRecord(mStartRecording);
            mStartRecording = !mStartRecording;
        }
    }


    private class PlayClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            if (mRecordingItem != null) {
                if (!isPlaying) {
                    //currently MediaPlayer is not playing audio
                    if (mMediaPlayer == null) {
                        startPlaying(); //start from beginning
                    } else {
                        resumePlaying(); //resume the currently paused MediaPlayer
                    }
                } else {
                    //pause the MediaPlayer
                    pausePlaying();

                }

                isPlaying = !isPlaying;
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
    };

    private interface State extends Serializable {
        void setUp();
        void setFragment(SoundRecordFragment fragment);
    }

    /**
     * ESTA GUARDANDO LAS REFERENCIAS A LOS CONTROLES DE LA ACTIVITY DESTRUIDA
     *
     * EL SERVICIO QUEDA DANDO VUELTAS EN EL ETER...
     * Ver de iniciarlo al empezar a grabar, volver a hacer el bind en el onStart/onResume y matarlo en el stopRecording
     * Habria que ver que no vuelva a empezar a grabar cuando se hace el re-bind (if ya no estoy grabando...)
     *
     * EL PATTERN ESTA HORRIBLE
     *
     *
     * VER TAMBIEN da cuando se apaga la pantalla:
     * java.lang.RuntimeException: Parcelable encountered IOException writing serializable object (name = org.cientopolis.samplers.framework.soundRecord.SoundRecordFragment$IdleState)
     * */

    private class IdleState implements State {
        private SoundRecordFragment fragment;

        public IdleState(SoundRecordFragment fragment) {
            this.fragment = fragment;
        }

        public void setFragment(SoundRecordFragment fragment) {
            this.fragment = fragment;
        }

        @Override
        public void setUp() {
            fragment.bt_recordStop.setImageResource(R.drawable.ic_launcher);
            fragment.progressBar.setIndeterminateDrawable(null);
            fragment.mChronometer.stop();
            fragment.mChronometer.setBase(SystemClock.elapsedRealtime());
            //timeWhenPaused = 0;
//            fragment.lb_recording_status.setText(getString(R.string.record_prompt));

            /*enable playback button*/
            fragment.bt_playStop.setImageResource(PLAY_RESOURSE_ID);
            fragment.bt_playStop.setEnabled(true);
                /*and seekbar callbacks*/

            //allow the screen to turn off again once recording is finished
            if (fragment.getActivity() != null)
                fragment.getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            Log.e("SoundRecordFragment", "IDLE");
        }
    }

    private class RecordingState implements State {
        private SoundRecordFragment fragment;
        private long startingTime = 0;

        public RecordingState(SoundRecordFragment fragment) {
            this.fragment = fragment;
        }

        public void setFragment(SoundRecordFragment fragment) {
            this.fragment = fragment;
        }

        @Override
        public void setUp() {
            /* this change image from "start" to "stop" */
            fragment.bt_recordStop.setImageResource(R.drawable.ic_media_stop);
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

//            fragment.lb_recording_status.setText(getString(R.string.record_in_progress));

            Log.e("SoundRecordFragment", "RECORDING");
        }
    }

    private class PlayingState implements State {
        private SoundRecordFragment fragment;

        public PlayingState(SoundRecordFragment fragment) {
            this.fragment = fragment;
        }

        public void setFragment(SoundRecordFragment fragment) {
            this.fragment = fragment;
        }

        @Override
        public void setUp() {
            fragment.bt_playStop.setImageResource(PAUSE_RESOURSE_ID);

            Log.e("SoundRecordFragment", "PLAYING");
        }
    }

}
