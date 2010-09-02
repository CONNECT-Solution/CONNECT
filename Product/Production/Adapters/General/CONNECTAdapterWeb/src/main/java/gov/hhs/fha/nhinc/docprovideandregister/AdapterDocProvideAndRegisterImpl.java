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

package gov.hhs.fha.nhinc.docprovideandregister;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayProvideAndRegisterDocumentSetRequestRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayProvideAndRegisterDocumentSetRequestResponseType;
import gov.hhs.fha.nhinc.docrepositoryadapter.proxy.AdapterDocumentRepositoryProxy;
import gov.hhs.fha.nhinc.docrepositoryadapter.proxy.AdapterDocumentRepositoryProxyObjectFactory;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

/**
 *
 * @author svalluripalli
 */
public class AdapterDocProvideAndRegisterImpl {
    public RespondingGatewayCrossGatewayProvideAndRegisterDocumentSetRequestResponseType adapterDocProvideAndRegisterOperation(RespondingGatewayCrossGatewayProvideAndRegisterDocumentSetRequestRequestType part1)
    {
        RespondingGatewayCrossGatewayProvideAndRegisterDocumentSetRequestResponseType res =
                    new RespondingGatewayCrossGatewayProvideAndRegisterDocumentSetRequestResponseType();
        if(part1 != null)
        {
            AdapterDocumentRepositoryProxyObjectFactory objFactory = new AdapterDocumentRepositoryProxyObjectFactory();
            AdapterDocumentRepositoryProxy registryProxy = objFactory.getAdapterDocumentRepositoryProxy();
            RegistryResponseType response = registryProxy.provideAndRegisterDocumentSet(part1.getProvideAndRegisterDocumentSetRequest());
            res.setRegistryResponse(response);
        }
        return res;
    }
}
