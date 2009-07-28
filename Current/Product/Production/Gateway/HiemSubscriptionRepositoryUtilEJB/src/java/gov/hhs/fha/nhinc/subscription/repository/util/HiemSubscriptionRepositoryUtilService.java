package gov.hhs.fha.nhinc.subscription.repository.util;

import gov.hhs.fha.nhinc.hiemsubscriptionrepositoryutil.HiemSubscriptionRepositoryUtilPortType;
import javax.ejb.Stateless;
import javax.jws.WebService;

/**
 * 
 * 
 * @author Neil Webb
 */
@WebService(serviceName = "HiemSubscriptionRepositoryUtilService", portName = "HiemSubscriptionRepositoryUtilPortTypeBindingPort", endpointInterface = "gov.hhs.fha.nhinc.hiemsubscriptionrepositoryutil.HiemSubscriptionRepositoryUtilPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:hiemsubscriptionrepositoryutil", wsdlLocation = "META-INF/wsdl/HiemSubscriptionRepositoryUtilService/HiemSubscriptionRepositoryUtil.wsdl")
@Stateless
public class HiemSubscriptionRepositoryUtilService implements HiemSubscriptionRepositoryUtilPortType
{
    public gov.hhs.fha.nhinc.gateway.hiemsubscriptionrepositoryutil.GetSubscriptionCountResponseType getSubscriptionCount(gov.hhs.fha.nhinc.gateway.hiemsubscriptionrepositoryutil.GetSubscriptionCountRequestType getSubscriptionCountRequest)
    {
        return new HiemSubscriptionRepositoryUtilServiceImpl().getSubscriptionCount(getSubscriptionCountRequest);
    }

    public gov.hhs.fha.nhinc.gateway.hiemsubscriptionrepositoryutil.EmptySubscriptionRepositoryResponseType emptySubscriptionRepository(gov.hhs.fha.nhinc.gateway.hiemsubscriptionrepositoryutil.EmptySubscriptionRepositoryRequestType emptySubscriptionRepositoryRequest)
    {
        return new HiemSubscriptionRepositoryUtilServiceImpl().emptySubscriptionRepository(emptySubscriptionRepositoryRequest);
    }

}
