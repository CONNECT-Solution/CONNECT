package gov.hhs.fha.nhinc.docquery.entity;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.orchestration.OrchestrationContextBuilder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Returns the OrchestrationContextBuilder appropriate for the particular apiLevel
 * @author paul.eftis
 */
public class OutboundDocQueryFactory{

    private static Log log = LogFactory.getLog(OutboundDocQueryFactory.class);

    private static OutboundDocQueryFactory INSTANCE = new OutboundDocQueryFactory();

    private OutboundDocQueryFactory(){
    }


    public OrchestrationContextBuilder createOrchestrationContextBuilder(NhincConstants.GATEWAY_API_LEVEL apiLevel) {
        log.debug("EntityDocQueryFactory has apiLevel=" + apiLevel.toString());
        switch(apiLevel){
            case LEVEL_g0:return new OutboundDocQueryOrchestrationContextBuilder_g0();
            case LEVEL_g1: return new OutboundDocQueryOrchestrationContextBuilder_g1();
            default: return new OutboundDocQueryOrchestrationContextBuilder_g1();
        }
    }

    public static OutboundDocQueryFactory getInstance(){
        return INSTANCE;
    }
}
