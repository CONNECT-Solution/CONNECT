package gov.hhs.fha.nhinc.hiem.entity.proxy;

import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.NhincProxySubscriptionManagerSecuredPortType;
import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.ResourceUnknownFault;
import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.UnableToDestroySubscriptionFault;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;
import javax.jws.HandlerChain;

/**
 *
 * @author dunnek
 */
@WebService(serviceName = "NhincProxySubscriptionManagerSecured", portName = "NhincProxySubscriptionManagerSecuredPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.NhincProxySubscriptionManagerSecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxysubscriptionmanagement", wsdlLocation = "META-INF/wsdl/ProxyHiemUnsubscribeSecured/NhincProxySubscriptionManagementSecured.wsdl")
@Stateless
@HandlerChain(file = "ProxyHiemUnsubscribeHeaderHandler.xml")
public class ProxyHiemUnsubscribeSecured implements NhincProxySubscriptionManagerSecuredPortType
{

    @Resource
    private WebServiceContext context;

    public org.oasis_open.docs.wsn.b_2.UnsubscribeResponse unsubscribe(gov.hhs.fha.nhinc.common.nhinccommonproxy.UnsubscribeRequestSecuredType unsubscribeRequestSecured) throws ResourceUnknownFault, UnableToDestroySubscriptionFault
    {
        ProxyHiemUnsubscribeImpl hiemUnsubscribeImpl = new ProxyHiemUnsubscribeImpl();
        return hiemUnsubscribeImpl.unsubscribe(unsubscribeRequestSecured, context);
    }
}
