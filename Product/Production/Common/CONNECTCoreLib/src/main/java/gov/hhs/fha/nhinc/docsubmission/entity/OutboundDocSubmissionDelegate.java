/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.docsubmission.entity;

import gov.hhs.fha.nhinc.orchestration.OutboundOrchestratable;
import gov.hhs.fha.nhinc.orchestration.OutboundDelegate;
import gov.hhs.fha.nhinc.orchestration.Orchestratable;
import gov.hhs.fha.nhinc.orchestration.OrchestrationContextFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author zmelnick
 */
public class OutboundDocSubmissionDelegate implements OutboundDelegate {

    private static Log log = LogFactory.getLog(OutboundDocSubmissionDelegate.class);

    @Override
    public OutboundOrchestratable process(OutboundOrchestratable message) {
        getLogger().debug("begin process");
        if (message instanceof OutboundDocSubmissionOrchestratable) {
            return process((OutboundDocSubmissionOrchestratable) message);
        }
        getLogger().error("message is not an instance of NhinDocSubmissionOrchestratable!");
        return null;
    }

    public Orchestratable process(Orchestratable message) {
        if (message instanceof OutboundOrchestratable) {
            return process((OutboundOrchestratable) message);
        }
        return null;
    }

    private Log getLogger() {
        return log;
    }

    private OutboundDocSubmissionOrchestratable process(OutboundDocSubmissionOrchestratable dsMessage) {
        getLogger().debug("processing of Document Submission orchestratable has begun");

        OutboundDocSubmissionOrchestrationContextBuilder contextBuilder = (OutboundDocSubmissionOrchestrationContextBuilder) OrchestrationContextFactory.getInstance().getBuilder(
                dsMessage.getAssertion().getHomeCommunity(), dsMessage.getServiceName());

        contextBuilder.setAssertionType(dsMessage.getAssertion());
        contextBuilder.setNhinDelegate(dsMessage.getNhinDelegate());
        contextBuilder.setNhinTargetSystemType(dsMessage.getTarget());
        contextBuilder.setRequestType(dsMessage.getRequest());

        return (OutboundDocSubmissionOrchestratable) contextBuilder.build().execute();
    }
}
