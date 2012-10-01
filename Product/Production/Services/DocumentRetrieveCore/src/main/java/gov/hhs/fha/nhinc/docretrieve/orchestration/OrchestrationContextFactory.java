/**
 * 
 */
package gov.hhs.fha.nhinc.docretrieve.orchestration;

import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.connectmgr.NhinEndpointManager;
import gov.hhs.fha.nhinc.docretrieve.entity.OutboundDocRetrieveFactory;
import gov.hhs.fha.nhinc.docretrieve.nhin.InboundDocRetrieveFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.orchestration.OrchestrationContextBuilder;
import gov.hhs.fha.nhinc.orchestration.AbstractOrchestrationContextFactory;
import gov.hhs.fha.nhinc.connectmgr.AdapterEndpointManager;

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
        return OutboundDocRetrieveFactory.getInstance().createOrchestrationContextBuilder(apiLevel);
    }
    
    private OrchestrationContextBuilder getBuilder(NhincConstants.ADAPTER_API_LEVEL apiLevel, String serviceName) {

        if (NhincConstants.ADAPTER_DOC_RETRIEVE_SERVICE_NAME.equals(serviceName)) {

            return InboundDocRetrieveFactory.getInstance().createOrchestrationContextBuilder(apiLevel);
        }
        return null;
    }
    
    public OrchestrationContextBuilder getBuilder(HomeCommunityType homeCommunityType, String serviceName) {
        AdapterEndpointManager aem = new AdapterEndpointManager();
        NhincConstants.ADAPTER_API_LEVEL apiLevel = aem.getApiVersion(serviceName);
        return getBuilder(apiLevel, serviceName);
    }
    
    private OrchestrationContextBuilder getBuilder(NhincConstants.GATEWAY_API_LEVEL apiLevel,
            NhincConstants.NHIN_SERVICE_NAMES serviceName) {
        return OutboundDocRetrieveFactory.getInstance().createOrchestrationContextBuilder(apiLevel);
    }


}
