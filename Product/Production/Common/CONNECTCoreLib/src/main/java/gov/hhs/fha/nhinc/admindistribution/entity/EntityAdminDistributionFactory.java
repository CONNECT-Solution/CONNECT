package gov.hhs.fha.nhinc.admindistribution.entity;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.orchestration.OrchestrationContextBuilder;

/**
 *
 * @author nnguyen
 */
public class EntityAdminDistributionFactory {

    private static EntityAdminDistributionFactory INSTANCE = new EntityAdminDistributionFactory();

    private EntityAdminDistributionFactory() {
    }

    public OrchestrationContextBuilder createOrchestrationContextBuilder(NhincConstants.GATEWAY_API_LEVEL apiLevel) {
        switch (apiLevel) {
            case LEVEL_g0: return new EntityAdminDistributionOrchestrationContextBuilder_g0();
            case LEVEL_g1: return new EntityAdminDistributionOrchestrationContextBuilder_g1();
            default: return null;
        }
    }

    public static EntityAdminDistributionFactory getInstance() {
        return INSTANCE;
    }
}
