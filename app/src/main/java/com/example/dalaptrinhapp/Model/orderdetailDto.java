package com.example.dalaptrinhapp.Model;

public class orderdetailDto {
    private String title;
    private String author;
    private double unitprice;
    private int quantity;
    private String image;

    public orderdetailDto(final String title, final String author, final double unitprice, final int quantity, final String image) {
        this.title = title;
        this.author = author;
        this.unitprice = unitprice;
        this.quantity = quantity;
        this.image = image;
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

    public double getUnitprice() {
        return this.unitprice;
    }

    public void setUnitprice(final double unitprice) {
        this.unitprice = unitprice;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(final int quantity) {
        this.quantity = quantity;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
