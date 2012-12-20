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
package gov.hhs.fha.nhinc.transform.document;

import java.util.List;
import java.util.Iterator;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;
import gov.hhs.fha.nhinc.util.format.PatientIdFormatUtil;

import java.io.StringWriter;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.AdhocQueryType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ValueListType;

import org.apache.log4j.Logger;

/**
 * Class to perform transform operations for document query messages.
 * 
 * @author Neil Webb
 */
public class DocumentQueryTransform {
    private static final Logger LOG = Logger.getLogger(DocumentQueryTransform.class);

    /**
     * Replace the patient identifier information in an AdhocQuery message with the information provided.
     * 
     * @param sourceQuery Original AdhocQuery message
     * @param homeCommuinty Home community identifier
     * @param assigningAuthority Assigning authority
     * @param patientId Patient identifier
     * @return Altered AdhocQuery Message
     */
    public AdhocQueryRequest replaceAdhocQueryPatientId(AdhocQueryRequest sourceQuery, String homeCommuinty,
            String assigningAuthority, String patientId) {
        LOG.debug("DocumentQueryTransform.replaceAdhocQueryPatientId() -- Begin");
        AdhocQueryRequest adhocQueryRequest = null;

        if (sourceQuery != null) {
            adhocQueryRequest = sourceQuery;

            // Home community ID
            // -------------------
            if (NullChecker.isNotNullish(homeCommuinty)) {
                if (adhocQueryRequest.getAdhocQuery() == null) {
                    adhocQueryRequest.setAdhocQuery(new AdhocQueryType());
                }
                adhocQueryRequest.getAdhocQuery().setHome(homeCommuinty);
            }

            // Patient ID
            // -------------
            if (NullChecker.isNotNullish(patientId) && NullChecker.isNotNullish(assigningAuthority)) {
                if (adhocQueryRequest.getAdhocQuery() == null) {
                    adhocQueryRequest.setAdhocQuery(new AdhocQueryType());
                }

                String formattedPatientId = PatientIdFormatUtil.hl7EncodePatientId(patientId, assigningAuthority);

                // Look for the entries in the slot that contain the patient ID and fix them. If none were found, create
                // one.
                // -------------------------------------------------------------------------------------------------------------
                boolean foundEntry = false;
                List<SlotType1> slotType1 = adhocQueryRequest.getAdhocQuery().getSlot();
                Iterator<SlotType1> iterSlotType1 = slotType1.iterator();
                while (iterSlotType1.hasNext()) {
                    SlotType1 slot = iterSlotType1.next();
                    if ((slot.getName() != null)
                            && (slot.getName().equals(DocumentTransformConstants.EBXML_DOCENTRY_PATIENT_ID))) {
                        ValueListType slotValueList = new ValueListType();
                        slot.setValueList(slotValueList);
                        List<String> slotValues = null; // Handle to a list of strings
                        slotValues = slotValueList.getValue();
                        slotValues.add(formattedPatientId);
                        foundEntry = true;
                    }
                }

                // If we did not replace an entry - then we need to create one...
                // -----------------------------------------------------------------
                if (!foundEntry) {
                    SlotType1 slot = new SlotType1();
                    slot.setName(DocumentTransformConstants.EBXML_DOCENTRY_PATIENT_ID);
                    ValueListType slotValueList = new ValueListType();
                    slot.setValueList(slotValueList);
                    List<String> slotValues = null; // Handle to a list of strings
                    slotValues = slotValueList.getValue();
                    slotValues.add(formattedPatientId);
                    slotType1.add(slot);
                } // if (!bFoundEntry)
            } // if ((oInsertDocQueryPatIds.getQualifiedSubjectId() != null) &&
        } // if ((oInsertDocQueryPatIds != null) &&

        if (LOG.isDebugEnabled()) {
            LOG.debug("The result as it should be: ");
            outputAdhocQueryRequest(adhocQueryRequest);
            LOG.debug("----------------------------");
        }

        LOG.debug("DocumentQueryTransform.replaceAdhocQueryPatientId() -- End");
        return adhocQueryRequest;
    }

    /**
     * Output the contents of the adhoc query request.
     * 
     * @param oAdhocQueryRequest The object to be printed out.
     */
    public static void outputAdhocQueryRequest(AdhocQueryRequest oAdhocQueryRequest) {
        LOG.debug("DocumentQueryTransform.outputAdhocQueryRequest() -- Begin");
        try {
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext jc = oHandler.getJAXBContext("oasis.names.tc.ebxml_regrep.xsd.query._3");
            Marshaller marshaller = jc.createMarshaller();
            StringWriter oXML = new StringWriter();
            marshaller.marshal(oAdhocQueryRequest, oXML);
            LOG.debug("Done marshalling the message.");

            LOG.debug("");
            LOG.debug(oXML);
            LOG.debug("");
        } catch (Exception e) {
            LOG.error("Unexpected exception: " + e.getMessage());
            e.printStackTrace();
        }
        LOG.debug("DocumentQueryTransform.outputAdhocQueryRequest() -- End");
    }

}
