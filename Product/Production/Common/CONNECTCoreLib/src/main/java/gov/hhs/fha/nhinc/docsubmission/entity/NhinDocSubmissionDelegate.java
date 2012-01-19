/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.docsubmission.entity;

import gov.hhs.fha.nhinc.orchestration.EntityOrchestratable;
import gov.hhs.fha.nhinc.orchestration.NhinDelegate;
import gov.hhs.fha.nhinc.orchestration.Orchestratable;
import gov.hhs.fha.nhinc.orchestration.OrchestrationContext;
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
        EntityOrchestratable response = null;
        if (message instanceof EntityDocSubmissionOrchestratable) {
            getLogger().debug("processing of Document Submission orchestratable has begun");
            EntityDocSubmissionOrchestratable dsMessage = (EntityDocSubmissionOrchestratable) message;
            // TODO: check connection manager for which endpoint to use

        EntityDocSubmissionOrchestrationContextBuilder_g0 contextBuilder = (EntityDocSubmissionOrchestrationContextBuilder_g0) OrchestrationContextFactory.getInstance().getBuilder(
                    dsMessage.getTarget().getHomeCommunity(),
                    dsMessage.getServiceName());

            contextBuilder.setAssertionType(dsMessage.getAssertion());
            contextBuilder.setNhinDelegate(dsMessage.getNhinDelegate());


            OrchestrationContext context = contextBuilder.build();

            response = (EntityOrchestratable)context.execute();
        } else {
            getLogger().error("message is not an instance of NhinDocRetrieveOrchestratable!");
        }
        return response;
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
}
