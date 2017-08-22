package org.cientopolis.samplers.ui.take_sample;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.cientopolis.samplers.R;
import org.cientopolis.samplers.persistence.MultimediaIOManagement;

import java.io.File;
import java.io.IOException;

/**
 * Created by lilauth on 3/9/17.
 */

@SuppressWarnings("deprecation")
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
public class Camera1Fragment extends Fragment {

    private static final String ARG_INSTRUCTIONS = "param_instructions";
    private static final String ARG_CALLBACKS = "param_callbacks";

    private Camera camera;
    private Uri imageURI;
    private SurfaceHolder surfaceHolder;

    private String instructions;
    private PhotoFragmentCallbacks mListener;


    public Camera1Fragment() {
        // Required empty public constructor
    }

    public static Camera1Fragment newInstance(PhotoFragmentCallbacks mListener, String instructions) {
        Camera1Fragment fragment = null;
        try {
            fragment = new Camera1Fragment();
            Bundle args = new Bundle();
            args.putSerializable(ARG_CALLBACKS, mListener);
            args.putString(ARG_INSTRUCTIONS, instructions);
            fragment.setArguments(args);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            this.mListener = (PhotoFragmentCallbacks) getArguments().getSerializable(ARG_CALLBACKS);
            this.instructions =  getArguments().getString(ARG_INSTRUCTIONS);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_camera1, container, false);

        SurfaceView surfaceView = (SurfaceView) rootView.findViewById(R.id.surfaceView2);

        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(new SurfaceCallbacksHelper());
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        TextView textView = (TextView) rootView.findViewById(R.id.lb_instructions);
        textView.setText(instructions);

        Button b_takePicture = (Button) rootView.findViewById(R.id.b_take_picture);
        b_takePicture.setOnClickListener(new TakePictureClick());

        return rootView;
    }


    @Override
    public void onSaveInstanceState (Bundle outState) {
        // TODO no se esta ejecutando este metodo
        //camera is streaming. So, we released the camera and stop the streaming
        releaseCamera();

        super.onSaveInstanceState(outState);
    }


    private boolean openCamera(){
        try {
            camera = Camera.open();
            return true;

        } catch (RuntimeException e) {
            e.printStackTrace();
            return false;
         }
    }

    private void releaseCamera(){
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    private boolean startPreview(){
        try {
            setCameraDisplayOrientation(0, camera);
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void setCameraDisplayOrientation(int cameraId, Camera camera) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = getActivity().getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360; // compensate the mirror
        } else { // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

    public void captureImage() throws IOException{
        //take picture and calls onPictureTaken()
        camera.takePicture(null, null, new CameraCallbaksHelper());
    }

    private class TakePictureClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            try {
                captureImage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /*surface holder callbacks*/
    private class SurfaceCallbacksHelper implements SurfaceHolder.Callback{

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            //Surface holder = this.surfaceHolder
            if (openCamera()) {
                startPreview();
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            if (holder.getSurface() == null) {
                return;
            }

            if (camera == null){
                openCamera();
            }

            try {
                camera.stopPreview();
                startPreview();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            //release camera happens when picture is taken

        }
    }
    /*Camera callbacks helper*/
    private class CameraCallbaksHelper implements Camera.PictureCallback{

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File file = null;
            try {
                /**
                 *
                 * for testing purposes if needed, add to Manifest permission for writing on external storage
                 * and let MultimediaIOManagement save file to Galery
                 *
                 * MultimediaIOManagement.savePublicTempFile(getActivity().getApplicationContext(), MultimediaIOManagement.PHOTO_EXTENSION, data);
                 */
                file = MultimediaIOManagement.saveTempFile(getActivity().getApplicationContext(), MultimediaIOManagement.PHOTO_EXTENSION, data);

                Log.e("getAbsolutePath",file.getAbsolutePath());
                imageURI = Uri.fromFile(file);
                releaseCamera();

            } catch (Exception e) {
                e.printStackTrace();
            }

            Log.e("Image URI",Uri.fromFile(file).toString());

            if(imageURI == null){
                throw new RuntimeException("unable to create picture");
            }
            else{
                mListener.onPhotoTaked(imageURI);
            }
        }
    }

}
