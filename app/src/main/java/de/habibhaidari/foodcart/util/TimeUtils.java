package de.habibhaidari.foodcart.util;

public class TimeUtils {
    public static final String COLON = ":";

    public static Integer parseTime(String s) {
        String[] timeStrings = s.split(COLON, 2);
        if (timeStrings[0] != null) {
            int time = 0;
            time += Integer.parseInt(timeStrings[0]) * 60;
            if (timeStrings.length > 1) {
                time += Integer.parseInt(timeStrings[1]);
            }
            return time;
        }
        return null;
    }


}
