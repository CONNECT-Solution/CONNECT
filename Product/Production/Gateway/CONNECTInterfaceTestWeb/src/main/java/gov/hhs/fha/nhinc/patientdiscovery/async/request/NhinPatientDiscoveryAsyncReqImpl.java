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

package gov.hhs.fha.nhinc.patientdiscovery.async.request;

import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.nhincproxypatientdiscoveryasyncreq.NhincProxyPatientDiscoveryAsyncReq;
import gov.hhs.fha.nhinc.nhincproxypatientdiscoveryasyncreq.NhincProxyPatientDiscoveryAsyncReqPortType;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractorHelper;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.ProxyPRPAIN201305UVProxyRequestType;

/**
 *
 * @author JHOPPESC
 */
public class NhinPatientDiscoveryAsyncReqImpl {
    private static Log log = LogFactory.getLog(NhinPatientDiscoveryAsyncReqImpl.class);
    private static final String SERVICE_NAME = "mockpatientdiscoveryasyncreq";

    public MCCIIN000002UV01 respondingGatewayPRPAIN201305UV02(PRPAIN201305UV02 body, WebServiceContext context) {
        MCCIIN000002UV01 response = null;
        ProxyPRPAIN201305UVProxyRequestType request = new ProxyPRPAIN201305UVProxyRequestType();

        request.setPRPAIN201305UV02(body);
        request.setAssertion(SamlTokenExtractor.GetAssertion(context));

        String homeCommunityId = null;
        if (body != null &&
                NullChecker.isNotNullish(body.getReceiver()) &&
                body.getReceiver().get(0) != null &&
                body.getReceiver().get(0).getDevice() != null &&
                body.getReceiver().get(0).getDevice().getAsAgent() != null &&
                body.getReceiver().get(0).getDevice().getAsAgent().getValue() != null &&
                body.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization() != null &&
                body.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue() != null &&
                NullChecker.isNotNullish(body.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId()) &&
                body.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0) != null &&
                NullChecker.isNotNullish(body.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot())) {
            homeCommunityId = body.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot();
        } else {
            homeCommunityId = SamlTokenExtractorHelper.getHomeCommunityId();
        }

        if (NullChecker.isNotNullish(homeCommunityId)) {
            NhincProxyPatientDiscoveryAsyncReq service = new NhincProxyPatientDiscoveryAsyncReq();
            NhincProxyPatientDiscoveryAsyncReqPortType port = service.getNhincProxyPatientDiscoveryAsyncReqPortType();
            ((javax.xml.ws.BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, SamlTokenExtractorHelper.getEndpointURL(homeCommunityId, SERVICE_NAME));

            response = port.proxyProcessPatientDiscoveryAsyncReq(request);
        } else {
            response = null;
        }
        return response;
    }

}
