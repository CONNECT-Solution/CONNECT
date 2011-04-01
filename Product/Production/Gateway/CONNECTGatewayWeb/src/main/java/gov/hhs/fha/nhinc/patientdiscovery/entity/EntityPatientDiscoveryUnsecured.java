/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.patientdiscovery.entity;

import javax.jws.WebService;
import javax.xml.ws.BindingType;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02ResponseType;
import javax.xml.ws.soap.Addressing;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Neil Webb
 */
@WebService(serviceName = "EntityPatientDiscovery", portName = "EntityPatientDiscoveryPortSoap", endpointInterface = "gov.hhs.fha.nhinc.entitypatientdiscovery.EntityPatientDiscoveryPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:entitypatientdiscovery", wsdlLocation = "WEB-INF/wsdl/EntityPatientDiscoveryUnsecured/EntityPatientDiscovery.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@Addressing(enabled=true)
public class EntityPatientDiscoveryUnsecured
{
    private static final Log log = LogFactory.getLog(EntityPatientDiscoveryUnsecured.class);

    protected EntityPatientDiscoveryUnsecuredImpl getEntityPatientDiscoveryUnsecuredImpl()
    {
        return new EntityPatientDiscoveryUnsecuredImpl();
    }

    public RespondingGatewayPRPAIN201306UV02ResponseType respondingGatewayPRPAIN201305UV02(RespondingGatewayPRPAIN201305UV02RequestType respondingGatewayPRPAIN201305UV02Request)
    {
        log.debug("Begin EntityPatientDiscoveryUnsecured.respondingGatewayPRPAIN201305UV02...");
        RespondingGatewayPRPAIN201306UV02ResponseType response = null;

        EntityPatientDiscoveryUnsecuredImpl impl = getEntityPatientDiscoveryUnsecuredImpl();
        if(impl != null)
        {
            response = impl.respondingGatewayPRPAIN201305UV02(respondingGatewayPRPAIN201305UV02Request);
        }
        log.debug("End EntityPatientDiscoveryUnsecured.respondingGatewayPRPAIN201305UV02...");
        return response;
    }

}
