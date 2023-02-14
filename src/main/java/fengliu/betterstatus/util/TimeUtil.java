package fengliu.betterstatus.util;

import java.text.DecimalFormat;

public class TimeUtil {
    public static String getSimpleTimeToString(int duration){
        duration = duration / 2;
        if (duration / 36000 > 0){
            return new DecimalFormat("#h").format(duration / 36000);
        }

        if (duration / 600 > 0){
            return new DecimalFormat("#m").format(duration / 600);
        }

        if (duration / 10 > 0){
            return new DecimalFormat("#s").format(duration / 10);
        }

        return duration+"";
    }

}
