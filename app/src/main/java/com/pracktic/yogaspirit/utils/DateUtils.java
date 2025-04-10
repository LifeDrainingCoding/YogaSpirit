package com.pracktic.yogaspirit.utils;


import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Date;

public class DateUtils {

    private final static String TAG = DateUtils.class.getName();

    public static String getStringFromDateTime(Date date){
        return new SimpleDateFormat("dd-MM-yyyy HH:mm").format(date);
    }
    public static String getStringFromDate(Date date){
        return new SimpleDateFormat("dd-MM-yyyy").format(date);
    }
    public static String getStringFromDate(LocalDate localDate){
        return new SimpleDateFormat("dd-MM-yyyy").format(asDate(localDate));
    }
    public static LocalDate getLDFromString(String localDate){
        return asLocalDate(getDateFromString(localDate));
    }

    public static Date getDateFromString(String stringDate){
        try {
            return new SimpleDateFormat("dd-MM-yyyy").parse(stringDate);
        }catch (ParseException ex){
            Log.e(TAG, "getDateFromString: error during parsing date", ex );
            return new Date();
        }
    }
    public static LocalDateTime asLocalDateTime(Date date){
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
    public static Date asDate(LocalDateTime ldt){
        return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
    }
    public static Date asDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }
    public static LocalDate asLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }
    
}
