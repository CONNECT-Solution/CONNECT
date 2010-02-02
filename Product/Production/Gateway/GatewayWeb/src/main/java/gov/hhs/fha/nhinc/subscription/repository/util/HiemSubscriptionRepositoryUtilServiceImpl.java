package gov.hhs.fha.nhinc.subscription.repository.util;

import gov.hhs.fha.nhinc.subscription.repository.service.HiemSubscriptionRepositoryService;

/**
 * Implementation class for HiemSubscriptionRepositoryUtilService
 * 
 * @author Neil Webb
 */
public class HiemSubscriptionRepositoryUtilServiceImpl
{
    public gov.hhs.fha.nhinc.gateway.hiemsubscriptionrepositoryutil.GetSubscriptionCountResponseType getSubscriptionCount(gov.hhs.fha.nhinc.gateway.hiemsubscriptionrepositoryutil.GetSubscriptionCountRequestType getSubscriptionCountRequest)
    {
        gov.hhs.fha.nhinc.gateway.hiemsubscriptionrepositoryutil.GetSubscriptionCountResponseType response = new gov.hhs.fha.nhinc.gateway.hiemsubscriptionrepositoryutil.GetSubscriptionCountResponseType();
        HiemSubscriptionRepositoryService service = new HiemSubscriptionRepositoryService();
        response.setSubscriptionCount(service.subscriptionCount());
        return response;
    }

    public gov.hhs.fha.nhinc.gateway.hiemsubscriptionrepositoryutil.EmptySubscriptionRepositoryResponseType emptySubscriptionRepository(gov.hhs.fha.nhinc.gateway.hiemsubscriptionrepositoryutil.EmptySubscriptionRepositoryRequestType emptySubscriptionRepositoryRequest)
    {
        gov.hhs.fha.nhinc.gateway.hiemsubscriptionrepositoryutil.EmptySubscriptionRepositoryResponseType response = new gov.hhs.fha.nhinc.gateway.hiemsubscriptionrepositoryutil.EmptySubscriptionRepositoryResponseType();
        HiemSubscriptionRepositoryService service = new HiemSubscriptionRepositoryService();
        service.emptyRepository();
        return response;
    }
}
