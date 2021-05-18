package org.cientopolis.samplers.bus;

import androidx.annotation.Nullable;

import org.cientopolis.samplers.authentication.User;

/**
 * Created by Xavier on 05/04/2018.
 */

public class LoginEvent {

    public User user;

    public LoginEvent(@Nullable User user) {
        this.user = user;
    }
}
