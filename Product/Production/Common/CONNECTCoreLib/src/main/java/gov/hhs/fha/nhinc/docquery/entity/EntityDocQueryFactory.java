package gov.hhs.fha.nhinc.docquery.entity;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.orchestration.OrchestrationContextBuilder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Returns the OrchestrationContextBuilder appropriate for the particular apiLevel
 * @author paul.eftis
 */
public class EntityDocQueryFactory{

    private static Log log = LogFactory.getLog(EntityDocQueryFactory.class);

    private static EntityDocQueryFactory INSTANCE = new EntityDocQueryFactory();

    private EntityDocQueryFactory(){
    }


    public OrchestrationContextBuilder createOrchestrationContextBuilder(NhincConstants.GATEWAY_API_LEVEL apiLevel) {
        log.debug("EntityDocQueryFactory has apiLevel=" + apiLevel.toString());
        switch(apiLevel){
            case LEVEL_g0:return new EntityDocQueryOrchestrationContextBuilder_g0();
            case LEVEL_g1: return new EntityDocQueryOrchestrationContextBuilder_g1();
            default: return new EntityDocQueryOrchestrationContextBuilder_g1();
        }
    }

    public static EntityDocQueryFactory getInstance(){
        return INSTANCE;
    }
}
