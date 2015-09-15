package lania.com.mx.calllog;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by clerks on 9/14/15.
 */
public final class CalendarHelper {
    public static Calendar getCalendarToBeginingOfCurrentDate() {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    public static Calendar getCalendarToEndOfCurrentDate() {
        Calendar calendar = getCalendarToBeginingOfCurrentDate();
        calendar.add(Calendar.DATE, 1);
        calendar.set(Calendar.SECOND, -1);

        return calendar;
    }
}
