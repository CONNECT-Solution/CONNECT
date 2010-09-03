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

package gov.hhs.fha.nhinc.fta;

import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.xml.ws.BindingType;

/**
 *
 * @author svalluripalli
 */
@WebService(serviceName = "AdapterNotificationConsumerSecured", portName = "AdapterNotificationConsumerPortSecureType", endpointInterface = "gov.hhs.fha.nhinc.adapternotificationconsumersecured.AdapterNotificationConsumerPortSecureType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapternotificationconsumersecured", wsdlLocation = "META-INF/wsdl/AdapterFTAServiceSecured/AdapterNotificationConsumerSecured.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@Stateless
public class AdapterFTAServiceSecured {

    public gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType notifySubscribersOfDocument(gov.hhs.fha.nhinc.common.nhinccommonadapter.NotifySubscribersOfDocumentRequestSecuredType notifySubscribersOfDocumentRequestSecured) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType notify(org.oasis_open.docs.wsn.b_2.Notify notifyRequestSecured) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

}
