package gov.hhs.fha.nhinc.subscription.repository.test;

import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.subscription.CommunityType;
import gov.hhs.fha.nhinc.common.subscription.CriteriaType;
import gov.hhs.fha.nhinc.common.subscription.CriterionType;
import gov.hhs.fha.nhinc.common.subscription.ReferenceParameterType;
import gov.hhs.fha.nhinc.common.subscription.ReferenceParametersType;
import gov.hhs.fha.nhinc.common.subscription.SubscribeeType;
import gov.hhs.fha.nhinc.common.subscription.SubscriberType;
import gov.hhs.fha.nhinc.common.subscription.SubscriptionCriteriaType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gov.hhs.fha.nhinc.common.subscription.SubscriptionItemType;
import gov.hhs.fha.nhinc.common.subscription.SubscriptionItemsType;
import gov.hhs.fha.nhinc.common.subscription.SubscriptionReferenceType;
import gov.hhs.fha.nhinc.subscription.repository.GatewaySubscriptionRepository;
import gov.hhs.fha.nhinc.common.nhinccommon.QualifiedSubjectIdentifierType;
import gov.hhs.fha.nhinc.common.subscription.RetrieveBySubscriptionReferenceRequestMessageType;
import org.junit.Ignore;

/**
 * Unit test for the SubscriptionRepository class
 * 
 * @author Neil Webb
 */
@Ignore //Refactor or move to Integration  test Suite
public class SubscriptionRepositoryTest
{
    private static final String CRITERION1_KEY = "CriterionKey";
    private static final String CRITERION1_VALUE = "CriterionValue";
    private static final String CRITERION2_KEY = "CriterionKey";
    private static final String CRITERION2_VALUE = "CriterionValue";
    private static SubscriptionItemType subscriptionItem = null;
    private static SubscriptionReferenceType subscriptionReference = null;

    @Before
    public void setUp() throws Exception
    {
        System.out.println("Begin setUp");
        try
        {
            subscriptionItem = createSubscription();

            subscriptionReference = new GatewaySubscriptionRepository().storeSubscription(subscriptionItem);
        }
        catch (Throwable t)
        {
            t.printStackTrace();
            fail(t.getMessage());
        }
        System.out.println("End setUp");
    }

    @After
    public void tearDown() throws Exception
    {
        System.out.println("Begin tearDown");
        try
        {
            gov.hhs.fha.nhinc.common.subscription.DeleteSubscriptionMessageRequestType deleteSubscriptionMessage = new gov.hhs.fha.nhinc.common.subscription.DeleteSubscriptionMessageRequestType();
            deleteSubscriptionMessage.setSubscriptionReference(subscriptionReference);
            AcknowledgementType ack = new GatewaySubscriptionRepository().deleteSubscription(deleteSubscriptionMessage);
            assertNotNull("Ack", ack);
        }
        catch (Throwable t)
        {
            t.printStackTrace();
            fail(t.getMessage());
        }
        System.out.println("End tearDown");
    }

    @Test
    public void testStoreSubscription()
    {
        System.out.println("Begin testStoreSubscription");
        try
        {
            // Validate subscription response
            assertNotNull("Subscription reference was null", subscriptionReference);
            assertNotNull("Subscription manager endpoint address was null", subscriptionReference.getSubscriptionManagerEndpointAddress());
            assertNotNull("Reference parameters list was null", subscriptionReference.getReferenceParameters());
            assertEquals("Reference parameter count", 1, subscriptionReference.getReferenceParameters().getReferenceParameter().size());
            ReferenceParameterType refParam = subscriptionReference.getReferenceParameters().getReferenceParameter().get(0);
            assertNotNull("Reference parameter was nulll", refParam);
            assertNotNull("Reference parameter - namepace was null", refParam.getNamespace());
            assertNotNull("Reference parameter - namepace prefix was null", refParam.getPrefix());
            assertNotNull("Reference parameter - element name was null", refParam.getElementName());
            assertNotNull("Reference parameter - value was null", refParam.getValue());
        }
        catch (Throwable t)
        {
            t.printStackTrace();
            fail(t.getMessage());
        }
        System.out.println("End testStoreSubscription");
    }

    @Test
    public void testRetriveByCriteria()
    {
        System.out.println("Begin testRetriveByCriteria");
        try
        {
            // Create subscription criteria
            SubscriptionCriteriaType subCriteria = new SubscriptionCriteriaType();

            QualifiedSubjectIdentifierType subscriberPatient = new QualifiedSubjectIdentifierType();
            subscriberPatient.setSubjectIdentifier("subscriberpatientid");
            subscriberPatient.setAssigningAuthorityIdentifier("subscriberPatientAssigningAuthorityID");
            subCriteria.setSubscriberPatient(subscriberPatient);

            QualifiedSubjectIdentifierType subscribeePatient = new QualifiedSubjectIdentifierType();
            subscribeePatient.setSubjectIdentifier("subscribeepatientid");
            subscribeePatient.setAssigningAuthorityIdentifier("subscribeePatientAssigningAuthorityID");
            subCriteria.setSubscribeePatient(subscribeePatient);

            CriteriaType criteria = new CriteriaType();
            CriterionType crit = new CriterionType();
            crit.setKey(CRITERION1_KEY);
            crit.setValue(CRITERION1_VALUE);
            criteria.getCriterion().add(crit);
            crit = new CriterionType();
            crit.setKey(CRITERION2_KEY);
            crit.setValue(CRITERION2_VALUE);
            criteria.getCriterion().add(crit);
            subCriteria.setCriteria(criteria);

            SubscriptionItemsType subscriptionItems = new GatewaySubscriptionRepository().retrieveByCriteria(subCriteria);
            assertNotNull("Subscription list was null", subscriptionItems);
            assertNotNull("Subscription item list was null", subscriptionItems.getSubscriptionItem());
            assertEquals("A single subscription was not returned", 1, subscriptionItems.getSubscriptionItem().size());
        }
        catch (Throwable t)
        {
            t.printStackTrace();
            fail(t.getMessage());
        }
        System.out.println("End testRetriveByCriteria");
    }

