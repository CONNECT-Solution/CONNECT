/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.policyengine.adapter.pip;

import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;
import org.hl7.v3.POCDMT000040ClinicalDocument;
import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * This class is used to serialize/deserialize teh Patient Preferences documents.
 *
 * @author Les Westberg
 */
public class CdaPdfSerializer
{
    private Log log = null;

    /**
     * Default constructor.
     */
    public CdaPdfSerializer()
    {
        log = createLogger();
    }

    /**
     * Sets up the logger object.
     */
    protected Log createLogger()
    {
        return ((log != null) ? log : LogFactory.getLog(getClass()));

    }

    /**
     * This method takes in an object representation of the HL7 Clinical Document
     * and serializes it to a text string representation of the document.
     *
     * @param oCda The object representation of the clinical document.
     * @return The textual string representation of the HL7 clinical document.
     * @throws gov.hhs.fha.nhinc.policyengine.adapterpip.AdapterPIPException
     *         This exception is thrown if an error occurs.
     */
    public String serialize(POCDMT000040ClinicalDocument oCda)
        throws AdapterPIPException
    {
        String sCda = "";

        try
        {
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext oJaxbContext = oHandler.getJAXBContext("org.hl7.v3");
            Marshaller oMarshaller = oJaxbContext.createMarshaller();

            StringWriter swXML = new StringWriter();

            org.hl7.v3.ObjectFactory oObjectFactory = new org.hl7.v3.ObjectFactory();
            JAXBElement oJaxbElement = oObjectFactory.createClinicalDocument(oCda);

            oMarshaller.marshal(oJaxbElement, swXML);
            sCda = swXML.toString();
        }
        catch (Exception e)
        {
            String sErrorMessage = "Failed to serialize the Clinical Document (CDA) to a string.  Error: " +
                                   e.getMessage();
            log.error(sErrorMessage, e);
            throw new AdapterPIPException(sErrorMessage, e);
        }

        return sCda;
    }

    /**
     * This method takes a string version of the HL7 clinical document and
     * creates the JAXB object version of the same document.
     *
     * @param sCda The string version of the clinical document.
     * @return The JAXB object version of the clinical document.
     * @throws gov.hhs.fha.nhinc.policyengine.adapterpip.AdapterPIPException
     *         This is thrown if there is an error deserializing the string.
     */
    public POCDMT000040ClinicalDocument deserialize(String sCda)
        throws AdapterPIPException
    {
        POCDMT000040ClinicalDocument oCda = null;

        try
        {
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext oJaxbContext = oHandler.getJAXBContext("org.hl7.v3");
            Unmarshaller oUnmarshaller = oJaxbContext.createUnmarshaller();

            StringReader srXML = new StringReader(sCda);

            JAXBElement oJAXBElementConsent = (JAXBElement) oUnmarshaller.unmarshal(srXML);
            if (oJAXBElementConsent.getValue() instanceof POCDMT000040ClinicalDocument)
            {
                oCda = (POCDMT000040ClinicalDocument) oJAXBElementConsent.getValue();
            }
        }
        catch (Exception e)
        {
            String sErrorMessage = "Failed to deserialize the clinical document string: " + sCda + "  Error: " +
                                   e.getMessage();
            log.error(sErrorMessage, e);
            throw new AdapterPIPException(sErrorMessage, e);
        }

        return oCda;
    }


}
