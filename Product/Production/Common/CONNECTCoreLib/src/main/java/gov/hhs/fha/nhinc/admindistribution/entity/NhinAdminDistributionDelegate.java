/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.admindistribution.entity;

import gov.hhs.fha.nhinc.orchestration.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author nnguyen
 */
public class NhinAdminDistributionDelegate implements NhinDelegate {

    private static Log log = LogFactory.getLog(NhinAdminDistributionDelegate.class);

    public Orchestratable process(Orchestratable message) {
        if (message instanceof EntityAdminDistributionOrchestratable) {
            return process((EntityOrchestratable) message);
        }
        return null;
    }

    //@Overrides
    public EntityOrchestratable process(EntityOrchestratable message) {
        getLogger().debug("begin process");
        if (message instanceof EntityAdminDistributionOrchestratable) {
            getLogger().debug("processing AD orchectratable ");
            EntityAdminDistributionOrchestratable adMessage = (EntityAdminDistributionOrchestratable) message;

            OrchestrationContextBuilder contextBuilder = OrchestrationContextFactory.getInstance().getBuilder(
                adMessage.getAssertion().getHomeCommunity(), adMessage.getServiceName());
                    
            if (contextBuilder instanceof EntityAdminDistributionOrchestrationContextBuilder_g0) {
                ((EntityAdminDistributionOrchestrationContextBuilder_g0) contextBuilder).init(message);
            } else if (contextBuilder instanceof EntityAdminDistributionOrchestrationContextBuilder_g1) {
                ((EntityAdminDistributionOrchestrationContextBuilder_g1) contextBuilder).init(message);
            } else  {
                return null;
            }
            return (EntityOrchestratable)contextBuilder.build().execute();
        }
        getLogger().error("message is not an instance of NhinAdminDistributionOrchestratable!");
        return null;
    }

    private Log getLogger() {
        return log;
    }
}
