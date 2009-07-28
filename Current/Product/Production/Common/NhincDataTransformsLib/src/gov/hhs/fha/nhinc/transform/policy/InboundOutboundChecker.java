/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.transform.policy;

import gov.hhs.fha.nhinc.nhinclib.NullChecker;

/**
 *
 * @author rayj
 */
public class InboundOutboundChecker {

    public static boolean IsInbound(String value) {
        boolean result = false;
        if (NullChecker.isNotNullish(value)) {
            result = value.equalsIgnoreCase("inbound");
        }
        return result;
    }
    public static boolean IsOutbound(String value) {
        boolean result = false;
        if (NullChecker.isNotNullish(value)) {
            result = value.equalsIgnoreCase("outbound");
        }
        return result;
    }
}
