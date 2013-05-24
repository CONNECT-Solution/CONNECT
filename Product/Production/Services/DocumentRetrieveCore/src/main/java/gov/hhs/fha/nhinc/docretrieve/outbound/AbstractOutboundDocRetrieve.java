/**
 * 
 */
package gov.hhs.fha.nhinc.docretrieve.outbound;

import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.ADAPTER_API_LEVEL;
import gov.hhs.fha.nhinc.xdcommon.XDCommonResponseHelper;
import gov.hhs.fha.nhinc.xdcommon.XDCommonResponseHelper.ErrorCodes;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;

/**
 * @author msw
 * 
 */
public class AbstractOutboundDocRetrieve {

    /**
     * 
     */
    public AbstractOutboundDocRetrieve() {
        super();
    }

    /**
     * @param targets
     * @param entityAPILevel
     * @return
     */
    protected boolean validateGuidance(NhinTargetCommunitiesType targets, ADAPTER_API_LEVEL entityAPILevel) {
        boolean valid = true;
        String guidance = targets.getUseSpecVersion();

        if ("3.0".equals(guidance) && entityAPILevel.equals(ADAPTER_API_LEVEL.LEVEL_a0)) {
            valid = false;
        }
        return valid;
    }

    protected RetrieveDocumentSetResponseType createGuidanceErrorResponse(ADAPTER_API_LEVEL entityAPILevel) {
        RetrieveDocumentSetResponseType response = new RetrieveDocumentSetResponseType();
        XDCommonResponseHelper helper = new XDCommonResponseHelper();
        response.setRegistryResponse(helper.createError(NhincConstants.INIT_MULTISPEC_ERROR_UNSUPPORTED_GUIDANCE,
                ErrorCodes.XDSRepositoryError, NhincConstants.INIT_MULTISPEC_LOC_ENTITY_DR + entityAPILevel.toString()));

        return response;
    }

}