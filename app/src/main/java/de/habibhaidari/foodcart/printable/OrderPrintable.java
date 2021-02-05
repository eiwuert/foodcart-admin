package de.habibhaidari.foodcart.printable;

import android.annotation.SuppressLint;

import com.mazenrashed.printooth.data.printable.Printable;
import com.mazenrashed.printooth.data.printable.TextPrintable;
import com.mazenrashed.printooth.data.printer.DefaultPrinter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;

import de.habibhaidari.foodcart.model.Order;
import de.habibhaidari.foodcart.util.CurrencyUtils;

public class OrderPrintable extends ArrayList<Printable> {


    @SuppressLint("SimpleDateFormat")
    public OrderPrintable(Order o, Map<String, ?> r) {
        super();
        addAll(new HeaderPrintable(r));
        // Bestellnummer
        add(new TextPrintable.Builder()
                .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC1252())
                .setText(String.format("%1$-8s%2$24s", "#" + o.getId(), new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(o.getCreatedAt().getTime())))
                .setEmphasizedMode(DefaultPrinter.Companion.getEMPHASIZED_MODE_BOLD())
                .setNewLinesAfter(1)
                .build());

        //Kunde
        add(new TextPrintable.Builder()
                .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC1252())
                .setText("Kundendaten:\n")
                .setEmphasizedMode(DefaultPrinter.Companion.getEMPHASIZED_MODE_BOLD())
                .setNewLinesAfter(0)
                .build());

        StringBuilder str = new StringBuilder(o.getName());
        str.append("\n");
        str.append(o.getStreet());
        str.append("\n");
        str.append(o.getPostcode().getPostcode());
        str.append(" ");
        if (o.getPostcode().getCities().size() > 0) {
            str.append(o.getPostcode().getCities().get(0).getName());
        }
        str.append("\n");
        if (o.getFloor() != null) {
            str.append(o.getFloor());
            str.append("\n");
        }
        str.append(o.getPhone());
        str.append("\n");
        str.append(o.getUser().getEmail());

        add(new TextPrintable.Builder()
                .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC1252())
                .setText(str.toString())
                .setNewLinesAfter(1)
                .build());

        // Anmerkungen
        if (o.getNotes() != null) {
            add(new TextPrintable.Builder()
                    .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC1252())
                    .setEmphasizedMode(DefaultPrinter.Companion.getEMPHASIZED_MODE_BOLD())
                    .setText("Anmerkungen: \n")
                    .setNewLinesAfter(0)
                    .build());
            add(new TextPrintable.Builder()
                    .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC1252())
                    .setText(o.getNotes())
                    .setNewLinesAfter(1)
                    .build());

        }
        // Lieferzeit
        if (o.getDelivery() != -1) {
            add(new TextPrintable.Builder()
                    .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC1252())
                    .setEmphasizedMode(DefaultPrinter.Companion.getEMPHASIZED_MODE_BOLD())
                    .setText(String.format("%1$-22s%2$10s", "Lieferung:", o.getDeliveryFormatted()))
                    .setNewLinesAfter(1)
                    .build());
        }
        // Method
        add(new TextPrintable.Builder()
                .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC1252())
                .setEmphasizedMode(DefaultPrinter.Companion.getEMPHASIZED_MODE_BOLD())
                .setText(o.getMethod().getName())
                .setNewLinesAfter(1)
                .build());

        // Produkte
        addAll(new PositionPrintable(o.getPositions()));
        // Summen
        add(new TextPrintable.Builder()
                .setText(String.format("%1$-22s%2$10s", "Zwischensumme:", CurrencyUtils.getFormattedCurrency(o.getSubtotal()) + " €") + "\n")
                .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC1252())
                .setNewLinesAfter(0)
                .build());
        add(new TextPrintable.Builder()
                .setText(String.format("%1$-22s%2$10s", "Lieferung:", CurrencyUtils.getFormattedCurrency(o.getRate().getCosts()) + " €") + "\n")
                .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC1252())
                .setNewLinesAfter(1)
                .build());
        add(new TextPrintable.Builder()
                .setText(String.format("%1$-22s%2$10s", "Gesamt:", CurrencyUtils.getFormattedCurrency(o.getTotal()) + " €"))
                .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC1252())
                .setEmphasizedMode(DefaultPrinter.Companion.getEMPHASIZED_MODE_BOLD())
                .setNewLinesAfter(1)
                .build());

        // footer
        addAll(new FooterPrintable());

    }
}
