package gov.hhs.fha.nhinc.patientdiscovery.entity;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.orchestration.OrchestrationContextBuilder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Returns the OrchestrationContextBuilder appropriate for the particular apiLevel
 * @author paul
 */
public class EntityPatientDiscoveryFactory{

    private static Log log = LogFactory.getLog(EntityPatientDiscoveryFactory.class);

    private static EntityPatientDiscoveryFactory INSTANCE = new EntityPatientDiscoveryFactory();

    private EntityPatientDiscoveryFactory(){
    }


    public OrchestrationContextBuilder createOrchestrationContextBuilder(NhincConstants.GATEWAY_API_LEVEL apiLevel) {
        log.debug("EntityPatientDiscoveryFactory has apiLevel=" + apiLevel.toString());
        switch(apiLevel){
            case LEVEL_g0: return new EntityPatientDiscoveryOrchestrationContextBuilder_g0();
            case LEVEL_g1: return new EntityPatientDiscoveryOrchestrationContextBuilder_g1();
            default: return new EntityPatientDiscoveryOrchestrationContextBuilder_g1();
        }
    }

    public static EntityPatientDiscoveryFactory getInstance(){
        return INSTANCE;
    }
}
