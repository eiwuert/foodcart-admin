package de.habibhaidari.foodcart.callback;

import java.util.ArrayList;

import de.habibhaidari.foodcart.model.Order;

public interface OrderServiceCallback {
    void onOrdersChanged(ArrayList<Order> o);
}