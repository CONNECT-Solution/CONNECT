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
package gov.hhs.fha.nhinc.admingui.converter;

import java.util.Calendar;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.DateTimeConverter;
import javax.faces.convert.FacesConverter;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 *
 * @author tjafri
 */
@FacesConverter("xmlGregorianConverter")
public class XMLGregorianConverter extends DateTimeConverter {

    private static final TimeZone DEFAULT_TIME_ZONE = Calendar.getInstance().getTimeZone();
    private String dateStyle = "default";
    private Locale locale = null;
    private String pattern = null;
    private String timeStyle = "default";
    private TimeZone timeZone = DEFAULT_TIME_ZONE;
    private String type = "date";

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String newValue) {
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (null != component && null != component.getAttributes()) {
            Map<String, Object> attributes = component.getAttributes();
            if (attributes.containsKey("pattern")) {
                pattern = (String) attributes.get("pattern");
            }
            setPattern(pattern);
            if (attributes.containsKey("locale")) {
                locale = (Locale) attributes.get("locale");
            }
            setLocale(locale);
            if (attributes.containsKey("timeZone")) {
                timeZone = (TimeZone) attributes.get("timeZone");
            }
            setTimeZone(timeZone);
            if (attributes.containsKey("dateStyle")) {
                dateStyle = (String) attributes.get("dateStyle");
            }
            setDateStyle(dateStyle);
            if (attributes.containsKey("timeStyle")) {
                timeStyle = (String) attributes.get("timeStyle");
            }
            setTimeStyle(timeStyle);
            if (attributes.containsKey("type")) {
                type = (String) attributes.get("type");
            }
            setType(type);
        }
        if (null != value) {
            return super.getAsString(context, component, ((XMLGregorianCalendar) value).toGregorianCalendar().
                getTime());
        }
        return "";
    }
}
