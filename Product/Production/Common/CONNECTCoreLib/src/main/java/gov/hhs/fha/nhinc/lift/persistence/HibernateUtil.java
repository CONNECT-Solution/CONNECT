/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.lift.persistence;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.HibernateAccessor;
import java.io.File;
import org.hibernate.cfg.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;

/**
 *
 * @author Les Westberg
 */
public class HibernateUtil
{
    private static SessionFactory sessionFactory = null;
    private Log log = null;

    public HibernateUtil()
    {
        log = createLogger();
        sessionFactory = createSessionFactory();
    }

    /**
     * Create a logger object.
     * 
     * @return The logger object.
     */
    protected Log createLogger()
    {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    /**
     * Create a new session factory.
     *
     * @return The new session factory.
     */
    protected SessionFactory createNewSessionFactory()
    {
        return new Configuration().configure(getConfigFile()).buildSessionFactory();
    }

    /**
     * This creates the session factory for the hibernate session.
     *
     * @return The session factory for the hibernate session.
     */
    protected SessionFactory createSessionFactory()
    {
        SessionFactory oSessionFactory = sessionFactory;

        try
        {
            if (sessionFactory != null)
            {
                oSessionFactory = sessionFactory;
            }
            else
            {
                oSessionFactory = createNewSessionFactory();
            }
        }
        catch (Throwable ex)
        {
            log.error("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
        return oSessionFactory;
    }

    /**
     * Method returns an instance of Hibernate SessionFactory
     * @return SessionFactory
     */
    public SessionFactory getSessionFactory()
    {
        return sessionFactory;
    }

    /**
     * Retrieve the hibernate config file.
     *
     * @return the hibernate config file.
     */
    protected File getConfigFile()
    {
        File result = null;

        try
        {
            result = HibernateAccessor.getHibernateFile(NhincConstants.HIBERNATE_LIFTMESSAGE_REPOSITORY);
        } catch (Exception ex)
        {
            log.error("Unable to load " + NhincConstants.HIBERNATE_LIFTMESSAGE_REPOSITORY + " " + ex.getMessage(), ex);
        }

        return result;
    }
}
