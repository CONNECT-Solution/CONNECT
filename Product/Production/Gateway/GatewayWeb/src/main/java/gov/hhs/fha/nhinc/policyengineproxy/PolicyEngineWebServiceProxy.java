/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.policyengineproxy;

import gov.hhs.fha.nhinc.adapterpolicyengine.AdapterPolicyEngine;
import gov.hhs.fha.nhinc.adapterpolicyengine.AdapterPolicyEnginePortType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author jhoppesc
 */
public class PolicyEngineWebServiceProxy implements IPolicyEngineProxy {

    private static Log log = LogFactory.getLog(PolicyEngineWebServiceProxy.class);
    static AdapterPolicyEngine policyService = new AdapterPolicyEngine();

    public CheckPolicyResponseType checkPolicy(CheckPolicyRequestType checkPolicyRequest) {
        // Get the local home community id
        String homeCommunityId = null;
        try {
            log.info("Attempting to retrieve property: " + NhincConstants.HOME_COMMUNITY_ID_PROPERTY + " from property file: " + NhincConstants.GATEWAY_PROPERTY_FILE);
            homeCommunityId = PropertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.HOME_COMMUNITY_ID_PROPERTY);
            log.info("Retrieve local home community id: " + homeCommunityId);
        } catch (PropertyAccessException ex) {
            log.error("Error: Failed to retrieve " + NhincConstants.HOME_COMMUNITY_ID_PROPERTY + " from property file: " + NhincConstants.GATEWAY_PROPERTY_FILE);
            log.error(ex.getMessage());
        }

        // Get the URL for the Audit Logging Component at this community
        String url = null;
        if (NullChecker.isNotNullish(homeCommunityId)) {
            try {
                url = ConnectionManagerCache.getEndpointURLByServiceName(homeCommunityId, NhincConstants.POLICYENGINE_SERVICE_NAME);
            } catch (ConnectionManagerException ex) {
                log.error("Error: Failed to retrieve url for service: " + NhincConstants.POLICYENGINE_SERVICE_NAME + " for community id: " + homeCommunityId);
                log.error(ex.getMessage());
            }
        }
        
        AdapterPolicyEnginePortType port = getPort(url);

        return port.checkPolicy(checkPolicyRequest);
    }

    private AdapterPolicyEnginePortType getPort(String url) {
        AdapterPolicyEnginePortType port = null;

        if (NullChecker.isNotNullish(url)) {
            port = policyService.getAdapterPolicyEnginePortSoap11();
            gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, url);
        }

        return port;
    }
}
