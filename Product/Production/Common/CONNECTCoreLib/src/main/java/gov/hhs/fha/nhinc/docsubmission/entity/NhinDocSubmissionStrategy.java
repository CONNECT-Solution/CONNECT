/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.docsubmission.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.docsubmission.XDRAuditLogger;
import gov.hhs.fha.nhinc.docsubmission.nhin.proxy.NhinDocSubmissionProxy;
import gov.hhs.fha.nhinc.docsubmission.nhin.proxy.NhinDocSubmissionProxyObjectFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.orchestration.Orchestratable;
import gov.hhs.fha.nhinc.orchestration.OrchestrationStrategy;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;

/**
 *
 * @author zmelnick
 */
public abstract class NhinDocSubmissionStrategy implements OrchestrationStrategy {

    public NhinDocSubmissionStrategy() {
        //empty on purpose
    }

    @Override
    public void execute(Orchestratable message) {
        if (message == null) {
            getLogger().debug("NhinOrchestratable was null");
            return;
        }
        if (message instanceof EntityDocSubmissionOrchestratable) {
            execute((EntityDocSubmissionOrchestratable) message);
        }
    }

    public abstract void execute(EntityDocSubmissionOrchestratable message);

    public void execute(EntityDocSubmissionOrchestratableImpl_a0 message) {
        NhinDocSubmissionProxy nhincDocSubmission = new NhinDocSubmissionProxyObjectFactory().getNhinDocSubmissionProxy();
       RegistryResponseType response = nhincDocSubmission.provideAndRegisterDocumentSetB(message.getRequest(), message.getAssertion(), message.getTarget());
       message.setResponse(response);
    }

    protected abstract Log getLogger();
}

