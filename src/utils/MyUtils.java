package utils;

import java.time.LocalDateTime;

abstract public class MyUtils {
    public static String log(String data){
        LocalDateTime time = LocalDateTime.now();

        return "[" + time.getYear() + "/"  + time.getMonth().getValue()
                + "/" + time.getDayOfMonth() + " "
                + time.getHour() + ":" + time.getMinute() + ":"  + time.getSecond() + "] " + data;
    }
}
