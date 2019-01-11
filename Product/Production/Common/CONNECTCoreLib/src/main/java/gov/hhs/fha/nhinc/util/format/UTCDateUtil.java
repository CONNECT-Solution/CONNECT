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
package gov.hhs.fha.nhinc.util.format;

import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Date utility for UTC dates.
 *
 * @author Neil Webb
 */
public class UTCDateUtil {

    public static final String DATE_ONLY_FORMAT = "yyyyMMdd";
    public static final String DATE_FORMAT_UTC = "yyyyMMddHHmmss";
    public static final String DATE_FORMAT_FULL = DATE_FORMAT_UTC + "Z";
    private static final String TIME_ZONE_UTC = "UTC";

    private static final Logger LOG = LoggerFactory.getLogger(UTCDateUtil.class);

    /**
     * Parse a date string as a UTC date. The date may optionally have a time zone which will be used if present.
     *
     * @param dateString Date string to parse
     * @return Parsed date
     */
    public Date parseUTCDateOptionalTimeZone(final String dateString) {
        return parseDate(dateString, DATE_FORMAT_FULL, TimeZone.getTimeZone(TIME_ZONE_UTC));
    }

    /**
     * Format a date using a UTC format.
     *
     * @param sourceDate Date to
     * @return
     */
    public String formatUTCDate(final Date sourceDate) {
        return formatDate(sourceDate, DATE_FORMAT_UTC);
    }

    /**
     * Format a date using a UTC Date Only format.
     *
     * @param sourceDate Date to
     * @return
     */
    public String formatUTCDateOnly(final Date sourceDate) {
        return formatDate(sourceDate, DATE_ONLY_FORMAT);
    }

    /**
     * Parses a string and returns a Date object having the given date format.
     *
     * @param dateString String to be parsed containing a date
     * @param dateFormat Format of the date to be parsed
     * @param timeZone
     * @return Returns the Date object for the given string and format.
     */
    public Date parseDate(final String dateString, final String dateFormat, final TimeZone timeZone) {
        // Candidate to move to a super class for other format types
        if (LOG.isDebugEnabled()) {
            LOG.debug("Parsing (" + dateString + ") using format string (" + dateFormat + ") and time zone ("
                    + (timeZone == null ? "none" : timeZone.getDisplayName()) + ").");
        }
        Date parsed = null;
        if (StringUtils.isNotEmpty(dateString) && StringUtils.isNotEmpty(dateFormat)) {
            try {
                final String formatString = prepareDateFormatString(dateFormat, dateString);
                final DateFormat formatter = createDateFormatter(formatString, timeZone);
                parsed = formatter.parse(dateString);
                if (parsed != null) {
                    LOG.debug("Date parsed successfully");
                }
            } catch (final ParseException pe) {
                LOG.warn("Error parsing '" + dateString + "' using format: '" + dateFormat + "'", pe);
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
    private DateFormat createDateFormatter(final String formatString, final TimeZone timeZone)
            throws IllegalArgumentException {
        // Candidate to move to a super class for other format types
        if (NullChecker.isNullish(formatString)) {
            throw new IllegalArgumentException("Date format string is required to create a date formatter");
        }
        final DateFormat formatter = new SimpleDateFormat(formatString);
        if (timeZone != null) {
            formatter.setTimeZone(timeZone);
        }
        return formatter;
    }

    /**
     * Prepare a date format string based on the length of the date string to be parsed. The date Format will be reduced
     * to meet the length of the date provided to match the accuracy of the date string.
     *
     * @param dateFormat Date format string (ex. yyyyMMddhhmmssZ)
     * @param dateString Date string to be parsed (ex. 19990205)
     * @return Modified format string based on the date string length (ex. yyyyMMdd)
     */
    private String prepareDateFormatString(final String dateFormat, final String dateString) {
        // Candidate to move to a super class for other format types
        String formatString = dateFormat;
        if (StringUtils.isNotEmpty(dateString) && StringUtils.isNotEmpty(dateFormat) && dateString.length() > 0
                && dateString.length() < dateFormat.length()) {
            formatString = dateFormat.substring(0, dateString.length());
            if (LOG.isDebugEnabled()) {
                LOG.debug("New dateFormat: " + dateFormat);
            }
        }

        return formatString;
    }

    private String formatDate(final Date sourceDate, final String formatString) {
        // Candidate to move to a super class for other format types
        String formatted = "";
        if (sourceDate != null) {
            try {
                final DateFormat dateFormatter = createDateFormatter(formatString, TimeZone.getTimeZone(TIME_ZONE_UTC));
                formatted = dateFormatter.format(sourceDate);
            } catch (final IllegalArgumentException iae) {
                LOG.warn("Failed to format a date (" + sourceDate.toString()
                        + ") to a formatted string using the format '" + formatString + "': " + iae.getMessage(), iae);
            }
        }
        return formatted;
    }
}
