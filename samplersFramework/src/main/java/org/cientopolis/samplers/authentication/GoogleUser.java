package org.cientopolis.samplers.authentication;

/**
 * Created by Xavier on 04/04/2018.
 */

public class GoogleUser implements User {

    public static final String AUTHENTICATION_NAME = "google";

    private String userName;
    private String userId;

    public GoogleUser(String userName, String userId) {
        this.userName = userName;
        this.userId = userId;
    }

    @Override
    public String getAuthenticationName() {
        return AUTHENTICATION_NAME;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public String getUserId() {
        return userId;
    }
}
