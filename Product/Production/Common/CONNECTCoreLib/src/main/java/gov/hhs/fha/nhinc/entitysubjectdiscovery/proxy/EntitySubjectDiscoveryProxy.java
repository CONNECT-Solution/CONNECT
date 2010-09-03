/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.entitysubjectdiscovery.proxy;

import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PIXConsumerPRPAIN201301UVRequestType;
import org.hl7.v3.PIXConsumerPRPAIN201302UVRequestType;
import org.hl7.v3.PIXConsumerPRPAIN201304UVRequestType;
import org.hl7.v3.PIXConsumerPRPAIN201309UVResponseType;
import org.hl7.v3.PIXConsumerPRPAIN201309UVRequestType;

public interface EntitySubjectDiscoveryProxy {

    public MCCIIN000002UV01 pixConsumerPRPAIN201301UV(PIXConsumerPRPAIN201301UVRequestType body);
    public MCCIIN000002UV01 pixConsumerPRPAIN201302UV(PIXConsumerPRPAIN201302UVRequestType body);
    public MCCIIN000002UV01 pixConsumerPRPAIN201304UV(PIXConsumerPRPAIN201304UVRequestType body);
    public PIXConsumerPRPAIN201309UVResponseType pixConsumerPRPAIN201309UV(PIXConsumerPRPAIN201309UVRequestType request);

}
