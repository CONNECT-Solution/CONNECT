/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docsubmission.entity;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.orchestration.OrchestrationContextBuilder;

/**
 *
 * @author zmelnick
 */
public class OutboundDocSubmissionFactory {

    private static OutboundDocSubmissionFactory INSTANCE = new OutboundDocSubmissionFactory();

    private OutboundDocSubmissionFactory(){
    }

    public OrchestrationContextBuilder createOrchestrationContextBuilder(NhincConstants.GATEWAY_API_LEVEL apiLevel){
       switch (apiLevel) {
            case LEVEL_g0:
                return new OutboundDocSubmissionOrchestrationContextBuilderImpl_g0_g1();
           case LEVEL_g1:
               return new OutboundDocSubmissionOrchestrationContextBuilderImpl_g0_g1();
            default:
                return new OutboundDocSubmissionOrchestrationContextBuilderImpl_g0_g1();
       }
    }

    public static OutboundDocSubmissionFactory getInstance() {
        return INSTANCE;
    }

}
