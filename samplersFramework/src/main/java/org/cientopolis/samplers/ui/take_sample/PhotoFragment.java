package org.cientopolis.samplers.ui.take_sample;

import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by lilauth on 3/9/17.
 */

public class PhotoFragment extends StepFragment implements SurfaceHolder.Callback, Camera.PictureCallback{

    private ViewGroup photo_layout;
    private ViewGroup preview_layout;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private Camera camera;
    private ImageView photo_preview;
    private Uri imageURI;

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



        surfaceView = (SurfaceView) rootView.findViewById(R.id.surfaceView2);
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

        return new PhotoStepResult(imageURI.toString());
    }

    /*implements Camera.PictureCallback*/
    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        try {
            //private File savePhoto;
            File file = savePicture(data);
            imageURI = Uri.fromFile(file);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity().getApplicationContext(), "Error:" + e.toString(), Toast.LENGTH_LONG).show();
        }

        //refreshCamera();
        showPreviewLayout();

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

    /*helpers*/
    private File savePicture(byte[] data) throws IOException {
        ContextWrapper cw = new ContextWrapper(getActivity().getApplicationContext());

        //private app directory
        //File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File directory = new File(getActivity().getApplicationContext().getFilesDir(),"tmp");
        directory.mkdirs();
        // El nombre del archivo armado con la fecha/hora
        String filename = String.format("%d.jpg", System.currentTimeMillis());
        // file creation
        File file = new File(directory,filename);
        // file output stream (to write on the file)
        FileOutputStream fos =  new FileOutputStream(file);
        // save output stream and close
        fos.write(data);
        fos.close();

        return file;
    }

    private void showPreviewLayout () {
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
