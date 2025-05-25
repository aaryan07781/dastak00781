package com.aaryan7.dastakmobile.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Date;

@Entity(tableName = "bills")
public class Bill {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private Date date;
    private double totalAmount;
    private double totalProfit;

    public Bill(Date date, double totalAmount, double totalProfit) {
        this.date = date;
        this.totalAmount = totalAmount;
        this.totalProfit = totalProfit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(double totalProfit) {
        this.totalProfit = totalProfit;
    }
}
