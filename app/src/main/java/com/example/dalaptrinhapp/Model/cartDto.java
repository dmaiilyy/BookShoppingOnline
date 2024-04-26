package com.example.dalaptrinhapp.Model;

import com.google.gson.annotations.SerializedName;

public class cartDto {
    private int id;
    private int user_id;
    private int book_id;
    private int quantity;
    @SerializedName("title")
    private String book_title;
    @SerializedName("image")
    private String book_image;
    @SerializedName("author")
    private String book_author;
    @SerializedName("price")
    private double book_price;

    public cartDto(final int id, final int user_id, final int book_id, final int quantity, final String book_title, final String book_image, final String book_author, final double book_price) {
        this.id = id;
        this.user_id = user_id;
        this.book_id = book_id;
        this.quantity = quantity;
        this.book_title = book_title;
        this.book_image = book_image;
        this.book_author = book_author;
        this.book_price = book_price;
    }

    public int getId() {
        return this.id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public int getUser_id() {
        return this.user_id;
    }

    public void setUser_id(final int user_id) {
        this.user_id = user_id;
    }

    public int getBook_id() {
        return this.book_id;
    }

    public void setBook_id(final int book_id) {
        this.book_id = book_id;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(final int quantity) {
        this.quantity = quantity;
    }

    public String getBook_title() {
        return this.book_title;
    }

    public void setBook_title(final String book_title) {
        this.book_title = book_title;
    }

    public String getBook_image() {
        return this.book_image;
    }

    public void setBook_image(final String book_image) {
        this.book_image = book_image;
    }

    public String getBook_author() {
        return this.book_author;
    }

    public void setBook_author(final String book_author) {
        this.book_author = book_author;
    }

    public double getBook_price() {
        return this.book_price;
    }

    public void setBook_price(final double book_price) {
        this.book_price = book_price;
    }
}
