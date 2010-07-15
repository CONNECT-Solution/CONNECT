/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.nhinsubjectdiscovery.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201301UV02;
import org.hl7.v3.PRPAIN201302UV02;
import org.hl7.v3.PRPAIN201304UV02;
import org.hl7.v3.PRPAIN201309UV02;
import org.hl7.v3.PRPAIN201310UV02;

/**
 *
 * @author Jon Hoppesch
 */
public interface NhinSubjectDiscoveryProxy {

    public MCCIIN000002UV01 pixConsumerPRPAIN201301UV(PRPAIN201301UV02 request, AssertionType assertion, NhinTargetSystemType target);

    public MCCIIN000002UV01 pixConsumerPRPAIN201302UV(PRPAIN201302UV02 request, AssertionType assertion, NhinTargetSystemType target);

    public MCCIIN000002UV01 pixConsumerPRPAIN201304UV(PRPAIN201304UV02 request, AssertionType assertion, NhinTargetSystemType target);

    public PRPAIN201310UV02 pixConsumerPRPAIN201309UV(PRPAIN201309UV02 request, AssertionType assertion, NhinTargetSystemType target);

}
