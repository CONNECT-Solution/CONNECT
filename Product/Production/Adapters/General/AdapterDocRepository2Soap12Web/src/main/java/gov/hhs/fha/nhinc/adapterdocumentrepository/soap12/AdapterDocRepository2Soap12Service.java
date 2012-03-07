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
package gov.hhs.fha.nhinc.adapterdocumentrepository.soap12;

import gov.hhs.fha.nhinc.adapterdocrepository.AdapterDocRepository2Soap12Client;
import ihe.iti.xds_b._2007.DocumentRepositoryPortType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This web service class takes a soap 1.1 document retrieve or provide and register document set request and calls a
 * soap 1.2 client to retrieve or store documents from/to a document repository.
 * 
 * @author shawc
 */
@WebService(serviceName = "DocumentRepository_Service", portName = "DocumentRepository_Port_Soap", endpointInterface = "ihe.iti.xds_b._2007.DocumentRepositoryPortType", targetNamespace = "urn:ihe:iti:xds-b:2007", wsdlLocation = "WEB-INF/wsdl/AdapterDocRepository2Soap12Service/AdapterComponentDocRepository.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class AdapterDocRepository2Soap12Service implements DocumentRepositoryPortType {
    private static Log log = LogFactory.getLog(AdapterDocRepository2Soap12Service.class);

    /**
     * This method supports the AdapterComponentDocRepository.wsdl for storing a document to a document repository for a
     * given soap 1.1 request message.
     * 
     * @param storeRequest A ProvideAndRegisterDocumentSetRequestType object containing the desired document and
     *            metadata to store into a document repository.
     * @return Returns a RegistryResponseType indicating whether the document was successfully stored.
     */
    public oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType documentRepositoryProvideAndRegisterDocumentSetB(
            ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType storeRequest) {
        log.debug("Entering AdapterDocRepository2Soap12Service.documentRepositoryProvideAndRegisterDocumentSetB() method");

        RegistryResponseType response = null;
        try {
            if (storeRequest != null) {
                log.debug("ProvideAndRegisterDocumentSetRequest was not null");

                AdapterDocRepository2Soap12Client oClient = new AdapterDocRepository2Soap12Client();

                response = oClient.provideAndRegisterDocumentSet(storeRequest);

            } else {
                String sErrorMessage = "The store document request message was null.";
                log.error(sErrorMessage);
                throw new RuntimeException(sErrorMessage);
            }
        } catch (Exception exp) {
            log.error(exp.getMessage());
            exp.printStackTrace();
        }

        log.debug("leaving AdapterDocRepository2Soap12Service.documentRepositoryProvideAndRegisterDocumentSetB() method");
        return response;
    }

    /**
     * This method supports the AdapterComponentDocRepository.wsdl for retrieving a document from a document repository
     * for a given soap 1.1 request message.
     * 
     * @param retrieveRequest A RetrieveDocumentSetRequestType object containing the document id and repository id for
     *            the desired document.
     * @return Returns a RetrieveDocumentSetResponseType containing the desired document.
     */
    public ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType documentRepositoryRetrieveDocumentSet(
            ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType retrieveRequest) {
        log.debug("Entering AdapterDocRepository2Soap12Service.documentRepositoryRetrieveDocumentSet() method");

        RetrieveDocumentSetResponseType response = null;

        try {
            if (retrieveRequest != null) {
                log.debug("retrieveRequest was not null");

                AdapterDocRepository2Soap12Client oClient = new AdapterDocRepository2Soap12Client();

                response = oClient.retrieveDocument(retrieveRequest);

            } else {
                String sErrorMessage = "The retrieve document request message was null.";
                log.error(sErrorMessage);
                throw new RuntimeException(sErrorMessage);
            }
        } catch (Exception exp) {
            log.error(exp.getMessage());
            exp.printStackTrace();
        }

        log.debug("Leaving AdapterDocRepository2Soap12Service.documentRepositoryRetrieveDocumentSet() method");
        return response;
    }
}
