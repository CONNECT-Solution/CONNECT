/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.docsubmission.entity;

import gov.hhs.fha.nhinc.docsubmission.nhin.proxy.NhinDocSubmissionProxy;
import gov.hhs.fha.nhinc.docsubmission.nhin.proxy.NhinDocSubmissionProxyObjectFactory;
import gov.hhs.fha.nhinc.orchestration.Orchestratable;
import gov.hhs.fha.nhinc.orchestration.OrchestrationStrategy;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author zmelnick
 */
class OutboundDocSubmissionStrategyImpl_g0 implements OrchestrationStrategy {

    private static Log log = LogFactory.getLog(OutboundDocSubmissionStrategyImpl_g0.class);

    public OutboundDocSubmissionStrategyImpl_g0() {
    }

    protected Log getLogger() {
        return log;
    }

    @Override
    public void execute(Orchestratable message) {
        if (message instanceof OutboundDocSubmissionOrchestratable) {
            execute((OutboundDocSubmissionOrchestratable) message);
        } else {
            getLogger().error("Not an OutboundDocSubmissionOrchestratable.");
        }
    }

    public void execute(OutboundDocSubmissionOrchestratable message) {
        getLogger().debug("Begin OutboundDocSubmissionOrchestratableImpl_g0.process");
        if (message == null) {
            getLogger().debug("OutboundDocSubmissionOrchestratable was null");
            return;
        }

        if (message instanceof OutboundDocSubmissionOrchestratable) {
            NhinDocSubmissionProxy nhincDocSubmission = new NhinDocSubmissionProxyObjectFactory().getNhinDocSubmissionProxy();
            RegistryResponseType response = nhincDocSubmission.provideAndRegisterDocumentSetB(message.getRequest(), message.getAssertion(), message.getTarget());
            message.setResponse(response);
        } else {
            getLogger().error("OutboundDocSubmissionOrchestratableImpl_g0 AdapterDelegateImpl_a0.process recieved a message " +
                    "which was not of type OutboundDocSubmissionOrchestratableImpl_a0.");
        }
        getLogger().debug("End OutboundDocSubmissionOrchestratableImpl_g0.process");
    }
}
