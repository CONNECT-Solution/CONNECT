/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.policyengine.adapter.pip;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.PatientPreferencesType;
import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;
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
public class PatientPreferencesSerializer
{
    private Log log = null;

    /**
     * Default constructor.
     */
    public PatientPreferencesSerializer()
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
     * This method takes in an object representation of the Patient Preferences
     * and serializes it to a text string representation of the document.
     *
     * @param oPtPref The object representation of the Patient Preferences.
     * @return The textual string representation of the Patient preferences document.
     * @throws gov.hhs.fha.nhinc.policyengine.adapterpip.AdapterPIPException
     *         This exception is thrown if an error occurs.
     */
    public String serialize(PatientPreferencesType oPtPref)
        throws AdapterPIPException
    {
        String sPtPref = "";

        try
        {
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext oJaxbContext = oHandler.getJAXBContext("gov.hhs.fha.nhinc.common.nhinccommonadapter");
            Marshaller oMarshaller = oJaxbContext.createMarshaller();

            StringWriter swXML = new StringWriter();

            gov.hhs.fha.nhinc.common.nhinccommonadapter.ObjectFactory oObjectFactory = new gov.hhs.fha.nhinc.common.nhinccommonadapter.ObjectFactory();
            JAXBElement oJaxbElement = oObjectFactory.createPatientPreferences(oPtPref);

            oMarshaller.marshal(oJaxbElement, swXML);
            sPtPref = swXML.toString();
        }
        catch (Exception e)
        {
            String sErrorMessage = "Failed to serialize the Patient Preferences document to a string.  Error: " +
                                   e.getMessage();
            log.error(sErrorMessage, e);
            throw new AdapterPIPException(sErrorMessage, e);
        }

        return sPtPref;
    }

    /**
     * This method takes a string version of the Patient Pref document and
     * creates the JAXB object version of the same document.
     *
     * @param sPtPref The string version of the patient preference document.
     * @return The JAXB object version of the patient preferences document.
     * @throws gov.hhs.fha.nhinc.policyengine.adapterpip.AdapterPIPException
     *         This is thrown if there is an error deserializing the string.
     */
    public PatientPreferencesType deserialize(String sPtPref)
        throws AdapterPIPException
    {
        PatientPreferencesType oPtPref = null;

        try
        {
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext oJaxbContext = oHandler.getJAXBContext("gov.hhs.fha.nhinc.common.nhinccommonadapter");
            Unmarshaller oUnmarshaller = oJaxbContext.createUnmarshaller();

            StringReader srXML = new StringReader(sPtPref);

            JAXBElement oJAXBElementConsent = (JAXBElement) oUnmarshaller.unmarshal(srXML);
            if (oJAXBElementConsent.getValue() instanceof PatientPreferencesType)
            {
                oPtPref = (PatientPreferencesType) oJAXBElementConsent.getValue();
            }
        }
        catch (Exception e)
        {
            String sErrorMessage = "Failed to deserialize the patient preferences string: " + sPtPref + "  Error: " +
                                   e.getMessage();
            log.error(sErrorMessage, e);
            throw new AdapterPIPException(sErrorMessage, e);
        }

        return oPtPref;
    }


}
