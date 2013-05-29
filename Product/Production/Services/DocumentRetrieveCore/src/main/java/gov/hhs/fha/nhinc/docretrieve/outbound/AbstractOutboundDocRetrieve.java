/**
 * 
 */
package gov.hhs.fha.nhinc.docretrieve.outbound;

import org.apache.commons.lang.StringUtils;

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
        boolean valid = false;
        String guidance = targets.getUseSpecVersion();
        
        if (StringUtils.isBlank(guidance)) {
            // assume guidance
            if (ADAPTER_API_LEVEL.LEVEL_a0.equals(entityAPILevel)) {
                guidance = "2.0";
            } else {
                guidance = "3.0";
            }
        }

        if (("3.0".equals(guidance) || ("2.0".equals(guidance))) && entityAPILevel.equals(ADAPTER_API_LEVEL.LEVEL_a1)) {
            valid = true;
        } else if ("2.0".equals(guidance) && entityAPILevel.equals(ADAPTER_API_LEVEL.LEVEL_a0)) {
            valid = true;
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