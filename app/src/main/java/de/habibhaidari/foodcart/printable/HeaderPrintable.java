package de.habibhaidari.foodcart.printable;

import com.mazenrashed.printooth.data.printable.Printable;
import com.mazenrashed.printooth.data.printable.TextPrintable;
import com.mazenrashed.printooth.data.printer.DefaultPrinter;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import de.habibhaidari.foodcart.BuildConfig;

public class HeaderPrintable extends ArrayList<Printable> {


    public HeaderPrintable(Map<String, ?> restaurant) {
        super();
        StringBuilder str = new StringBuilder("\n");
        str.append((String)restaurant.get("name"));
        str.append("\n");
        str.append((String)restaurant.get("street"));
        str.append("\n");
        str.append((String)restaurant.get("city"));
        str.append("\n");
        add(new TextPrintable.Builder()
                .setText(str.toString())
                .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC1252())
                .setNewLinesAfter(1)
                .build());

        try {
            add(new TextPrintable.Builder()
                    .setText(new URL(BuildConfig.APP_HOST).getHost())
                    .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
                    .setEmphasizedMode(DefaultPrinter.Companion.getEMPHASIZED_MODE_BOLD()) //Bold or normal
                    .setUnderlined(DefaultPrinter.Companion.getUNDERLINED_MODE_ON()) // Underline on/off
                    .setNewLinesAfter(1)
                    .build());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


    }
}
