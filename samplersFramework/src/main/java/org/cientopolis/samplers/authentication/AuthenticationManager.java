package org.cientopolis.samplers.authentication;

import android.app.Fragment;

import org.cientopolis.samplers.bus.BusProvider;
import org.cientopolis.samplers.bus.LoginEvent;
import org.cientopolis.samplers.ui.LoginFragment;

/**
 * Created by Xavier on 04/04/2018.
 */

public class AuthenticationManager {

    private static User user = null;
    private static boolean authenticationEnabled = false;
    private static boolean authenticationOptional = true;
    private static Class loginFragmentClass = LoginFragment.class;

    public static <T extends Fragment> Class<T> getLoginFragmentClass() {
        return loginFragmentClass;
    }

    public static <T extends Fragment> void setToginFragmentClass(Class<T> type) {
        AuthenticationManager.loginFragmentClass = type;
    }

    public static User getUser() {
        return user;
    }

    public static void login(User user) {
        AuthenticationManager.user = user;
        BusProvider.getInstance().post(new LoginEvent(user));
    }

    public static void logout() {
        AuthenticationManager.user = null;
        BusProvider.getInstance().post(new LoginEvent(null));
    }


    public static boolean isAuthenticationEnabled() {
        return authenticationEnabled;
    }

    public static void setAuthenticationEnabled(boolean authenticationEnabled) {
        AuthenticationManager.authenticationEnabled = authenticationEnabled;
    }

    public static boolean isAuthenticationOptional() {
        return authenticationOptional;
    }

    public static void setAuthenticationOptional(boolean authenticationOptional) {
        AuthenticationManager.authenticationOptional = authenticationOptional;
    }
}
