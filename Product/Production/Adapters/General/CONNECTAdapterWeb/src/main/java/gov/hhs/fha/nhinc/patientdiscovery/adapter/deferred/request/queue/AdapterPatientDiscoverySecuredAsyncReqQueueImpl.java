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

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.fha.nhinc.transform.subdisc.HL7AckTransforms;
import javax.xml.ws.WebServiceContext;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02SecuredRequestType;

/**
 *
 * @author JHOPPESC
 */
public class AdapterPatientDiscoverySecuredAsyncReqQueueImpl
{

    public MCCIIN000002UV01 addPatientDiscoveryAsyncReqSecured(RespondingGatewayPRPAIN201305UV02SecuredRequestType request, WebServiceContext context)
    {
        RespondingGatewayPRPAIN201305UV02RequestType unsecureRequest = new RespondingGatewayPRPAIN201305UV02RequestType();
        unsecureRequest.setNhinTargetCommunities(request.getNhinTargetCommunities());
        unsecureRequest.setPRPAIN201305UV02(request.getPRPAIN201305UV02());
        unsecureRequest.setAssertion(SamlTokenExtractor.GetAssertion(context));

        // Extract the message id value from the WS-Addressing Header and place it in the Assertion Class
        if (request != null &&
                unsecureRequest.getAssertion() != null)
        {
            AsyncMessageIdExtractor msgIdExtractor = new AsyncMessageIdExtractor();
            unsecureRequest.getAssertion().setMessageId(msgIdExtractor.GetAsyncMessageId(context));
        }

        MCCIIN000002UV01 ack = addPatientDiscoveryAsyncReq(unsecureRequest);

        return ack;
    }

    public MCCIIN000002UV01 addPatientDiscoveryAsyncReqUnsecured(RespondingGatewayPRPAIN201305UV02RequestType request, WebServiceContext context)
    {
        return addPatientDiscoveryAsyncReq(request);
    }

    public MCCIIN000002UV01 addPatientDiscoveryAsyncReq(RespondingGatewayPRPAIN201305UV02RequestType request)
    {
        AdapterPatientDiscoveryAsyncReqQueueProxyOrchImpl oOrchestrator = new AdapterPatientDiscoveryAsyncReqQueueProxyOrchImpl();
        return oOrchestrator.addPatientDiscoveryAsyncReq(request);
    }
}
