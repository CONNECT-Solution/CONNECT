/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.docsubmission.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.docsubmission.nhin.proxy.NhinDocSubmissionProxy;
import gov.hhs.fha.nhinc.docsubmission.nhin.proxy.NhinDocSubmissionProxyObjectFactory;
import gov.hhs.fha.nhinc.orchestration.Orchestratable;
import gov.hhs.fha.nhinc.orchestration.OrchestrationStrategy;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;

/**
 *
 * @author zmelnick
 */
public abstract class OutboundDocSubmissionStrategy implements OrchestrationStrategy {

    public OutboundDocSubmissionStrategy() {
        //empty on purpose
    }

    @Override
    public void execute(Orchestratable message) {
        if (message == null) {
            getLogger().debug("NhinOrchestratable was null");
            return;
        }
        if (message instanceof OutboundDocSubmissionOrchestratable) {
            execute((OutboundDocSubmissionOrchestratable) message);
        }
    }

    public abstract void execute(OutboundDocSubmissionOrchestratable message);

    public void execute(OutboundDocSubmissionOrchestratableImpl_a0 message) {
        NhinDocSubmissionProxy nhincDocSubmission = new NhinDocSubmissionProxyObjectFactory().getNhinDocSubmissionProxy();
       RegistryResponseType response = nhincDocSubmission.provideAndRegisterDocumentSetB(message.getRequest(), message.getAssertion(), message.getTarget());
       message.setResponse(response);
    }

    protected abstract Log getLogger();
}

