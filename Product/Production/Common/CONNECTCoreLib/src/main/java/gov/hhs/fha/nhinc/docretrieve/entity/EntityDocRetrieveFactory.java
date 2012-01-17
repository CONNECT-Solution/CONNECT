package gov.hhs.fha.nhinc.docretrieve.entity;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;

public class EntityDocRetrieveFactory {


    private static EntityDocRetrieveFactory INSTANCE = new EntityDocRetrieveFactory();

    private EntityDocRetrieveFactory() {
    }

    public EntityDocRetrieveOrchestrationContextBuilder_g0 createOrchestrationContextBuilder(NhincConstants.GATEWAY_API_LEVEL apiLevel) {
        switch (apiLevel) {
            case LEVEL_g0:
                return new EntityDocRetrieveOrchestrationContextBuilder_g0();
            default:
                return null;
        }
    }

    public static EntityDocRetrieveFactory getInstance() {
        return INSTANCE;
    }
}
