package com.example.evgeniy.mypizzamegaapp.Models;

public class Product {
    public int id;
    public String name;
    public int count;
    public int cost;
    public String unit;

    public Product(String name, int cost, String unit) {
        this.name = name;
        this.cost = cost;
        this.unit = unit;
    }

    public Product() {

    }
}
