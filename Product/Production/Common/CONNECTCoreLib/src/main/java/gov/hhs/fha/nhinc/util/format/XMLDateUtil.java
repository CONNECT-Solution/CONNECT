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

import java.util.Date;
import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author richard.ettema
 */
public class XMLDateUtil {

    private static final Logger LOG = LoggerFactory.getLogger(XMLDateUtil.class);

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
            LOG.error("Could not convert to Gregorian date: " + e.getLocalizedMessage(), e);
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
