package in.eightfolds.utils;

import java.util.Calendar;

/**
 * Created by Sanjay on 5/7/2016.
 */
public class EightfoldsCalendar {
    private static EightfoldsCalendar eightfoldsCalendar;

    private EightfoldsCalendar() {
    }

    public static EightfoldsCalendar getInstance() {
        if (eightfoldsCalendar == null) {
            eightfoldsCalendar = new EightfoldsCalendar();
        }
        return eightfoldsCalendar;
    }

    /**
     * @param calendar
     */
    public void setMaxTime(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, calendar.getMaximum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calendar.getMaximum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getMaximum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, calendar.getMaximum(Calendar.MILLISECOND));
    }

    /**
     * @param calendar
     */
    public void setMinTime(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, calendar.getMinimum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calendar.getMinimum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getMinimum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, calendar.getMinimum(Calendar.MILLISECOND));
    }
}
