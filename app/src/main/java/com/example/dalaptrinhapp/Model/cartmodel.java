package com.example.dalaptrinhapp.Model;

public class cartmodel {
    private int id;
    private int user_id;
    private int book_id;
    private int quantity;

    public cartmodel(final int id, final int user_id, final int book_id, final int quantity) {
        this.id = id;
        this.user_id = user_id;
        this.book_id = book_id;
        this.quantity = quantity;
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
}
