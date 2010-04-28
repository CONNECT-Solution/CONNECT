package gov.hhs.fha.nhinc.hiem.entity.proxy;

import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.ResourceUnknownFault;
import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.UnableToDestroySubscriptionFault;
import javax.jws.WebService;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import javax.jws.HandlerChain;

/**
 *
 * @author Neil Webb
 */
@WebService(serviceName = "NhincProxySubscriptionManagerSecured", portName = "NhincProxySubscriptionManagerSecuredPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.NhincProxySubscriptionManagerSecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxysubscriptionmanagement", wsdlLocation = "WEB-INF/wsdl/ProxyHiemSubscribeSecured/NhincProxySubscriptionManagementSecured.wsdl")
@HandlerChain(file = "ProxyHiemUnsubscribeHeaderHandler.xml")
public class ProxyHiemUnsubscribeSecured
{
    @Resource
    private WebServiceContext context;

    public org.oasis_open.docs.wsn.b_2.UnsubscribeResponse unsubscribe(gov.hhs.fha.nhinc.common.nhinccommonproxy.UnsubscribeRequestSecuredType unsubscribeRequestSecured) throws ResourceUnknownFault, UnableToDestroySubscriptionFault
    {
        return new ProxyHiemUnsubscribeImpl().unsubscribe(unsubscribeRequestSecured, context);
    }

}
