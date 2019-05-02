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
package gov.hhs.fha.nhinc.util;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author tran tang
 *
 */
public class GenericDBUtils {
    private static final Logger LOG = LoggerFactory.getLogger(GenericDBUtils.class);

    private GenericDBUtils() {
    }

    public static void closeSession(Session session) {
        if (session != null && session.isOpen()) {
            try {
                session.close();
            } catch (HibernateException ex) {
                LOG.error("There an error while closing the hibernate-session-close: {} ", ex.getMessage(), ex);
            }
        }
    }

    public static void rollbackTransaction(Transaction transaction) {
        if (transaction != null) {
            try {
                transaction.rollback();
            } catch (HibernateException ex) {
                LOG.error("There an error while rolling back the hibernate-transaction: {} ", ex.getMessage(), ex);
            }
        }
    }

    public static <T> boolean save(Session session, T entity) {
        List<T> entities = new ArrayList<T>();
        entities.add(entity);
        return GenericDBUtils.saveAll(session, entities);
    }

    public static <T> boolean saveAll(Session session, List<T> entities) {
        LOG.trace("GenericDBUtil.save");
        boolean result = false;
        Transaction tx = null;
        int i = 0;
        try {
            tx = session.beginTransaction();
            for (T entity : entities) {
                i = i + 1;
                session.saveOrUpdate(entity);
                if (i == 20) {
                    session.flush();
                    i = 0;
                }
            }
            tx.commit();
            result = true;
            LOG.trace("GenericDBUtil.save - commit()");
        } catch (HibernateException | NullPointerException e) {
            if (tx != null) {
                tx.rollback();
            }
            LOG.error("Exception during save caused by : {}", e.getMessage(), e);
            throw e;
        } finally {
            closeSession(session);
        }
        return result;
    }

    public static <T> boolean delete(Session session, T entity) {
        LOG.trace("GenericDBUtil.delete");
        boolean result = false;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(entity);
            tx.commit();
            result = true;
            LOG.trace("GenericDBUtil.delete - commit()");
        } catch (HibernateException | NullPointerException e) {
            if (tx != null) {
                tx.rollback();
            }
            LOG.error("Exception during delete caused by : {}", e.getMessage(), e);
            throw e;
        } finally {
            closeSession(session);
        }
        return result;
    }

    public static <T> boolean deleteAll(Session session, List<T> entities) {
        LOG.trace("GenericDBUtil.deleteAll");
        boolean result = false;
        Transaction tx = null;
        int i = 0;
        try {
            tx = session.beginTransaction();
            for (T entity : entities) {
                i = i + 1;
                session.delete(entity);
            }
            tx.commit();
            result = true;
            LOG.trace("GenericDBUtil.deleteAll - commit()");
        } catch (HibernateException | NullPointerException e) {
            if (tx != null) {
                tx.rollback();
            }
            LOG.error("Exception during delete caused by : {}", e.getMessage(), e);
            throw e;
        } finally {
            closeSession(session);
        }
        return result;
    }

    public static <T> T readBy(Session session, Class<T> clazz, Long id) {
        LOG.trace("GenericDBUtil.readBy");
        T entity = null;
        try {
            entity = session.get(clazz, id);
            LOG.trace("GenericDBUtil.readBy - entity = session.get()");
        } catch (HibernateException | NullPointerException e) {
            LOG.error("Exception during read caused by : {}", e.getMessage(), e);
            throw e;
        } finally {
            closeSession(session);
        }
        return entity;
    }

    public static <T> T readBy(Session session, Class<T> clazz, String id) {
        LOG.trace("GenericDBUtil.readBy");
        T entity = null;
        try {
            entity = session.get(clazz, id);
            LOG.trace("GenericDBUtil.readBy - entity = session.get()");
        } catch (HibernateException | NullPointerException e) {
            throw e;
        } finally {
            closeSession(session);
        }
        return entity;
    }

    public static <T> List<T> findAll(Session session, Class<T> clazz) {
        return findAllBy(session, clazz, new ArrayList<Criterion>());
    }

    public static <T> List<T> findAllBy(Session session, Class<T> clazz, Criterion criterion) {
        List<Criterion> criterions = new ArrayList<>();
        criterions.add(criterion);
        return findAllBy(session, clazz, criterions);
    }

    public static <T> List<T> findAllBy(Session session, Class<T> clazz, List<Criterion> criterions) {
        LOG.trace("GenericDBUtil.findAllBy");
        List<T> queryList = new ArrayList<>();
        try {
            Criteria aCriteria = session.createCriteria(clazz);
            if (CollectionUtils.isNotEmpty(criterions)) {
                for (Criterion criterion : criterions) {
                    aCriteria.add(criterion);
                }
            }
            queryList = aCriteria.list();
            LOG.trace("GenericDBUtil.findAllBy - queryList = aCriteria.list");
        } catch (HibernateException | NullPointerException e) {
            LOG.error("Exception during findAllBy occured due to : {}", e.getMessage(), e);
            throw e;
        } finally {
            closeSession(session);
        }
        return queryList;
    }

    public static LogicalExpression getOrIsNullIsLe(String colName, Object colValue) {
        return Restrictions.or(Restrictions.isNull(colName), Restrictions.le(colName, colValue));
    }

    public static LogicalExpression getOrIsNullIsGe(String colName, Object colValue) {
        return Restrictions.or(Restrictions.isNull(colName), Restrictions.ge(colName, colValue));
    }
}
