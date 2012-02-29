/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docretrieve.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.orchestration.AuditTransformer;
import gov.hhs.fha.nhinc.orchestration.NhinAggregator;
import gov.hhs.fha.nhinc.orchestration.OutboundDelegate;
import gov.hhs.fha.nhinc.orchestration.PolicyTransformer;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;

/**
 * 
 * @author mweaver
 */
public class OutboundDocRetrieveOrchestratableImpl extends OutboundDocRetrieveOrchestratable {
    
    public OutboundDocRetrieveOrchestratableImpl(RetrieveDocumentSetRequestType body, AssertionType assertion,
            PolicyTransformer pt, AuditTransformer at, OutboundDelegate nd, NhinAggregator na,
            NhinTargetSystemType target) {
        super(pt, at, nd, na);
        super.setRequest(body);
        super.setAssertion(assertion);
        super.setTarget(target);
    }

   

}
