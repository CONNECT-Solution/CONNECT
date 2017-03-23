/*
 * Copyright (c) 2009-2017, United States Government, as represented by the Secretary of Health and Human Services.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above
 *       copyright notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the documentation
 *       and/or other materials provided with the distribution.
 *     * Neither the name of the United States Government nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package gov.hhs.fha.nhinc.patientcorrelation.nhinc.persistence;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.HibernateAccessor;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import java.io.File;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class will be used as a Utility Class to access the Data Object using Hibernate SessionFactory
 *
 * @author svalluripalli
 */
public class HibernateUtil {

    private SessionFactory sessionFactory;
    private static final Logger LOG = LoggerFactory.getLogger(HibernateUtil.class);

    /**
     * Method builds the Hibernate SessionFactory.
     */
    public void buildSessionFactory() {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            if (sessionFactory == null || sessionFactory.isClosed()) {
                sessionFactory = new Configuration().configure()
                        .buildSessionFactory(new StandardServiceRegistryBuilder().configure(getConfigFile()).build());
            }
        } catch (HibernateException he) {
            // Make sure you log the exception, as it might be swallowed
            LOG.error("Initial SessionFactory creation failed. {}", he.getLocalizedMessage(), he);
        }
    }

    /**
     * Method closes the Hibernate SessionFactory
     */
    public void closeSessionFactory() {
        try {
            if (sessionFactory != null && !sessionFactory.isClosed()) {
                sessionFactory.close();
            }
        } catch (HibernateException e) {
            LOG.error("Initial SessionFactory creation failed. {}", e.getLocalizedMessage(), e);
        }
    }

    /**
     * Method returns an instance of Hibernate SessionFactory
     *
     * @return SessionFactory
     */
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    private static File getConfigFile() {
        File result = null;

        try {
            result = HibernateAccessor.getInstance().getHibernateFile(NhincConstants.HIBERNATE_PATIENT_CORRELATION);
        } catch (PropertyAccessException ex) {
            LOG.error("Unable to load " + NhincConstants.HIBERNATE_PATIENT_CORRELATION + " " + ex.getMessage(), ex);
        }

        return result;

    }
}
