package gov.hhs.fha.nhinc.hiem.entity.unsubscribe;

import gov.hhs.fha.nhinc.hiem.consumerreference.ReferenceParametersElements;
import gov.hhs.fha.nhinc.hiem.consumerreference.ReferenceParametersHelper;
import gov.hhs.fha.nhinc.hiem.dte.SoapUtil;
import com.sun.xml.ws.developer.WSBindingProvider;
import gov.hhs.fha.nhinc.entitysubscriptionmanagement.ResourceUnknownFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagement.UnableToDestroySubscriptionFault;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.UnsubscribeRequestType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis_open.docs.wsn.b_2.UnsubscribeResponse;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.EntitySubscriptionManagerSecured;
import gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.EntitySubscriptionManagerSecuredPortType;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import java.util.StringTokenizer;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author dunnek
 */
public class EntityUnsubscribeServiceImpl
{

    private static Log log = LogFactory.getLog(EntityUnsubscribeServiceImpl.class);
    private static EntitySubscriptionManagerSecured service = new EntitySubscriptionManagerSecured();

    public UnsubscribeResponse unsubscribe(UnsubscribeRequestType unsubscribeRequest, WebServiceContext context) throws gov.hhs.fha.nhinc.entitysubscriptionmanagement.UnableToDestroySubscriptionFault, gov.hhs.fha.nhinc.entitysubscriptionmanagement.ResourceUnknownFault, ResourceUnknownFault, UnableToDestroySubscriptionFault
    {
        log.debug("begin unsubscribe");
        UnsubscribeResponse result = null;

        String url = getURL();
        EntitySubscriptionManagerSecuredPortType port = getPort(url);

        AssertionType assertIn = unsubscribeRequest.getAssertion();

        log.debug("extracting reference parameters from soap header");
        ReferenceParametersHelper referenceParametersHelper = new ReferenceParametersHelper();
        ReferenceParametersElements referenceParametersElements = referenceParametersHelper.createReferenceParameterElements(context, NhincConstants.HTTP_REQUEST_ATTRIBUTE_SOAPMESSAGE);
        log.debug("extracted reference parameters from soap header");

        SoapUtil soapUtil = new SoapUtil();
        soapUtil.attachReferenceParameterElements((WSBindingProvider) port, referenceParametersElements);

        SamlTokenCreator tokenCreator = new SamlTokenCreator();
        Map requestContext = tokenCreator.CreateRequestContext(assertIn, url, NhincConstants.HIEM_NOTIFY_ENTITY_SERVICE_NAME_SECURED);
        ((BindingProvider) port).getRequestContext().putAll(requestContext);

        try
        {
		int retryCount = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getRetryAttempts();
		int retryDelay = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getRetryDelay();
        String exceptionText = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getExceptionText();
        javax.xml.ws.WebServiceException catchExp = null;
        if (retryCount > 0 && retryDelay > 0 && exceptionText != null && !exceptionText.equalsIgnoreCase("")) {
            int i = 1;
            while (i <= retryCount) {
                try {
                    result = port.unsubscribe(unsubscribeRequest.getUnsubscribe());
                    break;
                } catch (javax.xml.ws.WebServiceException e) {
                    catchExp = e;
                    int flag = 0;
                    StringTokenizer st = new StringTokenizer(exceptionText, ",");
                    while (st.hasMoreTokens()) {
                        if (e.getMessage().contains(st.nextToken())) {
                            flag = 1;
                        }
                    }
                    if (flag == 1) {
                        log.warn("Exception calling ... web service: " + e.getMessage());
                        System.out.println("retrying the connection for attempt [ " + i + " ] after [ " + retryDelay + " ] seconds");
                        log.info("retrying attempt [ " + i + " ] the connection after [ " + retryDelay + " ] seconds");
                        i++;
                        try {
                            Thread.sleep(retryDelay);
                        } catch (InterruptedException iEx) {
                            log.error("Thread Got Interrupted while waiting on EntitySubscriptionManagerSecured call :" + iEx);
                        } catch (IllegalArgumentException iaEx) {
                            log.error("Thread Got Interrupted while waiting on EntitySubscriptionManagerSecured call :" + iaEx);
                        }
                        retryDelay = retryDelay + retryDelay; //This is a requirement from Customer
                    } else {
                        log.error("Unable to call EntitySubscriptionManagerSecured Webservice due to  : " + e);
                        throw e;
                    }
                }
            }

            if (i > retryCount) {
                log.error("Unable to call EntitySubscriptionManagerSecured Webservice due to  : " + catchExp);
                throw catchExp;
            }

        } else {
            result = port.unsubscribe(unsubscribeRequest.getUnsubscribe());
        }
        }
        catch (gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.ResourceUnknownFault ex)
        {
            throw new ResourceUnknownFault(ex.getMessage(), ex.getFaultInfo(), ex);
        }
        catch (gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.UnableToDestroySubscriptionFault ex)
        {
            throw new UnableToDestroySubscriptionFault(ex.getMessage(), ex.getFaultInfo(), ex);
        }

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
        catch (Exception ex)
        {
            log.error(ex.getMessage(), ex);
        }

        return url;
    }

    private EntitySubscriptionManagerSecuredPortType getPort(String url)
    {
        EntitySubscriptionManagerSecuredPortType port = service.getEntitySubscriptionManagerSecuredPortSoap11();
        gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, url);
        return port;
    }
}
