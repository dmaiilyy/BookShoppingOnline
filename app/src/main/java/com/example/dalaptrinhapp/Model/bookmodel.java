package com.example.dalaptrinhapp.Model;

import java.io.Serializable;

public class bookmodel implements Serializable {
    private int book_id;
    private String title;
    private String author;
    private String description;
    private double price;
    private int stock_quantity;
    private int category_id;
    private String image;

    public bookmodel(final int book_id, final String title, final String author, final String description, final double price, final int stock_quantity, final int category_id, final String image) {
        this.book_id = book_id;
        this.title = title;
        this.author = author;
        this.description = description;
        this.price = price;
        this.stock_quantity = stock_quantity;
        this.category_id = category_id;
        this.image = image;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(final double price) {
        this.price = price;
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
}
