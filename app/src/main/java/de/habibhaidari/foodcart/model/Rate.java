package de.habibhaidari.foodcart.model;

import com.google.gson.annotations.SerializedName;

public class Rate {
    private int id;
    private int minimum;


    @SerializedName("region_id")
    private int regionId;
    private int costs;

    public int getRegionId() {
        return regionId;
    }

    public void setRegionId(int regionId) {
        this.regionId = regionId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMinimum() {
        return minimum;
    }

    public void setMinimum(int minimum) {
        this.minimum = minimum;
    }

    public int getCosts() {
        return costs;
    }

    public void setCosts(int costs) {
        this.costs = costs;
    }
}
