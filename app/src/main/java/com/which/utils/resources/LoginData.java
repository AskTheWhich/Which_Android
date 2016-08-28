package com.which.utils.resources;

import com.which.data.entitties.User;

/**
 * Created by tomeramir on 27/08/2016.
 */
public class LoginData {
    private String email;
    private String password;

    public LoginData() {
    }

    public LoginData(User user) {
        this.email = user.getEmail();
        this.password = user.getPassword();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
