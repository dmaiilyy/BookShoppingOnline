package com.example.dalaptrinhapp.Model;

import com.google.gson.annotations.SerializedName;

public class ordermodel {
    private int order_id;
    private int total_amount;
    private double total_price;
    private int user_id;
    private String user_name;
    private String user_phone;
    private String user_email;
    private String user_add;
    @SerializedName("created_at")
    private String createdate;
    @SerializedName("create_date")
    private String created;


    public ordermodel(final int order_id, final int total_amount, final double total_price, final int user_id, final String user_name, final String user_phone, final String user_email, final String user_add, final String createdate, final String created) {
        this.order_id = order_id;
        this.total_amount = total_amount;
        this.total_price = total_price;
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_phone = user_phone;
        this.user_email = user_email;
        this.user_add = user_add;
        this.createdate = createdate;
        this.created = created;
    }

    public int getOrder_id() {
        return this.order_id;
    }

    public void setOrder_id(final int order_id) {
        this.order_id = order_id;
    }

    public int getTotal_amount() {
        return this.total_amount;
    }

    public void setTotal_amount(final int total_amount) {
        this.total_amount = total_amount;
    }

    public double getTotal_price() {
        return this.total_price;
    }

    public void setTotal_price(final double total_price) {
        this.total_price = total_price;
    }

    public int getUser_id() {
        return this.user_id;
    }

    public void setUser_id(final int user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return this.user_name;
    }

    public void setUser_name(final String user_name) {
        this.user_name = user_name;
    }

    public String getUser_phone() {
        return this.user_phone;
    }

    public void setUser_phone(final String user_phone) {
        this.user_phone = user_phone;
    }

    public String getUser_email() {
        return this.user_email;
    }

    public void setUser_email(final String user_email) {
        this.user_email = user_email;
    }

    public String getUser_add() {
        return this.user_add;
    }

    public void setUser_add(final String user_add) {
        this.user_add = user_add;
    }

    public String getCreatedate() {
        return this.createdate;
    }

    public void setCreatedate(final String createdate) {
        this.createdate = createdate;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}
