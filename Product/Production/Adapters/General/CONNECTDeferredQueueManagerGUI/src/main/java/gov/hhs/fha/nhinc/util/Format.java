/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class Format {

    private static final Log log = LogFactory.getLog("gov.hhs.fha.nhinc.util.Format");

    public static final String MMDDYYYY_DATEFORMAT = "MMddyyyy";
    public static final String MMDDYYYYHHMMSS_DATEFORMAT = "MMddyyyy HH:mm:ss";    
    public static final String DATETIME_DISPLAYFORMAT = "d MMM, yyyy hh:mm a";
    public static final String DATETIME_WITH_SECONDS_DISPLAYFORMAT = "d MMM, yyyy hh:mm:ss a";

    // Prevent instantiation
    private Format() {
    }

    /*
     * Date Functions
     */
    /**
     * Validate passed date string against the passed date format
     *
     * @param dateStr
     * @param format
     * @return boolean
     */
    public static boolean isValidDateString(String dateStr, String format) {
        boolean isValidDate = false;

        if (log.isDebugEnabled()) {
            log.debug("=== isValidDateString('" + dateStr + "','" + format + "') ===");
        }

        try {
            Calendar calendar = getCalendarInstance(format, dateStr, true);
            if (calendar != null) {
                isValidDate = true;
            }
        } catch (Exception e) {
            log.debug(e);
            isValidDate = false;
        }

        if (log.isDebugEnabled()) {
            log.debug("=== isValidDateString('" + dateStr + "','" + format + "') = " + isValidDate + " ===");
        }

        return isValidDate;
    }

    /**
     *
     * @param date
     * @return
     * @throws ServiceException
     */
    public static Calendar getCalendarInstance(String format, String date) {
        return getCalendarInstance(format, date, false);
    }

    public static Calendar getCalendarInstance(String format, String date, boolean strict) {
        // The date has to be in a valid format
        Calendar calendar = null;
        java.sql.Date dateValue = null;
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        formatter.setLenient(false);
        try {
            if (date != null && !date.trim().equals("") && formatter.parse(date) != null) {
                ParsePosition pos = new ParsePosition(0);
                Date parseDate = formatter.parse(date, pos);

                if (strict && ((pos.getIndex() < format.length()) || date.length() != format.length())) {
                    // no-op
                    if (log.isDebugEnabled()) {
                        log.debug("Strict Formatting Failed! [format='" + format + "'; date='" + date + "'; strict=" + strict + "]");
                    }
                } else {
                    if (log.isDebugEnabled()) {
                        log.debug("parseDate = " + parseDate + "[format='" + format + "'; date='" + date + "'; strict=" + strict + "]");
                    }
                    if (parseDate != null) {
                        dateValue = new java.sql.Date(parseDate.getTime());
                        calendar = Calendar.getInstance();
                        calendar.setTime(dateValue);
                    }
                }
            }
        } catch (ParseException pe) {
            return null;
        }
        return calendar;
    }

    /**
     *
     * @param date
     * @return
     * @throws ServiceException
     */
    public static Timestamp getTimestampInstance(String format, String date) {
        return getTimestampInstance(format, date, false);
    }

    public static Timestamp getTimestampInstance(String format, String date, boolean strict) {
        // The date has to be in a valid format
        Timestamp ts = null;
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        formatter.setLenient(false);
        try {
            if (date != null && !date.trim().equals("") && formatter.parse(date) != null) {
                ParsePosition pos = new ParsePosition(0);
                Date parseDate = formatter.parse(date, pos);

                if (strict && ((pos.getIndex() < format.length()) || date.length() != format.length())) {
                    // no-op
                    if (log.isDebugEnabled()) {
                        log.debug("Strict Formatting Failed! [format='" + format + "'; date='" + date + "'; strict=" + strict + "]");
                    }
                } else {
                    if (log.isDebugEnabled()) {
                        log.debug("parseDate = " + parseDate + "[format='" + format + "'; date='" + date + "'; strict=" + strict + "]");
                    }
                    if (parseDate != null) {
                        ts = new Timestamp(parseDate.getTime());
                    }
                }
            }
        } catch (ParseException pe) {
            return null;
        }
        return ts;
    }

    /**
     *
     * @param format
     * @param cal
     * @return
     */
    public static String getFormattedDate(String format, Calendar cal) {
        String retStr = null;
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        if (cal != null) {
            retStr = formatter.format(cal.getTime());
        }
        return retStr;
    }

    public static String getFormattedDate(String format, Date date) {
        String retStr = null;
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        if (date != null) {
            Calendar cal = getCalendarFromDate(date);
            retStr = formatter.format(cal.getTime());
        }
        return retStr;
    }

    public static String getFormattedDate(String format, Timestamp ts) {
        String retStr = null;
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        if (ts != null) {
            retStr = formatter.format(ts.getTime());
        }
        return retStr;
    }

    public static Calendar getCalendarFromDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }


    /*
     * String Functions
     */
    public static String padLeading(int value, String pad, int length) {
        String str = Integer.toString(value);
        return padLeading(str, pad, length);
    }

    public static String padLeading(String str, String pad, int length) {
        StringBuffer strbuf = new StringBuffer("");

        if (str == null) {
            str = "";
        }
        int strLength = str.length();

        if (pad == null) {
            pad = " ";
        }
        if (pad.length() > 1) {
            pad = pad.substring(0, 1);
        }

        if (strLength < length) {
            int padLength = length - strLength;
            for (int i = 0; i < padLength; i++) {
                strbuf.append(pad);
            }
            strbuf.append(str);
        } else {
            str = str.substring(0, length);
            strbuf = new StringBuffer(str);
        }

        return strbuf.toString();
    }

    public static String padTrailing(String str, String pad, int length) {
        StringBuffer strbuf = new StringBuffer("");

        if (str == null) {
            str = "";
        }
        int strLength = str.length();

        if (pad == null) {
            pad = " ";
        }
        if (pad.length() > 1) {
            pad = pad.substring(0, 1);
        }

        if (strLength < length) {
            strbuf.append(str);
            int padLength = length - strLength;
            for (int i = 0; i < padLength; i++) {
                strbuf.append(pad);
            }
        } else {
            str = str.substring(0, length);
            strbuf = new StringBuffer(str);
        }

        return strbuf.toString();
    }

    public static boolean isNumericString(String str) {

        boolean answer = true;
        if (str.equals("")) {
            answer = false;
        } else {
            for (int i = 0; i < str.length(); i++) {
                if (!Character.isDigit(str.charAt(i))) {
                    answer = false;
                    break;
                }
            }
        }
        return answer;
    }
}

