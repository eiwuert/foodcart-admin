package de.habibhaidari.foodcart.service;

import android.content.Context;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import de.habibhaidari.foodcart.constant.APIConstants;
import de.habibhaidari.foodcart.model.Postcode;

public class PostcodeService extends ModelService<Postcode> {

    public PostcodeService(Context context) {
        super(context);
    }

    @Override
    String getRoute() {
        return APIConstants.POSTCODES_ROUTE;
    }

    @Override
    Type getListType() {
        return null;
    }

    @Override
    Type getType() {
        return new TypeToken<Postcode>() {
        }.getType();
    }

}
