package de.habibhaidari.foodcart.service;

import android.content.Context;

import static de.habibhaidari.foodcart.constant.APIConstants.INVOICE_ROUTE;

public class InvoiceService extends FileService {

    public static final String INVOICE_FILETYPE =  "application/pdf";
    public static final String INVOICE_FOLDERNAME = "rechnungen";

    public InvoiceService(Context context) {
        super(context);
    }

    @Override
    String getRoute() {
        return INVOICE_ROUTE;
    }

    @Override
    String getLocalFoldername() {
        return INVOICE_FOLDERNAME;
    }

    public String getFiletype(){
       return INVOICE_FILETYPE;
    }
}
