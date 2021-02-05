package de.habibhaidari.foodcart.callbackhandler;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.mazenrashed.printooth.utilities.PrintingCallback;

public class PrintingCallbackHandler implements PrintingCallback {

    private final Context context;

    public PrintingCallbackHandler(Context context) {
        this.context = context;
    }

    @Override
    public void connectingWithPrinter() {
        Toast.makeText(context, "Verbinde mit Drucker...", Toast.LENGTH_LONG).show();
    }

    @Override
    public void connectionFailed(@NonNull String s) {
        Toast.makeText(context, "Verbinden fehlgeschlagen: " + s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onError(@NonNull String s) {
        Toast.makeText(context, "Fehler: " + s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onMessage(@NonNull String s) {
        Toast.makeText(context, s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void printingOrderSentSuccessfully() {
        Toast.makeText(context, "Auftrag an Drucker gesendet...", Toast.LENGTH_LONG).show();
    }

}
