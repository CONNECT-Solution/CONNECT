/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docquery.adapter;

import javax.annotation.Resource;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 * 
 * @author Sai Valluripalli
 */

@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class AdapterDocQuerySecured implements gov.hhs.fha.nhinc.adapterdocquerysecured.AdapterDocQuerySecuredPortType {

    @Resource
    private WebServiceContext context;

    public oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse respondingGatewayCrossGatewayQuery(
            oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest body) {
        return new AdapterDocQueryImpl().respondingGatewayCrossGatewayQuery(body, context);
    }

}
