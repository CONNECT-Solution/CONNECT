/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.docretrieve.adapter.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;

/**
 *
 * @author Naresh Subramanyan
 */
public abstract class BaseAdapterDocRetrieveProxy implements AdapterDocRetrieveProxy {

    private WebServiceProxyHelper oProxyHelper = new WebServiceProxyHelper();

    /**
     * This method returns the URL endpoint based on the ImplementsSpecVersion
     *
     * @param assertion Assertion received.
     * @param serviceName
     * @return The endpoint URL.
     * @throws ConnectionManagerException A ConnectionManagerException if one occurs.
     */
    String getEndPointFromConnectionManagerByAdapterAPILevel(AssertionType assertion, String serviceName) throws ConnectionManagerException {
        String url = null;
        //get the Implments Spec version from the assertion
        if ((assertion != null) && (assertion.getImplementsSpecVersion() != null)) {
            //if the implementationSpecVersion is for 2010 then look for adapter a0 else a1
            if (assertion.getImplementsSpecVersion().equals(NhincConstants.UDDI_SPEC_VERSION.SPEC_2_0.toString())) {
                //loop for ADAPTER_API_LEVEL a0 if not found then look for a1
                url = getoProxyHelper().getEndPointFromConnectionManagerByAdapterAPILevel(serviceName, NhincConstants.ADAPTER_API_LEVEL.LEVEL_a0);
                if (NullChecker.isNullish(url)) {
                    url = getoProxyHelper().getEndPointFromConnectionManagerByAdapterAPILevel(serviceName, NhincConstants.ADAPTER_API_LEVEL.LEVEL_a1);
                }
            } //if the implementationSpecVersion is for 2011 then look for adapter a1 else a0
            else if (assertion.getImplementsSpecVersion().equals(NhincConstants.UDDI_SPEC_VERSION.SPEC_3_0.toString())) {
                //look for ADAPTER_API_LEVEL a1 if not found then look for a0
                url = getoProxyHelper().getEndPointFromConnectionManagerByAdapterAPILevel(serviceName, NhincConstants.ADAPTER_API_LEVEL.LEVEL_a1);
                if (NullChecker.isNullish(url)) {
                    url = getoProxyHelper().getEndPointFromConnectionManagerByAdapterAPILevel(serviceName, NhincConstants.ADAPTER_API_LEVEL.LEVEL_a0);
                }
            }
        } else { //If the preferred API level is not configured, then return which ever one is available
            url = getoProxyHelper().getAdapterEndPointFromConnectionManager(serviceName);
        }
        return url;
    }

    /**
     * @return the oProxyHelper
     */
    final WebServiceProxyHelper getoProxyHelper() {
        return oProxyHelper;
    }

    /**
     * @param oProxyHelper the oProxyHelper to set
     */
    final void setoProxyHelper(WebServiceProxyHelper oProxyHelper) {
        this.oProxyHelper = oProxyHelper;
    }
}
