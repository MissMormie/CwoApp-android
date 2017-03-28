package cwoapp.nl.cwoapp.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by sonja on 3/23/2017.
 */

public class DateUtil {

    public static String dateToYYYYMMDDString(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);

        return year + "-" + month + "-" + day;
    }

    /**
     * works for strings y*-m*-d*
     *
     * @return
     */
    public static Date stringToDate(String dateString) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
        try {
            return sdf.parse(dateString);
        } catch (ParseException e) {
            return null;
        }


    }
}
