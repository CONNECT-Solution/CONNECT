/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.saml.extraction;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Jon Hoppesch
 */
public class SamlTokenCreator {

    private static Log log = LogFactory.getLog(SamlTokenCreator.class);

    /**
     * This method will populate a Map with information from the assertion that is used within
     * the SAML Token.  This Map can be used to set up the requestContext prior to sending
     * a message on the Nhin.
     *
     * @param assertion The assertion object that contains information required by the SAML Token.
     * @param url The URL to the destination service.
     * @param action The specified Action for this message.
     * @return A Map containing all of the information needed for creation of the SAML Token.
     */
    public Map CreateRequestContext(AssertionType assertion, String url, String action) {
        log.debug("Entering SamlTokenCreator.CreateRequestContext...");

        Map requestContext = new HashMap();

        if (NullChecker.isNotNullish(action)) {
            requestContext.put(NhincConstants.ACTION_PROP, action);
        }

        if (NullChecker.isNotNullish(url)) {
            requestContext.put(NhincConstants.RESOURCE_PROP, url);
        }

        if (assertion != null) {
            if (NullChecker.isNotNullish(assertion.getExpirationDate())) {
                requestContext.put(NhincConstants.EXPIRE_PROP, assertion.getExpirationDate());
            }
            if (NullChecker.isNotNullish(assertion.getDateOfSignature())) {
                requestContext.put(NhincConstants.SIGN_PROP, assertion.getDateOfSignature());
            }
            if (NullChecker.isNotNullish(assertion.getClaimFormRef())) {
                requestContext.put(NhincConstants.CONTENT_REF_PROP, assertion.getClaimFormRef());
            }
            if (assertion.getClaimFormRaw() != null) {
                requestContext.put(NhincConstants.CONTENT_PROP, assertion.getClaimFormRaw());
            }
            if (assertion.getUserInfo() != null) {
                if (NullChecker.isNotNullish(assertion.getUserInfo().getUserName())) {
                    requestContext.put(NhincConstants.USER_NAME_PROP, assertion.getUserInfo().getUserName());
                }
                if (assertion.getUserInfo().getOrg() != null) {
                    if (NullChecker.isNotNullish(assertion.getUserInfo().getOrg().getName())) {
                        requestContext.put(NhincConstants.USER_ORG_PROP, assertion.getUserInfo().getOrg().getName());
                    }
                } else {
                    log.error("Error: samlSendOperation input assertion user org is null");
                }
                if (assertion.getUserInfo().getRoleCoded() != null) {
                    if (NullChecker.isNotNullish(assertion.getUserInfo().getRoleCoded().getCode())) {
                        requestContext.put(NhincConstants.USER_CODE_PROP, assertion.getUserInfo().getRoleCoded().getCode());
                    }
                    if (NullChecker.isNotNullish(assertion.getUserInfo().getRoleCoded().getCodeSystem())) {
                        requestContext.put(NhincConstants.USER_SYST_PROP, assertion.getUserInfo().getRoleCoded().getCodeSystem());
                    }
                    if (NullChecker.isNotNullish(assertion.getUserInfo().getRoleCoded().getCodeSystemName())) {
                        requestContext.put(NhincConstants.USER_SYST_NAME_PROP, assertion.getUserInfo().getRoleCoded().getCodeSystemName());
                    }
                    if (NullChecker.isNotNullish(assertion.getUserInfo().getRoleCoded().getDisplayName())) {
                        requestContext.put(NhincConstants.USER_DISPLAY_PROP, assertion.getUserInfo().getRoleCoded().getDisplayName());
                    }
                } else {
                    log.error("Error: samlSendOperation input assertion user info role is null");
                }
                if (assertion.getUserInfo().getPersonName() != null) {
                    if (NullChecker.isNotNullish(assertion.getUserInfo().getPersonName().getGivenName())) {
                        requestContext.put(NhincConstants.USER_FIRST_PROP, assertion.getUserInfo().getPersonName().getGivenName());
                    }
                    if (NullChecker.isNotNullish(assertion.getUserInfo().getPersonName().getSecondNameOrInitials())) {
                        requestContext.put(NhincConstants.USER_MIDDLE_PROP, assertion.getUserInfo().getPersonName().getSecondNameOrInitials());
                    }
                    if (NullChecker.isNotNullish(assertion.getUserInfo().getPersonName().getFamilyName())) {
                        requestContext.put(NhincConstants.USER_LAST_PROP, assertion.getUserInfo().getPersonName().getFamilyName());
                    }
                } else {
                    log.error("Error: samlSendOperation input assertion user person name is null");
                }
            } else {
                log.error("Error: samlSendOperation input assertion user info is null");
            }
            if (assertion.getPurposeOfDisclosureCoded() != null) {
                if (NullChecker.isNotNullish(assertion.getPurposeOfDisclosureCoded().getCode())) {
                    requestContext.put(NhincConstants.PURPOSE_CODE_PROP, assertion.getPurposeOfDisclosureCoded().getCode());
                }
                if (NullChecker.isNotNullish(assertion.getPurposeOfDisclosureCoded().getCodeSystem())) {
                    requestContext.put(NhincConstants.PURPOSE_SYST_PROP, assertion.getPurposeOfDisclosureCoded().getCodeSystem());
                }
                if (NullChecker.isNotNullish(assertion.getPurposeOfDisclosureCoded().getCodeSystemName())) {
                    requestContext.put(NhincConstants.PURPOSE_SYST_NAME_PROP, assertion.getPurposeOfDisclosureCoded().getCodeSystemName());
                }
                if (NullChecker.isNotNullish(assertion.getPurposeOfDisclosureCoded().getDisplayName())) {
                    requestContext.put(NhincConstants.PURPOSE_DISPLAY_PROP, assertion.getPurposeOfDisclosureCoded().getDisplayName());
                }
            } else {
                log.error("Error: samlSendOperation input assertion purpose is null");
            }
        } else {
            log.error("Error: samlSendOperation input assertion is null");
        }

        log.info("Request Context:");
        Set allKeys = requestContext.keySet();
        for (Object key : allKeys) {
            log.info(key + " = " + requestContext.get(key));
        }

        log.debug("Exiting SamlTokenCreator.CreateRequestContext...");
        return requestContext;
    }
}
