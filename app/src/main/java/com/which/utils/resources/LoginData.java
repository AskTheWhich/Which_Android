package com.which.utils.resources;

import com.which.data.entitties.User;

/**
 * Created by tomeramir on 27/08/2016.
 */
public class LoginData {
    private String username;
    private String password;

    public LoginData() {
    }

    public LoginData(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
