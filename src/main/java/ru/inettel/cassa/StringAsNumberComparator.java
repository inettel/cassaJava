package ru.inettel.cassa;

import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringAsNumberComparator implements Comparator<String> {
    public StringAsNumberComparator() {
    }

    public int compare(String o1, String o2) {
        float numO1 = 0.0F;
        float numO2 = 0.0F;
        int compareResult = 0;

        try {
            numO1 = Float.parseFloat(o1);
        } catch (NumberFormatException var8) {
            numO1 = this.parseMixed(o1);
        }

        try {
            numO2 = Float.parseFloat(o2);
        } catch (NumberFormatException var7) {
            numO2 = this.parseMixed(o2);
        }

        if (numO1 > numO2) {
            compareResult = 1;
        } else if (numO1 < numO2) {
            compareResult = -1;
        }

        return compareResult;
    }

    private float parseMixed(String mixedString) {
        float result = 0.0F;
        if (!mixedString.isEmpty()) {
            Pattern pattern = Pattern.compile("\\D");
            Matcher matcher = pattern.matcher(mixedString);
            if (matcher.find() & matcher.start() > 0) {
                result = Float.parseFloat(mixedString.substring(0, matcher.start()));
            }

            result = (float)((double)result + 0.5);
        }

        return result;
    }
}
