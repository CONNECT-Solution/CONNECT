/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 * 
 */
package gov.hhs.fha.nhinc.docretrieve._30.passthru;

import gov.hhs.fha.nhinc.docretrieve.passthru.NhincProxyDocRetrieveOrchImpl;

import javax.annotation.Resource;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;

/**
 * 
 * @author Neil Webb
 */

@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@Addressing(enabled = true)
public class NhincProxyDocRetrieveUnsecured implements gov.hhs.fha.nhinc.nhincproxydocretrieve.NhincProxyDocRetrievePortType {
    
    private NhincProxyDocRetrieveOrchImpl orchImpl;
    
    @Resource
    private WebServiceContext context;

    public ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(
            gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveRequestType respondingGatewayCrossGatewayRetrieveRequest) {
        return new NhincProxyDocRetrieveImpl(orchImpl).respondingGatewayCrossGatewayRetrieve(
                respondingGatewayCrossGatewayRetrieveRequest, context);
    }
    
    public void setOrchestratorImpl(NhincProxyDocRetrieveOrchImpl orchImpl) {
        this.orchImpl = orchImpl;
    }

}
