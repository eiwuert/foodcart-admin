package de.habibhaidari.foodcart.constant;

public class FormatConstants {
    public static final String APPLICATION_NAME = "FoodCart";
    public static final String FOOD_WITH_NUMBER_FORMAT = "%s (#%s)";

    public static final String RESOURCE_FORMAT_CONTACT_WA = "https://wa.me/%s";
    public static final String RESOURCE_FORMAT_CONTACT_TEL = "tel:%s";
    public static final String RESOURCE_FORMAT_CONTACT_MAIL = "mailto:%s";

    public static final String TIME_FORMAT = "%d:%02d Uhr";

    public static final String FROM_TO_TIME_FORMAT = "Von %s bis %s";

    public static final String MINIMUM_SUBTOTAL_FORMAT = "Ab einem Bestellwert von %s €";

    public static final String ORDER_NUMBER_FORMAT = "#%d";

    public static final String ORDER_NUMBER_NAME_FORMAT = "#%d %s";

    public static final String DATA_RETRIVAL_FAILED = "Fehler %d beim Abrufen der Daten";

    public static final String FILE_RETRIVAL_FAILED = "Fehler %d beim Abrufen der Datei";

    public static final String SERVER_CONNECTION_FAILED ="Fehler bei der Verbindung zum Server";

    public static final String[] WEEKDAYS_NAMES = new String[]{
            "Sonntag", "Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag"
    };


    public static final int RESOURCE_WA_DEFORMAT = 14;
    public static final int RESOURCE_TEL_DEFORMAT = 4;
    public static final int RESOURCE_MAILTO_DEFORMAT = 7;
}
