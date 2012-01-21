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
public class EntityDocSubmissionFactory {

    private static EntityDocSubmissionFactory INSTANCE = new EntityDocSubmissionFactory();

    private EntityDocSubmissionFactory(){
    }

    public OrchestrationContextBuilder createOrchestrationContextBuilder(NhincConstants.GATEWAY_API_LEVEL apiLevel){
       switch (apiLevel) {
            case LEVEL_g0:
                return new EntityDocSubmissionOrchestrationContextBuilderImpl_g0_g1();
           case LEVEL_g1:
               return new EntityDocSubmissionOrchestrationContextBuilderImpl_g0_g1();
            default:
                return new EntityDocSubmissionOrchestrationContextBuilderImpl_g0_g1();
       }
    }

    public static EntityDocSubmissionFactory getInstance() {
        return INSTANCE;
    }

}
