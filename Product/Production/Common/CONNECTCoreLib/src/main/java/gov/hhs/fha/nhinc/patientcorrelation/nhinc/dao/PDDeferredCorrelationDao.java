/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.patientcorrelation.nhinc.dao;

import static gov.hhs.fha.nhinc.util.GenericDBUtils.closeSession;
import static gov.hhs.fha.nhinc.util.GenericDBUtils.rollbackTransaction;

import gov.hhs.fha.nhinc.patientcorrelation.nhinc.model.PDDeferredCorrelation;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.persistence.HibernateUtil;
import gov.hhs.fha.nhinc.persistence.HibernateUtilFactory;
import java.util.Date;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hl7.v3.II;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author akong
 */
public class PDDeferredCorrelationDao {

    private static final Logger LOG = LoggerFactory.getLogger(PDDeferredCorrelationDao.class);

    /**
     * Query by Message Id. This should return only one record.
     *
     * @param messageId
     * @return matching records
     */
    public II queryByMessageId(String messageId) {
        LOG.debug("Performing database record retrieve using message id: {}", messageId);

        Session sess = null;
        II patientId = null;

        try {
            sess = getSession();
            Query query = sess.getNamedQuery("queryByMessageId");
            query.setParameter("MessageId", messageId);
            List<PDDeferredCorrelation> pdCorrelations = query.list();

            if (CollectionUtils.isNotEmpty(pdCorrelations) && pdCorrelations.size() == 1) {
                PDDeferredCorrelation pdCorrelation = pdCorrelations.get(0);
                patientId = new II();
                patientId.setExtension(pdCorrelation.getPatientId());
                patientId.setRoot(pdCorrelation.getAssigningAuthorityId());
            } else {
                LOG.error("Failed to find a unique patient id with the given message id {}", messageId);
            }

            if (LOG.isDebugEnabled()) {
                LOG.debug("Completed database record retrieve by message id. Results found: {}",
                    pdCorrelations == null ? "0" : Integer.toString(pdCorrelations.size()));
            }
        } catch (HibernateException | NullPointerException ex) {
            LOG.error("Error encounter during the queryByMessageId: {}", ex.getLocalizedMessage(), ex);
        } finally {
            closeSession(sess);
        }

        return patientId;
    }

    /**
     * Copies the field values from the source to the destination.
     *
     * @param source
     * @param dest
     */
    public void copyValues(PDDeferredCorrelation source, PDDeferredCorrelation dest) {
        dest.setAssigningAuthorityId(source.getAssigningAuthorityId());
        dest.setCreationTime(source.getCreationTime());
        dest.setPatientId(source.getPatientId());
    }

    /**
     * Saves a record to the database. Updates if the message id already exists in the database.
     *
     * @param messageId
     * @param patientId
     */
    public void saveOrUpdate(String messageId, II patientId) {
        PDDeferredCorrelation pdCorrelation = new PDDeferredCorrelation();
        pdCorrelation.setMessageId(messageId);
        pdCorrelation.setPatientId(patientId.getExtension());
        pdCorrelation.setAssigningAuthorityId(patientId.getRoot());
        pdCorrelation.setCreationTime(new Date());
        saveOrUpdate(pdCorrelation);
    }

    /**
     * Save a record to the database. Updates if the message id already exists in the database.
     *
     * @param pdCorrelation PDDeferredCorrelation to save
     */
    public void saveOrUpdate(PDDeferredCorrelation pdCorrelation) {
        LOG.debug("PDDeferredCorrelationDao.save() - Begin");

        Session sess = null;
        Transaction trans = null;
        try {
            sess = getSession();
            trans = sess.beginTransaction();
            Query query = sess.getNamedQuery("queryByMessageId");
            query.setParameter("MessageId", pdCorrelation.getMessageId());
            List<PDDeferredCorrelation> pdCorrelations = query.list();

            if (CollectionUtils.isNotEmpty(pdCorrelations) && pdCorrelations.size() == 1) {
                PDDeferredCorrelation updatedPdCorrelation = pdCorrelations.get(0);
                copyValues(pdCorrelation, updatedPdCorrelation);
                sess.saveOrUpdate(updatedPdCorrelation);
            } else {
                sess.saveOrUpdate(pdCorrelation);
            }
            trans.commit();
        } catch (HibernateException | NullPointerException ex) {
            rollbackTransaction(trans);
            LOG.error("Error encounter during the saveOrUpdate-correlation: {}", ex.getLocalizedMessage(), ex);
        } finally {
            closeSession(sess);
        }

        LOG.debug("PDDeferredCorrelationDao.save() - End");
    }

    protected Session getSession() {
        HibernateUtil util = HibernateUtilFactory.getPatientCorrHibernateUtil();
        if (null == util) {
            LOG.error("Session factory was null");
            return null;
        }

        SessionFactory fact = null;
        fact = util.getSessionFactory();
        if (null == fact) {
            LOG.error("Failed to obtain a session from the sessionFactory");
            return null;
        }

        return fact.openSession();
    }
}
