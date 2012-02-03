package gov.hhs.fha.nhinc.admindistribution.entity;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.orchestration.OrchestrationContextBuilder;

/**
 *
 * @author nnguyen
 */
public class OutboundAdminDistributionFactory {

    private static OutboundAdminDistributionFactory INSTANCE = new OutboundAdminDistributionFactory();

    private OutboundAdminDistributionFactory() {
    }

    public OrchestrationContextBuilder createOrchestrationContextBuilder(NhincConstants.GATEWAY_API_LEVEL apiLevel) {
        switch (apiLevel) {
            case LEVEL_g0: return new OutboundAdminDistributionOrchestrationContextBuilder_g0();
            case LEVEL_g1: return new OutboundAdminDistributionOrchestrationContextBuilder_g1();
            default: return new OutboundAdminDistributionOrchestrationContextBuilder_g1();
        }
    }

    public static OutboundAdminDistributionFactory getInstance() {
        return INSTANCE;
    }
}
