/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
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
public class NhinSubjectDiscoveryNoOpImpl implements NhinSubjectDiscoveryProxy {

    public MCCIIN000002UV01 pixConsumerPRPAIN201301UV(PRPAIN201301UV02 body, AssertionType assertion, NhinTargetSystemType target) {
        return new MCCIIN000002UV01();
    }

    public MCCIIN000002UV01 pixConsumerPRPAIN201302UV(PRPAIN201302UV02 body, AssertionType assertion, NhinTargetSystemType target) {
        return new MCCIIN000002UV01();
    }

    public MCCIIN000002UV01 pixConsumerPRPAIN201304UV(PRPAIN201304UV02 body, AssertionType assertion, NhinTargetSystemType target) {
        return new MCCIIN000002UV01();
    }

    public PRPAIN201310UV02 pixConsumerPRPAIN201309UV(PRPAIN201309UV02 body, AssertionType assertion, NhinTargetSystemType target) {
        return new PRPAIN201310UV02();
    }

}
