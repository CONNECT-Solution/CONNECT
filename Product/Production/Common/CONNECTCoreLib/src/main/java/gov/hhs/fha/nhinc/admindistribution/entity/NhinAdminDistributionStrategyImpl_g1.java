/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.admindistribution.entity;

import gov.hhs.fha.nhinc.admindistribution.nhin.proxy.NhinAdminDistributionProxy;
import gov.hhs.fha.nhinc.admindistribution.nhin.proxy.NhinAdminDistributionProxyObjectFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.orchestration.EntityOrchestratable;
import gov.hhs.fha.nhinc.orchestration.OrchestrationStrategy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author nnguyen
 */
public class NhinAdminDistributionStrategyImpl_g1 implements OrchestrationStrategy {

    private static Log log = LogFactory.getLog(NhinAdminDistributionStrategyImpl_g1.class);

    public NhinAdminDistributionStrategyImpl_g1() {
    }

    private Log getLogger() {
        return log;
    }

    @Override
    public void execute(EntityOrchestratable message) {
        if (message instanceof EntityAdminDistributionOrchestratable) {
            execute((EntityAdminDistributionOrchestratable) message);
        } else {
            getLogger().error("Not an EntityAdminDistributionOrchestratable.");
        }
    }

    public void execute(EntityAdminDistributionOrchestratable message) {
        getLogger().debug("Begin NhinAdminDistributionOrchestratableImpl_g1.process");
        if (message == null) {
            getLogger().error("EntityAdminDistributionOrchestratable was null");
            return;
        }

        if (message instanceof EntityAdminDistributionOrchestratableImpl_a1) {
            NhinAdminDistributionProxy nhincAdminDist = new NhinAdminDistributionProxyObjectFactory().getNhinAdminDistProxy();
            nhincAdminDist.sendAlertMessage(message.getRequest().getEDXLDistribution(),
                    message.getRequest().getAssertion(), message.getTarget(),
                    NhincConstants.GATEWAY_API_LEVEL.LEVEL_g1);
        } else {
            getLogger().error("NhinAdminDistributionImpl_g1 AdapterDelegateImpl_a1.process recieved a message " +
                    "which was not of type NhinAdminDistributionOrchestratableImpl_g1.");
        }
        getLogger().debug("End NhinAdminDistributionOrchestratableImpl_g1.process");
    }
}
