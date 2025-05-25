package com.aaryan7.dastakmobile.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "products")
public class Product {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private double purchasePrice;
    private double sellingPrice;
    private double profit;

    public Product(String name, double purchasePrice, double sellingPrice) {
        this.name = name;
        this.purchasePrice = purchasePrice;
        this.sellingPrice = sellingPrice;
        this.profit = sellingPrice - purchasePrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(double purchasePrice) {
        this.purchasePrice = purchasePrice;
        this.profit = this.sellingPrice - purchasePrice;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
        this.profit = sellingPrice - this.purchasePrice;
    }

    public double getProfit() {
        return profit;
    }
}
