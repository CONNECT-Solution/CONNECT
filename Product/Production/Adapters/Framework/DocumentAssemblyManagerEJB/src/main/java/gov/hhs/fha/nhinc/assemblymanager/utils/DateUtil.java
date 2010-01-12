/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.assemblymanager.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kim
 */
public class DateUtil {

  private static DateFormat dfYYYYMMDD = null;
  private static DateFormat cdaDateFormat = null;
  

  static {
    dfYYYYMMDD = new SimpleDateFormat("yyyyMMdd");
    cdaDateFormat = new SimpleDateFormat("yyyyMMddHHmmssZ"); // YYYYMMDDHHmmss-0000
  }

  public static String formatYYYYMMDD(GregorianCalendar gCal) {
    return String.valueOf(gCal.get(GregorianCalendar.YEAR)) +
            String.valueOf(gCal.get(GregorianCalendar.MONTH)) +
            String.valueOf(gCal.get(GregorianCalendar.DATE));
  }

  public static Date unmarshalYYYYMMDD(String date) {
    try {
      return dfYYYYMMDD.parse(date);
    } catch (ParseException ex) {
      ex.printStackTrace();
      return null;
    }
  }

  public static String marshalYYYYMMDD(Date date) {
    return dfYYYYMMDD.format(date);
  }

  public static String convertToCDATime(Date date) {
    return cdaDateFormat.format(date);
  }

  /**
   * convert T-format date to CDA format date.
   * 
   * @param tDate
   * @return
   */
  public static String convertTFormatToCDATime(String tDate) throws Exception {
     if (tDate == null || tDate.length() < 0)
        return null;
     
     // today date
     if (tDate.equalsIgnoreCase("T"))
        convertToCDATime(Calendar.getInstance().getTime());

     throw new Exception("Need to be implemented!");
  }  
}
