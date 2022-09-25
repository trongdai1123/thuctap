package com.example.mthshop.fortmat;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FortMartData {
    public static String fortMartTypeDoubleToMoney(double data) {
        DecimalFormat dfGerman = new DecimalFormat("#,###.##",
                new DecimalFormatSymbols(Locale.GERMAN));
        return dfGerman.format(data);
    }

    public static String getDateCurrent() {
        Date date = new Date();
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            String tmp = outputFormat.format(date);
            return tmp;
        }catch (Exception ex) {
            return null;
        }
    }

}
