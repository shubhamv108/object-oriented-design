package logginglibrary.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static String getFormattedDate(final Date date, final String pattern) {
        return new SimpleDateFormat(pattern).format(date);
    }

}
