package com.example.dalaptrinhapp.Model;

public class apiresponse {
    private int user_id;
    private String status;
    private String message;

    public apiresponse(final int user_id, final String status, final String message) {
        this.user_id = user_id;
        this.status = status;
        this.message = message;
    }

    public int getUser_id() {
        return this.user_id;
    }

    public void setUser_id(final int user_id) {
        this.user_id = user_id;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
