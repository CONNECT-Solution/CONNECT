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
public class EntityDocSubmissionOrchestrationContextBuilderImpl_g0_g1 extends EntityDocSubmissionOrchestrationContextBuilder {

    private static Log log = LogFactory.getLog(EntityDocSubmissionOrchestrationContextBuilderImpl_g0_g1.class);

    @Override
    public OrchestrationContext build() {
        log.debug("begin build");
        return new OrchestrationContext(new NhinDocSubmissionStrategyImpl_g0_g1(), new EntityDocSubmissionOrchestratableImpl_a0(getAssertionType(), getNhinDelegate(), getRequest(), getNhinTargetSystemType()));
    }
}
