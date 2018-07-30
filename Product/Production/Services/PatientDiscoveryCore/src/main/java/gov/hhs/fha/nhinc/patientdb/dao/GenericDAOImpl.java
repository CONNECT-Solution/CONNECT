/*
 * Copyright (c) 2009-2018, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.patientdb.dao;

import gov.hhs.fha.nhinc.persistence.HibernateUtilFactory;
import gov.hhs.fha.nhinc.util.GenericDBUtils;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author PVenkatakrishnan
 *
 */
public class GenericDAOImpl<T> implements GenericDAO<T> {

    protected Class<T> entityClass;
    private static final Logger LOG = LoggerFactory.getLogger(GenericDAOImpl.class);

    Session session = null;
    Transaction tx = null;

    public GenericDAOImpl(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public boolean create(T record) {
        boolean result = false;
        try{
            session = getSession();
            tx = session.beginTransaction();
            LOG.trace("Inserting Record...");
            session.persist(record);
            LOG.trace("Address Inserted seccussfully...");
            tx.commit();
            result = true;
        } catch (HibernateException | NullPointerException e) {
            if (tx != null) {
                tx.rollback();
            }
            LOG.error("Exception during insertion caused by : {}", e.getMessage(), e);
        } finally {
            GenericDBUtils.closeSession(session);
        }
        LOG.debug("PatientDB-GenericDAOImp.create() - End");
        return result;
    }

    @Override
    public T read(Object id) {
        return readBy(id, "id");
    }

    public T readBy(Object id, String idColumn) {
        List<T> queryList = null;
        T foundRecord = null;
        try {
            session = getSession();
            LOG.trace("Reading Record...");
            Criteria aCriteria = session.createCriteria(entityClass);
            aCriteria.add(Expression.eq(idColumn, id));
            queryList = aCriteria.list();
            if (CollectionUtils.isNotEmpty(queryList)) {
                foundRecord = queryList.get(0);
            }
        } catch (HibernateException | NullPointerException e) {
            LOG.error("Exception during read occured due to : {}", e.getMessage(), e);
        } finally {
            GenericDBUtils.closeSession(session);
        }
        LOG.debug("PatientDB-GenericDAOImp.read() - End");
        return foundRecord;
    }

    @Override
    public boolean update(T record) {
        LOG.debug("PatientDB-GenericDAOImp.update() calling save() - Begin");
        return save(record);
    }

    @Override
    public boolean save(T record) {
        boolean result = false;
        LOG.debug("PatientDB-GenericDAOImp.save() - Begin");

        try {
            session = getSession();
            tx = session.beginTransaction();
            LOG.trace("Updating Record...");
            session.saveOrUpdate(record);
            LOG.trace("Patient Updated seccussfully...");
            tx.commit();
            result = true;
        } catch (HibernateException | NullPointerException e) {
            if (tx != null) {
                tx.rollback();
            }
            LOG.error("Exception during update caused by : {}", e.getMessage(), e);
        } finally {
            GenericDBUtils.closeSession(session);
        }

        LOG.debug("PatientDB-GenericDAOImp.save() - End");
        return result;
    }

    @Override
    public boolean delete(T record) {
        boolean result = false;
        try {
            session = getSession();
            tx = session.beginTransaction();
            LOG.trace("Deleting Record...");

            // Delete the Patient record
            session.delete(record);
            tx.commit();
            result = true;
        } catch (HibernateException | NullPointerException e) {
            LOG.error("Exception during delete occured due to : {}", e.getMessage(), e);
        } finally {
            GenericDBUtils.closeSession(session);
        }
        LOG.debug("PatientDB-GenericDAOImp.delete() - End");
        return result;
    }

    protected Session getSession() {
        try{
            session = HibernateUtilFactory.getPatientDiscoveryHibernateUtil().getSessionFactory().getCurrentSession();
        }catch(HibernateException e){
            LOG.error("Error getSession, while openSession: {}, {}",e.getMessage(), e);
        }
        return session;
    }

    public List<T> findRecords(Long patientId) {
        LOG.trace("PatientDB-GenericDAOImp.findRecords() - Begin");
        List<T> queryList = new ArrayList<T>();
        if (patientId == null) {
            LOG.trace("-- patientId Parameter is required for the Query --");
            LOG.trace("PatientDB-GenericDAOImp.findRecords() - End");
            return queryList;
        }

        try {
            session = getSession();
            LOG.trace("Reading Record...");

            // Build the criteria
            Criteria aCriteria = session.createCriteria(entityClass);
            aCriteria.add(Expression.eq("patient.patientId", patientId));
            queryList = aCriteria.list();
        } catch (HibernateException | NullPointerException e) {
            LOG.error("Exception during read occured due to : {}", e.getMessage(), e);
        } finally {
            GenericDBUtils.closeSession(session);
        }
        LOG.trace("PatientDB-GenericDAOImp.findRecords() - End");
        return queryList;
    }
}
