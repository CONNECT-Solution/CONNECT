/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapter.reident.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import org.hl7.v3.PRPAIN201309UV02;
import org.hl7.v3.PRPAIN201310UV02;

/**
 *
 * @author jhoppesc
 */
public class AdapterReidentProxyNoOpImpl implements AdapterReidentProxy {

    public PRPAIN201310UV02 getRealIdentifier(PRPAIN201309UV02 request, AssertionType assertion, NhinTargetCommunitiesType target) {
        return new PRPAIN201310UV02();
    }

}
