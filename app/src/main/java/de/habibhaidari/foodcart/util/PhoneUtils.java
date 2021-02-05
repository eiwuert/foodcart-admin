package de.habibhaidari.foodcart.util;

public class PhoneUtils {

    public static String DEFAULT_COUNTRY_CODE = "49";
    public static String DEFAULT_BEGINNING = "+";

    public static String ZERO = "0";
    public static String DOUBLE_ZERO = "00";

    public static String formatPhone(String phone) {
        phone = phone.replaceAll("\\s+", "");

        if (phone.startsWith(DOUBLE_ZERO)) {
            phone = DEFAULT_BEGINNING.concat(phone.substring(DOUBLE_ZERO.length()));
        } else if (phone.startsWith(ZERO)) {
            phone = DEFAULT_BEGINNING.concat(DEFAULT_COUNTRY_CODE).concat(phone.substring(ZERO.length()));
        }

        return phone;
    }

}
