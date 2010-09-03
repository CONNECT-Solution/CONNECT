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
package gov.hhs.fha.nhinc.unsubscribe;

import com.sun.xml.ws.developer.WSBindingProvider;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommoninternalorch.UnsubscribeRequestType;
import gov.hhs.fha.nhinc.common.subscription.ReferenceParameterType;
import gov.hhs.fha.nhinc.common.subscription.ReferenceParametersType;
import gov.hhs.fha.nhinc.common.subscription.SubscriptionReferenceType;
import gov.hhs.fha.nhinc.common.subscription.UnsubscribeType;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractorHelper;
import java.util.List;
import java.util.Map;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis_open.docs.wsn.b_2.Unsubscribe;
import org.oasis_open.docs.wsn.b_2.UnsubscribeResponse;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.data.CMBindingTemplate;
import gov.hhs.fha.nhinc.connectmgr.data.CMBusinessEntity;
import gov.hhs.fha.nhinc.connectmgr.data.CMBusinessService;
import gov.hhs.fha.nhinc.connectmgr.data.CMBusinessServices;
import gov.hhs.fha.nhinc.hiem.consumerreference.ReferenceParametersElements;
import gov.hhs.fha.nhinc.hiem.consumerreference.ReferenceParametersHelper;
import gov.hhs.fha.nhinc.hiem.dte.SoapUtil;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;

/**
 *
 * @author jhoppesc
 */
public class HiemUnsubscribeImpl {

    private static Log log = LogFactory.getLog(HiemUnsubscribeImpl.class);
    private static final String SERVICE_NAME = "mocksubscriptionmanager";

    public UnsubscribeResponse unsubscribe(Unsubscribe unsubscribeRequest, WebServiceContext context) {
        log.debug("Entering HiemUnsubscribeImpl.unsubscribe");

        // Response object handle
        UnsubscribeResponse resp = null;

        // Build the unsubscribe request for the internal unsubscribe process
        UnsubscribeRequestType request = new UnsubscribeRequestType();
        UnsubscribeType unsubscribeType = new UnsubscribeType();

        request.setUnsubscribe(unsubscribeType);
        AssertionType assertion = SamlTokenExtractor.GetAssertion(context);
        request.setAssertion(assertion);

        log.debug("extracting reference parameters from soap header");
        ReferenceParametersHelper referenceParametersHelper = new ReferenceParametersHelper();
        ReferenceParametersElements referenceParametersElements = referenceParametersHelper.createReferenceParameterElements(context, NhincConstants.HTTP_REQUEST_ATTRIBUTE_SOAPMESSAGE);
        log.debug("extracted reference parameters from soap header");

        // Collect info and create the subscription reference
//            SubscriptionReferenceType subscriptionReference = createSubscriptionReference(homeCommunityId, context);
//            unsubscribeType.setSubscriptionReference(subscriptionReference);

        // Call the internal unsubscribe process
        resp = callInternalUnsubscribe(request, referenceParametersElements);

        log.debug("Exiting HiemUnsubscribeImpl.unsubscribe");

        return resp;
    }

    private String collectSubscriptionId(WebServiceContext context) {
        String subscriptionId = null;
        try {
            if (context != null) {
                log.debug("***###@@@ Web service context object type: " + context.getClass().getName());
                MessageContext msgContext = context.getMessageContext();
                if (msgContext != null) {
                    @SuppressWarnings("unchecked")
                    Map<String, List<String>> msgCtxMap = (Map<String, List<String>>) msgContext.get(MessageContext.HTTP_REQUEST_HEADERS);
                    if (msgCtxMap != null) {
                        for (String key : msgCtxMap.keySet()) {
                            if ("Subscriptionid".equalsIgnoreCase(key)) {
                                List<String> values = msgCtxMap.get(key);
                                if ((values != null) && (!values.isEmpty())) {
                                    subscriptionId = values.get(0);
                                    log.debug("Collected subscriptionid: " + subscriptionId);
                                    break;
                                }
                            }
                        }
                    }
                } else {
                    log.debug("Message context was null.");
                }
            } else {
                log.debug("@@@ Web service context was null.");
            }
        } catch (Throwable t) {
            t.printStackTrace();
            log.debug("Error printing headers: " + t.getMessage());
        }
        return subscriptionId;
    }

