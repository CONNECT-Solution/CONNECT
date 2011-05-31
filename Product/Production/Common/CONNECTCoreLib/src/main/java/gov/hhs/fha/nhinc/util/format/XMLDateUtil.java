/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package gov.hhs.fha.nhinc.util.format;

import java.util.Date;
import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author richard.ettema
 */
public class XMLDateUtil {

    private static Log log = LogFactory.getLog(XMLDateUtil.class);

    /**
     * Transform a date in a long to a XMLGregorianCalendar
     *
     * @param date long value
     * @return <code>XMLGregorianCalendar</code>
     */
    public static XMLGregorianCalendar long2Gregorian(long date) {
        XMLGregorianCalendar xmlCal = null;

        DatatypeFactory dataTypeFactory;
        try {
            dataTypeFactory = DatatypeFactory.newInstance();

            GregorianCalendar gc = new GregorianCalendar();
            gc.setTimeInMillis(date);
            xmlCal = dataTypeFactory.newXMLGregorianCalendar(gc);
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            log.error(e);
        }

        return xmlCal;
    }

    /**
     * Transform a date in Date to XMLGregorianCalendar
     *
     * @param date value
     * @return <code>XMLGregorianCalendar</code>
     */
    public static XMLGregorianCalendar date2Gregorian(Date date) {
        XMLGregorianCalendar xmlCal = null;

        if (date != null) {
            xmlCal = long2Gregorian(date.getTime());
        }

        return xmlCal;
    }

    /**
     * Transform an XMLGregorianCalendar value to a Date value
     * 
     * @param xmlCal
     * @return <code>Date</code>
     */
    public static Date gregorian2date(XMLGregorianCalendar xmlCal) {
        Date date = null;

        if (xmlCal != null) {
            GregorianCalendar c = xmlCal.toGregorianCalendar();
            date = c.getTime();
        }

        return date;
    }

}
