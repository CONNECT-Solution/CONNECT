package gov.hhs.fha.nhinc.hiem.entity.proxy;

import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.ResourceUnknownFault;
import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.UnableToDestroySubscriptionFault;
import javax.jws.WebService;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import javax.jws.HandlerChain;
import javax.xml.ws.BindingType;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "NhincProxySubscriptionManagerSecured", portName = "NhincProxySubscriptionManagerSecuredPortSoap", endpointInterface = "gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.NhincProxySubscriptionManagerSecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxysubscriptionmanagement", wsdlLocation = "WEB-INF/wsdl/ProxyHiemUnsubscribeSecured/NhincProxySubscriptionManagementSecured.wsdl")
@HandlerChain(file = "ProxyHiemUnsubscribeHeaderHandler.xml")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class ProxyHiemUnsubscribeSecured
{
    @Resource
    private WebServiceContext context;

    public org.oasis_open.docs.wsn.b_2.UnsubscribeResponse unsubscribe(gov.hhs.fha.nhinc.common.nhinccommonproxy.UnsubscribeRequestSecuredType unsubscribeRequestSecured) throws ResourceUnknownFault, UnableToDestroySubscriptionFault
    {
        return new ProxyHiemUnsubscribeImpl().unsubscribe(unsubscribeRequestSecured, context);
    }

}
