/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.nhinsubjectdiscovery.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201301UV;
import org.hl7.v3.PRPAIN201302UV;
import org.hl7.v3.PRPAIN201303UV;
import org.hl7.v3.PRPAIN201304UV;
import org.hl7.v3.PRPAIN201309UV;
import org.hl7.v3.PRPAIN201310UV;

/**
 *
 * @author Jon Hoppesch
 */
public class NhinSubjectDiscoveryNoOpImpl implements NhinSubjectDiscoveryProxy {

    public MCCIIN000002UV01 pixConsumerPRPAIN201301UV(PRPAIN201301UV body, AssertionType assertion, NhinTargetSystemType target) {
        return new MCCIIN000002UV01();
    }

    public MCCIIN000002UV01 pixConsumerPRPAIN201302UV(PRPAIN201302UV body, AssertionType assertion, NhinTargetSystemType target) {
        return new MCCIIN000002UV01();
    }

    public MCCIIN000002UV01 pixConsumerPRPAIN201303UV(PRPAIN201303UV body, AssertionType assertion, NhinTargetSystemType target) {
        return new MCCIIN000002UV01();
    }

    public MCCIIN000002UV01 pixConsumerPRPAIN201304UV(PRPAIN201304UV body, AssertionType assertion, NhinTargetSystemType target) {
        return new MCCIIN000002UV01();
    }

    public PRPAIN201310UV pixConsumerPRPAIN201309UV(PRPAIN201309UV body, AssertionType assertion, NhinTargetSystemType target) {
        return new PRPAIN201310UV();
    }

}
