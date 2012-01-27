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
public class OutboundAdminDistributionDelegate implements OutboundDelegate {

    private static Log log = LogFactory.getLog(OutboundAdminDistributionDelegate.class);

    public Orchestratable process(Orchestratable message) {
        if (message instanceof OutboundAdminDistributionOrchestratable) {
            return process((OutboundOrchestratable) message);
        }
        return null;
    }

    //@Overrides
    public OutboundOrchestratable process(OutboundOrchestratable message) {
        getLogger().debug("begin process");
        if (message instanceof OutboundAdminDistributionOrchestratable) {
            getLogger().debug("processing AD orchectratable ");
            OutboundAdminDistributionOrchestratable adMessage = (OutboundAdminDistributionOrchestratable) message;

            OrchestrationContextBuilder contextBuilder = OrchestrationContextFactory.getInstance().getBuilder(
                adMessage.getAssertion().getHomeCommunity(), adMessage.getServiceName());
                    
            if (contextBuilder instanceof OutboundAdminDistributionOrchestrationContextBuilder_g0) {
                ((OutboundAdminDistributionOrchestrationContextBuilder_g0) contextBuilder).init(message);
            } else if (contextBuilder instanceof OutboundAdminDistributionOrchestrationContextBuilder_g1) {
                ((OutboundAdminDistributionOrchestrationContextBuilder_g1) contextBuilder).init(message);
            } else  {
                return null;
            }
            return (OutboundOrchestratable)contextBuilder.build().execute();
        }
        getLogger().error("message is not an instance of NhinAdminDistributionOrchestratable!");
        return null;
    }

    private Log getLogger() {
        return log;
    }
}
