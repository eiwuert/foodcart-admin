package de.habibhaidari.foodcart.service;

import android.content.Context;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import de.habibhaidari.foodcart.constant.APIConstants;
import de.habibhaidari.foodcart.model.OpeningHour;

public class OpeningHourService extends ModelService<OpeningHour> {


    public OpeningHourService(Context context) {
        super(context);
    }

    @Override
    String getRoute() {
        return APIConstants.OPENING_HOURS_ROUTE;
    }

    @Override
    Type getListType() {
        return new TypeToken<List<OpeningHour>>() {
        }.getType();
    }

    @Override
    Type getType() {
        return  new TypeToken<OpeningHour>() {
        }.getType();
    }


}