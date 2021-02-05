package de.habibhaidari.foodcart.service;

import android.content.Context;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import de.habibhaidari.foodcart.constant.APIConstants;
import de.habibhaidari.foodcart.model.Order;

public class OrderService extends ModelService<Order> {

    public OrderService(Context context) {
        super(context);
    }

    @Override
    String getRoute() {
        return APIConstants.ORDERS_ROUTE;
    }

    @Override
    Type getListType() {
        return new TypeToken<List<Order>>() {
        }.getType();
    }

    @Override
    Type getType() {
        return new TypeToken<Order>() {
        }.getType();
    }

}