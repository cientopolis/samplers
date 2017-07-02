package org.cientopolis.samplers.ui.take_sample;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.cientopolis.samplers.R;
import org.cientopolis.samplers.model.PhotoStep;
import org.cientopolis.samplers.model.PhotoStepResult;
import org.cientopolis.samplers.model.StepResult;
import org.cientopolis.samplers.persistence.MultimediaIOManagement;

import java.io.File;
import java.io.IOException;

/**
 * Created by lilauth on 3/9/17.
 */

public class PhotoFragment1 extends Fragment {

    private static final String ARG_INSTRUCTIONS = "param_instructions";
    private static final String ARG_CALLBACKS = "param_callbacks";

    private ViewGroup photo_layout;
    private Camera camera;
    private Uri imageURI;
    private String imageFileName;
    private SurfaceHolder surfaceHolder;

    private String instructions;
    private PhotoFragmentCallbacks mListener;


    public PhotoFragment1() {
        // Required empty public constructor
    }

    public static PhotoFragment1 newInstance(PhotoFragmentCallbacks mListener, String instructions) {
        PhotoFragment1 fragment = null;
        try {
            fragment = new PhotoFragment1();
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
        View rootView = inflater.inflate(R.layout.fragment_photo1, container, false);

        photo_layout = (ViewGroup) rootView.findViewById(R.id.photo_layout);

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
        //camera is streameing. So, we released the camera and stop the streaming
        releaseCamera();

        super.onSaveInstanceState(outState);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (openCamera()){
            startPreview();
        }
    }

    private boolean openCamera(){
        try {
            camera = Camera.open();
            return true;
        } catch (RuntimeException e) {
            System.err.println(e);
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
            System.err.println(e);
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

    private int getOrientation(String imagePath){
        int rotation = 0;
        try {
            ExifInterface exif = new ExifInterface(imagePath);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            rotation = getRotation(orientation);

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("error exif", "error loading exif data");
        }
        return rotation;
    }

    private int getRotation(int cameraOrientation) {
        //now we have to determine frame orientation
        int rotation = getActivity().getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        if ((cameraOrientation == 1) || (cameraOrientation == 0)){
            switch (rotation) {
                case Surface.ROTATION_0: //portrait normal
                    degrees = 90;
                    break;
                case Surface.ROTATION_90: //landscape, buttons on the right
                    degrees = 0;
                    break;
                case Surface.ROTATION_180: //portrait upside-down
                    degrees = 270;
                    break;
                case Surface.ROTATION_270: //landscape, buttons on the left
                    degrees = 180;
                    break;
            }

        }
        Log.e("rot and degree", "rotation: "+String.valueOf(rotation)+ " degrees: "+ String.valueOf(degrees));
        return degrees;
    }

    /** This is what exif.TAG_ORIENTATION means
         *   1        2       3      4         5            6           7          8

         888888  888888      88  88      8888888888  88                  88  8888888888
         88          88      88  88      88  88      88  88          88  88      88  88
         8888      8888    8888  8888    88          8888888888  8888888888          88
         88          88      88  88
         88          88  888888  888888
         */


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

            try {
                camera.stopPreview();
            }

            catch (Exception e) {
            }

            try {
                camera.setPreviewDisplay(holder);
                camera.startPreview();
            }
            catch (Exception e) {
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
                //private File savePhoto;
                file = MultimediaIOManagement.saveTempFile(getActivity().getApplicationContext(), MultimediaIOManagement.PHOTO_EXTENSION, data);
                imageFileName = file.getName();
                imageURI = Uri.fromFile(file);
                releaseCamera();

            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.e("Image URI",Uri.fromFile(file).toString());
            // TODO: 15/03/2017 check if no errors with file == null

            // FINALIZAR ACA !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            mListener.onPhotoTaked(imageURI);
        }
    }

}
