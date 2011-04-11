/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2011(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.perfrepo.persistance;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.HibernateAccessor;
import java.io.File;
import org.hibernate.cfg.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;

/**
 * 
 * @author richard.ettema
 */
public class HibernateUtil {

    private static final SessionFactory sessionFactory;
    private static Log log = LogFactory.getLog(HibernateUtil.class);

    static {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            sessionFactory = new Configuration().configure(getConfigFile()).buildSessionFactory();
        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            log.error("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Method returns an instance of Hibernate SessionFactory
     * @return SessionFactory
     */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    private static File getConfigFile() {
        File result = null;

        try {
            result = HibernateAccessor.getHibernateFile(NhincConstants.HIBERNATE_PERFREPO_REPOSITORY);
        } catch (Exception ex) {
            log.error("Unable to load " + NhincConstants.HIBERNATE_PERFREPO_REPOSITORY + " " + ex.getMessage(), ex);
        }

        return result;
    }
}
