/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.callback.purposeuse;

import java.util.HashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 *
 * @author rhalfert
 */
public class PurposeUseProxyDefaultImpl extends PurposeUseProxyBaseImpl {

    private static Log log = LogFactory.getLog(PurposeUseProxyDefaultImpl.class);

    /**
     * Returns boolean if purposeForUse should be used based on gateway.properties.
     * @param tokens A list of tokens to be used to make this decision.
     * @return true if purposeForUse should be used.
     */
    public boolean createPurposeUseElement(HashMap<Object, Object> tokens) {
        log.info("Entering PurposeUseProxyDefaultImpl.createPurposeUseElement...");

        return (isPurposeForUseEnabled());
    }

}
