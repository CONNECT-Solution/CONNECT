package gov.hhs.fha.nhinc.subscription.repository.service;

/**
 * 
 * 
 * @author Neil Webb
 */
public class SubscriptionRepositoryException extends Exception
{
    public SubscriptionRepositoryException()
    {
        super();
    }

    public SubscriptionRepositoryException(String message)
    {
        super(message);
    }

    public SubscriptionRepositoryException(Throwable t)
    {
        super(t);
    }

    public SubscriptionRepositoryException(String message, Throwable t)
    {
        super(message, t);
    }
}
