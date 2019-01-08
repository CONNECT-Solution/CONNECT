/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
 * All rights reserved.
 *  
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above
 *       copyright notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the documentation
 *       and/or other materials provided with the distribution.
 *     * Neither the name of the United States Government nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package gov.hhs.fha.nhinc.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Format {

    private static final Logger LOG = LoggerFactory.getLogger(Format.class);

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

        if (LOG.isDebugEnabled()) {
            LOG.debug("=== isValidDateString('{}','{}') ===", dateStr, format);
        }

        try {
            Calendar calendar = getCalendarInstance(format, dateStr, true);
            if (calendar != null) {
                isValidDate = true;
            }
        } catch (Exception e) {
            LOG.debug("Could not verify date: {}", e.getLocalizedMessage(), e);
            isValidDate = false;
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("=== isValidDateString('{}','{}') = {} ===", dateStr, format, isValidDate);
        }

        return isValidDate;
    }

    /**
     *
     * @param format
     * @param date
     * @return
     */
    public static Calendar getCalendarInstance(String format, String date) {
        return getCalendarInstance(format, date, false);
    }

    public static Calendar getCalendarInstance(String format, String date, boolean strict) {
        // The date has to be in a valid format
        Calendar calendar = null;
        java.sql.Date dateValue;
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        formatter.setLenient(false);

        try {
            if (date != null && !date.trim().isEmpty() && formatter.parse(date) != null) {
                ParsePosition pos = new ParsePosition(0);
                Date parseDate = formatter.parse(date, pos);

                if (strict && (pos.getIndex() < format.length() || date.length() != format.length())) {
                    // no-op
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Strict Formatting Failed! [format='{}'; date='{}'; strict={}]", format, date,
                                strict);
                    }
                } else {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("parseDate = {}[format='{}'; date='{}'; strict={}]", parseDate, format, date, strict);
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
     * @param format
     * @param date
     * @return
     */
    public static Timestamp getTimestampInstance(String format, String date) {
        // The date has to be in a valid format
        Timestamp ts = null;
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        formatter.setLenient(false);
        try {
            if (date != null && !date.trim().isEmpty() && formatter.parse(date) != null) {
                ParsePosition pos = new ParsePosition(0);
                Date parseDate = formatter.parse(date, pos);

                if (LOG.isDebugEnabled()) {
                    LOG.debug("parseDate = {}[format='{}'; date='{}']", parseDate, format, date);
                }
                if (parseDate != null) {
                    ts = new Timestamp(parseDate.getTime());
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
        if (str.isEmpty()) {
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
