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
public class OutboundAdminDistributionStrategyImpl_g0 implements OrchestrationStrategy {

    private static Log log = LogFactory.getLog(OutboundAdminDistributionStrategyImpl_g0.class);

    public OutboundAdminDistributionStrategyImpl_g0() {
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
        getLogger().debug("Begin NhinAdminDistributionOrchestratableImpl_g0.process");
        if (message == null) {
            getLogger().debug("EntityAdminDistributionOrchestratable was null");
            return;
        }


        if (message instanceof OutboundAdminDistributionOrchestratable) {
            
            NhinAdminDistributionProxy nhincAdminDist = new NhinAdminDistributionProxyObjectFactory().getNhinAdminDistProxy();
            nhincAdminDist.sendAlertMessage(message.getRequest().getEDXLDistribution(),
                    message.getRequest().getAssertion(), message.getTarget(), NhincConstants.GATEWAY_API_LEVEL.LEVEL_g0);
        } else {
            getLogger().error("NhinAdminDistributionImpl_g0 AdapterDelegateImpl.process received a message " +
                    "which was not of type NhinAdminDistributionOrchestratableImpl_g0.");
        }
        getLogger().debug("End NhinAdminDistributionOrchestratableImpl_g0.process");
    }
}
