/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.policyengine.proxy;

import gov.hhs.fha.nhinc.adapterpolicyengine.AdapterPolicyEngine;
import gov.hhs.fha.nhinc.adapterpolicyengine.AdapterPolicyEnginePortType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Jon Hoppesch
 */
public class PolicyEngineWebServiceProxy implements PolicyEngineProxy {

    private static Log log = LogFactory.getLog(PolicyEngineWebServiceProxy.class);
    static AdapterPolicyEngine policyService = new AdapterPolicyEngine();

    public CheckPolicyResponseType checkPolicy(CheckPolicyRequestType checkPolicyRequest) {
        // Get the URL for the Audit Logging Component at this community
        String url = null;
        try {
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.POLICYENGINE_SERVICE_NAME);
        } catch (ConnectionManagerException ex) {
            log.error("Error: Failed to retrieve url for service: " + NhincConstants.POLICYENGINE_SERVICE_NAME + " for local home community");
            log.error(ex.getMessage());
        }

        AdapterPolicyEnginePortType port = getPort(url);
        log.debug("####### Sending policy request to secure service");
        return port.checkPolicy(checkPolicyRequest);
    }

    private AdapterPolicyEnginePortType getPort(String url) {
        AdapterPolicyEnginePortType port = policyService.getAdapterPolicyEnginePortSoap11();

        if (NullChecker.isNotNullish(url)) {
            gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, url);
        }

        return port;
    }
}
