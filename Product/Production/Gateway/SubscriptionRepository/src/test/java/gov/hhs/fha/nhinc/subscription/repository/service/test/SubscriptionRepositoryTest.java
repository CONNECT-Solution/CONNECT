package gov.hhs.fha.nhinc.subscription.repository.service.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gov.hhs.fha.nhinc.subscription.repository.service.SubscriptionRepositoryFactory;
import gov.hhs.fha.nhinc.subscription.repository.service.SubscriptionRepositoryService;
import gov.hhs.fha.nhinc.subscription.repository.SubscriptionRepositoryException;
import gov.hhs.fha.nhinc.subscription.repository.data.*;
import org.junit.Ignore;

/**
 * Unit test for the SubscriptionRepository
 * 
 * @author Neil Webb
 */
@Ignore
public class SubscriptionRepositoryTest
{
    private SubscriptionRepositoryService repositoryService = null;
    private SubscriptionRecord rec = null;
    private SubscriptionReference ref = null;
    
    private static final String CRITERION_KEY = "CriterionKey";
    private static final String CRITERION_VALUE = "CriterionValue";
    
    @Before
    public void setUp()
    {
        System.out.println("Begin setUp");
        try
        {
            repositoryService = new SubscriptionRepositoryFactory().getSubscriptionRepositoryService();
            assertNotNull("Subscription service was null", repositoryService);
            rec = createSubscriptionRecord();
            ref = repositoryService.storeSubscription(rec);
        }
        catch(Throwable t)
        {
            t.printStackTrace();
            fail(t.getMessage());
        }
        System.out.println("End setUp");
    }

    @After
    public void tearDown()
    {
        System.out.println("Begin tearDown");
        try
        {
            repositoryService.deleteSubscription(rec);
        }
        catch(Throwable t)
        {
            t.printStackTrace();
            fail(t.getMessage());
        }
        System.out.println("End tearDown");
    }

    @Test
    public void testStore()
    {
        System.out.println("Begin testStore");
        try
        {
            assertNotNull("SubscriptionReference was null", ref);
            assertNotNull("SubscriptionManagerEndpointAddress was null", ref.getSubscriptionManagerEndpointAddress());
            assertFalse("SubscriptionManagerEndpointAddress was empty", ref.getSubscriptionManagerEndpointAddress().trim().length() == 0);
            System.out.println("SubscriptionManagerEndpointAddress: " + ref.getSubscriptionManagerEndpointAddress());
            assertNotNull("ReferenceParameters list was null", ref.getReferenceParameters());
            assertEquals("ReferenceParameters list did not contain 1 parameter", 1, ref.getReferenceParameters().size());
            ReferenceParameter param = ref.getReferenceParameters().get(0);
            assertNotNull("Reference parameter was null", param);
            assertNotNull("Reference parameter namespace was null", param.getNamespace());
            assertNotNull("Reference parameter namespace prefix was null", param.getNamespacePrefix());
            assertNotNull("Reference parameter element name was null", param.getElementName());
            assertNotNull("Reference parameter value was null", param.getValue());
            System.out.println("Subscription ID: " + param.getValue());
        }
        catch (Throwable t)
        {
            t.printStackTrace();
            fail(t.getMessage());
        }
        System.out.println("End testStore");
    }

