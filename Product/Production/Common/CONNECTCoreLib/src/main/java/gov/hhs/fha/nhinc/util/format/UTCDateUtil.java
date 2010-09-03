/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.util.format;

import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Date utility for UTC dates.
 *
 * @author Neil Webb
 */
public class UTCDateUtil
{
    private static final String DATE_FORMAT_UTC = "yyyyMMddHHmmss";
    private static final String DATE_FORMAT_FULL = DATE_FORMAT_UTC + "Z";
    private static final String TIME_ZONE_UTC = "UTC";

    private Log log = null;

    public UTCDateUtil()
    {
        log = createLogger();
    }

    protected Log createLogger()
    {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    /**
     * Parse a date string as a UTC date. The date may optionally have a time zone which will be
     * used if present.
     *
     * @param dateString Date string to parse
     * @return Parsed date
     */
    public Date parseUTCDateOptionalTimeZone(String dateString)
    {
        return parseDate(dateString, DATE_FORMAT_FULL, TimeZone.getTimeZone(TIME_ZONE_UTC));
    }

    /**
     * Format a date using a UTC format.
     *
     * @param sourceDate Date to
     * @return
     */
    public String formatUTCDate(Date sourceDate)
    {
        return formatDate(sourceDate, DATE_FORMAT_UTC);
    }

    /**
     * Parses a string and returns a Date object having the given date format.
     *
     * @param dateString String to be parsed containing a date
     * @param dateFormat Format of the date to be parsed
     * @return Returns the Date object for the given string and format.
     */
    private Date parseDate(String dateString, String dateFormat, TimeZone timeZone)
    {
        // Candidate to move to a super class for other format types
        if(log.isDebugEnabled())
        {
            log.debug("Parsing (" + dateString + ") using format string (" + dateFormat + 
                ") and time zone (" + ((timeZone == null) ? "none" : timeZone.getDisplayName()) +
                ").");
        }
        Date parsed = null;
        if ((dateString != null) && (dateFormat != null))
        {
            try
            {
                String formatString = prepareDateFormatString(dateFormat, dateString);
                DateFormat formatter = createDateFormatter(formatString, timeZone);
                parsed = formatter.parse(dateString);
                if(parsed != null)
                {
                    log.debug("Date parsed successfully");
                }
            }
            catch (Throwable t)
            {
                log.warn("Error parsing '" + dateString + "' using format: '" + dateFormat + "'", t);
            }
        }
        return parsed;
    }

    /**
     * Create a date formatter.
     *
     * @param formatString Date format string
     * @param timeZone Optional time zone. Not used if null.
     * @return Prepared date formatter
     */
    private DateFormat createDateFormatter(String formatString, TimeZone timeZone)
    {
        // Candidate to move to a super class for other format types
        if(NullChecker.isNullish(formatString))
        {
            throw new IllegalArgumentException("Date format string is required to create a date formatter");
        }
        DateFormat formatter = new SimpleDateFormat(formatString);
        if(timeZone != null)
        {
            formatter.setTimeZone(timeZone);
        }
        return formatter;
    }

    /**
     * Prepare a date format string based on the length of the date string
     * to be parsed. The date Format will be reduced to meet the length of the date provided to
     * match the accuracy of the date string.
     * 
     * @param dateFormat Date format string (ex. yyyyMMddhhmmssZ)
     * @param dateString Date string to be parsed (ex. 19990205)
     * @return Modified format string based on the date string length (ex. yyyyMMdd)
     */
    private String prepareDateFormatString(String dateFormat, String dateString)
    {
        // Candidate to move to a super class for other format types
        String formatString = dateFormat;
        if ((dateString != null) && (dateFormat != null) && (dateString.length() > 0) && (dateString.length() < dateFormat.length()))
        {
            formatString = dateFormat.substring(0, dateString.length());
            if(log.isDebugEnabled())
            {
                log.debug("New dateFormat: " + dateFormat);
            }
        }
        return formatString;
    }

    private String formatDate(Date sourceDate, String formatString)
    {
        // Candidate to move to a super class for other format types
        String formatted = "";
        if (sourceDate != null)
        {
            try
            {
                DateFormat dateFormatter = createDateFormatter(formatString, TimeZone.getTimeZone(TIME_ZONE_UTC));
                formatted = dateFormatter.format(sourceDate);
            }
            catch (Throwable t)
            {
                log.warn("Failed to format a date (" + ((sourceDate == null) ? "null" : sourceDate.toString()) + ") to a formatted string using the format '" + formatString + "': " + t.getMessage(), t);
            }
        }
        return formatted;
    }
}
