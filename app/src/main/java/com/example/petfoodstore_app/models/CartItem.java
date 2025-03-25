package com.example.petfoodstore_app.models;

public class CartItem {
    //This supposed to be productId, not cartId
    private int cartId;
    private String name;
    private String image;
    private int quantity;
    private int totalAmount;

    public CartItem(int cartId, String name, String image, int quantity, int totalAmount) {
        this.cartId = cartId;
        this.name = name;
        this.image = image;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
    }

    public int getCartId() {
        return cartId;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getTotalAmount() {
        return totalAmount;
    }
}
