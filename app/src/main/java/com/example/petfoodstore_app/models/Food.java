package com.example.petfoodstore_app.models;

import androidx.annotation.NonNull;

import java.util.Date;

public class Food {
    private int id;
    private String image;
    private String name;
    private String description;
    private int price;
    private String category;
    private String petType;
    private String foodType;
    private String create_at;

    public Food(int id, String image, String name, int price, String category, String petType, String foodType, String create_at, String description) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.price = price;
        this.category = category;
        this.petType = petType;
        this.foodType = foodType;
        this.create_at = create_at;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPetType() {
        return petType;
    }

    public void setPetType(String petType) {
        this.petType = petType;
    }

    public String getFoodType() {
        return foodType;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }

    public String getCreate_at() {
        return create_at;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }

    @NonNull
    @Override
    public String toString() {
        return name; 
    }
}
