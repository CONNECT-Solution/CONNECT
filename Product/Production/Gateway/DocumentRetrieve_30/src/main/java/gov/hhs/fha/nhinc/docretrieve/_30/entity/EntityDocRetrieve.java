/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 * 
 */
package gov.hhs.fha.nhinc.docretrieve._30.entity;

import javax.xml.ws.BindingType;
import javax.xml.ws.soap.Addressing;

/**
 * 
 * @author dunnek
 */

@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@Addressing(enabled = true)
public class EntityDocRetrieve implements gov.hhs.fha.nhinc.entitydocretrieve.EntityDocRetrievePortType {

    public ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(
            gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayRetrieveRequestType respondingGatewayCrossGatewayRetrieveRequest) {
        return getImpl().respondingGatewayCrossGatewayRetrieve(
                respondingGatewayCrossGatewayRetrieveRequest.getRetrieveDocumentSetRequest(),
                respondingGatewayCrossGatewayRetrieveRequest.getAssertion());
    }

    protected EntityDocRetreiveImpl getImpl() {
        return new EntityDocRetreiveImpl();
    }
}
