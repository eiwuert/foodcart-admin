package de.habibhaidari.foodcart.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Variant {

    private int id;
    private int price;

    @SerializedName("food_id")
    private int foodId;
    private Food food;
    private ArrayList<Variation> variations;


    public ArrayList<Variation> getVariations() {
        return variations;
    }

    public void setVariations(ArrayList<Variation> variations) {
        this.variations = variations;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }
}
