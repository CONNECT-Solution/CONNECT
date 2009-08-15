/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapter.reident.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import org.hl7.v3.PRPAIN201309UV;
import org.hl7.v3.PRPAIN201310UV;

/**
 *
 * @author jhoppesc
 */
public interface AdapterReidentProxy {
    public PRPAIN201310UV getRealIdentifier(PRPAIN201309UV request, AssertionType assertion, NhinTargetCommunitiesType target);
}
