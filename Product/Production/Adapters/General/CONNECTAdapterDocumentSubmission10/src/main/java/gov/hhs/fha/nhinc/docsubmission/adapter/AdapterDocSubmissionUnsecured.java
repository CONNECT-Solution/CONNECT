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

package gov.hhs.fha.nhinc.docsubmission.adapter;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;

/**
 *
 * @author JHOPPESC
 */
@WebService(serviceName = "AdapterXDR_Service", portName = "AdapterXDR_Port", endpointInterface = "gov.hhs.fha.nhinc.adapterxdr.AdapterXDRPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterxdr", wsdlLocation = "WEB-INF/wsdl/AdapterDocSubmissionUnsecured/AdapterXDR.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@Addressing(enabled=true)
public class AdapterDocSubmissionUnsecured {
    @Resource
    private WebServiceContext context;

    public oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType provideAndRegisterDocumentSetb(gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterProvideAndRegisterDocumentSetRequestType body) {
        return new AdapterDocSubmissionImpl().provideAndRegisterDocumentSetb(body, context);
    }

}
