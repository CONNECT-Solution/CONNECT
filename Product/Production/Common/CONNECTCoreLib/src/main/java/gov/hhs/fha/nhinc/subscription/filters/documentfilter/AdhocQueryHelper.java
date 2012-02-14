/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
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
package gov.hhs.fha.nhinc.subscription.filters.documentfilter;

//import gov.hhs.fha.nhinc.NHINCLib.NullChecker;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.*;
import org.oasis_open.docs.wsn.b_2.Subscribe;
import org.w3c.dom.Element;

/**
 * 
 * @author rayj
 */
public class AdhocQueryHelper {

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory
            .getLog(AdhocQueryHelper.class);

    public static AdhocQueryType getAdhocQuery(Subscribe nhinSubscribe) {
        AdhocQueryType adhocQuery = null;
        log.info("begin getAdhocQuery");
        List<Object> any = nhinSubscribe.getAny();
        log.info("found " + any.size() + " any item(s)");

        for (Object anyItem : any) {
            log.info("anyItem=" + anyItem);
            if (anyItem instanceof oasis.names.tc.ebxml_regrep.xsd.rim._3.AdhocQueryType) {
                adhocQuery = (AdhocQueryType) anyItem;
            }
            if (anyItem instanceof Element) {
                Element element = (Element) anyItem;
                log.info("element.getNodeName()=" + element.getNodeName());

                Object o = (JAXBElement<oasis.names.tc.ebxml_regrep.xsd.rim._3.AdhocQueryType>) nhinSubscribe.getAny();

                // Object o = (JAXBElement<oasis.names.tc.ebxml_regrep.xsd.rim._3.AdhocQueryType>) anyItem;
            }
            if (anyItem instanceof JAXBElement) {
                log.info("jaxbelement.getValue=" + ((JAXBElement) anyItem).getValue());
                if (((JAXBElement) anyItem).getValue() instanceof AdhocQueryType) {
                    adhocQuery = (AdhocQueryType) ((JAXBElement) anyItem).getValue();
                } else {
                    log.warn("unhandled anyitem jaxbelement value " + ((JAXBElement) anyItem).getValue());
                }
            } else {
                log.warn("unhandled anyitem " + anyItem);
            }
        }
        log.info("end getAdhocQuery");
        return adhocQuery;
    }

    public static List<SlotType1> getAllSlots(Subscribe nhinSubscribe) {
        AdhocQueryType adhocQuery = getAdhocQuery(nhinSubscribe);
        if ((adhocQuery == null) || (adhocQuery.getSlot() == null)) {
            return new ArrayList<SlotType1>();
        }
        return adhocQuery.getSlot();
    }

    public static List<String> findSlotValues(Subscribe nhinSubscribe, String slotName) {
        log.info("begin findSlotValue");
        List<SlotType1> allSlots = getAllSlots(nhinSubscribe);
        List<String> matchingSlotValues = findSlotValues(allSlots, slotName);
        log.info("total slotValues found " + matchingSlotValues.size());
        log.info("end findSlotValue");
        return matchingSlotValues;
    }

    public static List<String> findSlotValues(List<SlotType1> slots, String slotName) {
        log.info("begin findSlotValue");
        List<String> matchingSlotValues = new ArrayList<String>();

        for (SlotType1 slot : slots) {
            if (slot.getName().contentEquals(slotName)) {
                if (slot.getValueList() != null) {
                    List<String> slotValues = slot.getValueList().getValue();
                    for (String slotValue : slotValues) {
                        if (NullChecker.isNotNullish(slotValue)) {
                            log.info("adding slotValue " + slotValue);
                            matchingSlotValues.add(slotValue);
                        }
                    }
                }
            }
        }

        log.info("total slotValues found " + matchingSlotValues.size());
        log.info("end findSlotValue");
        return matchingSlotValues;
    }

    public static String findSlotValue(Subscribe nhinSubscribe, String slotName)
            throws MultipleSlotValuesFoundException {
        List<String> slotValues = findSlotValues(nhinSubscribe, slotName);
        return findSingleSlotValue(slotValues);
    }

    public static String findSlotValue(List<SlotType1> slots, String slotName) throws MultipleSlotValuesFoundException {
        List<String> slotValues = findSlotValues(slots, slotName);
        return findSingleSlotValue(slotValues);
    }

    private static String findSingleSlotValue(List<String> slotValues) throws MultipleSlotValuesFoundException {
        String slotValue = null;
        if ((slotValues == null) || (slotValues.size() == 0)) {
            slotValue = null;
        } else if (slotValues.size() > 1) {
            throw new MultipleSlotValuesFoundException();
        } else {
            slotValue = slotValues.get(0);
        }
        return slotValue;
    }

}
