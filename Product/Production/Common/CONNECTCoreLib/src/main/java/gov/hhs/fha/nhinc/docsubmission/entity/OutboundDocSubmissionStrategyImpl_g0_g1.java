/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.docsubmission.entity;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author zmelnick
 */
class OutboundDocSubmissionStrategyImpl_g0_g1 extends OutboundDocSubmissionStrategy {

    private static Log log = LogFactory.getLog(OutboundDocSubmissionStrategyImpl_g0_g1.class);

    public OutboundDocSubmissionStrategyImpl_g0_g1() {
    }

    public void execute(OutboundDocSubmissionOrchestratable message) {
        getLogger().debug("Begin NhinDocSubmissionOrchestratableImpl_g0_g1.process");
        if (message instanceof OutboundDocSubmissionOrchestratableImpl_a0) {
            execute((OutboundDocSubmissionOrchestratableImpl_a0) message);
        } else {
            getLogger().error("NhinDocSubmissionImpl AdapterDelegateImple_a0.process recieved a message " +
                    "which was not of type NhinDocSubmissionOrchestratableImpl_g0_g1");
        }
        getLogger().debug("End NhinDocSubmissionOrchestratableImpl_g0_g1.process");
    }

    protected Log getLogger() {
        return log;
    }
}
