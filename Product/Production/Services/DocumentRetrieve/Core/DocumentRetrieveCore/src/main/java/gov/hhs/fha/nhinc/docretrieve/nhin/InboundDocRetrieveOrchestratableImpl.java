/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docretrieve.nhin;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.orchestration.InboundDelegate;
import gov.hhs.fha.nhinc.orchestration.AuditTransformer;
import gov.hhs.fha.nhinc.orchestration.PolicyTransformer;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;

/**
 * 
 * @author mweaver
 */
public class InboundDocRetrieveOrchestratableImpl extends InboundDocRetrieveOrchestratable {
    private RetrieveDocumentSetRequestType request = null;
    private RetrieveDocumentSetResponseType response = null;
    private final String serviceName = "NhinDocumentRetrieve_g0";

    public InboundDocRetrieveOrchestratableImpl(RetrieveDocumentSetRequestType body, AssertionType assertion,
            PolicyTransformer pt, AuditTransformer at, InboundDelegate ad) {
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
    public String getServiceName() {
        return serviceName;
    }

}
