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
package gov.hhs.fha.nhinc.policyengine.adapter.pip;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.PatientPreferencesType;
import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;
import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

/**
 * This class is used to serialize/deserialize teh Patient Preferences documents.
 * 
 * @author Les Westberg
 */
public class PatientPreferencesSerializer {
    private static final Logger LOG = Logger.getLogger(PatientPreferencesSerializer.class);

    /**
     * This method takes in an object representation of the Patient Preferences and serializes it to a text string
     * representation of the document.
     * 
     * @param oPtPref The object representation of the Patient Preferences.
     * @return The textual string representation of the Patient preferences document.
     * @throws gov.hhs.fha.nhinc.policyengine.adapterpip.AdapterPIPException This exception is thrown if an error
     *             occurs.
     */
    public String serialize(PatientPreferencesType oPtPref) throws AdapterPIPException {
        String sPtPref = "";

        try {
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext oJaxbContext = oHandler.getJAXBContext("gov.hhs.fha.nhinc.common.nhinccommonadapter");
            Marshaller oMarshaller = oJaxbContext.createMarshaller();

            StringWriter swXML = new StringWriter();

            gov.hhs.fha.nhinc.common.nhinccommonadapter.ObjectFactory oObjectFactory = new gov.hhs.fha.nhinc.common.nhinccommonadapter.ObjectFactory();
            JAXBElement oJaxbElement = oObjectFactory.createPatientPreferences(oPtPref);

            oMarshaller.marshal(oJaxbElement, swXML);
            sPtPref = swXML.toString();
        } catch (Exception e) {
            String sErrorMessage = "Failed to serialize the Patient Preferences document to a string.  Error: "
                    + e.getMessage();
            LOG.error(sErrorMessage, e);
            throw new AdapterPIPException(sErrorMessage, e);
        }

        return sPtPref;
    }

    /**
     * This method takes a string version of the Patient Pref document and creates the JAXB object version of the same
     * document.
     * 
     * @param sPtPref The string version of the patient preference document.
     * @return The JAXB object version of the patient preferences document.
     * @throws gov.hhs.fha.nhinc.policyengine.adapterpip.AdapterPIPException This is thrown if there is an error
     *             deserializing the string.
     */
    public PatientPreferencesType deserialize(String sPtPref) throws AdapterPIPException {
        PatientPreferencesType oPtPref = null;

        try {
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext oJaxbContext = oHandler.getJAXBContext("gov.hhs.fha.nhinc.common.nhinccommonadapter");
            Unmarshaller oUnmarshaller = oJaxbContext.createUnmarshaller();

            StringReader srXML = new StringReader(sPtPref);

            JAXBElement oJAXBElementConsent = (JAXBElement) oUnmarshaller.unmarshal(srXML);
            if (oJAXBElementConsent.getValue() instanceof PatientPreferencesType) {
                oPtPref = (PatientPreferencesType) oJAXBElementConsent.getValue();
            }
        } catch (Exception e) {
            String sErrorMessage = "Failed to deserialize the patient preferences string: " + sPtPref + "  Error: "
                    + e.getMessage();
            LOG.error(sErrorMessage, e);
            throw new AdapterPIPException(sErrorMessage, e);
        }

        return oPtPref;
    }

}
