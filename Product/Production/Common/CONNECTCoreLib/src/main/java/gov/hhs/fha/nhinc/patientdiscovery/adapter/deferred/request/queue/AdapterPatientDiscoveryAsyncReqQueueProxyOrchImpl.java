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
package gov.hhs.fha.nhinc.patientdiscovery.adapter.deferred.request.queue;

import gov.hhs.fha.nhinc.transform.subdisc.HL7AckTransforms;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;

/**
 * This class contains the business logic for the AdapterPatientDiscoveryAsyncReqQueue.
 * Currently it is assumed that this will be overridden by the adapter implementation.
 * All this does is send back an ACK.
 *
 * @author westberg
 */
public class AdapterPatientDiscoveryAsyncReqQueueProxyOrchImpl
{

    /**
     * This simulates the patient discovery async request to being added to a queue.
     * @param request The request message.
     * @return The ACK.
     */
    public MCCIIN000002UV01 addPatientDiscoveryAsyncReq(RespondingGatewayPRPAIN201305UV02RequestType request)
    {
        MCCIIN000002UV01 ack = new MCCIIN000002UV01();

        String msgText = "Success";

        ack = HL7AckTransforms.createAckFrom201305(request.getPRPAIN201305UV02(), msgText);

        return ack;
    }
}
