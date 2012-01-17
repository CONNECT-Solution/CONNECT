/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docsubmission.entity;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants.GATEWAY_API_LEVEL;

/**
 *
 * @author zmelnick
 */
public class EntityDocSubmissionFactory {

    private static EntityDocSubmissionFactory INSTANCE = new EntityDocSubmissionFactory();

    private EntityDocSubmissionFactory(){
    //empty on purpose
    }

    public EntityDocSubmissionOrchestrationContextBuilder_g0 createOrchestrationContextBuilder(GATEWAY_API_LEVEL apiLevel){
       switch (apiLevel) {
            case LEVEL_g0:
                return new EntityDocSubmissionOrchestrationContextBuilder_g0();
            default:
                return null;
       }
    }

    public static EntityDocSubmissionFactory getInstance() {
        return INSTANCE;
    }

}
