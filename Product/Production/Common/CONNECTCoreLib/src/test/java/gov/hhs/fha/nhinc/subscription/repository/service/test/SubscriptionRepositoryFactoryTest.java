package gov.hhs.fha.nhinc.subscription.repository.service.test;

import gov.hhs.fha.nhinc.subscription.repository.service.SubscriptionRepositoryFactory;
import gov.hhs.fha.nhinc.subscription.repository.service.SubscriptionRepositoryService;
import gov.hhs.fha.nhinc.subscription.repository.SubscriptionRepositoryException;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the SubscriptionRepositoryFactory class.
 * 
 * @author Neil Webb
 */
@Ignore
public class SubscriptionRepositoryFactoryTest
{
    /**
     * Test the creation of a concrete subscription repository service
     */
    @Test
    public void testSubscriptionFactory()
    {
        System.out.println("Begin testSubscriptionFactory");
        try
        {
            SubscriptionRepositoryService repositoryService = new SubscriptionRepositoryFactory().getSubscriptionRepositoryService();
            assertNotNull("Subscription service was null", repositoryService);

            String serviceClassName = repositoryService.getClass().getName();
            System.out.println("Subscription repository class name: " + serviceClassName);
            assertEquals("Impl class name", "gov.hhs.fha.nhinc.subscription.repository.service.FileSubscriptionRepository", serviceClassName);
        }
        catch (SubscriptionRepositoryException ex)
        {
            ex.printStackTrace();
            fail(ex.getMessage());
        }
        System.out.println("End testSubscriptionFactory");
    }
}