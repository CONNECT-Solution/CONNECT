package gov.hhs.fha.nhinc.docretrieve.entity;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;

public class OutboundDocRetrieveFactory {


    private static OutboundDocRetrieveFactory INSTANCE = new OutboundDocRetrieveFactory();

    private OutboundDocRetrieveFactory() {
    }

    public OutboundDocRetrieveOrchestrationContextBuilder_g0 createOrchestrationContextBuilder(NhincConstants.GATEWAY_API_LEVEL apiLevel) {
        switch (apiLevel) {
            case LEVEL_g0:
                return new OutboundDocRetrieveOrchestrationContextBuilder_g0();
            default:
                return null;
        }
    }

    public static OutboundDocRetrieveFactory getInstance() {
        return INSTANCE;
    }
}
