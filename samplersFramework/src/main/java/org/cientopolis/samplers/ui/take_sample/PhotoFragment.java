package org.cientopolis.samplers.ui.take_sample;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

    private ImageView photo_preview;
    private Uri imageURI;

    private Fragment camera_fragment;

    private PhotoFragmentState fragmentState = PhotoFragmentState.TAKING_PHOTO;

    private static final String KEY_STATE = "org.cientopolis.samplers.PhotoFragmentState";
    private static final String KEY_PHOTOFILEURI = "org.cientopolis.samplers.PhotoFileUri";
    private static final String KEY_CAMERA_TYPE = "org.cientopolis.samplers.CameraType";
    private static final int REQUEST_CAMERA_PERMISSION = 1;

    private int cameraType = 0;


    public PhotoFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_photo;
    }

    @Override
    protected void onCreateViewStepFragment(View rootView, Bundle savedInstanceState) {

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
        // Extract file name from URI
        String fileName = imageURI.getPath();
        int cut = fileName.lastIndexOf('/');
        if (cut != -1) {
            fileName = fileName.substring(cut + 1);
        }

        return new PhotoStepResult(getStep().getId(),fileName);
    }

    @Override
    public void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_STATE,fragmentState);
        outState.putParcelable(KEY_PHOTOFILEURI, imageURI);
        outState.putSerializable(KEY_CAMERA_TYPE, cameraType);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            fragmentState = (PhotoFragmentState) savedInstanceState.getSerializable(KEY_STATE);
            imageURI = savedInstanceState.getParcelable(KEY_PHOTOFILEURI);
            cameraType = (int) savedInstanceState.getSerializable(KEY_CAMERA_TYPE);
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

    private void startCameraStreaming(){
        fragmentState = PhotoFragmentState.TAKING_PHOTO;
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.child_container, camera_fragment);
        transaction.commit();
    }

    private void showCamera() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP){
            Log.d("Photo Fragment", "camera1 selected");
            cameraType = 1;
            camera_fragment = Camera1Fragment.newInstance(this, getStep().getInstructionsToShow());
            startCameraStreaming();
        }
        else {
            cameraType = 2;
            //the fragment will return in 'onRequestPermissionsResult'
            if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestCameraPermission(); //if the user does not accept, the app stops
            } else { //we already have permission, instantiate the fragment
                Log.d("Photo Fragment", "camera2 selected");
                camera_fragment = Camera2Fragment.newInstance(null, getStep().getInstructionsToShow());
                startCameraStreaming();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d("Photo Fragment", "camera2 selected");
            camera_fragment = Camera2Fragment.newInstance(this, getStep().getInstructionsToShow());
            startCameraStreaming();
        }else{
            //TODO show error message
        }
    }

    private void showPreview() {
        fragmentState = PhotoFragmentState.SHOWING_PREVIEW;

        if (camera_fragment != null) {
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.remove(camera_fragment);
            transaction.commit();
            camera_fragment = null;
        }

        showPreviewLayout(imageURI.getPath());
    }

    /*code testing*/
    @TargetApi(Build.VERSION_CODES.M)
    private void requestCameraPermission() {
        if (this.shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
            new ConfirmationDialog().show(getChildFragmentManager(), "dialog");
        } else {
            getActivity().requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        }
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
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, /*ExifInterface.ORIENTATION_NORMAL*/-1);
            if(orientation == -1){
                throw new RuntimeException("-1, exif");
            }
            rotation = getRotation(orientation);

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("error exif", "error loading exif data");
        }
        return rotation;
    }

    private int getRotation(int cameraOrientation) {
        //now we have to determine frame orientation
        Log.e("get orient",String.valueOf(cameraOrientation));
        int rotation = getActivity().getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;

        if (((cameraOrientation == 1) || (cameraOrientation == 0)) && (cameraType == 1)){
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
        else {
            if (cameraType == 2){
                switch (cameraOrientation){
                    case 1: degrees = 0;
                        break;
                    case 6: degrees = 90;
                        break;
                    case 3: degrees = 180;
                        break;
                    case 8: degrees = 270;
                        break;
                }
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


    private void showPreviewLayout (String absoluteImagePath) {

        //get rotation in degrees for image
        int rotation = getOrientation(absoluteImagePath);
        //get rotation in degrees for image
        //test for preview problem
        Matrix matrix = new Matrix();
        matrix.postRotate(rotation);
        //test for max preview size supported

        //Bitmap b = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(absoluteImagePath,null), 1920, 1080, false);
        //Bitmap rotatedBitmap = Bitmap.createBitmap(b , 0, 0, b.getWidth(), b.getHeight(), matrix, true);

        //photo_preview.setImageBitmap(rotatedBitmap/*b Bitmap.createScaledBitmap(b, 1920, 1080, false)*/);
        // load image on the ui control
        photo_preview.setImageURI(imageURI);
        photo_preview.setRotation(rotation);
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
        SHOWING_PREVIEW
    }

    /**
     * Shows OK/Cancel confirmation dialog about camera permission.
     */
    public static class ConfirmationDialog extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            //final Fragment parent = getParentFragment();
            return new AlertDialog.Builder(getActivity()).setMessage(R.string.camera_request_permission).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
            { //onClick listener accept

                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    getActivity().requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                }
            })
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
                    { //onClick listener cancel
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Activity activity = getActivity();
                            if (activity != null) {
                                activity.finish();
                            }
                        }
                    })
                    .create();
        }
    }

}
