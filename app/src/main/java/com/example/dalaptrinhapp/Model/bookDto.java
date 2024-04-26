package com.example.dalaptrinhapp.Model;

import com.google.gson.annotations.SerializedName;

public class bookDto {
    private int book_id;
    private String title;
    private String author;
    private String description;
    private double price;
    private int stock_quantity;
    private int category_id;
    private String image;
    @SerializedName("name")
    private String category_name;

    public bookDto(final int book_id, final String title, final String author, final String description, final double price, final int stock_quantity, final int category_id, final String image, final String category_name) {
        this.book_id = book_id;
        this.title = title;
        this.author = author;
        this.description = description;
        this.price = price;
        this.stock_quantity = stock_quantity;
        this.category_id = category_id;
        this.image = image;
        this.category_name = category_name;
    }

    public int getBook_id() {
        return this.book_id;
    }

    public void setBook_id(final int book_id) {
        this.book_id = book_id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(final String author) {
        this.author = author;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(final double price) {
        this.price = price;
    }

    public int getStock_quantity() {
        return this.stock_quantity;
    }

    public void setStock_quantity(final int stock_quantity) {
        this.stock_quantity = stock_quantity;
    }

    public int getCategory_id() {
        return this.category_id;
    }

    public void setCategory_id(final int category_id) {
        this.category_id = category_id;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(final String image) {
        this.image = image;
    }

    public String getCategory_name() {
        return this.category_name;
    }

    public void setCategory_name(final String category_name) {
        this.category_name = category_name;
    }
}
