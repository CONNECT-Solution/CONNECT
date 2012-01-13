/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.admindistribution.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewaySendAlertMessageType;
import gov.hhs.fha.nhinc.orchestration.NhinDelegate;

/**
 *
 * @author nnguyen
 */
public class EntityAdminDistributionOrchestratableImpl_a1 extends EntityAdminDistributionOrchestratable {

    public EntityAdminDistributionOrchestratableImpl_a1(RespondingGatewaySendAlertMessageType request,
            NhinTargetSystemType targetSystem, AssertionType assertion, NhinDelegate nd)
    {
        super(nd);
        super.setRequest(request);
        super.setAssertion(assertion);
        super.setTarget(targetSystem);
    }

}
