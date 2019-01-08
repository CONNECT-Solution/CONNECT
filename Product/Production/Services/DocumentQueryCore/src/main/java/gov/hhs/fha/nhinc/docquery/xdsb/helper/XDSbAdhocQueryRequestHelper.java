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
package gov.hhs.fha.nhinc.docquery.xdsb.helper;

import java.util.Date;
import java.util.List;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;

/**
 *
 * @author tjafri
 */
public interface XDSbAdhocQueryRequestHelper {

    /**
     * Creates a slot with the given name and a single value. If a slot already exists, its value will be replaced with
     * the passed in value, even in cases where there are multiple existing values. For example, if a slot exists with 3
     * values, and this method is invoked the resulting slot will have a single value.
     *
     * @param slotName the slot name
     * @param value the value
     * @param message the message
     */
    public void createOrReplaceSlotValue(XDSbConstants.RegistryStoredQueryParameter slotName, String value,
        AdhocQueryRequest message);

    /**
     * Creates a slot with the given name and a single value. If a slot already exists, its value will be replaced with
     * the passed in value, even in cases where there are multiple existing values. For example, if a slot exists with 3
     * values, and this method is invoked the resulting slot will have a single value.
     *
     * @param slotName the slot name
     * @param value the value
     * @param message the message
     */
    public void createOrReplaceSlotValue(XDSbConstants.RegistryStoredQueryParameter slotName, List<String> value,
        AdhocQueryRequest message);

    /**
     * Format date in UTC representation with no trailing zeros.
     *
     * @param date the date
     * @return the string
     */
    public String formatXDSbDate(Date date);

    /**
     * Creates the single quote delimited value.
     *
     * @param value the value
     * @return the string
     */
    public String createSingleQuoteDelimitedValue(String value);

    /**
     * Creates the single quote delimited list.
     *
     * @param values the values
     * @return the list
     */
    public String createSingleQuoteDelimitedListValue(List<String> values);

    /**
     * @param documentTypeCode the documentTypeCode
     * @param string
     * @return the list
     */
    public List<String> createCodeSchemeValue(List<String> documentTypeCode, String string);

}
