/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.corex12.docsubmission.genericbatch.request.nhin.proxy;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.proxy.ComponentProxyObjectFactory;

/**
 *
 * @author svalluripalli
 */
public class NhinCORE_X12DGenericBatchRequestProxyObjectFactory extends ComponentProxyObjectFactory {

    private static final String CONFIG_FILE_NAME = NhincConstants.CORE_X12DS_GENERICBATCH_PROXY_CONFIG_FILE_NAME;
    private static final String BEAN_NAME = "nhincore_x12dsgenericbatchrequest";

    @Override
    protected String getConfigFileName() {
        return CONFIG_FILE_NAME;
    }

    /**
     *
     * @return NhinCORE_X12DSGenericBatchRequestProxy
     */
    public NhinCORE_X12DSGenericBatchRequestProxy getNhinCORE_X12DSGenericBatchRequestProxy() {
        return getBean(BEAN_NAME, NhinCORE_X12DSGenericBatchRequestProxy.class);
    }
}
