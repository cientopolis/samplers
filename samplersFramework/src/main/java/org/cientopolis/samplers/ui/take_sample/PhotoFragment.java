package org.cientopolis.samplers.ui.take_sample;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

// TODO: 15/03/2017 Refactor: create a inner class per implementation callback
public class PhotoFragment extends StepFragment implements SurfaceHolder.Callback, Camera.PictureCallback{

    private ViewGroup photo_layout;
    private ViewGroup preview_layout;
    private SurfaceHolder surfaceHolder;
    private Camera camera;
    private ImageView photo_preview;
    //private Uri imageURI;
    private String imageFileName;

    public PhotoFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_photo;
    }

    @Override
    protected void onCreateViewStepFragment(View rootView, Bundle savedInstanceState) {

        photo_layout = (ViewGroup) rootView.findViewById(R.id.photo_layout);
        preview_layout = (ViewGroup) rootView.findViewById(R.id.preview_layout);


        SurfaceView surfaceView = (SurfaceView) rootView.findViewById(R.id.surfaceView2);
        surfaceHolder = surfaceView.getHolder();

        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        Button b_takePicture = (Button) rootView.findViewById(R.id.b_take_picture);
        b_takePicture.setOnClickListener(new TakePictureClick());

        TextView textView = (TextView) rootView.findViewById(R.id.lb_instructions);
        textView.setText(getStep().getInstructionsToShow());

        photo_preview = (ImageView) rootView.findViewById(R.id.photo_preview);


    }

    @Override
    protected PhotoStep getStep() { return (PhotoStep) step; }

    @Override
    protected boolean validate() {
        return true;
    }

    @Override
    protected StepResult getStepResult() {

        return new PhotoStepResult(imageFileName);
    }

    /*implements Camera.PictureCallback*/
    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        File file = null;
        try {
            //private File savePhoto;
            file = MultimediaIOManagement.saveTempFile(getActivity().getApplicationContext(), MultimediaIOManagement.PHOTO_EXTENSION, data);
            //imageURI = Uri.fromFile(file);
            imageFileName = file.getName();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity().getApplicationContext(), "Error:" + e.toString(), Toast.LENGTH_LONG).show();
        }

        //refreshCamera();
        // TODO: 15/03/2017 check if no errors with file == null
        showPreviewLayout(Uri.fromFile(file));

    }

    public static Bitmap rotate(Bitmap bitmap, int degree) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Matrix mtx = new Matrix();
        //       mtx.postRotate(degree);
        mtx.setRotate(degree);

        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }

    /*implements SurfaceHolder.Callback*/
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            camera = Camera.open();
        }

        catch (RuntimeException e) {
            System.err.println(e);
            return;
        }

        try {
            setCameraDisplayOrientation(0, camera);
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        }

        catch (Exception e) {
            System.err.println(e);
            return;
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (surfaceHolder.getSurface() == null) {
            return;
        }

        try {
            camera.stopPreview();
        }

        catch (Exception e) {
        }

        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        }
        catch (Exception e) {
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.stopPreview();
        camera.release();
        camera = null;
    }


    private void showPreviewLayout (Uri imageURI) {
        // hide the camera layout
        photo_layout.setVisibility(View.INVISIBLE);
        // FIXME close camera here and open up again on new picture capture

        // Shows the preview layout
        preview_layout.setVisibility(View.VISIBLE);

        // load image on the ui control
        photo_preview.setImageURI(imageURI);
        photo_preview.refreshDrawableState();
        Log.e("Image URI",imageURI.toString());
    }

    private void setCameraDisplayOrientation(int cameraId, android.hardware.Camera camera) {
        android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
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


    public void captureImage() throws IOException {
        // take picture and calls onPictureTaken()
        camera.takePicture(null, null, this);
    }

    private class TakePictureClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            try {
                captureImage();
                //mListener.onPhotoCameraFragmentInteraction(imageURI);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
