package org.cientopolis.samplers;

/**
 * Created by Xavier on 16/04/2018.
 * Holder class for the network configurations params
 */

public class NetworkConfiguration {

    String URL;
    String PARAM_NAME_SAMPLE;
    String PARAM_NAME_USER_ID;
    String PARAM_NAME_AUTHENTICATION_TYPE;

    public NetworkConfiguration(String URL, String PARAM_NAME_SAMPLE, String PARAM_NAME_USER_ID, String PARAM_NAME_AUTHENTICATION_TYPE) {
        this.URL = URL;
        this.PARAM_NAME_SAMPLE = PARAM_NAME_SAMPLE;
        this.PARAM_NAME_USER_ID = (PARAM_NAME_USER_ID != null)?PARAM_NAME_USER_ID:"";
        this.PARAM_NAME_AUTHENTICATION_TYPE = (PARAM_NAME_AUTHENTICATION_TYPE != null)?PARAM_NAME_AUTHENTICATION_TYPE:"";
    }
}
