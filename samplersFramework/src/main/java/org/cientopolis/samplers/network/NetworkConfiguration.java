package org.cientopolis.samplers.network;

/**
 * Created by Xavier on 12/03/2017.
 */

public abstract class NetworkConfiguration {

    private static String URL; // e.g. "http://192.168.1.10/samplers/upload.php";
    private static String PARAM_NAME_SAMPLE; // e.g. "sample";
    private static String PARAM_NAME_USER_ID; // e.g. "user_id";
    private static String PARAM_NAME_AUTHENTICATION_TYPE; // e.g. "authentication_type";

    public static String getURL() {
        return URL;
    }

    public static String getPARAM_NAME_SAMPLE() {
        return PARAM_NAME_SAMPLE;
    }

    public static void setURL(String url) {
        URL = url;
    }

    public static void setPARAM_NAME_SAMPLE(String param_name) {
        PARAM_NAME_SAMPLE = param_name;
    }

    public static String getPARAM_NAME_USER_ID() {
        return PARAM_NAME_USER_ID;
    }

    public static void setPARAM_NAME_USER_ID(String paramNameUserId) {
        PARAM_NAME_USER_ID = paramNameUserId;
    }

    public static String getPARAM_NAME_AUTHENTICATION_TYPE() {
        return PARAM_NAME_AUTHENTICATION_TYPE;
    }

    public static void setPARAM_NAME_AUTHENTICATION_TYPE(String paramNameAuthenticationType) {
        PARAM_NAME_AUTHENTICATION_TYPE = paramNameAuthenticationType;
    }
}
