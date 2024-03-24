package com.zjp.Utility;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @author ZhuJinPeng
 * @version 1.0
 */
public class TimeUtility {
    private static LocalDateTime ldt = null;
    private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");

    public static String getCurrentTime()
    {
        ldt = LocalDateTime.now();
        return  ldt.format(dtf);
    }
}
