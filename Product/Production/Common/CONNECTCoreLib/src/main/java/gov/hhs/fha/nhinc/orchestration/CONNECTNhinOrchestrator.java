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
        if (message != null) {
            if (message instanceof NhinOrchestratable) {
                // audit
                auditRequest(message);

                if (message.isEnabled()) {
                    if (message.isPassthru()) {
                        // straight to adapter
                        delegateToAdapter((NhinOrchestratable) message);
                    } else {
                        // policy check
                        if (isPolicyOk(message, PolicyTransformer.Direction.INBOUND)) {
                            // if true, sent to adapter
                            delegateToAdapter((NhinOrchestratable) message);
                        }
                    }
                }
                // audit again
                auditResponse(message);
            }
        }
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
    /*
     * End Delegate Methods
     */
}
