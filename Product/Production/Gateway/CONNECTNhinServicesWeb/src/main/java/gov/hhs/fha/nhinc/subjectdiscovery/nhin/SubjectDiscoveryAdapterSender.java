/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.subjectdiscovery.nhin;

import gov.hhs.fha.nhinc.adapter.subjectdiscovery.proxy.AdapterSubjectDiscoveryProxy;
import gov.hhs.fha.nhinc.adapter.subjectdiscovery.proxy.AdapterSubjectDiscoveryProxyObjectFactory;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PIXConsumerPRPAIN201301UVRequestType;
import org.hl7.v3.PIXConsumerPRPAIN201302UVRequestType;
import org.hl7.v3.PIXConsumerPRPAIN201309UVRequestType;
import org.hl7.v3.PIXConsumerPRPAIN201309UVResponseType;
import org.hl7.v3.PRPAIN201310UV02;

/**
 *
 * @author jhoppesc
 */
public class SubjectDiscoveryAdapterSender {

    public MCCIIN000002UV01 send201301ToAgency(PIXConsumerPRPAIN201301UVRequestType request) {
        MCCIIN000002UV01 ack = new MCCIIN000002UV01();

        AdapterSubjectDiscoveryProxyObjectFactory factory = new AdapterSubjectDiscoveryProxyObjectFactory();
        AdapterSubjectDiscoveryProxy proxy = factory.getNhinSubjectDiscoveryProxy();
        ack = proxy.pixConsumerPRPAIN201301UV(request);

        return ack;
    }

    public MCCIIN000002UV01 send201302ToAgency(PIXConsumerPRPAIN201302UVRequestType request) {
        MCCIIN000002UV01 ack = new MCCIIN000002UV01();

        AdapterSubjectDiscoveryProxyObjectFactory factory = new AdapterSubjectDiscoveryProxyObjectFactory();
        AdapterSubjectDiscoveryProxy proxy = factory.getNhinSubjectDiscoveryProxy();
        ack = proxy.pixConsumerPRPAIN201302UV(request);

        return ack;
    }

    public PRPAIN201310UV02 send201309ToAgency(PIXConsumerPRPAIN201309UVRequestType request) {
        PIXConsumerPRPAIN201309UVResponseType results = new PIXConsumerPRPAIN201309UVResponseType();

        AdapterSubjectDiscoveryProxyObjectFactory factory = new AdapterSubjectDiscoveryProxyObjectFactory();
        AdapterSubjectDiscoveryProxy proxy = factory.getNhinSubjectDiscoveryProxy();
        results = proxy.pixConsumerPRPAIN201309UV(request);

        return results.getPRPAIN201310UV02();
    }
}
