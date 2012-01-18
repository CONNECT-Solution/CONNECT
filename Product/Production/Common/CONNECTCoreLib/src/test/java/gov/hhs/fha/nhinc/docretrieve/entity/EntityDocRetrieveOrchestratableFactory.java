/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docretrieve.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType.DocumentRequest;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType.DocumentResponse;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

/**
 *
 * @author mweaver
 */
public class EntityDocRetrieveOrchestratableFactory {

    public EntityDocRetrieveOrchestratableImpl_a0 getEntityDocRetrieveOrchestratableImpl_a0() {        
        EntityDocRetrieveOrchestratableImpl_a0 impl = new EntityDocRetrieveOrchestratableImpl_a0(getRetrieveDocumentSetRequestType(), getAssertion(), null, null, null, null, getNhinTargetSystemType());
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

    public NhinTargetSystemType getNhinTargetSystemType()
    {
        NhinTargetSystemType target = new NhinTargetSystemType();
        HomeCommunityType hcid = new HomeCommunityType();
        hcid.setHomeCommunityId("1.1");
        target.setHomeCommunity(hcid);
        return target;
    }

    public AssertionType getAssertion()
    {
        AssertionType assertion = new AssertionType();
        return assertion;
    }

}
