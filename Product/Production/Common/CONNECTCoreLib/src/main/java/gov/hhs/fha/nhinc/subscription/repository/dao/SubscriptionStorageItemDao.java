/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.subscription.repository.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.subscription.repository.data.SubscriptionStorageItem;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import gov.hhs.fha.nhinc.subscription.repository.persistence.HibernateUtil;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 * Data access object class for subscription storage items
 * 
 * @author Neil Webb
 */
public class SubscriptionStorageItemDao {

    Log log = LogFactory.getLog(SubscriptionStorageItemDao.class);

    /**
     * Store a subscription storage item.
     * 
     * @param subscriptionItem
     */
    public void save(SubscriptionStorageItem subscriptionItem) {
        log.debug("Performing subscription item save");
        Session sess = null;
        Transaction trans = null;
        try {
            SessionFactory fact = HibernateUtil.getSessionFactory();
            if (fact != null) {
                sess = fact.openSession();
                if (sess != null) {
                    trans = sess.beginTransaction();
                    sess.saveOrUpdate(subscriptionItem);
                } else {
                    log.error("Failed to obtain a session from the sessionFactory");
                }
            } else {
                log.error("Session factory was null");
            }
        } finally {
            if (trans != null) {
                try {
                    trans.commit();
                } catch (Throwable t) {
                    log.error("Failed to commit transaction: " + t.getMessage(), t);
                }
            }
            if (sess != null) {
                try {
                    sess.close();
                } catch (Throwable t) {
                    log.error("Failed to close session: " + t.getMessage(), t);
                }
            }
        }

        log.debug("Completed subscription item save");
    }

    /**
     * Retrieve a subscription storage item by identifier
     *
     * @param recordId Subscription database identifier
     * @return Retrieved subscription
     */
    public SubscriptionStorageItem findById(String recordId) {
        log.debug("Performing subscription retrieve using id: " + recordId);
        SubscriptionStorageItem subscription = null;
        Session sess = null;
        try {
            SessionFactory fact = HibernateUtil.getSessionFactory();
            if (fact != null) {
                sess = fact.openSession();
                if (sess != null) {
                    subscription = (SubscriptionStorageItem) sess.get(SubscriptionStorageItem.class, recordId);
                } else {
                    log.error("Failed to obtain a session from the sessionFactory");
                }
            } else {
                log.error("Session factory was null");
            }
            if (log.isDebugEnabled()) {
                log.debug("Completed subscription retrieve by id. Result was " + ((subscription == null) ? "not " : "") + "found");
            }
        } finally {
            if (sess != null) {
                try {
                    sess.close();
                } catch (Throwable t) {
                    log.error("Failed to close session: " + t.getMessage(), t);
                }
            }
        }
        return subscription;
    }

    /**
     * Retrieve a subscription storage item by subscription identifier
     *
     * @param subscriptionId Subscription identifier
     * @return Retrieved subscriptions
     */
    @SuppressWarnings({"unchecked", "unchecked"})
    public List<SubscriptionStorageItem> findBySubscriptionId(String subscriptionId) {
        log.debug("Performing subscription retrieve using subscription id: " + subscriptionId);
        List<SubscriptionStorageItem> subscriptions = null;
        Session sess = null;
        try {
            SessionFactory fact = HibernateUtil.getSessionFactory();
            if (fact != null) {
                sess = fact.openSession();
                if (sess != null) {
                    Criteria criteria = sess.createCriteria(SubscriptionStorageItem.class);
                    criteria.add(Restrictions.eq("subscriptionId", subscriptionId));
                    subscriptions = criteria.list();
                } else {
                    log.error("Failed to obtain a session from the sessionFactory");
                }
            } else {
                log.error("Session factory was null");
            }
            if (log.isDebugEnabled()) {
                log.debug("Completed subscription retrieve by subscription id. Results found: " + ((subscriptions == null) ? "0" : Integer.toString(subscriptions.size())));
            }
        } finally {
            if (sess != null) {
                try {
                    sess.close();
                } catch (Throwable t) {
                    log.error("Failed to close session: " + t.getMessage(), t);
                }
            }
        }
        return subscriptions;
    }

