/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.fta;

import gov.hhs.fha.nhinc.adaptersubscriptionmanagement.AdapterNotificationProducerPortType;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis_open.docs.wsn.b_2.SubscribeResponse;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "AdapterNotificationProducer", portName = "AdapterNotificationProducerPortSoap", endpointInterface = "gov.hhs.fha.nhinc.adaptersubscriptionmanagement.AdapterNotificationProducerPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adaptersubscriptionmanagement", wsdlLocation = "META-INF/wsdl/AdapterSubcriptionManagement/AdapterSubscriptionManagement.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@Stateless
public class AdapterSubcriptionManagement implements AdapterNotificationProducerPortType {

    private static Log log = LogFactory.getLog(AdapterSubcriptionManagement.class);

    public org.oasis_open.docs.wsn.b_2.SubscribeResponse subscribe(gov.hhs.fha.nhinc.common.nhinccommonadapter.SubscribeRequestType subscribeRequest) {
        SubscribeResponse response = new SubscribeResponse();

        log.info("Received Subscribe Request: " + subscribeRequest);


        return response;

    }

    public org.oasis_open.docs.wsn.b_2.SubscribeResponse subscribeDocument(gov.hhs.fha.nhinc.common.nhinccommonadapter.SubscribeDocumentRequestType subscribeDocumentRequest) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public org.oasis_open.docs.wsn.b_2.SubscribeResponse subscribeCdcBioPackage(gov.hhs.fha.nhinc.common.nhinccommonadapter.SubscribeCdcBioPackageRequestType subscribeCdcBioPackageRequest) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

}
