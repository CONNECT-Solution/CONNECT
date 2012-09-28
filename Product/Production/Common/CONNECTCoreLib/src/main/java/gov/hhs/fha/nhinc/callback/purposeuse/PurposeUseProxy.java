/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.callback.purposeuse;

import gov.hhs.fha.nhinc.callback.openSAML.CallbackProperties;

/**
 * 
 * @author rhalfert
 */
public interface PurposeUseProxy {
    
    /**
     * Test whether "purposeFor" syntax should be used instead of "purposeOf" based on configuration.
     * @param callbackProperties used to pull information about the target endpoint.
     * @return true if "purposeFor" syntax should be used instead of "purposeOf"
     */
    public boolean isPurposeForUseEnabled(CallbackProperties callbackProperties);

}
