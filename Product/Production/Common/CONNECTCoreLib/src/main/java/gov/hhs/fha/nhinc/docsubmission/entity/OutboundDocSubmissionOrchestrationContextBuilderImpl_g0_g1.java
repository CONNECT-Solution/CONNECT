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
public class OutboundDocSubmissionOrchestrationContextBuilderImpl_g0_g1 extends OutboundDocSubmissionOrchestrationContextBuilder {

    private static Log log = LogFactory.getLog(OutboundDocSubmissionOrchestrationContextBuilderImpl_g0_g1.class);

    @Override
    public OrchestrationContext build() {
        log.debug("begin build");
        return new OrchestrationContext(new OutboundDocSubmissionStrategyImpl_g0_g1(), new OutboundDocSubmissionOrchestratableImpl_a0(getAssertionType(), getNhinDelegate(), getRequest(), getNhinTargetSystemType()));
    }
}
