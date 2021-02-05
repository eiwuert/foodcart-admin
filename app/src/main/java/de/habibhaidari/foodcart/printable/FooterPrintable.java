package de.habibhaidari.foodcart.printable;

import com.mazenrashed.printooth.data.printable.Printable;
import com.mazenrashed.printooth.data.printable.TextPrintable;
import com.mazenrashed.printooth.data.printer.DefaultPrinter;

import java.util.ArrayList;

public class FooterPrintable extends ArrayList<Printable> {

    public FooterPrintable() {
        super();
        add(new TextPrintable.Builder()
                .setText("Guten Apetitt! ")
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
                .setFontSize(DefaultPrinter.Companion.getFONT_SIZE_LARGE())
                .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC1252())
                .setNewLinesAfter(1)
                .build());
        add(new TextPrintable.Builder()
                .setText("Dieser Beleg ist keine Rechnung!")
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
                .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC1252())
                .setNewLinesAfter(3)
                .build());
    }
}
