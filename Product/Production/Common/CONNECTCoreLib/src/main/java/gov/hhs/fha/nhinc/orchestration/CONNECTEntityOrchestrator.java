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
public class CONNECTEntityOrchestrator extends CONNECTOrchestrationBase implements CONNECTOrchestrator {

    private static final Log logger = LogFactory.getLog(CONNECTEntityOrchestrator.class);

    public void process(Orchestratable message) {
        if (message != null) {
            if (message instanceof EntityOrchestratable) {
                if (message.isEnabled()) {
                    // audit
                    audit(message);

                    if (message.isPassthru()) {
                        // straight to adapter
                        delegateToNhin((EntityOrchestratable) message);
                    } else {
                        // policy check
                        if (isPolicyOk(message, PolicyTransformer.Direction.INBOUND)) {
                            // if true, sent to adapter
                            delegateToNhin((EntityOrchestratable) message);
                        }
                    }
                    // audit again
                    audit(message);
                }
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
    protected void delegateToNhin(EntityOrchestratable message) {
        getLogger().debug("Entering CONNECTNhinOrchestrator.delegateToAdapter(...)");
        NhinDelegate p = message.getNhinDelegate();
        p.process(message);
        getLogger().debug("Exiting CONNECTNhinOrchestrator.delegateToAdapter(...)");
    }
    /*
     * End Delegate Methods
     */
}
