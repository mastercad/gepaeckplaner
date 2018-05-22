package de.byte_artist.luggage_planner.service;

import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class Date {

    public String localizeDate(Context context, String dateString, String format, Locale locale) {
        if (null != dateString
            && !dateString.isEmpty()
        ) {
            if (format.isEmpty()) {
                format = "yyyy-MM-dd";
            }
            SimpleDateFormat sdf = new SimpleDateFormat(format, locale);
            java.util.Date date;
            try {
                date = sdf.parse(dateString);
            } catch (ParseException e) {
                return dateString;
            }
            java.text.DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(context);
            return dateFormat.format(date);
        }
        return dateString;
    }
}
