package com.example.eppdraft1.main.utils;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;
import org.threeten.bp.YearMonth;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.temporal.WeekFields;

import java.util.ArrayList;
import java.util.Locale;

public class CalendarUtils {

    public static LocalDate selectedDate;


    public static String formattedTime(LocalTime time)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss a");
        return time.format(formatter);
    }

    public static String monthYearFromDate(LocalDate date)  {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }

    public static int getWeekNumber(LocalDate date){
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        return date.get(weekFields.weekOfWeekBasedYear());
    }

    public static ArrayList<LocalDate> daysInMonthArray(LocalDate date) {

        ArrayList<LocalDate> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth   = YearMonth.from(date);

        int daysInMonth  = yearMonth.lengthOfMonth();

        LocalDate firstDayOfMonth  = CalendarUtils.selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstDayOfMonth.getDayOfWeek().getValue();

        for (int i=1; i<=42; i++) {
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek)
                daysInMonthArray.add(null);
            else
                daysInMonthArray.add(LocalDate.of(selectedDate.getYear(),
                        selectedDate.getMonth(), i - dayOfWeek ));
        }

        return daysInMonthArray;

    }

    public static ArrayList<LocalDate> daysInWeekArray(LocalDate selectedDate) {
        ArrayList<LocalDate> days = new ArrayList<>();
        LocalDate current = sundayForDate(selectedDate); // establishing selectedDate's sunday
        assert current != null;
        LocalDate endDate = current.plusWeeks(1); // establishing one week ahead of current date

        // constructing selectedDate's week, which is an array of LocalDate's
        while (current.isBefore(endDate))
        {
            days.add(current);
            current = current.plusDays(1);
        }
        return days; // returning that array of local dates
    }

    private static LocalDate sundayForDate(LocalDate current)
    {
        LocalDate oneWeekAgo = current.minusWeeks(1);

        while (current.isAfter(oneWeekAgo))
        {
            if(current.getDayOfWeek() == DayOfWeek.SUNDAY)
                return current;

            current = current.minusDays(1);
        }

        return null;
    }


}
