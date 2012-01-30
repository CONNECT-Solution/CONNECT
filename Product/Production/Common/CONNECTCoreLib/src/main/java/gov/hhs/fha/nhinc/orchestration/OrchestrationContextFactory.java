package gov.hhs.fha.nhinc.orchestration;

import gov.hhs.fha.nhinc.admindistribution.entity.OutboundAdminDistributionFactory;
import gov.hhs.fha.nhinc.docquery.entity.OutboundDocQueryFactory;
import gov.hhs.fha.nhinc.patientdiscovery.entity.OutboundPatientDiscoveryFactory;
import gov.hhs.fha.nhinc.docretrieve.entity.OutboundDocRetrieveFactory;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.docsubmission.entity.OutboundDocSubmissionFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;

public class OrchestrationContextFactory {

    private static OrchestrationContextFactory INSTANCE = new OrchestrationContextFactory();

    private OrchestrationContextFactory() {
    }

    public static OrchestrationContextFactory getInstance() {
        return INSTANCE;
    }

    public OrchestrationContextBuilder getBuilder(HomeCommunityType homeCommunityType, String serviceName) {
        NhincConstants.GATEWAY_API_LEVEL apiLevel = ConnectionManagerCache.getInstance().getApiVersion(
                homeCommunityType.getHomeCommunityId(), serviceName);
        return getBuilder(apiLevel, serviceName);
    }
    
    private OrchestrationContextBuilder getBuilder(
            NhincConstants.GATEWAY_API_LEVEL apiLevel, String serviceName){

        if(NhincConstants.DOC_RETRIEVE_SERVICE_NAME.equals(serviceName)){
            return OutboundDocRetrieveFactory.getInstance().
                    createOrchestrationContextBuilder(apiLevel);
        }else if(NhincConstants.ADMIN_DIST_SERVICE_NAME.equals(serviceName)){
            return OutboundAdminDistributionFactory.getInstance().
                    createOrchestrationContextBuilder(apiLevel);
        }else if(NhincConstants.DOC_QUERY_SERVICE_NAME.equalsIgnoreCase(serviceName)){
            return OutboundDocQueryFactory.getInstance().
                    createOrchestrationContextBuilder(apiLevel);
        }else if(NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME.equalsIgnoreCase(serviceName)){
            return OutboundPatientDiscoveryFactory.getInstance().
                    createOrchestrationContextBuilder(apiLevel);
        } else if (NhincConstants.DOC_SUBMISSION_SERVICE_PROP.equals(serviceName)) {
            return OutboundDocSubmissionFactory.getInstance().createOrchestrationContextBuilder(apiLevel);
        }
        return null;
	}
}
