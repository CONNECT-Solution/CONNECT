/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.admindistribution.entity;

import gov.hhs.fha.nhinc.orchestration.OrchestrationContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author nnguyen
 */
public class OutboundAdminDistributionOrchestrationContextBuilder_g0 extends OutboundAdminDistributionOrchestrationContextBuilder {

    private static Log log = LogFactory.getLog(OutboundAdminDistributionOrchestrationContextBuilder_g0.class);

    @Override
    public OrchestrationContext build() {
        getLog().debug("begin build");
        return new OrchestrationContext(new OutboundAdminDistributionStrategyImpl_g0(),
                new OutboundAdminDistributionOrchestratable( getNhinDelegate(), getRequest(), getTargetSystem(), getAssertionType()));
    }
    
    @Override
    public Log getLog() {
        return log;
    }
}
