/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docretrieve.nhin;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.orchestration.OrchestrationContextBuilder;

/**
 * 
 * @author mweaver
 */
public class InboundDocRetrieveFactory {
    private static InboundDocRetrieveFactory INSTANCE = new InboundDocRetrieveFactory();

    private InboundDocRetrieveFactory() {
    }

    public OrchestrationContextBuilder createOrchestrationContextBuilder(NhincConstants.ADAPTER_API_LEVEL apiLevel) {
        switch (apiLevel) {
        default:
            return new InboundDocRetrieveOrchestrationContextBuilder();
        }
    }

    public static InboundDocRetrieveFactory getInstance() {
        return INSTANCE;
    }
}