    @Test
    public void testRetriveByCriteria()
    {
        System.out.println("Begin testRetriveByCriteria");
        try
        {
            // Create subscription criteria
            SubscriptionCriteria subCriteria = new SubscriptionCriteria();

            Patient subscriberPatient = new Patient();
            subscriberPatient.setPatientId("Subscriber patient id");
            Community subscriberPatientAssnAuth = new Community();
            subscriberPatientAssnAuth.setCommunityId("Subscriber patient assigning authority id");
            subscriberPatientAssnAuth.setCommunityName("Subscriber patient assigning authority name");
            subscriberPatient.setAssigningAuthority(subscriberPatientAssnAuth);
            subCriteria.setSubscriberPatient(subscriberPatient);

            Patient subscribeePatient = new Patient();
            subscribeePatient.setPatientId("Subscribee patient id");
            Community subscribeePatientAssnAuth = new Community();
            subscribeePatientAssnAuth.setCommunityId("Subscribee patient assigning authority id");
            subscribeePatientAssnAuth.setCommunityName("Subscribee patient assigning authority name");
            subscribeePatient.setAssigningAuthority(subscribeePatientAssnAuth);
            subCriteria.setSubscribeePatient(subscribeePatient);

            Criterion crit = new Criterion();
            crit.setKey(CRITERION_KEY);
            crit.setValue(CRITERION_VALUE);
            subCriteria.addCriterion(crit);
            
            SubscriptionRecordList subscriptions = repositoryService.retrieveByCriteria(subCriteria, SubscriptionType.SUBSCRIPTION);
            assertNotNull("Subscription record list was null", subscriptions);
            assertEquals("A single subscription was not returned", 1, subscriptions.size());
        }
        catch (Throwable t)
        {
            t.printStackTrace();
            fail(t.getMessage());
        }
        System.out.println("End testRetriveByCriteria");
    }

    @Test
    public void testRetriveByTopicExpression()
    {
        System.out.println("Begin testRetriveByTopicExpression");
        try
        {
            // Create subscription criteria
            SubscriptionCriteria subCriteria = new SubscriptionCriteria();

            TopicExpression topicExpression = new TopicExpression();
            topicExpression.setTopicExpressionValue("TopicExpressionVal");
            subCriteria.setTopicExpression(topicExpression);
            
            SubscriptionRecordList subscriptions = repositoryService.retrieveByCriteria(subCriteria, SubscriptionType.SUBSCRIPTION);
            assertNotNull("Subscription record list by topic expression was null", subscriptions);
            assertEquals("A single subscription by topic expression was not returned", 1, subscriptions.size());
        }
        catch (Throwable t)
        {
            t.printStackTrace();
            fail(t.getMessage());
        }
        System.out.println("End testRetriveByTopicExpression");
    }

    @Test
    public void testRetriveBySubscriptionReference()
    {
        System.out.println("Begin testRetriveBySubscriptionReference");
        try
        {
            SubscriptionRecord record = repositoryService.retrieveBySubscriptionReference(ref, SubscriptionType.SUBSCRIPTION);
            assertNotNull("Subscription record was null", record);
            assertEquals("Subscription record was not equal to that stored", rec, record);
        }
        catch (Throwable t)
        {
            t.printStackTrace();
            fail(t.getMessage());
        }
        System.out.println("End testRetriveBySubscriptionReference");
    }

    @Test
    public void testRetriveByParentSubscriptionReference()
    {
        System.out.println("Begin testRetriveByParentSubscriptionReference");
        try
        {
            // Parent subscription ref
            String subMgrEndptAddr = "Submgredptaddr";
            String namespace = "namespace";
            String namespacePrefix = "prefix";
            String elementName = "elementName";
            String value = "elementValue";

            ReferenceParameter refParam = new ReferenceParameter();
            refParam.setNamespace(namespace);
            refParam.setNamespacePrefix(namespacePrefix);
            refParam.setElementName(elementName);
            refParam.setValue(value);

            SubscriptionReference subRef = new SubscriptionReference();
            subRef.setSubscriptionManagerEndpointAddress(subMgrEndptAddr);
            subRef.addReferenceParameter(refParam);

            SubscriptionRecordList subscriptions = repositoryService.retrieveByParentSubscriptionReference(subRef, SubscriptionType.SUBSCRIPTION);
            assertNotNull("Subscriptoins list was null", subscriptions);
            assertEquals("Subscriptoins list was not size of 1", 1, subscriptions.size());
            
            SubscriptionRecord record = subscriptions.get(0);
            assertNotNull("Subscription record was null", record);
            assertEquals("Subscription record was not equal to that stored", rec, record);
        }
        catch (Throwable t)
        {
            t.printStackTrace();
            fail(t.getMessage());
        }
        System.out.println("End testRetriveByParentSubscriptionReference");
    }

