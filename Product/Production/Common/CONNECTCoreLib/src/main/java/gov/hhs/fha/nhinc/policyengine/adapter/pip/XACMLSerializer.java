/*
 * Copyright (c) 2009-2016, United States Government, as represented by the Secretary of Health and Human Services.
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
import java.io.InputStream;
import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import oasis.names.tc.xacml._2_0.policy.schema.os.PolicyType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is used to serialize/deserialize teh XACML documents.
 *
 * @author Les Westberg
 */
public class XACMLSerializer {
    private static final Logger LOG = LoggerFactory.getLogger(XACMLSerializer.class);

    /**
     * This method takes in an object representation of the XACML Policy and serializes it to a text string
     * representation of the document.
     *
     * @param oConsentXACML The object representation of the XACML Consent Policy.
     * @return The textual string representation of the XACML Consent document.
     * @throws gov.hhs.fha.nhinc.policyengine.adapterpip.AdapterPIPException This exception is thrown if an error
     *             occurs.
     */
    public String serializeConsentXACMLDoc(PolicyType oConsentXACML) throws AdapterPIPException {
        String sConsentXACML;

        try {
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext oJaxbContext = oHandler.getJAXBContext("oasis.names.tc.xacml._2_0.policy.schema.os");
            Marshaller oMarshaller = oJaxbContext.createMarshaller();
            // System.out.println("###### Marshaller class: " + oMarshaller.getClass().getName());
            StringWriter swXML = new StringWriter();

            oasis.names.tc.xacml._2_0.policy.schema.os.ObjectFactory oXACMLObjectFactory = new oasis.names.tc.xacml._2_0.policy.schema.os.ObjectFactory();
            JAXBElement oJaxbElement = oXACMLObjectFactory.createPolicy(oConsentXACML);

            oMarshaller.marshal(oJaxbElement, swXML);
            sConsentXACML = swXML.toString();
        } catch (Exception e) {
            String sErrorMessage = "Failed to serialize the XACML document to a string.  Error: " + e.getMessage();
            LOG.error(sErrorMessage, e);
            throw new AdapterPIPException(sErrorMessage, e);
        }

        return sConsentXACML;
    }

    /**
     * This method takes a string version of the Patient Pref document and creates the JAXB object version of the same
     * document.
     *
     * @param sConsentXACML The string version of the patient preference XACML document.
     * @return The JAXB object version of the patient preferences XACML document.
     * @throws gov.hhs.fha.nhinc.policyengine.adapterpip.AdapterPIPException This is thrown if there is an error
     *             deserializing the string.
     */
    public PolicyType deserializeConsentXACMLDoc(String sConsentXACML) throws AdapterPIPException {
        PolicyType oConsentXACML = null;
        InputStream is = null;

        try {
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext oJaxbContext = oHandler.getJAXBContext("oasis.names.tc.xacml._2_0.policy.schema.os");
            Unmarshaller oUnmarshaller = oJaxbContext.createUnmarshaller();

            is = new ByteArrayInputStream(sConsentXACML.getBytes());
            JAXBUnmarshallingUtil util = new JAXBUnmarshallingUtil();

            JAXBElement oJAXBElementConsentXACML = (JAXBElement) oUnmarshaller
                    .unmarshal(util.getSafeStreamReaderFromInputStream(is));
            if (oJAXBElementConsentXACML.getValue() instanceof PolicyType) {
                oConsentXACML = (PolicyType) oJAXBElementConsentXACML.getValue();
            }
        } catch (Exception e) {
            LOG.error("Failed to deserialize the XACML consent string...", e);
            throw new AdapterPIPException("Failed to deserialize the XACML consent string...", e);
        } finally {
            StreamUtils.closeStreamSilently(is);
        }

        return oConsentXACML;
    }

}
