package br.edu.ifpe.tads.pdm.projeto.util;

import org.parceler.apache.commons.lang.time.DateFormatUtils;

import java.util.Calendar;

/**
 * Created by Edmilson on 24/10/2016.
 */

public class Util {

    /**
     * Converte uma data em uma string no formato yyyy-MM-dd
     * @param calendar
     * @return
     */
    public static String dateToString(Calendar calendar) {
        String datePattern = "yyyy-MM-dd";
        return DateFormatUtils.format(calendar, datePattern);
    }
}
