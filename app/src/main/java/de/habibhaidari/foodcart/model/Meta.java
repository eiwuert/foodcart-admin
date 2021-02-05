package de.habibhaidari.foodcart.model;

import static de.habibhaidari.foodcart.constant.FormatConstants.RESOURCE_FORMAT_CONTACT_MAIL;
import static de.habibhaidari.foodcart.constant.FormatConstants.RESOURCE_FORMAT_CONTACT_TEL;
import static de.habibhaidari.foodcart.constant.FormatConstants.RESOURCE_FORMAT_CONTACT_WA;
import static de.habibhaidari.foodcart.constant.FormatConstants.RESOURCE_MAILTO_DEFORMAT;
import static de.habibhaidari.foodcart.constant.FormatConstants.RESOURCE_TEL_DEFORMAT;
import static de.habibhaidari.foodcart.constant.FormatConstants.RESOURCE_WA_DEFORMAT;

public class Meta {

    public static final String GLOBAL_PREFERENCES = "de.habibhaidari.foodcart_preferences";

    public static final String EMPTY_STRING = "";

    public static final String META_NAME_CLOSED = "closed";
    public static final String META_NAME_COUNTRY = "country";
    public static final String META_NAME_CITY = "city";
    public static final String META_NAME_STREET = "street";
    public static final String META_NAME_AUTHORITY = "authority";
    public static final String META_NAME_NAME = "name";
    public static final String META_NAME_VATID = "vat_id";
    public static final String META_NAME_DESCRIPTION = "description";
    public static final String META_NAME_CONTACT_TEL = "contact_tel";
    public static final String META_NAME_CONTACT_WA = "contact_wa";
    public static final String META_NAME_CONTACT_MAIL = "contact_mail";
    public static final String META_NAME_REPRESENTATIVE = "representative";


    private String name;
    private Object value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public void formatValue() {
        if (name.equals(META_NAME_CONTACT_WA)) {
            value = String.format(RESOURCE_FORMAT_CONTACT_WA, value);
        }

        if (name.equals(META_NAME_CONTACT_TEL)) {
            value = String.format(RESOURCE_FORMAT_CONTACT_TEL, value);
        }

        if (name.equals(META_NAME_CONTACT_MAIL)) {
            value = String.format(RESOURCE_FORMAT_CONTACT_MAIL, value);
        }
    }

    public void deformatValue() {
        if (value instanceof String) {
            if (name.equals(META_NAME_CONTACT_WA)) {
                value = ((String) value).substring(RESOURCE_WA_DEFORMAT);
            }
            if (name.equals(META_NAME_CONTACT_MAIL)) {
                value = ((String) value).substring(RESOURCE_MAILTO_DEFORMAT);
            }
            if (name.equals(META_NAME_CONTACT_TEL)) {
                value = ((String) value).substring(RESOURCE_TEL_DEFORMAT);
            }
        }
    }

}
