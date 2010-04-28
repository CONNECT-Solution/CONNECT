package gov.hhs.fha.nhinc.hiem.entity.proxy;

import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.ResourceUnknownFault;
import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.UnableToDestroySubscriptionFault;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;
import javax.jws.HandlerChain;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "NhincProxySubscriptionManager", portName = "NhincProxySubscriptionManagerPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.NhincProxySubscriptionManagerPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxysubscriptionmanagement", wsdlLocation = "WEB-INF/wsdl/ProxyHiemSubscribe/NhincProxySubscriptionManagement.wsdl")
@HandlerChain(file = "ProxyHiemUnsubscribeHeaderHandler.xml")
public class ProxyHiemUnsubscribe {

    @Resource
    private WebServiceContext context;
    
    public org.oasis_open.docs.wsn.b_2.UnsubscribeResponse unsubscribe(gov.hhs.fha.nhinc.common.nhinccommonproxy.UnsubscribeRequestType unsubscribeRequest) throws ResourceUnknownFault, UnableToDestroySubscriptionFault {
        return new ProxyHiemUnsubscribeImpl().unsubscribe(unsubscribeRequest, context);
    }

}