    private SubscriptionRecord createSubscriptionRecord() throws SubscriptionRepositoryException
    {
        SubscriptionRecord rec = new SubscriptionRecord();
        rec.setType(SubscriptionType.SUBSCRIPTION);
        SubscriptionItem subItem = new SubscriptionItem();
        rec.setSubscription(subItem);
        
        // Subscriber
        SubscriptionParticipant subscriber = new SubscriptionParticipant();
        subscriber.setNotificationEndpointAddress("Subscriber notification endpoint address");
        subscriber.setUserAddress("Subscriber user address");
        Community subscriberCommunity = new Community();
        subscriberCommunity.setCommunityId("Subscriber community id");
        subscriberCommunity.setCommunityName("Subscriber community name");
        subscriber.setCommunity(subscriberCommunity);
        subItem.setSubscriber(subscriber);
        
        // Subscribee
        SubscriptionParticipant subscribee = new SubscriptionParticipant();
        subscribee.setNotificationEndpointAddress("Subscribee notification endpoint address");
        subscribee.setUserAddress("Subscribee user address");
        Community subscribeeCommunity = new Community();
        subscribeeCommunity.setCommunityId("Subscribee community id");
        subscribeeCommunity.setCommunityName("Subscribee community name");
        subscribee.setCommunity(subscribeeCommunity);
        subItem.setSubscribee(subscribee);
        
        // Criteria
        SubscriptionCriteria subCriteria = new SubscriptionCriteria();
        
        Patient subscriberPatient = new Patient();
        subscriberPatient.setPatientId("Subscriber patient id");
        Community subscriberPatientAssnAuth = new Community();
        subscriberPatientAssnAuth.setCommunityId("Subscriber patient assigning authority id");
        subscriberPatientAssnAuth.setCommunityName("Subscriber patient assigning authority name");
        subscriberPatient.setAssigningAuthority(subscriberPatientAssnAuth);
        subCriteria.setSubscriberPatient(subscriberPatient);
        
        Patient subscribeePatient = new Patient();
        subscribeePatient.setPatientId("Subscribee patient id");
        Community subscribeePatientAssnAuth = new Community();
        subscribeePatientAssnAuth.setCommunityId("Subscribee patient assigning authority id");
        subscribeePatientAssnAuth.setCommunityName("Subscribee patient assigning authority name");
        subscribeePatient.setAssigningAuthority(subscribeePatientAssnAuth);
        subCriteria.setSubscribeePatient(subscribeePatient);
        
        Criterion crit = new Criterion();
        crit.setKey(CRITERION_KEY);
        crit.setValue(CRITERION_VALUE);
        subCriteria.addCriterion(crit);
        
        // Topic expression
        TopicExpression topicExpression = new TopicExpression();
        topicExpression.setDialect("YerBasicDialect");
        topicExpression.setTopicExpressionValue("TopicExpressionVal");
        subCriteria.setTopicExpression(topicExpression);

        subItem.setSubscriptionCriteria(subCriteria);
        
        // Parent subscription ref
        String subMgrEndptAddr = "Submgredptaddr";
        String namespace = "namespace";
        String namespacePrefix = "prefix";
        String elementName = "elementName";
        String value = "elementValue";

        ReferenceParameter refParam = new ReferenceParameter();
        refParam.setNamespace(namespace);
        refParam.setNamespacePrefix(namespacePrefix);
        refParam.setElementName(elementName);
        refParam.setValue(value);

        SubscriptionReference subRef = new SubscriptionReference();
        subRef.setSubscriptionManagerEndpointAddress(subMgrEndptAddr);
        subRef.addReferenceParameter(refParam);
        subItem.setParentSubscriptionReference(subRef);
        
        return rec;
    }
}