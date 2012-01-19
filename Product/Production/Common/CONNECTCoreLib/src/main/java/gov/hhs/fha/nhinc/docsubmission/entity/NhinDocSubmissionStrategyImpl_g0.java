/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.docsubmission.entity;

import gov.hhs.fha.nhinc.docsubmission.nhin.proxy.NhinDocSubmissionProxy;
import gov.hhs.fha.nhinc.docsubmission.nhin.proxy.NhinDocSubmissionProxyObjectFactory;
import gov.hhs.fha.nhinc.orchestration.Orchestratable;
import gov.hhs.fha.nhinc.orchestration.OrchestrationStrategy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author zmelnick
 */
class NhinDocSubmissionStrategyImpl_g0 implements OrchestrationStrategy {

    private static Log log = LogFactory.getLog(NhinDocSubmissionStrategyImpl_g0.class);

    public NhinDocSubmissionStrategyImpl_g0() {
        //empty on purpose
    }

    @Override
    public void execute(Orchestratable message) {
        if (message instanceof EntityDocSubmissionOrchestratable) {
            execute((EntityDocSubmissionOrchestratable) message);
        }
    }

    public void execute(EntityDocSubmissionOrchestratable message) {
        getLogger().debug("Begin NhinDocSubmissionOrchestratableImple_g0.process");
        if (message == null) {
            getLogger().debug("NhinOrchestratable was null");
            return;
        }
        if (message instanceof EntityDocSubmissionOrchestratableImpl_a0) {
            NhinDocSubmissionProxy nhincDocSubmission = new NhinDocSubmissionProxyObjectFactory().getNhinDocSubmissionProxy();
            nhincDocSubmission.provideAndRegisterDocumentSetB(message.getRequest(), message.getAssertion(), message.getTarget());
        } else {
            getLogger().error("NhinDocSubmissionImpl_g0 AdapterDelegateImple_a0.process recieved a message " +
                    "which was not of type NhinDocSubmissionOrchestratableImple_g0");
        }
        getLogger().debug("End NhinDocSubmissionOrchestratableImpl_g0.process");
    }

    private Log getLogger() {
        return log;
    }
}
