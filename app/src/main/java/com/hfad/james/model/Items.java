package com.hfad.james.model;


/**
 * Created by heleneshaikh on 01/09/16.
 */
public class Items {
    double amount;
    double price;
    String title;
    double totalPricePerItem;
    double totalPrice;


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

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
