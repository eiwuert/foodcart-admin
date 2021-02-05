package de.habibhaidari.foodcart.service;

import android.content.Context;

import java.lang.reflect.Type;

import de.habibhaidari.foodcart.model.ReportRequest;

import static de.habibhaidari.foodcart.constant.APIConstants.REPORTS_ROUTE;

public class ReportService extends ModelService<ReportRequest> {


    public ReportService(Context context) {
        super(context);
    }

    @Override
    String getRoute() {
        return REPORTS_ROUTE;
    }

    @Override
    Type getListType() {
        return null;
    }

    @Override
    Type getType() {
        return null;
    }


}
