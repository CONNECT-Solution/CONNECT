/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.adaptermpi;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractorHelper;
import javax.xml.ws.WebServiceContext;
import gov.hhs.fha.nhinc.adaptercomponentmpi.AdapterComponentMpiService;
import gov.hhs.fha.nhinc.adaptercomponentmpi.AdapterComponentMpiPortType;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;

/**
 * 
 * @author dunnek
 */
public class AdapterMpiSecuredImpl {
    private static Log log = LogFactory.getLog(AdapterMpiSecuredImpl.class);
    private static final String SERVICE_NAME = "mockadaptermpi";
    private static AdapterComponentMpiService service = new AdapterComponentMpiService();

    public org.hl7.v3.PRPAIN201306UV02 findCandidates(org.hl7.v3.PRPAIN201305UV02 findCandidatesRequest,
            WebServiceContext context) {
        PRPAIN201306UV02 response = null;

        String homeCommunityId = SamlTokenExtractorHelper.getHomeCommunityId();
        if (NullChecker.isNotNullish(homeCommunityId)) {

            RespondingGatewayPRPAIN201305UV02RequestType requestMessage = new RespondingGatewayPRPAIN201305UV02RequestType();
            requestMessage.setAssertion(SamlTokenExtractor.GetAssertion(context));
            requestMessage.setPRPAIN201305UV02(findCandidatesRequest);

            AdapterComponentMpiPortType port = service.getAdapterComponentMpiPort();
            response = port.findCandidates(requestMessage);
            ((javax.xml.ws.BindingProvider) port).getRequestContext().put(
                    javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                    SamlTokenExtractorHelper.getEndpointURL(homeCommunityId, SERVICE_NAME));
            // response = port.findCandidates(findCandidatesRequest);
            // ((javax.xml.ws.BindingProvider)
            // port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
            // SamlTokenExtractorHelper.getEndpointURL(homeCommunityId, SERVICE_NAME));
        }

        return response;
    }
}
