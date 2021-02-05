package de.habibhaidari.foodcart.model;

import java.util.ArrayList;

public class Region {
    private int id;
    private ArrayList<Rate> rates;

    private ArrayList<Postcode> postcodes;

    public ArrayList<Postcode> getPostcodes() {
        return postcodes;
    }

    public void setPostcodes(ArrayList<Postcode> postcodes) {
        this.postcodes = postcodes;
    }

    public ArrayList<Rate> getRates() {
        return rates;
    }

    public void setRates(ArrayList<Rate> rates) {
        this.rates = rates;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
