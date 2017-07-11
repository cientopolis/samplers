package org.cientopolis.samplers.ui.take_sample;

import android.net.Uri;

import java.io.Serializable;

/**
 * Created by Xavier on 01/07/2017.
 */

public interface PhotoFragmentCallbacks extends Serializable{

    public void onPhotoTaked(Uri imageURI);
}
