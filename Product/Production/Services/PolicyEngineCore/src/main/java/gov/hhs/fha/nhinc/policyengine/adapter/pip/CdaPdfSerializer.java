/*
 * Copyright (c) 2009-2015, United States Government, as represented by the Secretary of Health and Human Services.
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

import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;
import gov.hhs.fha.nhinc.util.JAXBUnmarshallingUtil;
import gov.hhs.fha.nhinc.util.StreamUtils;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLStreamException;
import org.hl7.v3.POCDMT000040ClinicalDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is used to serialize/deserialize the Patient Preferences documents.
 *
 * @author Les Westberg
 */
public class CdaPdfSerializer {

    private static final Logger LOG = LoggerFactory.getLogger(CdaPdfSerializer.class);

    /**
     * This method takes in an object representation of the HL7 Clinical Document and serializes it to a text string
     * representation of the document.
     *
     * @param oCda The object representation of the clinical document.
     * @return The textual string representation of the HL7 clinical document.
     * @throws gov.hhs.fha.nhinc.policyengine.adapter.pip.AdapterPIPException This exception is thrown if an error
     * occurs.
     */
    public String serialize(POCDMT000040ClinicalDocument oCda) throws AdapterPIPException {
        String sCda;

        try {
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext oJaxbContext = oHandler.getJAXBContext("org.hl7.v3");
            Marshaller oMarshaller = oJaxbContext.createMarshaller();

            StringWriter swXML = new StringWriter();

            org.hl7.v3.ObjectFactory oObjectFactory = new org.hl7.v3.ObjectFactory();
            JAXBElement oJaxbElement = oObjectFactory.createClinicalDocument(oCda);

            oMarshaller.marshal(oJaxbElement, swXML);
            sCda = swXML.toString();
        } catch (Exception e) {
            String sErrorMessage = "Failed to serialize the Clinical Document (CDA) to a string.  Error: "
                + e.getMessage();
            LOG.error(sErrorMessage, e);
            throw new AdapterPIPException(sErrorMessage, e);
        }

        return sCda;
    }

    /**
     * This method takes a string version of the HL7 clinical document and creates the JAXB object version of the same
     * document.
     *
     * @param sCda The string version of the clinical document.
     * @return The JAXB object version of the clinical document.
     * @throws gov.hhs.fha.nhinc.policyengine.adapter.pip.AdapterPIPException This is thrown if there is an error
     * deserializing the string
     */
    public POCDMT000040ClinicalDocument deserialize(String sCda) throws AdapterPIPException {
        POCDMT000040ClinicalDocument oCda = null;
        ByteArrayInputStream baInStrm = null;

        try {
            JAXBUnmarshallingUtil util = new JAXBUnmarshallingUtil();
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext oJaxbContext = oHandler.getJAXBContext("org.hl7.v3");
            Unmarshaller oUnmarshaller = oJaxbContext.createUnmarshaller();

            baInStrm = new ByteArrayInputStream(sCda.getBytes());

            JAXBElement oJAXBElementConsent
                = (JAXBElement) oUnmarshaller.unmarshal(util.getSafeStreamReaderFromInputStream(baInStrm));
            if (oJAXBElementConsent.getValue() instanceof POCDMT000040ClinicalDocument) {
                oCda = (POCDMT000040ClinicalDocument) oJAXBElementConsent.getValue();
            }
        } catch (JAXBException e) {
            LOG.error("Failed to deserialize the clinical document to a string. Error: " + e.getMessage(), e);
            throw new AdapterPIPException(e.getMessage(), e);
        } catch (XMLStreamException e) {
            LOG.error("Failed to deserialize the clinical document to a string: Error: " + e.getMessage(), e);
            throw new AdapterPIPException(e.getMessage(), e);
        } finally {
            StreamUtils.closeStreamSilently(baInStrm);
        }

        return oCda;
    }

}
