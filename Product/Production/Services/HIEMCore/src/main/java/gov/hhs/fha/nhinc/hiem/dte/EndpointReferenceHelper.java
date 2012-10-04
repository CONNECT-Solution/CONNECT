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
package gov.hhs.fha.nhinc.hiem.dte;

import gov.hhs.fha.nhinc.xmlCommon.XmlUtility;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3._2005._08.addressing.AttributedURIType;
import org.w3._2005._08.addressing.EndpointReferenceType;
import org.w3._2005._08.addressing.ReferenceParametersType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 
 * @author rayj
 */
public class EndpointReferenceHelper {

    /**
     * @deprecated
     * @param endpointReferenceXml
     * @return
     */
    public EndpointReferenceType createEndpointReference(Element endpointReferenceXml) {
        org.w3._2005._08.addressing.ObjectFactory wsaObjFact = new org.w3._2005._08.addressing.ObjectFactory();
        EndpointReferenceType endpointReference = wsaObjFact.createEndpointReferenceType();

        String address = getAddressFromXml(endpointReferenceXml);
        setAddress(endpointReference, address);

        // todo: handle "ReferenceParameters"
        // todo: handle "Metadata"
        // todo: handle "other"

        return endpointReference;
    }

    private String getAddressFromXml(Element endpointReferenceXml) {
        String address = null;
        Element addressXml = XmlUtility.getSingleChildElement(endpointReferenceXml, Namespaces.WSA, "Address");
        if (addressXml != null) {
            address = XmlUtility.getNodeValue(addressXml);
        }
        return address;
    }

    public EndpointReferenceType createEndpointReferenceAddressOnly(String address) {
        org.w3._2005._08.addressing.ObjectFactory wsaObjFact = new org.w3._2005._08.addressing.ObjectFactory();
        EndpointReferenceType endpointReference = wsaObjFact.createEndpointReferenceType();
        setAddress(endpointReference, address);
        return endpointReference;
    }

    private void setAddress(EndpointReferenceType endpointReference, String address) {
        org.w3._2005._08.addressing.ObjectFactory addrObjFact = new org.w3._2005._08.addressing.ObjectFactory();
        AttributedURIType addressElement = addrObjFact.createAttributedURIType();
        addressElement.setValue(address);
        endpointReference.setAddress(addressElement);
    }

    public void attachSimpleReferenceParameter(EndpointReferenceType endpointReference, String referenceParameterUri,
            String referenceParameterElementName, String referenceParameterValue) {
        org.w3._2005._08.addressing.ObjectFactory addrObjFact = new org.w3._2005._08.addressing.ObjectFactory();
        ReferenceParametersType refParams = addrObjFact.createReferenceParametersType();

        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        Document doc = null;
        try {
            doc = docBuilderFactory.newDocumentBuilder().newDocument();
        } catch (ParserConfigurationException ex) {
            throw new RuntimeException(ex);
        }
        doc.setXmlStandalone(true);
        Element referenceParameter = doc.createElementNS(referenceParameterUri, referenceParameterElementName);
        referenceParameter.setTextContent(referenceParameterValue);
        refParams.getAny().add(referenceParameter);
        endpointReference.setReferenceParameters(refParams);
    }
}
