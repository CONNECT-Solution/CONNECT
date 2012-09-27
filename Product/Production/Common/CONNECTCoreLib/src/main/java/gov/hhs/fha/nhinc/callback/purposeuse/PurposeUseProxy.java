/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.callback.purposeuse;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author rhalfert
 */
public interface PurposeUseProxy {
    public boolean createPurposeUseElement(Map<Object, Object> tokenVals);
}
