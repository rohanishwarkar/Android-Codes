package com.example.rohan.databases;

/**
 * Created by rohan on 9/5/18.
 */

public class product {
    private int id;
    private String name;

    public product(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