    @Test
    public void testRetriveBySubscriptionReference()
    {
        System.out.println("Begin testRetriveBySubscriptionReference");
        try
        {
            RetrieveBySubscriptionReferenceRequestMessageType retrieveBySubscriptionReferenceMessage = new RetrieveBySubscriptionReferenceRequestMessageType();
            retrieveBySubscriptionReferenceMessage.setSubscriptionReference(subscriptionReference);
            SubscriptionItemType subItem = new GatewaySubscriptionRepository().retrieveBySubscriptionReference(retrieveBySubscriptionReferenceMessage);
            assertNotNull("Subscription item was null", subItem);
            assertNotNull("SubscriptionItem - subscriber was null", subItem.getSubscriber());
            assertNotNull("SubscriptionItem - subscribee was null", subItem.getSubscribee());
            assertNotNull("SubscriptionItem - subscription criteria was null", subItem.getSubscriptionCriteria());
            assertNotNull("SubscriptionItem - subscription reference was null", subItem.getSubscriptionReference());
        }
        catch (Throwable t)
        {
            t.printStackTrace();
            fail(t.getMessage());
        }
        System.out.println("End testRetriveBySubscriptionReference");
    }

    private static SubscriptionItemType createSubscription()
    {
        SubscriptionItemType subItem = new SubscriptionItemType();

        // SUBSCRIBER
        SubscriberType subscriber = new SubscriberType();
        subscriber.setNotificationConsumerEndpointAddress("SubscriberNotificationConsumerEndpointAddress");
        subscriber.setUserAddress("SubscriberUserAddress");
        CommunityType subscriberCommunity = new CommunityType();
        subscriberCommunity.setId("subscribercommunityid");
        subscriberCommunity.setName("subscribercommunityname");
        subscriber.setCommunity(subscriberCommunity);
        subItem.setSubscriber(subscriber);

        // SUBSCRIBEE
        SubscribeeType subscribee = new SubscribeeType();
        subscribee.setNotificationProducerEndpointAddress("SubscribeeNotificationProducerEndpointAddress");
        subscribee.setUserAddress("SubscribeeUserAddress");
        CommunityType subscribeeCommunity = new CommunityType();
        subscribeeCommunity.setId("subscribeecommunityid");
        subscribeeCommunity.setName("subscribeecommunityname");
        subscribee.setCommunity(subscribeeCommunity);
        subItem.setSubscribee(subscribee);

        // SUBSCRIPTION CRITERIA
        SubscriptionCriteriaType subscriptionCriteria = new SubscriptionCriteriaType();

        // Subscriber patient
        QualifiedSubjectIdentifierType subscriberPatient = new QualifiedSubjectIdentifierType();
        subscriberPatient.setSubjectIdentifier("subscriberpatientid");
        subscriberPatient.setAssigningAuthorityIdentifier("subscriberPatientAssigningAuthorityID");
        subscriptionCriteria.setSubscriberPatient(subscriberPatient);

        // Subscribee patient
        QualifiedSubjectIdentifierType subscribeePatient = new QualifiedSubjectIdentifierType();
        subscribeePatient.setSubjectIdentifier("subscribeepatientid");
        subscribeePatient.setAssigningAuthorityIdentifier("subscribeePatientAssigningAuthorityID");
        subscriptionCriteria.setSubscribeePatient(subscribeePatient);

        // Criteria
        CriteriaType criteria = new CriteriaType();
        CriterionType criterion = new CriterionType();
        criterion.setKey(CRITERION1_KEY);
        criterion.setValue(CRITERION1_VALUE);
        criteria.getCriterion().add(criterion);
        criterion = new CriterionType();
        criterion.setKey(CRITERION2_KEY);
        criterion.setValue(CRITERION2_VALUE);
        criteria.getCriterion().add(criterion);
        subscriptionCriteria.setCriteria(criteria);

        subItem.setSubscriptionCriteria(subscriptionCriteria);

        // SUBSCRIPTION REFERENCE
        SubscriptionReferenceType subRef = new SubscriptionReferenceType();
        subRef.setSubscriptionManagerEndpointAddress("SubscriptionReferenceManagerEndpointAddress");
        ReferenceParametersType refParams = new ReferenceParametersType();
        ReferenceParameterType refParam = new ReferenceParameterType();
        refParam.setNamespace("refparamnamespace");
        refParam.setPrefix("refparamnsprefix");
        refParam.setElementName("refparamelementname");
        refParam.setValue("refparamvalue");
        refParams.getReferenceParameter().add(refParam);
        subRef.setReferenceParameters(refParams);
        subItem.setSubscriptionReference(subRef);

        return subItem;
    }
}