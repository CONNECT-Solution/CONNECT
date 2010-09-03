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
package gov.hhs.fha.nhinc.hiem.dte;

import org.w3._2005._08.addressing.EndpointReferenceType;

/**
 *
 * @author rayj
 */
public class ConsumerReferenceHelper {

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(ConsumerReferenceHelper.class);

    public EndpointReferenceType createConsumerReferenceEndpointReference(String consumerReferenceAddress) {
        EndpointReferenceHelper helper = new EndpointReferenceHelper();
        EndpointReferenceType endpointReference = helper.createEndpointReferenceAddressOnly(consumerReferenceAddress);
        return endpointReference;
    }
}
