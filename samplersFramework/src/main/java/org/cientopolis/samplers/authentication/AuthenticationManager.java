package org.cientopolis.samplers.authentication;


import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import org.cientopolis.samplers.bus.BusProvider;
import org.cientopolis.samplers.bus.LoginEvent;

/**
 * Created by Xavier on 04/04/2018.
 */

public class AuthenticationManager {

    private static final String PREFERENCES_NAME = "org.cientopolis.samplers.PREFERENCES_NAME";
    private static final String PREFERENCES_KEY_USER = "org.cientopolis.samplers.PREFERENCES_KEY_USER";
    private static final String PREFERENCES_KEY_USER_CLASS = "org.cientopolis.samplers.PREFERENCES_KEY_USER_CLASS";

    private static boolean authenticationEnabled = false;
    private static boolean authenticationOptional = true;
    private static Class loginFragmentClass = StandardLoginFragment.class;

    public static <T extends LoginFragment> Class<T> getLoginFragmentClass() {
        return loginFragmentClass;
    }

    public static <T extends LoginFragment> void setLoginFragmentClass(Class<T> type) {
        AuthenticationManager.loginFragmentClass = type;
    }

    public static User getUser(Context context) {

        return retrieveUser(context);
    }

    public static void login(User user, Context context) {

        saveUser(user, context);

        BusProvider.getInstance().post(new LoginEvent(user));
    }

    public static void logout(Context context) {

        removeUser(context);

        BusProvider.getInstance().post(new LoginEvent(null));
    }

    private static void saveUser(User user, Context context) {

        SharedPreferences sharedPref = context.getSharedPreferences (PREFERENCES_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        Gson gson = new Gson();
        String jsonUser = gson.toJson(user);

        editor.putString(PREFERENCES_KEY_USER,jsonUser);
        editor.putString(PREFERENCES_KEY_USER_CLASS,user.getClass().getCanonicalName());
        editor.commit();

    }

    private static void removeUser(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences (PREFERENCES_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.remove(PREFERENCES_KEY_USER);
        editor.remove(PREFERENCES_KEY_USER_CLASS);
        editor.commit();
    }

    private static User retrieveUser(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences (PREFERENCES_NAME,Context.MODE_PRIVATE);

        Gson gson = new Gson();
        String jsonUser = sharedPref.getString(PREFERENCES_KEY_USER, "");
        String className = sharedPref.getString(PREFERENCES_KEY_USER_CLASS, "");
        User obj = null;

        if (!jsonUser.equals("")) {

            try {
                obj = (User) gson.fromJson(jsonUser, Class.forName(className));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException("");
            }
        }

        return obj;
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
