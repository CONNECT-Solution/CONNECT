package gov.hhs.fha.nhinc.hiem.entity.proxy;

import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.ResourceUnknownFault;
import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.UnableToDestroySubscriptionFault;
import javax.jws.WebService;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "NhincProxySubscriptionManager", portName = "NhincProxySubscriptionManagerPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.NhincProxySubscriptionManagerPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxysubscriptionmanagement", wsdlLocation = "WEB-INF/wsdl/ProxyHiemUnsubscribe/NhincProxySubscriptionManagement.wsdl")
public class ProxyHiemUnsubscribe {

    public org.oasis_open.docs.wsn.b_2.UnsubscribeResponse unsubscribe(gov.hhs.fha.nhinc.common.nhinccommonproxy.UnsubscribeRequestType unsubscribeRequest) throws ResourceUnknownFault, UnableToDestroySubscriptionFault {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

}
