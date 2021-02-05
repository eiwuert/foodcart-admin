package de.habibhaidari.foodcart.service;

import android.content.Context;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import de.habibhaidari.foodcart.constant.APIConstants;
import de.habibhaidari.foodcart.model.Region;

public class RegionService extends ModelService<Region> {

    public RegionService(Context context) {
        super(context);
    }

    @Override
    String getRoute() {
        return APIConstants.REGION_ROUTE;
    }

    @Override
    Type getListType() {
        return new TypeToken<List<Region>>() {
        }.getType();
    }

    @Override
    Type getType() {
        return new TypeToken<Region>() {
        }.getType();
    }
}
