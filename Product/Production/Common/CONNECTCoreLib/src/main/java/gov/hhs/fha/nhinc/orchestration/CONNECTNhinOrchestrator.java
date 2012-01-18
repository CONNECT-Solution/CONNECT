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
public class CONNECTNhinOrchestrator extends CONNECTOrchestrationBase implements
        CONNECTOrchestrator {

    private static final Log logger = LogFactory.getLog(CONNECTNhinOrchestrator.class);

    @Override
    protected Orchestratable processIfPolicyIsOk(Orchestratable message) {
        return processInboundIfPolicyIsOk(message);

    }
}
