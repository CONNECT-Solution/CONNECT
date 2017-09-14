/**
 *
 */
package gov.hhs.fha.nhinc.patientdb.dao;

import gov.hhs.fha.nhinc.patientdb.persistence.HibernateUtil;
import gov.hhs.fha.nhinc.patientdb.persistence.HibernateUtilFactory;
import java.io.Serializable;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author PVenkatakrishnan
 *
 */
public class GenericDaoJpaImpl<T, PK extends Serializable> implements GenericDao<T, PK> {

    protected Class<T> entityClass;
    private static final Logger LOG = LoggerFactory.getLogger(AddressDAO.class);

    Session session = null;
    Transaction tx = null;

    public GenericDaoJpaImpl() {

    }

    @Override
    public boolean create(T t) {
        boolean result = true;
        try{
            session = getSessionFactory().openSession();
            tx = session.beginTransaction();
            LOG.info("Inserting Record...");
            session.persist(t);
            LOG.info("Address Inserted seccussfully...");
            tx.commit();
        } catch (HibernateException | NullPointerException e) {
            result = false;
            if (tx != null) {
                tx.rollback();
            }
            LOG.error("Exception during insertion caused by : {}", e.getMessage(), e);
        } finally {
            // Actual record insertion will happen at this step
            if (session != null) {
                try {
                    session.close();
                } catch (HibernateException e) {
                    LOG.error("Exception while closing the session: {}", e.getMessage(), e);
                }

            }
        }
        LOG.debug("GenericDaoJpaImpl.create() - End");
        return result;
    }

    @Override
    public T read(PK id, Class<T> objectType) {
        List<T> queryList = null;
        T foundRecord = null;
        try {
            session = getSessionFactory().openSession();
            tx = session.beginTransaction();
            LOG.info("Reading Record...");
            Criteria aCriteria = session.createCriteria(objectType);
            aCriteria.add(Expression.eq("id", id));
            queryList = aCriteria.list();
            if (queryList != null && !queryList.isEmpty()) {
                foundRecord = queryList.get(0);
            }
        } catch (HibernateException | NullPointerException e) {
            LOG.error("Exception during read occured due to : {}", e.getMessage(), e);
        } finally {
            // Flush and close session
            if (session != null) {
                try {
                    session.flush();
                    session.close();
                } catch (HibernateException e) {
                    LOG.error("Exception while closing the session after a read: {}", e.getMessage(), e);
                }
            }
        }
        LOG.debug("GenericDaoJpaImpl.read() - End");
        return foundRecord;
    }

    @Override
    public boolean update(T t) {
        boolean result = true;
        LOG.debug("GenericDaoJpaImpl.update() - Begin");

        try {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            LOG.info("Updating Record...");
            session.saveOrUpdate(t);
            LOG.info("Patient Updated seccussfully...");
            tx.commit();
        } catch (HibernateException | NullPointerException e) {
            result = false;
            if (tx != null) {
                tx.rollback();
            }
            LOG.error("Exception during update caused by : {}", e.getMessage(), e);
        } finally {
            // Actual Patient update will happen at this step
            if (session != null) {
                try {
                    session.close();
                } catch (HibernateException e) {
                    LOG.error("Exception while closing the session after an update: {}", e.getMessage(), e);
                }
            }
        }

        LOG.debug("GenericDaoJpaImpl.update() - End");
        return result;
    }

    @Override
    public void delete(T t) {
        try {
            session = getSessionFactory().openSession();
            LOG.info("Deleting Record...");

            // Delete the Patient record
            session.delete(t);
        } catch (HibernateException | NullPointerException e) {
            LOG.error("Exception during delete occured due to : {}", e.getMessage(), e);
        } finally {
            // Flush and close session
            if (session != null) {
                try {
                    session.flush();
                    session.close();
                } catch (HibernateException e) {
                    LOG.error("Exception while closing the session after a delete: {}", e.getMessage(), e);
                }
            }
        }
        LOG.debug("GenericDaoJpaImpl.delete() - End");
    }

    protected SessionFactory getSessionFactory() {
        SessionFactory fact = null;
        HibernateUtil util = HibernateUtilFactory.getPatientDiscHibernateUtil();
        if (util != null) {
            fact = util.getSessionFactory();
        }
        return fact;
    }
}
