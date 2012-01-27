/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docsubmission.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.orchestration.OutboundDelegate;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;

/**
 *
 * @author zmelnick
 */
public class OutboundDocSubmissionOrchestratableImpl_a0 extends OutboundDocSubmissionOrchestratable {

    public OutboundDocSubmissionOrchestratableImpl_a0(AssertionType assertion, OutboundDelegate delegate,ProvideAndRegisterDocumentSetRequestType request, NhinTargetSystemType target) {
        super(assertion, delegate, request, target);
    }
}
