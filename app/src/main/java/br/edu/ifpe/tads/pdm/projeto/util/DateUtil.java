package br.edu.ifpe.tads.pdm.projeto.util;

import org.parceler.apache.commons.lang.time.DateFormatUtils;
import org.parceler.apache.commons.lang.time.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Edmilson on 24/10/2016.
 */

public class DateUtil {

    public static final String DATE_PATTERN = "yyyy-MM-dd";

    /**
     * Converte uma data de um Calendar em uma string no formato yyyy-MM-dd
     * @param calendar
     * @return
     */
    public static String dateToString(Calendar calendar) {
        return DateFormatUtils.format(calendar, DATE_PATTERN);
    }

    /**
     * Converte uma string em uma data no formato yyyy-MM-dd
     * @param date
     * @return
     */
    public static Date stringToDate(String date) {

        try {
            return DateUtils.parseDate(date, new String[]{DATE_PATTERN});
        } catch (ParseException e) {
            return null;
        }
    }



    /**
     * Obtém o ano de um data
     * @param date
     * @return
     */
    public static String getYearOfDate(Date date) {
        String year = "";
        if (date != null ) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            year = String.valueOf(calendar.get(Calendar.YEAR));
        }
        return year;
    }
}
