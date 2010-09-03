/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Class to perform transform operations for document query messages.
 *
 * @author Neil Webb
 */
public class DocumentQueryTransform 
{
    private static Log log = LogFactory.getLog(DocumentQueryTransform.class);

    /**
     * Replace the patient identifier information in an AdhocQuery message with the information
     * provided.
     * 
     * @param sourceQuery Original AdhocQuery message
     * @param homeCommuinty Home community identifier
     * @param assigningAuthority Assigning authority
     * @param patientId Patient identifier
     * @return Altered AdhocQuery Message
     */
    public AdhocQueryRequest replaceAdhocQueryPatientId(AdhocQueryRequest sourceQuery, String homeCommuinty, String assigningAuthority, String patientId)
    {
        log.debug("DocumentQueryTransform.replaceAdhocQueryPatientId() -- Begin");
        AdhocQueryRequest adhocQueryRequest = null;
        
        if (sourceQuery != null)
        {
            adhocQueryRequest = sourceQuery;

            // Home community ID
            //-------------------
            if (NullChecker.isNotNullish(homeCommuinty))
            {
                if (adhocQueryRequest.getAdhocQuery() == null)
                {
                    adhocQueryRequest.setAdhocQuery(new AdhocQueryType());
                }
                adhocQueryRequest.getAdhocQuery().setHome(homeCommuinty);
            }

            // Patient ID
            //-------------
            if (NullChecker.isNotNullish(patientId) && NullChecker.isNotNullish(assigningAuthority))
            {
                if (adhocQueryRequest.getAdhocQuery() == null)
                {
                    adhocQueryRequest.setAdhocQuery(new AdhocQueryType());
                }
                
                String formattedPatientId = PatientIdFormatUtil.hl7EncodePatientId(patientId, assigningAuthority);

                // Look for the entries in the slot that contain the patient ID and fix them.  If none were found, create one.
                //-------------------------------------------------------------------------------------------------------------
                boolean foundEntry = false;
                List<SlotType1> slotType1 = adhocQueryRequest.getAdhocQuery().getSlot();
                Iterator<SlotType1> iterSlotType1 = slotType1.iterator();
                while (iterSlotType1.hasNext())
                {
                    SlotType1 slot = iterSlotType1.next();
                    if ((slot.getName() != null) &&
                        (slot.getName().equals(DocumentTransformConstants.EBXML_DOCENTRY_PATIENT_ID)))
                    {
                        ValueListType slotValueList = new ValueListType();
                        slot.setValueList(slotValueList);
                        List<String> slotValues = null;                       // Handle to a list of strings
                        slotValues = slotValueList.getValue();
                        slotValues.add(formattedPatientId);
                        foundEntry = true;
                    }
                }

                // If we did not replace an entry - then we need to create one...
                //-----------------------------------------------------------------
                if (!foundEntry)
                {
                    SlotType1 slot = new SlotType1();
                    slot.setName(DocumentTransformConstants.EBXML_DOCENTRY_PATIENT_ID);
                    ValueListType slotValueList = new ValueListType();
                    slot.setValueList(slotValueList);
                    List<String> slotValues = null;                       // Handle to a list of strings
                    slotValues = slotValueList.getValue();
                    slotValues.add(formattedPatientId);
                    slotType1.add(slot);
                }   // if (!bFoundEntry)
            }   // if ((oInsertDocQueryPatIds.getQualifiedSubjectId() != null) &&
        }   // if ((oInsertDocQueryPatIds != null) &&

        if(log.isDebugEnabled())
        {
            log.debug("The result as it should be: ");
            outputAdhocQueryRequest(adhocQueryRequest);
            log.debug("----------------------------");
        }

        log.debug("DocumentQueryTransform.replaceAdhocQueryPatientId() -- End");
        return adhocQueryRequest;
    }
    
    /**
     * Output the contents of the adhoc query request.
     * 
     * @param oAdhocQueryRequest  The object to be printed out.
     */
    public static void outputAdhocQueryRequest(AdhocQueryRequest oAdhocQueryRequest)
    {
        log.debug("DocumentQueryTransform.outputAdhocQueryRequest() -- Begin");
        try
        {
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext jc = oHandler.getJAXBContext("oasis.names.tc.ebxml_regrep.xsd.query._3");
            Marshaller marshaller = jc.createMarshaller();
            StringWriter oXML = new StringWriter();
            marshaller.marshal(oAdhocQueryRequest, oXML);
            log.debug("Done marshalling the message.");

            log.debug("");
            log.debug(oXML);
            log.debug("");
        }
        catch (Exception e)
        {
            log.error("Unexpected exception: " + e.getMessage());
            e.printStackTrace();
        }
        log.debug("DocumentQueryTransform.outputAdhocQueryRequest() -- End");
    }
    
}
