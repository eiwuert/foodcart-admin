package de.habibhaidari.foodcart.model;

import com.google.gson.annotations.SerializedName;

import static de.habibhaidari.foodcart.constant.FormatConstants.FOOD_WITH_NUMBER_FORMAT;

public class Food {
    private int id;
    private String name;
    private String description;
    private String number;


    @SerializedName("category_id")
    private int categoryId;

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

    public String getNameWithNumber() {
        if (number != null && !number.equals("")) {
            return String.format(FOOD_WITH_NUMBER_FORMAT, getName(), getNumber());
        } else return getName();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
}
