/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.hiem.entity.unsubscribe;


import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.subscription.ReferenceParameterType;
import gov.hhs.fha.nhinc.common.subscription.ReferenceParametersType;
import gov.hhs.fha.nhinc.common.subscription.SubscriptionItemType;
import gov.hhs.fha.nhinc.common.subscription.SubscriptionReferenceType;
import gov.hhs.fha.nhinc.common.subscription.UnsubscribeType;
import gov.hhs.fha.nhinc.hiem.consumerreference.ReferenceParametersElements;
import gov.hhs.fha.nhinc.hiem.consumerreference.ReferenceParametersHelper;
//import gov.hhs.fha.nhinc.hiemadapter.proxy.unsubscribe.HiemUnsubscribeAdapterProxy;
import gov.hhs.fha.nhinc.hiem.dte.SoapUtil;
import com.sun.xml.ws.developer.WSBindingProvider;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
//import gov.hhs.fha.nhinc.common.subscription.;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractorHelper;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import javax.xml.xpath.XPathExpressionException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis_open.docs.wsn.b_2.Unsubscribe;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
//import org.xmlsoap.schemas.ws._2004._08.addressing.ReferenceParametersType;
import gov.hhs.fha.nhinc.connectmgr.data.CMBindingTemplate;
import gov.hhs.fha.nhinc.connectmgr.data.CMBusinessEntity;
import gov.hhs.fha.nhinc.connectmgr.data.CMBusinessService;
import gov.hhs.fha.nhinc.connectmgr.data.CMBusinessServices;
//import gov.hhs.fha.nhinc.hiemadapter.proxy.unsubscribe.HiemUnsubscribeAdapterProxyObjectFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;

import gov.hhs.fha.nhinc.xmlCommon.XmlUtility;
import org.oasis_open.docs.wsn.bw_2.ResourceUnknownFault;
import org.oasis_open.docs.wsn.bw_2.UnableToDestroySubscriptionFault;
import org.w3c.dom.Element;

import gov.hhs.fha.nhinc.entitysubscriptionmanagement.EntitySubscriptionManagerPortType;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.jws.HandlerChain;
import javax.xml.ws.WebServiceContext;


import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.UnsubscribeRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.UnsubscribeRequestSecuredType;
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
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.data.CMBindingTemplate;
import gov.hhs.fha.nhinc.connectmgr.data.CMBusinessEntity;
import gov.hhs.fha.nhinc.connectmgr.data.CMBusinessService;
import gov.hhs.fha.nhinc.connectmgr.data.CMBusinessServices;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
//import gov.hhs.fha.nhinc.nhinhiem.proxy.unsubscribe.NhinHiemUnsubscribeProxyObjectFactory;
import org.oasis_open.docs.wsn.b_2.UnableToDestroySubscriptionFaultType;
import org.oasis_open.docs.wsn.b_2.UnsubscribeResponse;

import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import gov.hhs.fha.nhinc.entitysubscriptionmanagement.EntitySubscriptionManagerSecured;
import gov.hhs.fha.nhinc.entitysubscriptionmanagement.EntitySubscriptionManagerSecuredPortType;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.NhincProxySubscriptionManagerSecured;
import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.NhincProxySubscriptionManagerSecuredPortType;



/**
 *
 * @author dunnek
 */
public class EntityUnsubscribeServiceImpl {
    private static Log log = LogFactory.getLog(EntityUnsubscribeServiceImpl.class);
    private static EntitySubscriptionManagerSecured service = new EntitySubscriptionManagerSecured();

     public UnsubscribeResponse unsubscribe(UnsubscribeRequestType unsubscribeRequest, WebServiceContext context) throws gov.hhs.fha.nhinc.entitysubscriptionmanagement.UnableToDestroySubscriptionFault, gov.hhs.fha.nhinc.entitysubscriptionmanagement.ResourceUnknownFault {
        log.debug("begin proxy unsubscribe");
        UnsubscribeResponse result = null;

        String url = getURL();
        EntitySubscriptionManagerSecuredPortType port = getPort(url);

        AssertionType assertIn = unsubscribeRequest.getAssertion();

        SamlTokenCreator tokenCreator = new SamlTokenCreator();
        Map requestContext = tokenCreator.CreateRequestContext(assertIn, url, NhincConstants.HIEM_NOTIFY_ENTITY_SERVICE_NAME_SECURED);
        ((BindingProvider) port).getRequestContext().putAll(requestContext);
        UnsubscribeRequestSecuredType securedRequest = new UnsubscribeRequestSecuredType();
        
        securedRequest.setUnsubscribe(unsubscribeRequest.getUnsubscribe());

        log.debug("extracting reference parameters from soap header");
        ReferenceParametersHelper referenceParametersHelper = new ReferenceParametersHelper();
        ReferenceParametersElements referenceParametersElements = referenceParametersHelper.createReferenceParameterElements(context, NhincConstants.HTTP_REQUEST_ATTRIBUTE_SOAPMESSAGE);
        log.debug("extracted reference parameters from soap header");

        SoapUtil soapUtil = new SoapUtil();
        soapUtil.attachReferenceParameterElements((WSBindingProvider) port, referenceParametersElements);

        result = port.unsubscribe(securedRequest);

        log.debug("end proxy unsubscribe");
        return result;
     }


    private String getURL()
    {
        String url = "";

        try
        {
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.HIEM_UNSUBSCRIBE_ENTITY_SERVICE_NAME_SECURED);
        }
        catch(Exception ex)
        {
            log.error(ex.getMessage(), ex);
        }

        return url;
    }
    private EntitySubscriptionManagerSecuredPortType getPort(String url)
    {



        EntitySubscriptionManagerSecuredPortType port = service.getEntitySubscriptionManagerSecuredPortSoap11();

        log.info("Setting endpoint address to Entity SubscriptionManager Secured Service to " + url);
        ((BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);

        return port;
    }


}
