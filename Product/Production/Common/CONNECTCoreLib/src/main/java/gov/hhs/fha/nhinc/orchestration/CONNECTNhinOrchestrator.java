/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.orchestration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author mweaver
 */
public class CONNECTNhinOrchestrator extends CONNECTOrchestrationBase implements CONNECTOrchestrator {

    private static final Log logger = LogFactory.getLog(CONNECTNhinOrchestrator.class);

    public void process(Orchestratable message) {
        getLogger().debug("Entering CONNECTNhinOrchestrator for " + message.getServiceName());
        if (message != null) {
            if (message instanceof NhinOrchestratable) {
                // audit
                getLogger().debug("Calling audit for " + message.getServiceName());
                auditRequest(message);

                if (message.isEnabled()) {
                    getLogger().debug(message.getServiceName() + " service is enabled. Procesing message...");
                    if (message.isPassthru()) {
                        getLogger().debug(message.getServiceName() + " is in passthrough mode. Sending directly to adapter");
                        // straight to adapter
                        delegateToAdapter((NhinOrchestratable) message);
                    } else {
                        getLogger().debug(message.getServiceName() + "is not in passthrough mode. Calling internal processing");
                        // policy check
                        if (isPolicyOk(message, PolicyTransformer.Direction.INBOUND)) {
                            // if true, sent to adapter
                            delegateToAdapter((NhinOrchestratable) message);
                        } else {
                            getLogger().debug(message.getServiceName() + " failed policy check. Returning a error response");
                            createErrorResponse((NhinOrchestratable)message, message.getServiceName() + " failed policy check.");
                        }
                    }
                } else {
                    getLogger().debug(message.getServiceName() + " is not enabled. returning a error response");
                    createErrorResponse((NhinOrchestratable)message, message.getServiceName() + " is not enabled.");
                }
                // audit again
                getLogger().debug("Calling audit response for " + message.getServiceName());
                auditResponse(message);
            }
        }
        getLogger().debug("Returning from CONNECTNhinOrchestrator for " + message.getServiceName());
    }

    @Override
    protected Log getLogger() {
        return logger;
    }

    /*
     * Begin Delegate Methods
     */
    protected void delegateToAdapter(NhinOrchestratable message) {
        getLogger().debug("Entering CONNECTNhinOrchestrator.delegateToAdapter(...)");
        AdapterDelegate p = message.getAdapterDelegate();
        p.process(message);
        getLogger().debug("Exiting CONNECTNhinOrchestrator.delegateToAdapter(...)");
    }

    protected void createErrorResponse(NhinOrchestratable message, String error) {
        if (message != null && message.getAdapterDelegate() != null) {
            AdapterDelegate delegate = message.getAdapterDelegate();
            delegate.createErrorResponse(message, error);
        }
    }
    /*
     * End Delegate Methods
     */
}
