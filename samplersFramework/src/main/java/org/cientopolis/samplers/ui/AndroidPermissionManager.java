package org.cientopolis.samplers.ui;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;

/**
 * Created by Xavier on 27/10/2017.
 */

public class AndroidPermissionManager {

    private static final int PERMISSIONS_GRANTED = 1;

    @TargetApi(Build.VERSION_CODES.M)
    public static void requestPermission(Activity activity, String permission) {

        if (activity.shouldShowRequestPermissionRationale(permission)) {
            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.

            //new ConfirmationDialog().show(getChildFragmentManager(), "dialog");
        } else {
            activity.requestPermissions(new String[]{permission}, PERMISSIONS_GRANTED);
        }
    }

}
