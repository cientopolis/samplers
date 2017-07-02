package org.cientopolis.samplers.ui.take_sample;

import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.cientopolis.samplers.R;
import org.cientopolis.samplers.model.PhotoStep;
import org.cientopolis.samplers.model.PhotoStepResult;
import org.cientopolis.samplers.model.StepResult;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by lilauth on 3/9/17.
 */

public class PhotoFragment extends StepFragment implements PhotoFragmentCallbacks {

    private ViewGroup preview_layout;
    private ImageView photo_preview;
    private Uri imageURI;
    private String imageFileName;

    private PhotoFragment1 camera_fragment;

    private PhotoFragmentState fragmentState = PhotoFragmentState.TAKING_PHOTO;


    private static final String KEY_STATE = "org.cientopolis.samplers.PhotoFragmentState";
    private static final String KEY_PHOTOFILEURI = "org.cientopolis.samplers.PhotoFileUri";


    public PhotoFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_photo;
    }

    @Override
    protected void onCreateViewStepFragment(View rootView, Bundle savedInstanceState) {

        preview_layout = (ViewGroup) rootView.findViewById(R.id.preview_layout);

        photo_preview = (ImageView) rootView.findViewById(R.id.photo_preview);

        Button bt_retake_photo = (Button) rootView.findViewById(R.id.bt_retake_photo);
        bt_retake_photo.setOnClickListener(new ReTakePhotoClick());

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

    @Override
    public void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_STATE,fragmentState);
        outState.putParcelable(KEY_PHOTOFILEURI, imageURI);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            fragmentState = (PhotoFragmentState) savedInstanceState.getSerializable(KEY_STATE);
            imageURI = (Uri) savedInstanceState.getParcelable(KEY_PHOTOFILEURI);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (fragmentState == PhotoFragmentState.TAKING_PHOTO) {
            showCamera();
        }else{
            showPreview();

        }
    }

    private void showCamera() {
        fragmentState = PhotoFragmentState.TAKING_PHOTO;

        camera_fragment = PhotoFragment1.newInstance(this, getStep().getInstructionsToShow());

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.child_container, camera_fragment);
        transaction.commit();
    }

    private void showPreview() {
        fragmentState = PhotoFragmentState.SHOWING_PREVIEW;

        if (camera_fragment != null) {
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.remove(camera_fragment);
            transaction.commit();
            camera_fragment = null;
        }

        showPreviewLayout(imageURI, imageURI.getPath());
    }

    @Override
    public void onPhotoTaked(Uri imageURI) {
        this.imageURI = imageURI;

        showPreview();
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


    private void showPreviewLayout (Uri imageURI, String absoluteImagePath) {
        // Shows the preview layout
        //preview_layout.setVisibility(View.VISIBLE);

        //get rotation in degrees for image
        int rotation = getOrientation(absoluteImagePath);
        //get rotation in degrees for image
        //test for preview problem
        Matrix matrix = new Matrix();
        matrix.postRotate(rotation);
        //test for max preview size supported

        Bitmap b = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(absoluteImagePath,null), 1920, 1080, false);
        Bitmap rotatedBitmap = Bitmap.createBitmap(b , 0, 0, b.getWidth(), b.getHeight(), matrix, true);

        photo_preview.setImageBitmap(rotatedBitmap/*b Bitmap.createScaledBitmap(b, 1920, 1080, false)*/);
        // load image on the ui control
        //photo_preview.setImageURI(imageURI);
        //photo_preview.setRotation(rotation);
        photo_preview.refreshDrawableState();
        //Glide.with(getActivity().getApplicationContext()).load(imageURI.toString()).into(photo_preview);
    }



    /*private helper classes*/
    private class ReTakePhotoClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            showCamera();
        }
    }


    private enum PhotoFragmentState implements Serializable {
        TAKING_PHOTO,
        SHOWING_PREVIEW;
    }

}
