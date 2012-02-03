/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.docsubmission.entity;

import gov.hhs.fha.nhinc.orchestration.OutboundOrchestratable;
import gov.hhs.fha.nhinc.orchestration.OutboundDelegate;
import gov.hhs.fha.nhinc.orchestration.Orchestratable;
import gov.hhs.fha.nhinc.orchestration.OrchestrationContextBuilder;
import gov.hhs.fha.nhinc.orchestration.OrchestrationContextFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author zmelnick
 */
public class OutboundDocSubmissionDelegate implements OutboundDelegate {

    private static Log log = LogFactory.getLog(OutboundDocSubmissionDelegate.class);

    public Orchestratable process(Orchestratable message) {
        if (message instanceof OutboundOrchestratable) {
            return process((OutboundOrchestratable) message);
        }
        return null;
    }

    @Override
    public OutboundOrchestratable process(OutboundOrchestratable message) {
        getLogger().debug("begin process");
        if (message instanceof OutboundDocSubmissionOrchestratable) {
            getLogger().debug("processing DS orchestratable ");
            OutboundDocSubmissionOrchestratable dsMessage = (OutboundDocSubmissionOrchestratable) message;

            OrchestrationContextBuilder contextBuilder = OrchestrationContextFactory.getInstance().getBuilder(
                dsMessage.getAssertion().getHomeCommunity(), dsMessage.getServiceName());

            if (contextBuilder instanceof OutboundDocSubmissionOrchestrationContextBuilder_g0) {
                ((OutboundDocSubmissionOrchestrationContextBuilder_g0) contextBuilder).init(message);
            } else if (contextBuilder instanceof OutboundDocSubmissionOrchestrationContextBuilder_g1) {
                ((OutboundDocSubmissionOrchestrationContextBuilder_g1) contextBuilder).init(message);
            } else  {
                return null;
            }
            return (OutboundOrchestratable)contextBuilder.build().execute();
        }
        getLogger().error("message is not an instance of OutboundDocSubmissionOrchestratable!");
        return null;
    }

    private Log getLogger() {
        return log;
    }
}
