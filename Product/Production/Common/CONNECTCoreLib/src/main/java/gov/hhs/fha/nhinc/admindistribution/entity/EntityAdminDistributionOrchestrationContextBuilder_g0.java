/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.admindistribution.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewaySendAlertMessageType;
import gov.hhs.fha.nhinc.orchestration.EntityOrchestratable;
import gov.hhs.fha.nhinc.orchestration.NhinDelegate;
import gov.hhs.fha.nhinc.orchestration.OrchestrationContext;
import gov.hhs.fha.nhinc.orchestration.OrchestrationContextBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author nnguyen
 */
public class EntityAdminDistributionOrchestrationContextBuilder_g0 implements OrchestrationContextBuilder {

    private static Log log = LogFactory.getLog(EntityAdminDistributionOrchestrationContextBuilder_g0.class);

    private RespondingGatewaySendAlertMessageType request;
    private AssertionType assertionType;
    private NhinDelegate nhinDelegate;
    private NhinTargetSystemType targetSystem;
    
    public OrchestrationContext build() {
        getLog().debug("begin build");
        return new OrchestrationContext(new NhinAdminDistributionStrategyImpl_g0(),
                new EntityAdminDistributionOrchestratableImpl_a0(
                    getRequest(), getTargetSystem(), getAssertionType(), getNhinDelegate()));
    }
    
    public AssertionType getAssertionType() {
        return assertionType;
    }

    public void setAssertionType(AssertionType assertionType) {
        this.assertionType = assertionType;
    }

    public NhinDelegate getNhinDelegate() {
        return nhinDelegate;
    }

    public void setNhinDelegate(NhinDelegate nhinDelegate) {
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

    public void init(EntityOrchestratable message) {
        setAssertionType(message.getAssertion());
        setRequest(((EntityAdminDistributionOrchestratable) message).getRequest());
        setTargetSystem(((EntityAdminDistributionOrchestratable) message).getTarget());
    }
}
