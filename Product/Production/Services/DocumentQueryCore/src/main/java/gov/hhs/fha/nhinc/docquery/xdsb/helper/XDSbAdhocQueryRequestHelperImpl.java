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

import gov.hhs.fha.nhinc.docquery.xdsb.helper.XDSbConstants.RegistryStoredQueryParameter;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ValueListType;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author tjafri
 */
public class XDSbAdhocQueryRequestHelperImpl implements XDSbAdhocQueryRequestHelper {

    /*
     * (non-Javadoc)
     *
     * RegistryStoredQueryParameter, java.lang.String, oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest)
     */
    @Override
    public void createOrReplaceSlotValue(RegistryStoredQueryParameter slotName, String value, AdhocQueryRequest message) {

        if (message != null && message.getAdhocQuery() != null && message.getAdhocQuery().getSlot() != null) {
            for (SlotType1 slot : message.getAdhocQuery().getSlot()) {
                if (slot != null) {
                    if (StringUtils.equalsIgnoreCase(slot.getName(), slotName.toString())) {
                        message.getAdhocQuery().getSlot().remove(slot);
                        break;
                    }
                }
            }
            message.getAdhocQuery().getSlot().add(createSlot(slotName, value));
        }
    }

    /**
     * Creates a slot.
     *
     * @param slotName the slot name
     * @param value the value
     * @return the slot type1
     */
    protected SlotType1 createSlot(RegistryStoredQueryParameter slotName, String value) {
        SlotType1 slot = new SlotType1();
        slot.setName(slotName.toString());
        slot.setValueList(new ValueListType());
        slot.getValueList().getValue().add(value);
        return slot;
    }

    /**
     * Creates a slot.
     *
     * @param slotName the slot name
     * @param values
     * @return the slot type1
     */
    protected SlotType1 createSlot(RegistryStoredQueryParameter slotName, List<String> values) {
        SlotType1 slot = new SlotType1();
        slot.setName(slotName.toString());
        slot.setValueList(new ValueListType());
        slot.getValueList().getValue().addAll(values);
        return slot;
    }

    @Override
    public String formatXDSbDate(Date date) {
        String sFormattedDate = null;
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(NhincConstants.DATE_PARSE_FORMAT);
            sFormattedDate = sdf.format(date);
            sFormattedDate = StringUtils.stripEnd(sFormattedDate, "0000000000");
            sFormattedDate = StringUtils.stripEnd(sFormattedDate, "00000000");
            sFormattedDate = StringUtils.stripEnd(sFormattedDate, "000000");
            sFormattedDate = StringUtils.stripEnd(sFormattedDate, "0000");
            sFormattedDate = StringUtils.stripEnd(sFormattedDate, "00");
        }
        return sFormattedDate;
    }

    @Override
    public String createSingleQuoteDelimitedValue(String value) {
        if (!StringUtils.startsWith(value, "'")) {
            value = "'".concat(value);
        }
        if (!StringUtils.endsWith(value, "'")) {
            value = value.concat("'");
        }
        return value;
    }

    @Override
    public String createSingleQuoteDelimitedListValue(List<String> values) {
        StringBuilder builder = new StringBuilder();
        builder.append("(");
        for (String s : values) {
            builder.append(createSingleQuoteDelimitedValue(s));
        }
        builder.append(")");
        return builder.toString();
    }

    @Override
    public List<String> createCodeSchemeValue(List<String> documentTypeCode, String schema) {
        List<String> docType = new ArrayList<>();
        for (String docTypeCode : documentTypeCode) {
            StringBuilder builder = new StringBuilder();
            builder.append("('");
            builder.append(docTypeCode);
            builder.append("^^");
            builder.append(schema);
            builder.append("')");
            docType.add(builder.toString());
        }
        return docType;
    }

    @Override
    public void createOrReplaceSlotValue(RegistryStoredQueryParameter slotName, List<String> value,
        AdhocQueryRequest message) {

        if (message != null && message.getAdhocQuery() != null && message.getAdhocQuery().getSlot() != null) {
            for (SlotType1 slot : message.getAdhocQuery().getSlot()) {
                if (slot != null) {
                    if (StringUtils.equalsIgnoreCase(slot.getName(), slotName.toString())) {
                        message.getAdhocQuery().getSlot().remove(slot);
                        break;
                    }
                }
            }
            message.getAdhocQuery().getSlot().add(createSlot(slotName, value));
        }
    }
}
