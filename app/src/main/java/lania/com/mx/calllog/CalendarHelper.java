package lania.com.mx.calllog;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by clerks on 9/14/15.
 */
public final class CalendarHelper {
    public static Calendar getCalendarToBeginingOfCurrentDate() {
        Calendar calendar = GregorianCalendar.getInstance();
        calendarToBeginingOfDate(calendar);
        return calendar;
    }

    public static void calendarToBeginingOfDate(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    public static Calendar getCalendarToEndOfCurrentDate() {
        Calendar calendar = getCalendarToBeginingOfCurrentDate();
        calendar.add(Calendar.DATE, 1);
        calendar.set(Calendar.SECOND, -1);

        return calendar;
    }

    public static Calendar dateToCalendar(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }
}
