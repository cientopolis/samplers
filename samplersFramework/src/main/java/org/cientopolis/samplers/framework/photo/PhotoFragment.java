package org.cientopolis.samplers.framework.photo;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import org.cientopolis.samplers.R;
import org.cientopolis.samplers.framework.StepResult;
import org.cientopolis.samplers.framework.StepFragment;
import org.cientopolis.samplers.ui.ErrorMessaging;

import java.io.IOException;
import java.io.Serializable;




/**
 * Created by lilauth on 3/9/17.
 */

public class PhotoFragment extends StepFragment implements PhotoFragmentCallbacks {

    private ImageView photo_preview;
    private Uri imageURI;
    private int imageRotation;

    private Fragment camera_fragment;

    private PhotoFragmentState fragmentState = PhotoFragmentState.TAKING_PHOTO;

    private static final String KEY_STATE = "org.cientopolis.samplers.PhotoFragment.State";
    private static final String KEY_PHOTOFILEURI = "org.cientopolis.samplers.PhotoFragment.PhotoFileUri";
    private static final String KEY_IMAGE_ROTATION = "org.cientopolis.samplers.PhotoFragment.ImageRotation";
    private static final String KEY_CAMERA_TYPE = "org.cientopolis.samplers.PhotoFragment.CameraType";
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
        photo_preview.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (fragmentState == PhotoFragmentState.SHOWING_PREVIEW)
                    showPreview();
            }
        });
        Log.e("onCreateViewStepFragmen","width:"+String.valueOf(photo_preview.getWidth())+" height:"+String.valueOf(photo_preview.getHeight()));

        Button bt_retake_photo = (Button) rootView.findViewById(R.id.bt_retake_photo);
        bt_retake_photo.setOnClickListener(new ReTakePhotoClick());

    }

    @Override
    protected PhotoStep getStep() { return (PhotoStep) step; }

    @Override
    protected boolean validate() {
        boolean ok = true;
        if (imageURI == null) {
            ok = false;
            ErrorMessaging.showValidationErrorMessage(getActivity().getApplicationContext(),getResources().getString(R.string.error_must_take_photo));
        }

        return ok;
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
        outState.putInt(KEY_IMAGE_ROTATION, imageRotation);
        outState.putSerializable(KEY_CAMERA_TYPE, cameraType);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            fragmentState = (PhotoFragmentState) savedInstanceState.getSerializable(KEY_STATE);
            imageURI = savedInstanceState.getParcelable(KEY_PHOTOFILEURI);
            imageRotation = savedInstanceState.getInt(KEY_IMAGE_ROTATION);
            cameraType = (int) savedInstanceState.getSerializable(KEY_CAMERA_TYPE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (fragmentState == PhotoFragmentState.TAKING_PHOTO) {
            showCamera();
        } else if (fragmentState == PhotoFragmentState.SHOWING_PREVIEW) {
            showPreview();

        }
    }

    private void startCameraStreaming() {
        Log.e("Photo Fragment", "startCameraStreaming");

        fragmentState = PhotoFragmentState.TAKING_PHOTO;
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.child_container, camera_fragment);
        transaction.commit();
    }

    private void startCamera1() {
        Log.e("Photo Fragment", "camera1 selected");

        camera_fragment = Camera1Fragment.newInstance(getStep().getInstructionsToShow());
        startCameraStreaming();
    }

    private void startCamera2() {
        Log.e("Photo Fragment", "camera2 selected");

        camera_fragment = Camera2Fragment.newInstance(getStep().getInstructionsToShow());
        startCameraStreaming();
    }

    private void showCamera() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            cameraType = 1;
            startCamera1();
        }
        else {
            cameraType = 2;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // Need to request permission at run time
                requestCameraPermission();
            }
            else { // Build.VERSION_CODES.LOLLIPOP and LOLLIPOP_MR1 dont need to request permissions and have camera2
                startCamera2();
            }

        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    private void requestCameraPermission() {

        if (getActivity().checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            fragmentState = PhotoFragmentState.REQUESTING_PERMISSIONS;

            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);

        } else { //we already have permission, instantiate the fragment
            startCamera2();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Dont start camer here, this method is called before the fragment is resumed
                // The camera will start in onResume() method, so we change the fragment's state here
                fragmentState = PhotoFragmentState.TAKING_PHOTO;

            } else {
                // Send a message to the user that we need permissions to access the camera to take the photo
                ErrorMessaging.showValidationErrorMessage(getActivity().getApplicationContext(),getResources().getString(R.string.camera_permissions_needed));
            }
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



    @Override
    public void onPhotoTaked(Uri imageURI, int rotation) {
        this.imageURI = imageURI;
        this.imageRotation = rotation;

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

            int exifwidth = exif.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, -1);
            Log.e("exif", "width:"+String.valueOf(exifwidth));

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("error exif", "error loading exif data");
        }
        return rotation;
    }

    private int getRotation(int cameraOrientation) {
        //now we have to determine frame orientation

        int degrees = 0;

        if ((cameraType == 1) || (cameraOrientation == -1)) {
            // In Camera1 Exif orientation is always 0, so we save the orientation when the photo
            // is taked and we use it here to rotate the preview
            // We also use it when the Exif information is missing
            switch (this.imageRotation) {
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
        else if (cameraType == 2){
            switch (cameraOrientation){ // See exif.TAG_ORIENTATION meaning below to understand this
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
        // the max bounds are the bounds of the ImageView control
        int maxWidth = photo_preview.getWidth(); //1920;
        int maxHeight = photo_preview.getHeight();  //1080;
        Log.e("showPreviewLayout","maxWidth:"+String.valueOf(maxWidth)+" maxHeight:"+String.valueOf(maxHeight));


        // if the ImageView control is painted...
        if ((maxWidth > 0) && (maxHeight > 0)){

            // create a scaled bitmap of the image
            Bitmap b = getScaledBitmap(absoluteImagePath, maxWidth, maxHeight);

            //get rotation in degrees for image
            int rotation = getOrientation(absoluteImagePath);

            // load image on the ui control
            photo_preview.setImageBitmap(b);

            // set rotation params
            photo_preview.setRotation(rotation);
            photo_preview.setScaleType(ImageView.ScaleType.FIT_CENTER);
            photo_preview.refreshDrawableState();

        }
    }

    private Bitmap getScaledBitmap(String absoluteImagePath, int maxWidth, int maxHeight) {

        // get the bounds of the image: inJustDecodeBounds get the bounds without loading the image
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(absoluteImagePath, options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;

        int newWidth;
        int newHeight;

        if (imageWidth < maxWidth) { // if the image is smaller than the ImageView control (poor camera...)
            newWidth = imageWidth;
            newHeight = imageHeight;
        }
        else { // the image is bigger than the ImageView control, we have to scale it
            newWidth = maxWidth;
            int percentage = (int) ((double)maxWidth / (double) imageWidth *100);
            newHeight = imageHeight * percentage /100;
        }

        // create a scaled bitmap of the image
        return Bitmap.createScaledBitmap(BitmapFactory.decodeFile(absoluteImagePath, null), newWidth, newHeight, false);

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
        SHOWING_PREVIEW,
        REQUESTING_PERMISSIONS
    }



}
