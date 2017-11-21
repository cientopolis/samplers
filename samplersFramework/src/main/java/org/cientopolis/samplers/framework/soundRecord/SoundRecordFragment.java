package org.cientopolis.samplers.framework.soundRecord;

import android.Manifest;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
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

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by laura on 06/09/17.
 */

public class SoundRecordFragment extends StepFragment {

    private Chronometer mChronometer;
    private TextView mRecordingPrompt;
   // private Button mRecordButton;

    private ImageButton mRecordButton;
    private String fileName;
    boolean mBound = false;
    private RecordingItem mRecordingItem;

    private int mRecordPromptCount = 0;
    //private long timeWhenPaused = 0; //stores time when user clicks pause button

    private boolean mStartRecording = true;
    //private boolean mPauseRecording = true;
    private RecordingService mRecordingService;

    //for playback ------------------------------
    private Handler mHandler = new Handler();

    private ImageButton mPlayButton;
    private MediaPlayer mMediaPlayer = null;
    private SeekBar mSeekBar = null;
    private TextView mCurrentProgressTextView = null;
    private TextView mFileLengthTextView = null;
    //stores minutes and seconds of the length of the file.
    private long minutes = 0;
    private long seconds = 0;
    //stores whether or not the mediaplayer is currently playing audio
    private boolean isPlaying = false;

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
        mRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRecord(mStartRecording);
                mStartRecording = !mStartRecording;
            }
        });

        // Playback -----------------------
        mFileLengthTextView = (TextView) rootView.findViewById(R.id.file_length_text_view);
        mFileLengthTextView.setText(String.format("%02d:%02d", minutes,seconds));
        mCurrentProgressTextView = (TextView) rootView.findViewById(R.id.current_progress_text_view);


        mPlayButton = (ImageButton) rootView.findViewById(R.id.btn_play);
        mPlayButton.setImageResource(R.drawable.ic_media_play);
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPlay(isPlaying);
                isPlaying = !isPlaying;
            }
        });
        /*
        mPlayButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                try {
                    PlaybackFragment playbackFragment = new PlaybackFragment().newInstance(mRecordingItem);
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    playbackFragment.show(getFragmentManager(),"");

                    playbackFragment.show(transaction, "dialog_playback");

                } catch (Exception e) {
                    Log.e("playback exc", "exception", e);
                }
            }
        });
*/
        mSeekBar = (SeekBar) rootView.findViewById(R.id.seekbar);
        // TODO: rename color name and set it in layout xml file
        ColorFilter filter = new LightingColorFilter(getResources().getColor(R.color.primary), getResources().getColor(R.color.primary));
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
                //timeWhenPaused = 0;
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


    private void onPlay(boolean isPlaying){
        if (!isPlaying) {
            //currently MediaPlayer is not playing audio
            if(mMediaPlayer == null) {
                startPlaying(); //start from beginning
            } else {
                resumePlaying(); //resume the currently paused MediaPlayer
            }
            mPlayButton.setImageResource(R.drawable.ic_media_stop);
        } else {
            //pause the MediaPlayer
            pausePlaying();
            mPlayButton.setImageResource(R.drawable.ic_media_play);
        }
    }

    private void startPlaying() {
        //mPlayButton.setImageResource(R.drawable.ic_media_pause);
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
            Log.e("PlaybackFragment", "prepare() failed");
        }

        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopPlaying();
                mPlayButton.setImageResource(R.drawable.ic_media_play);
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
            Log.e("PlaybackFragment", "prepare() failed");
        }

        //keep screen on while playing audio
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void pausePlaying() {
        //mPlayButton.setImageResource(R.drawable.ic_media_play);
        mHandler.removeCallbacks(mRunnable);
        mMediaPlayer.pause();
    }

    private void resumePlaying() {
        //mPlayButton.setImageResource(R.drawable.ic_media_pause);
        mHandler.removeCallbacks(mRunnable);
        mMediaPlayer.start();
        updateSeekBar();
    }

    private void stopPlaying() {
        //mPlayButton.setImageResource(R.drawable.ic_media_play);
        mHandler.removeCallbacks(mRunnable);
        mMediaPlayer.stop();
        mMediaPlayer.reset();
        mMediaPlayer.release();
        mMediaPlayer = null;

        mSeekBar.setProgress(mSeekBar.getMax());
        isPlaying = !isPlaying;

        mCurrentProgressTextView.setText(mFileLengthTextView.getText());
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
                mCurrentProgressTextView.setText(String.format("%02d:%02d", minutes,seconds));

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
                mCurrentProgressTextView.setText(String.format("%02d:%02d", minutes,seconds));
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
                mCurrentProgressTextView.setText(String.format("%02d:%02d", minutes, seconds));

                updateSeekBar();
            }
        }
    };

    private void updateSeekBar() {
        mHandler.postDelayed(mRunnable, 1000);
    }

}
