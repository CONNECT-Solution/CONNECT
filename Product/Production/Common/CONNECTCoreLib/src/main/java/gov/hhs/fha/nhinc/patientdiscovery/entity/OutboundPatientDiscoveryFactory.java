package gov.hhs.fha.nhinc.patientdiscovery.entity;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.orchestration.OrchestrationContextBuilder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Returns the OrchestrationContextBuilder appropriate for the particular apiLevel
 * @author paul.eftis
 */
public class OutboundPatientDiscoveryFactory{

    private static Log log = LogFactory.getLog(OutboundPatientDiscoveryFactory.class);

    private static OutboundPatientDiscoveryFactory INSTANCE = new OutboundPatientDiscoveryFactory();

    private OutboundPatientDiscoveryFactory(){
    }


    public OrchestrationContextBuilder createOrchestrationContextBuilder(NhincConstants.GATEWAY_API_LEVEL apiLevel) {
        log.debug("EntityPatientDiscoveryFactory has apiLevel=" + apiLevel.toString());
        switch(apiLevel){
            case LEVEL_g0: return new OutboundPatientDiscoveryOrchestrationContextBuilder_g0();
            case LEVEL_g1: return new OutboundPatientDiscoveryOrchestrationContextBuilder_g1();
            default: return new OutboundPatientDiscoveryOrchestrationContextBuilder_g1();
        }
    }

    public static OutboundPatientDiscoveryFactory getInstance(){
        return INSTANCE;
    }
}