    private SubscriptionReferenceType createSubscriptionReference(String homeCommunityId, WebServiceContext context) {
        SubscriptionReferenceType subscriptionReference = new SubscriptionReferenceType();

        String subscriptionId = collectSubscriptionId(context);
        if (NullChecker.isNotNullish(subscriptionId)) {
            ReferenceParameterType refParam = new ReferenceParameterType();
            refParam.setElementName("SubscriptionId");
            refParam.setNamespace("http://www.hhs.gov/healthit/nhin");
            refParam.setPrefix("nhin");
            refParam.setValue(subscriptionId);

            ReferenceParametersType refParamsType = new ReferenceParametersType();
            subscriptionReference.setReferenceParameters(refParamsType);
            refParamsType.getReferenceParameter().add(refParam);
        }

        subscriptionReference.setSubscriptionManagerEndpointAddress(getEndpointUrl(homeCommunityId, "subscriptionmanager"));

        return subscriptionReference;
    }

    private String getEndpointUrl(String homeCommunityId, String serviceName) {
        String endpointUrl = null;
        try {
            CMBusinessEntity businessEntity = ConnectionManagerCache.getBusinessEntityByServiceName(homeCommunityId, serviceName);
            if (businessEntity != null) {
                CMBusinessServices businessServices = businessEntity.getBusinessServices();
                if ((businessServices != null) && (businessServices.getBusinessService() != null)) {
                    for (CMBusinessService businessService : businessServices.getBusinessService()) {
                        if ((businessService.getBindingTemplates() != null) && (businessService.getBindingTemplates().getBindingTemplate() != null)) {
                            for (CMBindingTemplate bindingTemplate : businessService.getBindingTemplates().getBindingTemplate()) {
                                endpointUrl = bindingTemplate.getEndpointURL();
                                break;
                            }
                            break;
                        } else {
                            log.debug("No binding templates found for community '" + homeCommunityId + "' and service name '" + serviceName + "'.");
                        }
                    }
                } else {
                    log.debug("No business services found for community '" + homeCommunityId + "' and service name '" + serviceName + "'.");
                }
            }
        } catch (Throwable t) {
            log.error("Error collecting endpoint url for community '" + homeCommunityId + "' and service name '" + serviceName + "': " + t.getMessage(), t);
        }
        return endpointUrl;
    }

    private org.oasis_open.docs.wsn.b_2.UnsubscribeResponse callInternalUnsubscribe(UnsubscribeRequestType request, ReferenceParametersElements referenceParametersElements) {
        org.oasis_open.docs.wsn.b_2.UnsubscribeResponse result = null;
        try { // Call Web Service Operation
            log.debug("sending unsubscribe from test helper to adapter");

            String url = ConnectionManagerCache.getLocalEndpointURLByServiceName(SERVICE_NAME);
            log.debug("url=" + url);

            log.debug("preparing port");
            gov.hhs.fha.nhinc.nhincsubscription.NhincSubscriptionManagerService service = new gov.hhs.fha.nhinc.nhincsubscription.NhincSubscriptionManagerService();
            gov.hhs.fha.nhinc.nhincsubscription.SubscriptionManager port = service.getSubscriptionManagerPort();
            ((javax.xml.ws.BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);

            log.debug("attaching reference parameter headers");
            SoapUtil soapUtil = new SoapUtil();
            soapUtil.attachReferenceParameterElements((WSBindingProvider) port, referenceParametersElements);


            log.debug("send request to adapter");
            result = port.unsubscribe(request);
            log.debug("complete with send to adapter");
        } catch (Exception ex) {
            log.error("Error calling internal unsubscribe: " + ex.getMessage(), ex);
            result = new org.oasis_open.docs.wsn.b_2.UnsubscribeResponse();
        }
        return result;
    }
}
