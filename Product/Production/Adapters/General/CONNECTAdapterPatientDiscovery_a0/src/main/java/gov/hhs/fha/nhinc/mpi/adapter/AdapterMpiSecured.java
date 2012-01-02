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
package gov.hhs.fha.nhinc.mpi.adapter;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAIN201305UV02;

/**
 * This class is the implementation of the Secured AdapterMPI service.
 *
 * @author Sai Valluripalli, Les Westberg
 */
@WebService(serviceName = "AdapterMpiSecuredService", portName = "AdapterMpiSecuredPortType", endpointInterface = "gov.hhs.fha.nhinc.adaptermpi.AdapterMpiSecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adaptermpi", wsdlLocation = "WEB-INF/wsdl/AdapterMpiSecured/AdapterMpiSecured.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class AdapterMpiSecured
{
    @Resource
    private WebServiceContext context;

    /**
     * This method takes the input and peforms a query against the MPI via the AdapterComponentMpi services
     * and returns the response.
     *
     * @param findCandidatesRequest The query data.
     * @return The results from the MPI query.
     */
    public PRPAIN201306UV02 findCandidates(PRPAIN201305UV02 findCandidatesRequest)
    {
        AdapterMpiImpl oImpl = new AdapterMpiImpl();
        PRPAIN201306UV02 oResponse = oImpl.query(true, findCandidatesRequest, context);
        return oResponse;
    }
}
