/**
 * 
 */
package gov.hhs.fha.nhinc.docretrieve.orchestration;

import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.connectmgr.NhinEndpointManager;
import gov.hhs.fha.nhinc.docretrieve.entity.OutboundDocRetrieveOrchestrationContextFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.orchestration.AbstractOrchestrationContextFactory;
import gov.hhs.fha.nhinc.orchestration.OrchestrationContextBuilder;

/**
 * @author achidamb
 *
 */
public class OrchestrationContextFactory extends AbstractOrchestrationContextFactory {
    
    private static OrchestrationContextFactory INSTANCE = new OrchestrationContextFactory();

    private OrchestrationContextFactory() {
    }

    public static OrchestrationContextFactory getInstance() {
        return INSTANCE;
    }

    public OrchestrationContextBuilder getBuilder(HomeCommunityType homeCommunityType,
            NhincConstants.NHIN_SERVICE_NAMES serviceName) {
        NhinEndpointManager nem = new NhinEndpointManager();
        NhincConstants.GATEWAY_API_LEVEL apiLevel = nem.getApiVersion(homeCommunityType.getHomeCommunityId(),
                serviceName);
        return OutboundDocRetrieveOrchestrationContextFactory.getInstance().createOrchestrationContextBuilder(apiLevel);
    }
    
}
