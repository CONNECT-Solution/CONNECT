/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.admindistribution.entity;

import gov.hhs.fha.nhinc.admindistribution.nhin.proxy.NhinAdminDistributionProxy;
import gov.hhs.fha.nhinc.admindistribution.nhin.proxy.NhinAdminDistributionProxyObjectFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.orchestration.Orchestratable;
import gov.hhs.fha.nhinc.orchestration.OrchestrationStrategy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author nnguyen
 */
public class OutboundAdminDistributionStrategyImpl_g1 implements OrchestrationStrategy {

    private static Log log = LogFactory.getLog(OutboundAdminDistributionStrategyImpl_g1.class);

    public OutboundAdminDistributionStrategyImpl_g1() {
    }

    private Log getLogger() {
        return log;
    }

    @Override
    public void execute(Orchestratable message) {
        if (message instanceof OutboundAdminDistributionOrchestratable) {
            execute((OutboundAdminDistributionOrchestratable) message);
        } else {
            getLogger().error("Not an EntityAdminDistributionOrchestratable.");
        }
    }

    public void execute(OutboundAdminDistributionOrchestratable message) {
        getLogger().debug("Begin NhinAdminDistributionOrchestratableImpl_g1.process");
        if (message == null) {
            getLogger().error("EntityAdminDistributionOrchestratable was null");
            return;
        }

        if (message instanceof OutboundAdminDistributionOrchestratable) {
            NhinAdminDistributionProxy nhincAdminDist = new NhinAdminDistributionProxyObjectFactory().getNhinAdminDistProxy();
            nhincAdminDist.sendAlertMessage(message.getRequest().getEDXLDistribution(),
                    message.getRequest().getAssertion(), message.getTarget(),
                    NhincConstants.GATEWAY_API_LEVEL.LEVEL_g1);
        } else {
            getLogger().error("NhinAdminDistributionImpl_g1 AdapterDelegateImpl.process recieved a message " +
                    "which was not of type NhinAdminDistributionOrchestratableImpl_g1.");
        }
        getLogger().debug("End NhinAdminDistributionOrchestratableImpl_g1.process");
    }
}
