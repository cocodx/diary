package io.github.cocodx.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @author amazfit
 * @date 2022-08-03 下午11:24
 **/
public class DateUtils {

    static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static Date parseDay(String dateStr) throws ParseException {
        return simpleDateFormat.parse(dateStr);
    }

    public static String SIMPLE_UUID (){
        String s = UUID.randomUUID().toString();
        s.replaceAll("-","");
        return s;
    }
}
