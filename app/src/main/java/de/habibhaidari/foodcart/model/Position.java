package de.habibhaidari.foodcart.model;

import java.util.ArrayList;
import java.util.List;

public class Position {

    private int quantity;
    private Variant variant;
    private ArrayList<Extra> extras;

    public List<Extra> getExtras() {
        return extras;
    }

    public void setExtras(ArrayList<Extra> extras) {
        this.extras = extras;
    }

    public int getPrice() {
        return variant.getPrice() + getExtras().stream().mapToInt(Extra::getPrice).sum();
    }

    public int getTotal() {
        return getPrice() * getQuantity();
    }

    public Variant getVariant() {
        return variant;
    }

    public void setVariant(Variant variant) {
        this.variant = variant;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
