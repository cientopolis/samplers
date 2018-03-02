package org.cientopolis.samplers.ui;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.cientopolis.samplers.R;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by Xavier on 17/06/2017.
 */

public abstract class ErrorMessaging {

    public static void showInfoMessage(Context context, String mensaje) {
        showMessage(context, mensaje, R.layout.toast_info_message);
    }

    public static void showValidationErrorMessage(Context context, String mensaje) {

        showMessage(context, mensaje, R.layout.toast_validation_error_message);

    }

    public static void showErrorMessage(Context context, String mensaje) {

        showMessage(context, mensaje, R.layout.toast_error_message);

    }

    public static void showFatalErrorMessage(Context context, String mensaje) {

        showMessage(context, mensaje, R.layout.toast_fatal_error_message);

    }

    private static void showMessage(Context context, String mensaje, int layoutResource) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(layoutResource, null);
        //(ViewGroup) findViewById(R.id.toast_mensaje_error_container));

        TextView text = (TextView) layout.findViewById(R.id.lb_toast_error_text);
        text.setText(mensaje);

        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }


}