    public List<SubscriptionStorageItem> findByRootTopic(String rootTopic, String producer) {
        log.debug("Performing subscription retrieve using rootTopic='" + rootTopic + "';producer='" + producer + "'");

        List<SubscriptionStorageItem> subscriptions = null;
        Session sess = null;
        try {
            SessionFactory fact = HibernateUtil.getSessionFactory();
            if (fact != null) {
                sess = fact.openSession();
                if (sess != null) {
                    Criteria criteria = sess.createCriteria(SubscriptionStorageItem.class);
                    criteria.add(Restrictions.eq("rootTopic", rootTopic));
                    criteria.add(Restrictions.eq("producer", producer));
                    subscriptions = criteria.list();
                } else {
                    log.error("Failed to obtain a session from the sessionFactory");
                }
            } else {
                log.error("Session factory was null");
            }
            if (log.isDebugEnabled()) {
                log.debug("Completed subscription retrieve by subscription id. Results found: " + ((subscriptions == null) ? "0" : Integer.toString(subscriptions.size())));
            }
        } finally {
            if (sess != null) {
                try {
                    sess.close();
                } catch (Throwable t) {
                    log.error("Failed to close session: " + t.getMessage(), t);
                }
            }
        }
        return subscriptions;
    }

    public List<SubscriptionStorageItem> findByProducer(String producer) {
        log.debug("Performing subscription retrieve using producer='" + producer + "'");

        List<SubscriptionStorageItem> subscriptions = null;
        Session sess = null;
        try {
            SessionFactory fact = HibernateUtil.getSessionFactory();
            if (fact != null) {
                sess = fact.openSession();
                if (sess != null) {
                    Criteria criteria = sess.createCriteria(SubscriptionStorageItem.class);
                    criteria.add(Restrictions.eq("producer", producer));
                    subscriptions = criteria.list();
                } else {
                    log.error("Failed to obtain a session from the sessionFactory");
                }
            } else {
                log.error("Session factory was null");
            }
            if (log.isDebugEnabled()) {
                log.debug("Completed subscription retrieve by subscription id. Results found: " + ((subscriptions == null) ? "0" : Integer.toString(subscriptions.size())));
            }
        } finally {
            if (sess != null) {
                try {
                    sess.close();
                } catch (Throwable t) {
                    log.error("Failed to close session: " + t.getMessage(), t);
                }
            }
        }
        return subscriptions;
    }

    /**
     * Retrieve a subscription storage item by parent subscription identifier
     *
     * @param parentSubscriptionId Parent subscription identifier
     * @return Retrieved subscriptions
     */
    @SuppressWarnings("unchecked")
    public List<SubscriptionStorageItem> findByParentSubscriptionId(String parentSubscriptionId) {
        log.debug("Performing subscription retrieve using parent subscription id: " + parentSubscriptionId);
        List<SubscriptionStorageItem> subscriptions = null;
        Session sess = null;
        try {
            SessionFactory fact = HibernateUtil.getSessionFactory();
            if (fact != null) {
                sess = fact.openSession();
                if (sess != null) {
                    Criteria criteria = sess.createCriteria(SubscriptionStorageItem.class);
                    criteria.add(Restrictions.eq("parentSubscriptionId", parentSubscriptionId));
                    subscriptions = criteria.list();
                } else {
                    log.error("Failed to obtain a session from the sessionFactory");
                }
            } else {
                log.error("Session factory was null");
            }
            if (log.isDebugEnabled()) {
                log.debug("Completed subscription retrieve by parent subscription id. Results found: " + ((subscriptions == null) ? "0" : Integer.toString(subscriptions.size())));
            }
        } finally {
            if (sess != null) {
                try {
                    sess.close();
                } catch (Throwable t) {
                    log.error("Failed to close session: " + t.getMessage(), t);
                }
            }
        }
        return subscriptions;
    }

