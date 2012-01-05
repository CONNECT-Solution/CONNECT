package gov.hhs.fha.nhinc.orchestration;

import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.docretrieve.entity.EntityDocRetrieveFactory;

public class OrchestrationContextFactory {
	
	private static OrchestrationContextFactory INSTANCE = new OrchestrationContextFactory();
	
	private OrchestrationContextFactory() {
		
	}
	
	public static OrchestrationContextFactory getInstance() {
		return INSTANCE;
	}
	
	public OrchestrationContextBuilder getBuilder(HomeCommunityType homeCommunityType, String serviceName) {
		
		if ("docretrieve".equals(serviceName)) {
			return EntityDocRetrieveFactory.getInstance().createOrchestrationContextBuilder("0");
		}
		
		return null;	
	}

}
