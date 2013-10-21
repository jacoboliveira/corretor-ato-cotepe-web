/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ato.cotepe.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author jacoboliveira
 */
public class DateHelper {
    public static final String FORMAT_DDMMYYYY_HHMM="dd/MM/yyyy HH:mm";
    public static final String FORMAT_YYYYMMDD_HHMM="yyyy-MM-dd HH:mm";
    
    public static String getDataHoje(String format){
        return new SimpleDateFormat(format).format(new Date());
    }
}
