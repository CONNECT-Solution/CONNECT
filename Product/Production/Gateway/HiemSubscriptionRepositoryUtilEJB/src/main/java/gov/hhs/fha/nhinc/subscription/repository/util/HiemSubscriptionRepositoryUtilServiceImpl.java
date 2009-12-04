package gov.hhs.fha.nhinc.subscription.repository.util;

import gov.hhs.fha.nhinc.subscription.repository.service.SubscriptionRepositoryService;

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
        SubscriptionRepositoryService service = new SubscriptionRepositoryService();
        response.setSubscriptionCount(service.subscriptionCount());
        return response;
    }

    public gov.hhs.fha.nhinc.gateway.hiemsubscriptionrepositoryutil.EmptySubscriptionRepositoryResponseType emptySubscriptionRepository(gov.hhs.fha.nhinc.gateway.hiemsubscriptionrepositoryutil.EmptySubscriptionRepositoryRequestType emptySubscriptionRepositoryRequest)
    {
        gov.hhs.fha.nhinc.gateway.hiemsubscriptionrepositoryutil.EmptySubscriptionRepositoryResponseType response = new gov.hhs.fha.nhinc.gateway.hiemsubscriptionrepositoryutil.EmptySubscriptionRepositoryResponseType();
        SubscriptionRepositoryService service = new SubscriptionRepositoryService();
        service.emptyRepository();
        return response;
    }
}
