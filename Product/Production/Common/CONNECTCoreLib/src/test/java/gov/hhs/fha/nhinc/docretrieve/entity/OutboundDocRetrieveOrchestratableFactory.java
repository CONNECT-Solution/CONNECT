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
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType.DocumentRequest;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType.DocumentResponse;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

/**
 * 
 * @author mweaver
 */
public class OutboundDocRetrieveOrchestratableFactory {

    public OutboundDocRetrieveOrchestratableImpl getEntityDocRetrieveOrchestratableImpl_a0() {
        OutboundDocRetrieveOrchestratableImpl impl = new OutboundDocRetrieveOrchestratableImpl(
                getRetrieveDocumentSetRequestType(), getAssertion(), null, null, null, null, getNhinTargetSystemType());
        return impl;
    }

    public RetrieveDocumentSetRequestType getRetrieveDocumentSetRequestType() {
        RetrieveDocumentSetRequestType request = new RetrieveDocumentSetRequestType();
        DocumentRequest dr = new DocumentRequest();
        dr.setDocumentUniqueId("1.1.1");
        dr.setHomeCommunityId("2.2.2");
        dr.setRepositoryUniqueId("3.3.3");
        request.getDocumentRequest().add(dr);
        return request;
    }

    public RetrieveDocumentSetResponseType getRetrieveDocumentSetResponseType() {
        RetrieveDocumentSetResponseType resp = new RetrieveDocumentSetResponseType();
        resp.setRegistryResponse(new RegistryResponseType());
        DocumentResponse dr = new DocumentResponse();
        dr.setDocument(new byte[1]);
        dr.setDocumentUniqueId("1.1.1");
        dr.setHomeCommunityId("2.2.2");
        dr.setMimeType("energon");
        dr.setRepositoryUniqueId("3.3.3");
        resp.getDocumentResponse().add(dr);
        return resp;
    }

    public NhinTargetSystemType getNhinTargetSystemType() {
        NhinTargetSystemType target = new NhinTargetSystemType();
        HomeCommunityType hcid = new HomeCommunityType();
        hcid.setHomeCommunityId("1.1");
        target.setHomeCommunity(hcid);
        return target;
    }

    public AssertionType getAssertion() {
        AssertionType assertion = new AssertionType();
        return assertion;
    }
    
    public RetrieveDocumentSetResponseType getRetrieveDocumentSetResponseTypePartialSuccess(){
        RetrieveDocumentSetResponseType resp = new RetrieveDocumentSetResponseType();
        resp.setRegistryResponse(new RegistryResponseType());
        DocumentResponse dr = new DocumentResponse();
        dr.setDocument(new byte[1]);
        dr.setDocumentUniqueId("1.1.1");
        dr.setHomeCommunityId("2.2.2");
        dr.setMimeType("energon");
        dr.setRepositoryUniqueId("3.3.3");
        resp.getDocumentResponse().add(dr);
        RegistryErrorList regErrList = new RegistryErrorList();
        RegistryError regErr = new RegistryError();
        resp.getRegistryResponse().setRegistryErrorList(regErrList);
        regErrList.getRegistryError().add(regErr);
        regErr.setCodeContext("Document id not found");
        regErr.setErrorCode("XDSDocumentUniqueIdError");
        regErr.setSeverity(NhincConstants.XDS_REGISTRY_ERROR_SEVERITY_ERROR);
        return resp;
    }

}
