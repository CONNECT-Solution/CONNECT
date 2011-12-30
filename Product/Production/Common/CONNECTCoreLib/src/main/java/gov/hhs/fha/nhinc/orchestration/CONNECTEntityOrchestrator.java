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
                // audit
                auditRequest(message);

                if (message.isEnabled()) {
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
    protected EntityOrchestratable delegateToNhin(EntityOrchestratable message) {
        EntityOrchestratable resp = null;
        getLogger().debug("Entering CONNECTNhinOrchestrator.delegateToNhin(...)");
        NhinDelegate p = message.getNhinDelegate();
        resp = p.process(message);
        getLogger().debug("Exiting CONNECTNhinOrchestrator.delegateToNhin(...)");
        return resp;
    }
    /*
     * End Delegate Methods
     */
}
