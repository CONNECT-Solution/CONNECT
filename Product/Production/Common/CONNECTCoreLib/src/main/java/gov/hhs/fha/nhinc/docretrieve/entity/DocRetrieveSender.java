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
package gov.hhs.fha.nhinc.docretrieve.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveSecuredRequestType;
import gov.hhs.fha.nhinc.docretrieve.passthru.proxy.PassthruDocRetrieveProxy;
import gov.hhs.fha.nhinc.docretrieve.passthru.proxy.PassthruDocRetrieveProxyObjectFactory;
import gov.hhs.fha.nhinc.gateway.aggregator.SetResponseMsgDocRetrieveRequestType;
import gov.hhs.fha.nhinc.gateway.aggregator.document.DocRetrieveAggregator;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType.DocumentRequest;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * 
 * @author Neil Webb
 */
public class DocRetrieveSender {

    private static org.apache.commons.logging.Log log = null;
    String transactionId = null;
    RespondingGatewayCrossGatewayRetrieveSecuredRequestType request = null;
    AssertionType assertion = null;

    protected Log createLogger() {
        return LogFactory.getLog(getClass());
    }

    public DocRetrieveSender(String transactionId, RespondingGatewayCrossGatewayRetrieveSecuredRequestType request,
            AssertionType assertion) {
        this.transactionId = transactionId;
        this.request = request;
        this.assertion = assertion;

        log = createLogger();
    }

    public void sendMessage() {
        log.debug("Begin DocRetrieveSender.run");
        try {
            // Collect identifying attributes
            DocumentRequest docRequest = null;
            String documentUniqueId = null;
            String repositoryUniqueId = null;
            String homeCommunityId = null;
            if ((request != null) && (request.getRetrieveDocumentSetRequest() != null)
                    && (!request.getRetrieveDocumentSetRequest().getDocumentRequest().isEmpty())) {
                log.debug("Doc retrieve request had sufficient information - creating request");
                docRequest = request.getRetrieveDocumentSetRequest().getDocumentRequest().get(0);
                if (docRequest != null) {
                    documentUniqueId = docRequest.getDocumentUniqueId();
                    repositoryUniqueId = docRequest.getRepositoryUniqueId();
                    homeCommunityId = docRequest.getHomeCommunityId();
                } else {
                    log.warn("DocRetrieveSender - DocRequest was null.");
                }
            } else {
                log.warn("DocRetrieveSender - request did not contain sufficient inormation to create a doc retrieve request.");
            }

            // Call NHIN proxy
            RetrieveDocumentSetResponseType nhinResponse = null;
            try {
                PassthruDocRetrieveProxy docRetrieveProxy = getDocRetreiveProxy();

                RetrieveDocumentSetRequestType body = null;
                NhinTargetSystemType target = null;
                if (request != null) {
                    body = request.getRetrieveDocumentSetRequest();
                    target = request.getNhinTargetSystem();
                }

                log.debug("Calling doc retrieve proxy");
                nhinResponse = docRetrieveProxy.respondingGatewayCrossGatewayRetrieve(body, assertion, target);
            } catch (Throwable t) {
                log.error("Error sending doc retrieve message...");
                nhinResponse = new RetrieveDocumentSetResponseType();
                RegistryResponseType registryResponse = new RegistryResponseType();
                nhinResponse.setRegistryResponse(registryResponse);
                RegistryErrorList regErrList = new RegistryErrorList();
                RegistryError regErr = new RegistryError();
                regErrList.getRegistryError().add(regErr);
                regErr.setCodeContext("Fault encountered processing internal document retrieve for community "
                        + homeCommunityId);
                regErr.setErrorCode("XDSRegistryNotAvailable");
                regErr.setSeverity("Error");
                registryResponse.setRegistryErrorList(regErrList);
                registryResponse.setStatus("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure");
                log.error("Fault encountered processing internal document retrieve for community " + homeCommunityId);
            }

            // Add response to aggregator
            String status = null;
            if (nhinResponse != null) {
                log.debug("DocRetrieveSender - NHIN response was not null - processing result.");
                SetResponseMsgDocRetrieveRequestType setResponseMsgDocRetrieveRequest = new SetResponseMsgDocRetrieveRequestType();
                setResponseMsgDocRetrieveRequest.setTransactionId(transactionId);
                setResponseMsgDocRetrieveRequest.setRetrieveDocumentSetResponse(nhinResponse);
                setResponseMsgDocRetrieveRequest.setDocumentUniqueId(documentUniqueId);
                setResponseMsgDocRetrieveRequest.setHomeCommunityId(homeCommunityId);
                setResponseMsgDocRetrieveRequest.setRepositoryUniqueId(repositoryUniqueId);
                DocRetrieveAggregator oAggregator = new DocRetrieveAggregator();
                status = oAggregator.setResponseMsg(setResponseMsgDocRetrieveRequest);
                log.debug("Response added to aggregator. Status: " + status);
            } else {
                log.debug("Response added to aggregator. Status: " + status);
            }
        } catch (Throwable t) {
            log.error("Error sending doc retrieve message: " + t.getMessage(), t);
        }

        log.debug("End DocRetrieveSender.run");
    }

    protected PassthruDocRetrieveProxy getDocRetreiveProxy() {
        log.debug("Creating NHIN doc retrieve proxy");
        PassthruDocRetrieveProxyObjectFactory objFactory = new PassthruDocRetrieveProxyObjectFactory();
        return objFactory.getPassthruDocRetrieveProxy();
    }
}
