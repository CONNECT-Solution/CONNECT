/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.subscription.repository.persistence;

import gov.hhs.fha.nhinc.common.connectionmanager.persistence.HibernateAccessor;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import java.io.File;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Utility to create hibernate connections
 * 
 * @author Neil Webb
 */
public class HibernateUtil 
{
    private static final SessionFactory sessionFactory;
    private static Log log = LogFactory.getLog(HibernateUtil.class);
    
    static
    {
        try
        {
            // Create the SessionFactory from hibernate.cfg.xml
            sessionFactory = new Configuration().configure(getConfigFile()).buildSessionFactory();
        } 
        catch (Throwable ex)
        {
            // Make sure you log the exception, as it might be swallowed
            log.error("Initial SessionFactory creation failed: " + ex.getMessage());
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory()
    {
        return sessionFactory;
    }

    private static File getConfigFile() {
        File result = null;

        try {
            result = HibernateAccessor.getHibernateFile(NhincConstants.HIBERNATE_HIEMSUBREP_REPOSITORY);
        } catch (Exception ex) {
            log.error("Unable to load " + NhincConstants.HIBERNATE_HIEMSUBREP_REPOSITORY + " " + ex.getMessage(), ex);
        }

        return result;
    }

}
