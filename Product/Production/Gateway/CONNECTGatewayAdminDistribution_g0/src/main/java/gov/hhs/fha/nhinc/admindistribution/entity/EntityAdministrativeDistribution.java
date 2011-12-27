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

package gov.hhs.fha.nhinc.admindistribution.entity;

import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.soap.Addressing;

/**
 *
 * @author dunnek
 */
@WebService(serviceName = "AdministrativeDistribution_Service", portName = "AdministrativeDistribution_PortType", endpointInterface = "gov.hhs.fha.nhinc.entityadmindistribution.AdministrativeDistributionPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:entityadmindistribution", wsdlLocation = "WEB-INF/wsdl/EntityAdministrativeDistribution/EntityAdminDist.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@Addressing(enabled=true)
public class EntityAdministrativeDistribution {

    public void sendAlertMessage(gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewaySendAlertMessageType body) {
        //TODO implement this method
        getEntityImpl().sendAlertMessage(body, body.getAssertion(), body.getNhinTargetCommunities());
    }
    protected EntityAdminDistributionOrchImpl getEntityImpl()
    {
        return new EntityAdminDistributionOrchImpl();
    }
}
