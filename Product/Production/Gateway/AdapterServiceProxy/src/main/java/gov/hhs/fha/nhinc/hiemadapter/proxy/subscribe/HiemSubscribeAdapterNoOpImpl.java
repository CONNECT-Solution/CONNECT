/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.hiemadapter.proxy.subscribe;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import org.w3c.dom.Element;

/**
 *
 * @author Jon Hoppesch
 */
public class HiemSubscribeAdapterNoOpImpl implements HiemSubscribeAdapterProxy {
    public Element subscribe(Element subscribe, AssertionType assertion, NhinTargetSystemType target) throws Exception {
        return null;
    }
}
