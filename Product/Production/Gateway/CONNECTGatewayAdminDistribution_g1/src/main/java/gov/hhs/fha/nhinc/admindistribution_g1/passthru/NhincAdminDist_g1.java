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

package gov.hhs.fha.nhinc.admindistribution_g1.passthru;

import gov.hhs.fha.nhinc.admindistribution.passthru.*;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import javax.jws.WebService;
import javax.xml.ws.BindingType;

/**
 *
 * @author dunnek
 */
@WebService(serviceName = "NhincAdminDistService", portName = "NhincAdminDist_PortType", endpointInterface = "gov.hhs.fha.nhinc.nhincadmindistribution.NhincAdminDistPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincadmindistribution", wsdlLocation = "WEB-INF/wsdl/NhincProxyAdminDist/NhincAdminDist_g1.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class NhincAdminDist_g1 {

    public void sendAlertMessage(gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewaySendAlertMessageType body) {
            getNhincImpl().sendAlertMessage(body.getEDXLDistribution(),body.getAssertion(), body.getNhinTargetSystem(),
                NhincConstants.GATEWAY_API_LEVEL.LEVEL_g1);
    }
    public PassthruAdminDistributionOrchImpl getNhincImpl()
    {
        return new PassthruAdminDistributionOrchImpl();
    }
}
