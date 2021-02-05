package de.habibhaidari.foodcart.callback.model;

public interface UpdateCallback<T> {

    void onUpdated(Boolean updated, T object);

}
