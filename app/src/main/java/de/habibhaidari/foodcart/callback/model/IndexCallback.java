package de.habibhaidari.foodcart.callback.model;

import java.util.List;

public interface IndexCallback<T> {

    void onIndexed(List<T> index);

}
