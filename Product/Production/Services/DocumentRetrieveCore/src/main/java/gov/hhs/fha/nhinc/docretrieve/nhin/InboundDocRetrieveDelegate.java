/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.docretrieve.nhin;

import gov.hhs.fha.nhinc.docretrieve.MessageGenerator;
import gov.hhs.fha.nhinc.orchestration.InboundDelegate;
import gov.hhs.fha.nhinc.orchestration.InboundOrchestratable;
import gov.hhs.fha.nhinc.orchestration.Orchestratable;
import gov.hhs.fha.nhinc.orchestration.OrchestrationContext;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author mweaver
 */
public class InboundDocRetrieveDelegate implements InboundDelegate {

    private static Log log = LogFactory.getLog(InboundDocRetrieveDelegate.class);

    /**
     * defualt constructor.
     */
    public InboundDocRetrieveDelegate() {
    }

    /**
     * process the message and return it so it can be chained.
     * @param message
     * @return the message
     */
    @Override
    public Orchestratable process(Orchestratable message) {
        if (message instanceof InboundOrchestratable) {
            return process((InboundOrchestratable) message);
        } else {
            log.error("message is not an instance of NhinDocRetrieveOrchestratable!");
        }
        return message;
    }

    /**
     * Process a specific type for inbound orchestrable.
     * @param the message
     * @return the inbound orchestrable
     */
    public InboundOrchestratable process(InboundOrchestratable message) {

        OrchestrationContext context = new OrchestrationContext(new InboundDocRetrieveStrategyImpl(), message);

        return (InboundOrchestratable) context.execute();
    }

    public void createErrorResponse(InboundOrchestratable message, String error) {
        if (message == null) {
            log.debug("NhinOrchestratable was null");
            return;
        }

        if (message instanceof InboundDocRetrieveOrchestratable) {

            RetrieveDocumentSetResponseType response = MessageGenerator.getInstance()
                    .createRegistryResponseError(error);

            ((InboundDocRetrieveOrchestratable) message).setResponse(response);
        }
    }
}