    /**
     * Retrieve subscriptions that have a subscription reference that contains
     * the provided string
     * 
     * @param subRefFragment
     * @return All subscriptions
     */
    public List<SubscriptionStorageItem> retrieveCloseBySubscriptionReference(String subRefFragment) {
        log.debug("Performing retrieve all subscriptions");
        List<SubscriptionStorageItem> subscriptions = null;
        Session sess = null;
        try {
            SessionFactory fact = HibernateUtil.getSessionFactory();
            if (fact != null) {
                sess = fact.openSession();
                if (sess != null) {
                    //Criteria subRefCriteria = sess.;
                    //subscription = (SubscriptionStorageItem) sess.get(SubscriptionStorageItem.class, subscriptionId);
                } else {
                    log.error("Failed to obtain a session from the sessionFactory");
                }
            } else {
                log.error("Session factory was null");
            }
        } finally {
            if (sess != null) {
                try {
                    sess.close();
                } catch (Throwable t) {
                    log.error("Failed to close session: " + t.getMessage(), t);
                }
            }
        }
        return subscriptions;
    }

    /**
     * Delete a subscription storage item
     * 
     * @param subscriptionItem Subscription storage item to delete
     */
    public void delete(SubscriptionStorageItem subscriptionItem) {
        log.debug("Performing subscription storage item delete");

        Session sess = null;
        Transaction trans = null;
        try {
            SessionFactory fact = HibernateUtil.getSessionFactory();
            if (fact != null) {
                sess = fact.openSession();
                if (sess != null) {
                    trans = sess.beginTransaction();
                    sess.delete(subscriptionItem);
                } else {
                    log.error("Failed to obtain a session from the sessionFactory");
                }
            } else {
                log.error("Session factory was null");
            }
        } finally {
            if (trans != null) {
                try {
                    trans.commit();
                } catch (Throwable t) {
                    log.error("Failed to commit transaction: " + t.getMessage(), t);
                }
            }
            if (sess != null) {
                try {
                    sess.close();
                } catch (Throwable t) {
                    log.error("Failed to close session: " + t.getMessage(), t);
                }
            }
        }
        log.debug("Completed subscription storage item delete");
    }

    public int subscriptionCount() {
        int subscriptionCount = 0;
        log.debug("Performing subscriptionCount");
        Session sess = null;
        try {
            SessionFactory fact = HibernateUtil.getSessionFactory();
            if (fact != null) {
                sess = fact.openSession();
                if (sess != null) {
                    Criteria criteria = sess.createCriteria(SubscriptionStorageItem.class);
                    criteria.setProjection(Projections.rowCount());
                    subscriptionCount = ((Integer) criteria.list().get(0)).intValue();
                } else {
                    log.error("Failed to obtain a session from the sessionFactory");
                }
            } else {
                log.error("Session factory was null");
            }
            if (log.isDebugEnabled()) {
                log.debug("Completed subscriptionCount " + subscriptionCount);
            }
        } finally {
            if (sess != null) {
                try {
                    sess.close();
                } catch (Throwable t) {
                    log.error("Failed to close session: " + t.getMessage(), t);
                }
            }
        }
        return subscriptionCount;
    }

    public void emptyRepository() {
        log.debug("Performing emptyRepository");
        Session sess = null;
        Transaction trans = null;
        try {
            SessionFactory fact = HibernateUtil.getSessionFactory();
            if (fact != null) {
                sess = fact.openSession();
                if (sess != null) {
                    trans = sess.beginTransaction();
                    sess.createQuery("delete from SubscriptionStorageItem").executeUpdate();
                } else {
                    log.error("Failed to obtain a session from the sessionFactory");
                }
            } else {
                log.error("Session factory was null");
            }
        } finally {
            if (trans != null) {
                try {
                    trans.commit();
                } catch (Throwable t) {
                    log.error("Failed to commit transaction: " + t.getMessage(), t);
                }
            }
            if (sess != null) {
                try {
                    sess.close();
                } catch (Throwable t) {
                    log.error("Failed to close session: " + t.getMessage(), t);
                }
            }
        }
    }
}
