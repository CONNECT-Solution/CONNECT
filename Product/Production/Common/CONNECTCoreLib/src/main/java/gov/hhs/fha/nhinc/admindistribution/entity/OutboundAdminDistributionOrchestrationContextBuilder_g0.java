/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.admindistribution.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewaySendAlertMessageType;
import gov.hhs.fha.nhinc.orchestration.OutboundOrchestratable;
import gov.hhs.fha.nhinc.orchestration.OutboundDelegate;
import gov.hhs.fha.nhinc.orchestration.OrchestrationContext;
import gov.hhs.fha.nhinc.orchestration.OrchestrationContextBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author nnguyen
 */
public class OutboundAdminDistributionOrchestrationContextBuilder_g0 implements OrchestrationContextBuilder {

    private static Log log = LogFactory.getLog(OutboundAdminDistributionOrchestrationContextBuilder_g0.class);

    private RespondingGatewaySendAlertMessageType request;
    private AssertionType assertionType;
    private OutboundDelegate nhinDelegate;
    private NhinTargetSystemType targetSystem;
    
    public OrchestrationContext build() {
        getLog().debug("begin build");
        return new OrchestrationContext(new OutboundAdminDistributionStrategyImpl_g0(),
                new OutboundAdminDistributionOrchestratableImpl_a0(
                    getRequest(), getTargetSystem(), getAssertionType(), getNhinDelegate()));
    }
    
    public AssertionType getAssertionType() {
        return assertionType;
    }

    public void setAssertionType(AssertionType assertionType) {
        this.assertionType = assertionType;
    }

    public OutboundDelegate getNhinDelegate() {
        return nhinDelegate;
    }

    public void setNhinDelegate(OutboundDelegate nhinDelegate) {
        this.nhinDelegate = nhinDelegate;
    }

    public RespondingGatewaySendAlertMessageType getRequest() {
        return request;
    }

    public void setRequest(RespondingGatewaySendAlertMessageType request) {
        this.request = request;
    }

    public NhinTargetSystemType getTargetSystem() {
        return targetSystem;
    }

    public void setTargetSystem(NhinTargetSystemType targetSystem) {
        this.targetSystem = targetSystem;
    }
    public static Log getLog() {
        return log;
    }

    public void init(OutboundOrchestratable message) {
        setAssertionType(message.getAssertion());
        setRequest(((OutboundAdminDistributionOrchestratable) message).getRequest());
        setTargetSystem(((OutboundAdminDistributionOrchestratable) message).getTarget());
    }
}
