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

package gov.hhs.fha.nhinc.admindistribution.adapter;

import javax.jws.WebService;
import javax.xml.ws.BindingType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
/**
 *
 * @author dunnek
 */
@WebService(serviceName = "Adapter_AdministrativeDistribution", portName = "Adapter_AdministrativeDistribution_PortType", endpointInterface = "gov.hhs.fha.nhinc.adapteradmindistribution.AdapterAdministrativeDistributionPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapteradmindistribution", wsdlLocation = "WEB-INF/wsdl/AdapterAdministrativeDistribution/AdapterAdminDist.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class AdapterAdministrativeDistribution {
     @Resource
    private WebServiceContext context;

    public void sendAlertMessage(gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewaySendAlertMessageType body) {
        getImpl().sendAlertMessage(body.getEDXLDistribution(),body.getAssertion());
    }
    protected AdapterAdminDistributionOrchImpl getImpl()
    {
        return new AdapterAdminDistributionOrchImpl();
    }
}
