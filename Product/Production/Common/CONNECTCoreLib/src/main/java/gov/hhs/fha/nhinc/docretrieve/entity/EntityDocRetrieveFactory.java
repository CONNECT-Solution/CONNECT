package gov.hhs.fha.nhinc.docretrieve.entity;

public class EntityDocRetrieveFactory {
	
	private static EntityDocRetrieveFactory INSTANCE = new EntityDocRetrieveFactory();
	
	private EntityDocRetrieveFactory() {
	}
	
	public EntityDocRetrieveOrchestrationContextBuilder createOrchestrationContextBuilder(String version) {
		if ("0".equals(version)) {
			return new EntityDocRetrieveOrchestrationContextBuilder();
		}
		return null;
	}
	
	
	public static EntityDocRetrieveFactory getInstance() {
		return INSTANCE;
	}

}
