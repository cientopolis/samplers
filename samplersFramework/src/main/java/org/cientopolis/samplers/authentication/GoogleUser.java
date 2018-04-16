package org.cientopolis.samplers.authentication;

/**
 * Created by Xavier on 04/04/2018.
 */

public class GoogleUser implements User {

    public static final String AUTHENTICATION_TYPE = "google";

    private String userName;
    private String userId;

    public GoogleUser(String userName, String userId) {
        this.userName = userName;
        this.userId = userId;
    }

    @Override
    public String getAuthenticationType() {
        return AUTHENTICATION_TYPE;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "GoogleUser{" +
                "userName='" + userName + '\'' +
                '}';
    }
}
