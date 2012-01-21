/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.docsubmission.entity;

import gov.hhs.fha.nhinc.orchestration.EntityOrchestratable;
import gov.hhs.fha.nhinc.orchestration.NhinDelegate;
import gov.hhs.fha.nhinc.orchestration.Orchestratable;
import gov.hhs.fha.nhinc.orchestration.OrchestrationContextFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author zmelnick
 */
public class NhinDocSubmissionDelegate implements NhinDelegate {

    private static Log log = LogFactory.getLog(NhinDocSubmissionDelegate.class);

    @Override
    public EntityOrchestratable process(EntityOrchestratable message) {
        getLogger().debug("begin process");
        if (message instanceof EntityDocSubmissionOrchestratable) {
            return process((EntityDocSubmissionOrchestratable) message);
        }
        getLogger().error("message is not an instance of NhinDocSubmissionOrchestratable!");
        return null;
    }

    public Orchestratable process(Orchestratable message) {
        if (message instanceof EntityOrchestratable) {
            return process((EntityOrchestratable) message);
        }
        return null;
    }

    private Log getLogger() {
        return log;
    }

    private EntityDocSubmissionOrchestratable process(EntityDocSubmissionOrchestratable dsMessage) {
        getLogger().debug("processing of Document Submission orchestratable has begun");

        EntityDocSubmissionOrchestrationContextBuilder contextBuilder = (EntityDocSubmissionOrchestrationContextBuilder) OrchestrationContextFactory.getInstance().getBuilder(
                dsMessage.getAssertion().getHomeCommunity(), dsMessage.getServiceName());

        contextBuilder.setAssertionType(dsMessage.getAssertion());
        contextBuilder.setNhinDelegate(dsMessage.getNhinDelegate());
        contextBuilder.setNhinTargetSystemType(dsMessage.getTarget());
        contextBuilder.setRequestType(dsMessage.getRequest());

        return (EntityDocSubmissionOrchestratable) contextBuilder.build().execute();
    }
}
