package ru.inettel.cassa;

import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Сравниваем цифровые параметры с буквенно-цифровыми, в которых не цифровые символы
 * содержатся на последних позициях, например "11" и "11а"
 * При сравнении считаем, что параметр, содержащий не цировой символ, больше такого же
 * цифрового на 0.5. То есть "11"=11, "11a"=11.5
 */
public class StringAsNumberComparator implements Comparator<String> {

    /**
     * @param o1    первый сравниваемый параметр
     * @param o2    второй сравниваемый параметр
     * @return      1 если o1>o2, 0 если o1=o2, -1 если o1<o2
     */
    @Override
    public int compare(String o1, String o2) {
        float numO1 = 0;
        float numO2 = 0;
        int compareResult = 0;
        try {
            numO1 = Float.parseFloat(o1);
        } catch (NumberFormatException e) {
            numO1 = parseMixed(o1);
        }
        try {
            numO2 = Float.parseFloat(o2);
        } catch (NumberFormatException e) {
            numO2 = parseMixed(o2);
        }
        if (numO1 > numO2)
            compareResult = 1;
        else if (numO1 < numO2)
            compareResult = -1;
        return compareResult;
    }

    /**
     *  Вызвыаеся, если параметр не удалось привести к float типу, т.е. он имеет вид "11а", "15-2" и т.д.
     * @param mixedString   строка, содержащая цифровые символы перед не цифровыми
     * @return  начальные цифровые символы, как float + 0.5 т.е "11a"=11.5, "15-2"=15.5
     */
    private float parseMixed(String mixedString) {
        float result = 0;
        if (!mixedString.isEmpty()) {
            Pattern pattern = Pattern.compile("\\D");
            Matcher matcher = pattern.matcher(mixedString);
            if (matcher.find() & (matcher.start() > 0)) {
                result = Float.parseFloat(mixedString.substring(0, matcher.start()));
            }
            result += .5;
        }
        return result;
    }
}
