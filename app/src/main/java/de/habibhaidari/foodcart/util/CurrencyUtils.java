package de.habibhaidari.foodcart.util;

import android.util.Log;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class CurrencyUtils {

    public static final String SPACE_SYMBOL = " ";

    public static String getFormattedCurrency(int price) {
        NumberFormat formatter = NumberFormat.getInstance(Locale.GERMANY);
        formatter.setMinimumFractionDigits(2);
        return formatter.format(price / 100.0);
    }

    public static Integer parseFormattedCurrency(String price) {
        try {
            String[] splitPrice = price.split(SPACE_SYMBOL);
            NumberFormat nf = NumberFormat.getInstance(Locale.GERMAN);
            Number d = nf.parse(splitPrice[0]);
            if (d != null) {
                return (int) (d.doubleValue() * 100);
            }
        } catch (ParseException e) {
            Log.d(CurrencyUtils.class.getName(), e.toString());
        }
        return 0;
    }
}
