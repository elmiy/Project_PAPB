package com.example.tugasakhir.util;

public class Formatting {

    public static String toRupiahCurrency(int amount) {
        String result = "";
        char[] separated = String.valueOf(amount).toCharArray();
        int index = separated.length - 1;
        int count = 1;

        do {
            result = String.format("%s%s", separated[index], result);
            if (count % 3 == 0 && index != 0) {
                result = String.format(".%s", result);
            }
            index--;
            count++;
        } while (index != -1);

        return result;
    }
}
