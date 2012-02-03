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
public abstract class OutboundAdminDistributionOrchestrationContextBuilder implements OrchestrationContextBuilder {

    private static Log log = LogFactory.getLog(OutboundAdminDistributionOrchestrationContextBuilder.class);

    private RespondingGatewaySendAlertMessageType request;
    private AssertionType assertionType;
    private OutboundDelegate nhinDelegate;
    private NhinTargetSystemType targetSystem;
    
    public abstract OrchestrationContext build();
    
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
    
    public Log getLog() {
        return log;
    }

    public void init(OutboundOrchestratable message) {
        setAssertionType(message.getAssertion());
        setRequest(((OutboundAdminDistributionOrchestratable) message).getRequest());
        setTargetSystem(((OutboundAdminDistributionOrchestratable) message).getTarget());
    }
}
