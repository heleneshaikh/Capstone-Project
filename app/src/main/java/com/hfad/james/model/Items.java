package com.hfad.james.model;


import java.util.ArrayList;

/**
 * Created by heleneshaikh on 01/09/16.
 */
public class Items {
    double amount;
    double price;
    String title;
    double totalPricePerItem;
    int key;
    String type;
    double totalSum;

    public Items() {
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getTotalPricePerItem() {
        return totalPricePerItem;
    }

    public void setTotalPricePerItem(double totalPricePerItem) {
        this.totalPricePerItem = totalPricePerItem;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(double totalSum) {
        this.totalSum = totalSum;
    }

}
