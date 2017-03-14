package org.cientopolis.samplers.network;

/**
 * Created by Xavier on 12/03/2017.
 */

public abstract class NetworkConfiguration {

    private static String URL; // e.g. "http://192.168.1.10/samplers/upload.php";
    private static String PARAM_NAME; // e.g. "sample";

    public static String getURL() {
        return URL;
    }

    public static String getPARAM_NAME() {
        return PARAM_NAME;
    }

    public static void setURL(String url) {
        URL = url;
    }

    public static void setPARAM_NAME(String param_name) {
        PARAM_NAME = param_name;
    }

}
