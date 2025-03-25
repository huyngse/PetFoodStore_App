package com.example.petfoodstore_app.models;

import java.util.List;

public class Cart {
    private int cartId;
    private int totalAmount;
    private List<CartItem> items;

    public Cart(int cartId, int totalAmount, List<CartItem> items) {
        this.cartId = cartId;
        this.totalAmount = totalAmount;
        this.items = items;
    }

    public int getCartId() {
        return cartId;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public List<CartItem> getItems() {
        return items;
    }
}
