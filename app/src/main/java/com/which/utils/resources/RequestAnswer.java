package com.which.utils.resources;

/**
 * Created by tomeramir on 01/09/2016.
 */
public class RequestAnswer {
    String access_token;
    int ask_id;
    String pick;

    public RequestAnswer(String access_token, int ask_id, String pick) {
        this.access_token = access_token;
        this.ask_id = ask_id;
        this.pick = pick;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public int getAsk_id() {
        return ask_id;
    }

    public void setAsk_id(int ask_id) {
        this.ask_id = ask_id;
    }

    public String getPick() {
        return pick;
    }

    public void setPick(String pick) {
        this.pick = pick;
    }
}
