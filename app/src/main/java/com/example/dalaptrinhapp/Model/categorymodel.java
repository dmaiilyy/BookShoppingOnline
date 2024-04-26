package com.example.dalaptrinhapp.Model;

public class categorymodel {
    private int category_id;
    private String name;

    public categorymodel(final int category_id, final String name) {
        this.category_id = category_id;
        this.name = name;
    }

    public int getCategory_id() {
        return this.category_id;
    }

    public void setCategory_id(final int category_id) {
        this.category_id = category_id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

//    public String getCategoryName( int category_id){
//
//    }
}
