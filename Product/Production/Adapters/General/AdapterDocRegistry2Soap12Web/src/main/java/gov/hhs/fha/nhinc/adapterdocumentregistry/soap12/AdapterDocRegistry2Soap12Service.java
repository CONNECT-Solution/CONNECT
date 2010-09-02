/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.adapterdocumentregistry.soap12;

import gov.hhs.fha.nhinc.adapterdocregistry.AdapterDocRegistry2Soap12Client;
import ihe.iti.xds_b._2007.DocumentRegistryPortType;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This web service serves as a SOAP1.1->SOAP1.2 Adapter for transforming SOAP1.1 requests initiated
 * by CONNECT's adapters, prior to forwarding it to a SOAP 1.2 registry.
 *
 * @author Anand Sastry
 */
@WebService(serviceName = "DocumentRegistry_Service", portName = "DocumentRegistry_Port_Soap", endpointInterface = "ihe.iti.xds_b._2007.DocumentRegistryPortType", targetNamespace = "urn:ihe:iti:xds-b:2007", wsdlLocation = "WEB-INF/wsdl/AdapterDocRegistry2Soap12Service/AdapterComponentDocRegistry.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class AdapterDocRegistry2Soap12Service implements DocumentRegistryPortType {

    private static Log log = LogFactory.getLog(AdapterDocRegistry2Soap12Service.class);

    public oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType documentRegistryRegisterDocumentSetB(oasis.names.tc.ebxml_regrep.xsd.lcm._3.SubmitObjectsRequest body) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

     public oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse documentRegistryRegistryStoredQuery(oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest body) {
        log.debug("Entering AdapterDocRegistry2Soap12Service.documentRegistryRegistryStoredQuery() method");

        oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse response = null;

        try
        {
            if (body != null)
            {
                log.debug("Input request message was not null");

                AdapterDocRegistry2Soap12Client oClient = new AdapterDocRegistry2Soap12Client();

                response = oClient.documentRegistryRegistryStoredQuery(body);

    }
            else
            {
                String sErrorMessage = "Input request message was null.";
                log.error(sErrorMessage);
                throw new RuntimeException(sErrorMessage);
            }
        }
        catch(Exception exp)
        {
            log.error(exp.getMessage());
            exp.printStackTrace();
        }

        log.debug("Leaving AdapterDocRegistry2Soap12Service.documentRegistryRegistryStoredQuery() method");
        return response;
    }

    public org.hl7.v3.MCCIIN000002UV01 documentRegistryPRPAIN201301UV02(org.hl7.v3.PRPAIN201301UV02 body) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public org.hl7.v3.MCCIIN000002UV01 documentRegistryPRPAIN201302UV02(org.hl7.v3.PRPAIN201302UV02 body) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public org.hl7.v3.MCCIIN000002UV01 documentRegistryPRPAIN201304UV02(org.hl7.v3.PRPAIN201304UV02 body) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

}
