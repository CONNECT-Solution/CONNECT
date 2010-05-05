package gov.hhs.fha.nhinc.subscription.repository.persistence;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import gov.hhs.fha.nhinc.properties.HibernateAccessor;


/**
 * Utility to create hibernate connections
 * 
 * @author Neil Webb
 */
public class HibernateUtil 
{
    private static final SessionFactory sessionFactory;
    
    static
    {
        try
        {
            // Create the SessionFactory from hibernate.cfg.xml
            sessionFactory = new Configuration().configure(HibernateAccessor.getHibernateFile("HiemSubRepHibernate.cfg.xml")).buildSessionFactory();
        } 
        catch (Throwable ex)
        {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory()
    {
        return sessionFactory;
    }

}
