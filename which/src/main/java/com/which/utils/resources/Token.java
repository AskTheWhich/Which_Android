package com.which.utils.resources;

/**
 * Created by tomeramir on 01/09/2016.
 */
public class Token {
    private String access_token;

    public Token(String access_token) {
        this.access_token = access_token;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }
}
