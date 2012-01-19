package gov.hhs.fha.nhinc.orchestration;

import gov.hhs.fha.nhinc.admindistribution.entity.EntityAdminDistributionFactory;
import gov.hhs.fha.nhinc.docquery.entity.EntityDocQueryFactory;
import gov.hhs.fha.nhinc.patientdiscovery.entity.EntityPatientDiscoveryFactory;
import gov.hhs.fha.nhinc.docretrieve.entity.EntityDocRetrieveFactory;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.docsubmission.entity.EntityDocSubmissionFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;

public class OrchestrationContextFactory {

    private static OrchestrationContextFactory INSTANCE = new OrchestrationContextFactory();

    private OrchestrationContextFactory() {
    }

    public static OrchestrationContextFactory getInstance() {
        return INSTANCE;
    }

    public OrchestrationContextBuilder getBuilder(HomeCommunityType homeCommunityType, String serviceName) {
        NhincConstants.GATEWAY_API_LEVEL apiLevel = ConnectionManagerCache.getApiVersionForNhinTarget(
                homeCommunityType.getHomeCommunityId(), serviceName);
        return getBuilder(apiLevel, serviceName);
    }
    
    private OrchestrationContextBuilder getBuilder(
            NhincConstants.GATEWAY_API_LEVEL apiLevel, String serviceName){

        if(NhincConstants.DOC_RETRIEVE_SERVICE_NAME.equals(serviceName)){
            return EntityDocRetrieveFactory.getInstance().
                    createOrchestrationContextBuilder(apiLevel);
        }else if(NhincConstants.ADMIN_DIST_SERVICE_NAME.equals(serviceName)){
            return EntityAdminDistributionFactory.getInstance().
                    createOrchestrationContextBuilder(apiLevel);
        }else if(NhincConstants.DOC_QUERY_SERVICE_NAME.equalsIgnoreCase(serviceName)){
            return EntityDocQueryFactory.getInstance().
                    createOrchestrationContextBuilder(apiLevel);
        }else if(NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME.equalsIgnoreCase(serviceName)){
            return EntityPatientDiscoveryFactory.getInstance().
                    createOrchestrationContextBuilder(apiLevel);
        } else if (NhincConstants.DOC_SUBMISSION_SERVICE_PROP.equals(serviceName)) {
            return EntityDocSubmissionFactory.getInstance().createOrchestrationContextBuilder(apiLevel);
        }
        return null;
	}
}
