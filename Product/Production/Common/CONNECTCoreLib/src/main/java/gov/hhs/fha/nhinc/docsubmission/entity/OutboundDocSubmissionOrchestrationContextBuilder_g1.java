/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.docsubmission.entity;

import gov.hhs.fha.nhinc.orchestration.OrchestrationContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author zmelnick
 */
public class OutboundDocSubmissionOrchestrationContextBuilder_g1 extends OutboundDocSubmissionOrchestrationContextBuilder {

    private static Log log = LogFactory.getLog(OutboundDocSubmissionOrchestrationContextBuilder_g1.class);
    
    @Override
    public OrchestrationContext build() {
        log.debug("begin build");
        return new OrchestrationContext(new OutboundDocSubmissionStrategyImpl_g0(),
                new OutboundDocSubmissionOrchestratable(getNhinDelegate(), getRequest(), getTarget(), getAssertionType()));
    }

    @Override
    public Log getLog() {
        return log;
    }
}
