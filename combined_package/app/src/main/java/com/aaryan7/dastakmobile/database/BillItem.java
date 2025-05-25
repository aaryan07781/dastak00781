package com.aaryan7.dastakmobile.database;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "bill_items", 
        foreignKeys = {
            @ForeignKey(entity = Bill.class, 
                        parentColumns = "id", 
                        childColumns = "billId", 
                        onDelete = ForeignKey.CASCADE),
            @ForeignKey(entity = Product.class, 
                        parentColumns = "id", 
                        childColumns = "productId", 
                        onDelete = ForeignKey.NO_ACTION)
        },
        indices = {@Index("billId"), @Index("productId")})
public class BillItem {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int billId;
    private int productId;
    private String productName;
    private double price;
    private int quantity;
    private double totalPrice;
    private double profit;

    public BillItem(int billId, int productId, String productName, double price, int quantity, double profit) {
        this.billId = billId;
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.totalPrice = price * quantity;
        this.profit = profit * quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
        this.totalPrice = price * this.quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.totalPrice = this.price * quantity;
        // Recalculate profit per item * quantity
        this.profit = (this.profit / this.quantity) * quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }
}
