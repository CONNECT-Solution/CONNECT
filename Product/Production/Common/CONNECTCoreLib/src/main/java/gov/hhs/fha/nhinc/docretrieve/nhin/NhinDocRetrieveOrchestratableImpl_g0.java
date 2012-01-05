/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docretrieve.nhin;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.orchestration.AdapterDelegate;
import gov.hhs.fha.nhinc.orchestration.AuditTransformer;
import gov.hhs.fha.nhinc.orchestration.Delegate;
import gov.hhs.fha.nhinc.orchestration.PolicyTransformer;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;

/**
 *
 * @author mweaver
 */
public class NhinDocRetrieveOrchestratableImpl_g0 extends NhinDocRetrieveOrchestratable {
    private RetrieveDocumentSetRequestType request = null;
    private RetrieveDocumentSetResponseType response = null;
    private final String serviceName = "NhinDocumentRetrieve_g0";

    public NhinDocRetrieveOrchestratableImpl_g0(RetrieveDocumentSetRequestType body, AssertionType assertion, PolicyTransformer pt, AuditTransformer at, AdapterDelegate ad) {
        super(pt, at, ad);
        request = body;
        setAssertion(assertion);
    }
    public RetrieveDocumentSetRequestType getRequest() {
        return request;
    }

    public RetrieveDocumentSetResponseType getResponse() {
        return response;
    }

    public void setResponse(RetrieveDocumentSetResponseType response) {
        this.response = response;
    }

    @Override
    public String getServiceName()
    {
        return serviceName;
    }
	
}
