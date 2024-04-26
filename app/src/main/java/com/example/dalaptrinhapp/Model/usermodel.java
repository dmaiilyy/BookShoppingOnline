package com.example.dalaptrinhapp.Model;

public class usermodel {
    private int user_id;
    private String username;
    private String email;
    private String password;
    private String phonenumber;
    private String address;

    public usermodel(final int user_id, final String username, final String email, final String password, final String phonenumber, final String address) {
        this.user_id = user_id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.phonenumber = phonenumber;
        this.address = address;
    }

    public int getUser_id() {
        return this.user_id;
    }

    public void setUser_id(final int user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getPhonenumber() {
        return this.phonenumber;
    }

    public void setPhonenumber(final String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
