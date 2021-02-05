package de.habibhaidari.foodcart.util;

import java.util.Map;
import java.util.stream.Collectors;

public class UrlUtils {

    public static String formatParameters(Map<String, String> parameters) {
        if (parameters == null || parameters.size() == 0) {
            return "";
        }
        return "?".concat(parameters.keySet()
                .stream()
                .map(key -> key + "=" + parameters.get(key))
                .collect(Collectors.joining("&")));
    }

}
