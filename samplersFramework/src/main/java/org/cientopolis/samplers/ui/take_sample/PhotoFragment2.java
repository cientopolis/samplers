package org.cientopolis.samplers.ui.take_sample;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ExifInterface;
import android.media.Image;
import android.media.ImageReader;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import org.cientopolis.samplers.R;
import org.cientopolis.samplers.model.PhotoStep;
import org.cientopolis.samplers.model.PhotoStepResult;
import org.cientopolis.samplers.model.StepResult;
import org.cientopolis.samplers.persistence.MultimediaIOManagement;
import org.cientopolis.samplers.ui.ErrorMessaging;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Created by lilauth on 4/12/17.
 */

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class PhotoFragment2 extends StepFragment{


    private static final String KEY_STATE = "org.cientopolis.samplers.PhotoFragmentState";
    private static final String KEY_PHOTO2FILEPATH = "org.cientopolis.samplers.Photo2PathFile";

    /*textual copy from example*/
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    /**
     * Conversion from screen rotation to JPEG orientation.
     */
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    /**
     * Camera states:
     */
    private static final int STATE_PREVIEW = 0; //Showing camera preview.
    private static final int STATE_WAITING_LOCK = 1; //Waiting for the focus to be locked.
    private static final int STATE_WAITING_PRECAPTURE = 2; //Waiting for the exposure to be precapture state.
    private static final int STATE_WAITING_NON_PRECAPTURE = 3; //Waiting for the exposure state to be something other than precapture.
    private static final int STATE_PICTURE_TAKEN = 4; //Picture was taken.

    private static final int MAX_PREVIEW_WIDTH = 1920; //Max preview width that is guaranteed by Camera2 API
    private static final int MAX_PREVIEW_HEIGHT = 1080; //Max preview height that is guaranteed by Camera2 API

    String absoluteImagePath;
    String imageFileName;
    Uri imageURI;

    private ViewGroup photo_layout;
    private ViewGroup preview_layout;
    private ImageView photo_preview;

    private AutoFitTextureView autoFitTextureView; //An {@link AutoFitTextureView} for camera preview.

    private CameraCaptureSession cameraCaptureSession; //A {@link CameraCaptureSession } for camera preview.
    //private CameraDevice cameraDevice; //A reference to the opened {@link CameraDevice}.

    private Size previewSize; //The {@link android.util.Size} of camera preview.

    private HandlerThread backgroundThread; //An additional thread for running tasks that shouldn't block the UI.
    private Handler backgroundHandler; //A {@link Handler} for running tasks in the background.

    private ImageReader imageReader; //An {@link ImageReader} that handles still image capture.

    private int fragmentState = 1;
    //fragment state = 1, camera open and previewing
    //fragment state = 2 preview photo, valid imageFile

    /**
     * This a callback object for the {@link ImageReader}. "onImageAvailable" will be called when a
     * still image is ready to be saved.
     */
    private final ImageReader.OnImageAvailableListener onImageAvailableListener = new ImageReader.OnImageAvailableListener() {

        @Override
        public void onImageAvailable(ImageReader reader) {
            backgroundHandler.post(new ImageSaver(reader.acquireNextImage()));
        }

    };

    private CaptureRequest.Builder previewRequestBuilder; //{@link CaptureRequest.Builder} for the camera preview

    private int cameraState = STATE_PREVIEW;

    private Semaphore cameraOpenCloseLock = new Semaphore(1); //A {@link Semaphore} to prevent the app from exiting before closing the camera.
    private boolean flashSupported; //Whether the current camera device supports Flash or not.
    private int sensorOrientation; //Orientation of the camera sensor

    /**
     * Shows a message on the UI thread.
     *
     * @param text The message to show
     */
    private void showMessage(final String text) {
        final Activity activity = getActivity();
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ErrorMessaging.showValidationErrorMessage(activity, text);
                    Log.e("picture saved: ",text);
                }
            });
        }
    }

    /**
     * Given {@code choices} of {@code Size}s supported by a camera, choose the smallest one that
     * is at least as large as the respective texture view size, and that is at most as large as the
     * respective max size, and whose aspect ratio matches with the specified value. If such size
     * doesn't exist, choose the largest one that is at most as large as the respective max size,
     * and whose aspect ratio matches with the specified value.
     *
     * @param choices           The list of sizes that the camera supports for the intended output
     *                          class
     * @param textureViewWidth  The width of the texture view relative to sensor coordinate
     * @param textureViewHeight The height of the texture view relative to sensor coordinate
     * @param maxWidth          The maximum width that can be chosen
     * @param maxHeight         The maximum height that can be chosen
     * @param aspectRatio       The aspect ratio
     * @return The optimal {@code Size}, or an arbitrary one if none were big enough
     */
    private static Size chooseOptimalSize(Size[] choices, int textureViewWidth, int textureViewHeight, int maxWidth, int maxHeight, Size aspectRatio) {

        // Collect the supported resolutions that are at least as big as the preview Surface
        List<Size> bigEnough = new ArrayList<>();
        // Collect the supported resolutions that are smaller than the preview Surface
        List<Size> notBigEnough = new ArrayList<>();
        int w = aspectRatio.getWidth();
        int h = aspectRatio.getHeight();
        for (Size option : choices) {
            if (option.getWidth() <= maxWidth && option.getHeight() <= maxHeight && option.getHeight() == option.getWidth() * h / w) {
                if (option.getWidth() >= textureViewWidth && option.getHeight() >= textureViewHeight) {
                    bigEnough.add(option);
                } else {
                    notBigEnough.add(option);
                }
            }
        }

        // Pick the smallest of those big enough. If there is no one big enough, pick the
        // largest of those not big enough.
        if (bigEnough.size() > 0) {
            return Collections.min(bigEnough, new CompareSizesByArea());
        } else if (notBigEnough.size() > 0) {
            return Collections.max(notBigEnough, new CompareSizesByArea());
        } else {
            Log.e("camera2", "Couldn't find any suitable preview size");
            return choices[0];
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            fragmentState = (int) savedInstanceState.getSerializable(KEY_STATE);
            imageURI = savedInstanceState.getParcelable(KEY_PHOTO2FILEPATH);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(fragmentState == 1) {
            startCameraStreaming();
        }else{
            showPreviewLayout();
        }
    }


    @Override
    public void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_STATE,fragmentState);
        outState.putParcelable(KEY_PHOTO2FILEPATH, imageURI);
    }

    @Override
    public void onPause() {
        if(cameraCaptureSession != null) {
            closeCamera(cameraCaptureSession.getDevice());
        }
        super.onPause();
    }

    private void startCameraStreaming(){
        startBackgroundThread();

        // When the screen is turned off and turned back on, the SurfaceTexture is already
        // available, and "onSurfaceTextureAvailable" will not be called. In that case, we can open
        // a camera and start preview from here (otherwise, we wait until the surface is ready in
        // the SurfaceTextureListener).
        if (autoFitTextureView.isAvailable()) {
            openCamera(autoFitTextureView.getWidth(), autoFitTextureView.getHeight());
        } else {
            autoFitTextureView.setSurfaceTextureListener(new TVSurfaceTextureListener());
        }
    }


    /**
     * Sets up member variables related to camera.
     *  @param width  The width of available size for camera preview
     * @param height The height of available size for camera preview
     */
    private String setUpCameraOutputs(int width, int height, CameraManager manager) {
        try {
            for (String cam : manager.getCameraIdList()) {
                CameraCharacteristics cameraCharacteristics = manager.getCameraCharacteristics(cam);

                // We don't use a front facing camera in this sample.
                Integer facing = cameraCharacteristics.get(CameraCharacteristics.LENS_FACING);
                if (facing != null && facing == CameraCharacteristics.LENS_FACING_FRONT) {
                    continue;
                }

                StreamConfigurationMap map = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                if (map == null) {
                    continue;
                }

                // For still image captures, we use the largest available size.
                Size largest = Collections.max(Arrays.asList(map.getOutputSizes(ImageFormat.JPEG)), new CompareSizesByArea());
                //set up image reader
                if(imageReader != null){
                    imageReader = null;
                }
                imageReader = ImageReader.newInstance(largest.getWidth(), largest.getHeight(), ImageFormat.JPEG, /*maxImages*/2);
                imageReader.setOnImageAvailableListener(onImageAvailableListener, backgroundHandler);

                // Find out if we need to swap dimension to get the preview size relative to sensor coordinate.
                int displayRotation = getActivity().getWindowManager().getDefaultDisplay().getRotation();
                //noinspection ConstantConditions
                sensorOrientation = cameraCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
                boolean swappedDimensions = false;
                switch (displayRotation) {
                    case Surface.ROTATION_0:
                    case Surface.ROTATION_180:
                        if (sensorOrientation == 90 || sensorOrientation == 270) {
                            swappedDimensions = true;
                        }
                        break;
                    case Surface.ROTATION_90:
                    case Surface.ROTATION_270:
                        if (sensorOrientation == 0 || sensorOrientation == 180) {
                            swappedDimensions = true;
                        }
                        break;
                    default:
                        Log.e("camera2", "Display rotation is invalid: " + displayRotation);
                }

                Point displaySize = new Point();
                getActivity().getWindowManager().getDefaultDisplay().getSize(displaySize);
                int rotatedPreviewWidth = width;
                int rotatedPreviewHeight = height;
                int maxPreviewWidth = displaySize.x;
                int maxPreviewHeight = displaySize.y;

                if (swappedDimensions) {
                    rotatedPreviewWidth = height;
                    rotatedPreviewHeight = width;
                    maxPreviewWidth = displaySize.y;
                    maxPreviewHeight = displaySize.x;
                }

                if (maxPreviewWidth > MAX_PREVIEW_WIDTH) {
                    maxPreviewWidth = MAX_PREVIEW_WIDTH;
                }

                if (maxPreviewHeight > MAX_PREVIEW_HEIGHT) {
                    maxPreviewHeight = MAX_PREVIEW_HEIGHT;
                }

                // Danger, W.R.! Attempting to use too large a preview size could  exceed the camera
                // bus' bandwidth limitation, resulting in gorgeous previews but the storage of
                // garbage capture data.
                previewSize = chooseOptimalSize(map.getOutputSizes(SurfaceTexture.class), rotatedPreviewWidth, rotatedPreviewHeight, maxPreviewWidth, maxPreviewHeight, largest);

                // We fit the aspect ratio of TextureView to the size of preview we picked.
                int orient = getResources().getConfiguration().orientation;
                if (orient == Configuration.ORIENTATION_LANDSCAPE) {
                    autoFitTextureView.setAspectRatio(previewSize.getWidth(), previewSize.getHeight());
                } else {
                    autoFitTextureView.setAspectRatio(previewSize.getHeight(), previewSize.getWidth());
                }

                // Check if the flash is supported.
                Boolean available = cameraCharacteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
                flashSupported = available == null ? false : available;

                return cam;

            }
        } catch (CameraAccessException | NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }


    @TargetApi(Build.VERSION_CODES.M)
    private void openCamera(int width, int height) {
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestCameraPermission(); //if the user does not accept, the app stops
            return;
        }

        Activity activity = getActivity();
        CameraManager manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
        String cameraID = setUpCameraOutputs(width, height, manager); //this method defines wich camera will use
        configureTransform(width, height);

        try {
            if (!cameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
                throw new RuntimeException("Time out waiting to lock camera opening.");
            }

            CDStateCallback stateCallback = new CDStateCallback();
            if(cameraID != null) {
                manager.openCamera(cameraID, stateCallback, backgroundHandler);
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while trying to lock camera opening.", e);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void requestCameraPermission() {
        if (this.shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
            new ConfirmationDialog().show(getChildFragmentManager(), "dialog");
        } else {
            getActivity().requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], int[] grantResults) {
        //
    }

    /**
     * Shows OK/Cancel confirmation dialog about camera permission.
     */
    public static class ConfirmationDialog extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            //final Fragment parent = getParentFragment();
            return new AlertDialog.Builder(getActivity()).setMessage(R.string.request_permission).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
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


        /**
         * Closes the current {@link CameraDevice}.
         */
    private void closeCamera(CameraDevice camera) {
        try {
            cameraOpenCloseLock.acquire();
            if (null != cameraCaptureSession) {
                cameraCaptureSession.close();
                cameraCaptureSession = null;
            }
            if (null != camera) {
                camera.close();
            }
            if (null != imageReader) {
                imageReader.close();
                imageReader = null;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while trying to lock camera closing.", e);
        } finally {
            cameraOpenCloseLock.release();
        }
    }

    /**
     * Starts a background thread and its {@link Handler}.
     */
    private void startBackgroundThread() {
        backgroundThread = new HandlerThread("CameraBackground");
        backgroundThread.start();
        backgroundHandler = new Handler(backgroundThread.getLooper());
    }

    /**
     * Stops the background thread and its {@link Handler}.
     */
    private void stopBackgroundThread() {
        backgroundThread.quitSafely();
        try {
            backgroundThread.join();
            backgroundThread = null;
            backgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private class CCSStateCallback extends CameraCaptureSession.StateCallback{

        @Override
        public void onConfigured(@NonNull CameraCaptureSession session) {
            // When the session is ready, we start displaying the preview.
            cameraCaptureSession = session;

            try {
                // Auto focus should be continuous for camera preview.
                previewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                // Flash is automatically enabled when necessary.
                setAutoFlash(previewRequestBuilder);

                // Finally, we start displaying the camera preview.
                CaptureRequest previewRequest = previewRequestBuilder.build();

                cameraCaptureSession.setRepeatingRequest(previewRequest, new CameraPreCaptureCallback(), backgroundHandler);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onConfigureFailed(@NonNull CameraCaptureSession session) {
            showMessage("Failed");
        }
    }

    /**
     * Creates a new {@link CameraCaptureSession} for camera preview.
     */
    private void createCameraPreviewSession(CameraDevice camera) {
        try {
            cameraState = STATE_PREVIEW;
            SurfaceTexture texture = autoFitTextureView.getSurfaceTexture();
            assert texture != null;

            // We configure the size of default buffer to be the size of camera preview we want.
            texture.setDefaultBufferSize(previewSize.getWidth(), previewSize.getHeight());

            // This is the output Surface we need to start preview.
            Surface surface = new Surface(texture);

            // We set up a CaptureRequest.Builder with the output Surface.
            previewRequestBuilder = camera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            previewRequestBuilder.addTarget(surface);

            // Here, we create a CameraCaptureSession for camera preview.
            fragmentState = 1;
            camera.createCaptureSession(Arrays.asList(surface, imageReader.getSurface()), new CCSStateCallback(), null);

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Configures the necessary {@link android.graphics.Matrix} transformation to `autoFitTextureView`.
     * This method should be called after the camera preview size is determined in
     * setUpCameraOutputs and also the size of `autoFitTextureView` is fixed.
     *
     * @param viewWidth  The width of `autoFitTextureView`
     * @param viewHeight The height of `autoFitTextureView`
     */
    private void configureTransform(int viewWidth, int viewHeight) {
        Activity activity = getActivity();
        if (null == autoFitTextureView || null == previewSize || null == activity) {
            return;
        }
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        Matrix matrix = new Matrix();
        RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
        RectF bufferRect = new RectF(0, 0, previewSize.getHeight(), previewSize.getWidth());
        float centerX = viewRect.centerX();
        float centerY = viewRect.centerY();
        if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
            float scale = Math.max(
                    (float) viewHeight / previewSize.getHeight(),
                    (float) viewWidth / previewSize.getWidth());
            matrix.postScale(scale, scale, centerX, centerY);
            matrix.postRotate(90 * (rotation - 2), centerX, centerY);
        } else if (Surface.ROTATION_180 == rotation) {
            matrix.postRotate(180, centerX, centerY);
        }
        autoFitTextureView.setTransform(matrix);
    }

    /**
     * Initiate a still image capture.
     */
    private void takePicture() {
        try {
            // This is how to tell the camera to lock focus.
            previewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, CameraMetadata.CONTROL_AF_TRIGGER_START);
            // Tell #captureCallback to wait for the lock.
            cameraState = STATE_WAITING_LOCK;
            //captureCallback = new CameraPreCaptureCallback();
            cameraCaptureSession.capture(previewRequestBuilder.build(), new CameraPreCaptureCallback(), backgroundHandler);
            //preview builder as it is
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }


    private void captureStillPicture(CameraDevice camera) {
        try {
            final Activity activity = getActivity();
            if (null == activity || null == camera) {
                return;
            }
            // This is the CaptureRequest.Builder that we use to take a picture.
            final CaptureRequest.Builder captureBuilder = camera.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(imageReader.getSurface());

            // Use the same AE and AF modes as the preview.
            captureBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
            setAutoFlash(captureBuilder);

            // Orientation
            int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, getOrientation(rotation));

            cameraCaptureSession.stopRepeating();
            cameraCaptureSession.capture(captureBuilder.build(), new CameraPostCaptureCallback(), null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the JPEG orientation from the specified screen rotation.
     *
     * @param rotation The screen rotation.
     * @return The JPEG orientation (one of 0, 90, 270, and 360)
     */
    private int getOrientation(int rotation) {
        // Sensor orientation is 90 for most devices, or 270 for some devices (eg. Nexus 5X)
        // We have to take that into account and rotate JPEG properly.
        // For devices with orientation of 90, we simply return our mapping from ORIENTATIONS.
        // For devices with orientation of 270, we need to rotate the JPEG 180 degrees.
        return (ORIENTATIONS.get(rotation) + sensorOrientation + 270) % 360;
    }

    private int getRotation(int cameraOrientation) {
        //now we have to determine frame orientation
        int rotation = getActivity().getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        if (cameraOrientation == 1){
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

    private void showPreviewLayout () {
                // hide the camera layout
                getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                photo_layout.setVisibility(View.INVISIBLE);

                // Shows the preview layout
                preview_layout.setVisibility(View.VISIBLE);
                //get rotation in degrees for image
                int rotation = getOrientation(absoluteImagePath);
                // load image on the ui control
                photo_preview.setImageURI(imageURI);
                photo_preview.setRotation(rotation);
                photo_preview.refreshDrawableState();
            }
        });


        //Glide.with(getActivity().getApplicationContext()).load(imageURI.toString()).into(photo_preview);
    }

    private void hidePreviewLayout () {
                // hide the preview layout
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                photo_layout.setVisibility(View.VISIBLE);
                // FIXME close camera here and open up again on new picture capture

                // Shows the preview layout
                preview_layout.setVisibility(View.INVISIBLE);
            }
        });
    }

    /**
     * Unlock the focus. This method should be called when still image capture sequence is
     * finished.
     */
    private void unlockFocus(CameraDevice camera) {
        closeCamera(camera);
        //show picture and preview layout
        fragmentState = 2;
        showPreviewLayout();
    }

    private void setAutoFlash(CaptureRequest.Builder requestBuilder) {
        if (flashSupported) {
            requestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
        }
    }

    /**
     * Saves a JPEG {@link Image} into the specified {@link File}.
     */
    private class ImageSaver implements Runnable {

        /**
         * The JPEG image
         */
        private final Image mImage;

        public ImageSaver(Image image) {
            mImage = image;
        }

        @Override
        public void run() {
            File imageFile = null;
            ByteBuffer buffer = mImage.getPlanes()[0].getBuffer();
            byte[] data = new byte[buffer.remaining()];
            buffer.get(data);
            try {
                imageFile = MultimediaIOManagement.saveTempFile(getActivity().getApplicationContext(), MultimediaIOManagement.PHOTO_EXTENSION, data);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            if(imageFile != null) {
                imageURI = Uri.fromFile(imageFile);
                absoluteImagePath = imageFile.getAbsolutePath();
                imageFileName = imageFile.getName();
                Log.e("Image URI", imageURI.toString());
            }
            mImage.close();
            }


    }

    /**
     * Compares two {@code Size}s based on their areas.
     */
    static class CompareSizesByArea implements Comparator<Size> {

        @Override
        public int compare(Size lhs, Size rhs) {
            // We cast here to ensure the multiplications won't overflow
            return Long.signum((long) lhs.getWidth() * lhs.getHeight() -
                    (long) rhs.getWidth() * rhs.getHeight());
        }

    }

    /**
     * Shows an error message dialog.
     */


    /*things from Stepfragment*/
    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_photo2;
    }

    @Override
    protected void onCreateViewStepFragment(View rootView, Bundle savedInstanceState) {
        Button b_take_picture = (Button) rootView.findViewById(R.id.b_take_picture);
        b_take_picture.setOnClickListener(new TakePictureClick());
        autoFitTextureView = (AutoFitTextureView) rootView.findViewById(R.id.textureView);
        //layouts
        photo_layout = (ViewGroup) rootView.findViewById(R.id.photo_layout);
        preview_layout = (ViewGroup) rootView.findViewById(R.id.preview_layout);
        //picture preview
        photo_preview = (ImageView) rootView.findViewById(R.id.photo_preview);

        Button b_back = (Button) rootView.findViewById(R.id.bt_retake_photo);
        b_back.setOnClickListener(new BackFromPreviewClick());
    }

    @Override
    protected PhotoStep getStep() {
        return (PhotoStep) step;
    }

    @Override
    protected boolean validate() {
        // TODO implement validate
        return true;
    }

    @Override
    protected StepResult getStepResult() {
        return new PhotoStepResult(imageFileName);
    }

    /*Helper classes*/
    private class TakePictureClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            takePicture();
        }
    }

    private class BackFromPreviewClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            hidePreviewLayout();
            startCameraStreaming();
        }
    }

    /**
     * {@link TextureView.SurfaceTextureListener} handles several lifecycle events on a
     * {@link TextureView}.
     */
    private class TVSurfaceTextureListener implements TextureView.SurfaceTextureListener {

        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            openCamera(width, height);
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            configureTransform(width, height);
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {

        }
    }

    /**
     * is called when {@link CameraDevice} changes its state.
     */
    private class CDStateCallback extends CameraDevice.StateCallback{

        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            // This method is called when the camera is opened.  We start camera preview here.
            cameraOpenCloseLock.release();
            //cameraDevice = camera;
            createCameraPreviewSession(camera);

        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            cameraOpenCloseLock.release();
            camera.close();
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            cameraOpenCloseLock.release();
            camera.close();
            Activity activity = getActivity();
            if (null != activity) {
                activity.finish();
            }
        }
    }

    private class CameraPostCaptureCallback extends CameraCaptureSession.CaptureCallback {
        @Override
        public void onCaptureCompleted(@NonNull CameraCaptureSession session,
                                       @NonNull CaptureRequest request,
                                       @NonNull TotalCaptureResult result) {

            unlockFocus(session.getDevice());
        }
    }

    private class CameraPreCaptureCallback extends CameraCaptureSession.CaptureCallback {

        private void process(CaptureResult result, CameraDevice camera) {
            switch (cameraState) {
                case STATE_PREVIEW: {
                    // We have nothing to do when the camera preview is working normally.
                    break;
                }
                case STATE_WAITING_LOCK: {
                    Integer afState = result.get(CaptureResult.CONTROL_AF_STATE);
                    if ((afState == null) || (true)){
                        captureStillPicture(camera);
                    } else if (CaptureResult.CONTROL_AF_STATE_FOCUSED_LOCKED == afState || CaptureResult.CONTROL_AF_STATE_NOT_FOCUSED_LOCKED == afState)
                    {
                        // CONTROL_AE_STATE can be null on some devices
                        Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                        if (aeState == null ||
                                aeState == CaptureResult.CONTROL_AE_STATE_CONVERGED) {
                            cameraState = STATE_PICTURE_TAKEN;
                            captureStillPicture(camera);
                        } else {
                            showMessage("run precapture secuence");
                        }
                    }
                    break;
                }
                case STATE_WAITING_PRECAPTURE: {
                    // CONTROL_AE_STATE can be null on some devices
                    Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                    if (aeState == null ||
                            aeState == CaptureResult.CONTROL_AE_STATE_PRECAPTURE ||
                            aeState == CaptureRequest.CONTROL_AE_STATE_FLASH_REQUIRED) {
                        cameraState = STATE_WAITING_NON_PRECAPTURE;
                    }
                    break;
                }
                case STATE_WAITING_NON_PRECAPTURE: {
                    // CONTROL_AE_STATE can be null on some devices
                    Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                    if (aeState == null || aeState != CaptureResult.CONTROL_AE_STATE_PRECAPTURE) {
                        cameraState = STATE_PICTURE_TAKEN;
                        captureStillPicture(camera);
                    }
                    break;
                }
            }
        }

        @Override
        public void onCaptureProgressed(@NonNull CameraCaptureSession session,
                                        @NonNull CaptureRequest request,
                                        @NonNull CaptureResult partialResult) {
            process(partialResult, session.getDevice());
        }

        @Override
        public void onCaptureCompleted(@NonNull CameraCaptureSession session,
                                       @NonNull CaptureRequest request,
                                       @NonNull TotalCaptureResult result) {
            process(result, session.getDevice());
        }

    }
}
