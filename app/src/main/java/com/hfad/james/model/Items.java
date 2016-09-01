package com.hfad.james.model;

/**
 * Created by heleneshaikh on 01/09/16.
 */
public class Items {
    float amount;
    float price;
    String title;

    public Items() {
    }

    public Items(float amount, float price, String title) {
        this.amount = amount;
        this.price = price;
        this.title = title;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String toString() {
        return title;
    }
}
