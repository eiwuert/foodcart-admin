package de.habibhaidari.foodcart.printable;

import com.mazenrashed.printooth.data.printable.Printable;
import com.mazenrashed.printooth.data.printable.TextPrintable;
import com.mazenrashed.printooth.data.printer.DefaultPrinter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import de.habibhaidari.foodcart.model.Position;
import de.habibhaidari.foodcart.model.Variant;
import de.habibhaidari.foodcart.model.Variation;
import de.habibhaidari.foodcart.util.CurrencyUtils;

public class PositionPrintable extends ArrayList<Printable> {

    public static final int INDENTATION_WIDTH = 5;

    public PositionPrintable(List<Position> positions) {
        super();
        for (Position position : positions) {
            addVariant(position);
        }
        add(new TextPrintable.Builder()
                .setText("")
                .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC1252())
                .setNewLinesAfter(1)
                .build());
    }

    private void addVariant(Position p) {
        Variant v = p.getVariant();
        addIndented(p.getQuantity() + "x", v.getFood().getNameWithNumber());

        if (v.getVariations().size() > 0 || p.getExtras().size() > 0) {
            addNewLine();
            String[] extrasVariations = Stream.concat(
                    v.getVariations().stream().map(Variation::getName),
                    p.getExtras().stream().map(e -> "+" + e.getName())
            ).toArray(String[]::new);
            addIndented("", (String.join("\n", extrasVariations)));
        }
        addNewLine();

        addPrice(CurrencyUtils.getFormattedCurrency(p.getTotal()) + " €");


    }

    private void addNewLine() {
        add(new TextPrintable.Builder()
                .setText("\n")
                .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC1252())
                .setNewLinesAfter(0)
                .build());
    }


    private void addIndented(String str, String str2) {
        add(new TextPrintable.Builder()
                .setText(formatIndented(str, str2))
                .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC1252())
                .setNewLinesAfter(0)
                .build());
    }

    private void addPrice(String str) {
        add(new TextPrintable.Builder()
                .setText(str)
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_RIGHT())
                .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC1252())
                .setNewLinesAfter(1)
                .build());
    }

    private String formatIndented(String str, String str2) {
        StringBuilder result = new StringBuilder(str);
        int width = 32 - INDENTATION_WIDTH;

        StringBuilder spaces = new StringBuilder();
        for (int i = 0; i < INDENTATION_WIDTH; i++) {
            spaces.append(" ");
        }

        for (int i = 0; i < INDENTATION_WIDTH - str.length(); i++) {
            result.append(" ");
        }
        int i = 0;
        for (char c : str2.toCharArray()) {
            if (i == width) {
                result.append(spaces);
                i = 0;
            }
            result.append(c);
            i++;
            if (c == '\n') {
                result.append(spaces);
                i = 0;
            }
        }


        return result.toString();
    }

}
