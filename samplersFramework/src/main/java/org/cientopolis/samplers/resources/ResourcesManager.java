package org.cientopolis.samplers.resources;

import android.content.Context;

/**
 * Created by Xavier on 19/11/2019.
 */

public class ResourcesManager {

    public static String getStringResourceByName(Context context, String stringName) {

        String packageName = context.getPackageName();
        int resId = context.getResources().getIdentifier(stringName, "string", packageName);
        return context.getString(resId);
    }
}
