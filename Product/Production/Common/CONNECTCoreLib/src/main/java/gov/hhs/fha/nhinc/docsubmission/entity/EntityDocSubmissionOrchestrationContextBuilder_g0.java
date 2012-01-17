/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.docsubmission.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.orchestration.NhinDelegate;
import gov.hhs.fha.nhinc.orchestration.OrchestrationContext;
import gov.hhs.fha.nhinc.orchestration.OrchestrationContextBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author zmelnick
 */
public class EntityDocSubmissionOrchestrationContextBuilder_g0 implements OrchestrationContextBuilder {

    private static Log log = LogFactory.getLog(EntityDocSubmissionOrchestrationContextBuilder_g0.class);
    private AssertionType assertionType;
    private NhinDelegate nhinDelegate;

    public OrchestrationContext build() {
        log.debug("begin build");
        return new OrchestrationContext(new NhinDocSubmissionStrategyImpl_g0(), new EntityDocSubmissionOrchestratableImpl_a0(getAssertionType(), getNhinDelegate()));
    }

    public AssertionType getAssertionType() {
        return assertionType;
    }

    public void setAssertionType(AssertionType assertionType) {
        this.assertionType = assertionType;
    }

    public static Log getLog() {
        return log;
    }
    
    public NhinDelegate getNhinDelegate() {
        return nhinDelegate;
    }

    public void setNhinDelegate(NhinDelegate nhinDelegate) {
        this.nhinDelegate = nhinDelegate;
    }
}
