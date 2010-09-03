/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.repository.persistence;

import gov.hhs.fha.nhinc.properties.HibernateAccessor;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import java.io.File;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Utility to obtain hibernate connections.
 * 
 * @author Neil Webb
 */
public class HibernateUtil
{

    private static final SessionFactory sessionFactory;
    private static Log log = LogFactory.getLog(HibernateUtil.class);
    private static final String HIBERNATE_DYNAMIC_DOCUMENT_REPOSITORY = "dyndocrepo.hibernate.cfg.xml";

    static
    {
        try
        {
            // Create the SessionFactory from HIBERNATE_DOCUMENT_REPOSITORY
            sessionFactory = new Configuration().configure(getConfigFile()).buildSessionFactory();
        } catch (Throwable ex)
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
    private static File getConfigFile(){
        File result = null;

        try
        {
            result = HibernateAccessor.getHibernateFile(HIBERNATE_DYNAMIC_DOCUMENT_REPOSITORY);
        }
        catch (Exception ex)
        {
            log.error("Unable to load " + HIBERNATE_DYNAMIC_DOCUMENT_REPOSITORY + " " + ex.getMessage(), ex );
        }


        return result;


    }

}
