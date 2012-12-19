/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
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
package gov.hhs.fha.nhinc.subscription.repository.dao;

import org.apache.log4j.Logger;
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

    private static final Logger LOG = Logger.getLogger(SubscriptionStorageItemDao.class);

    /**
     * Store a subscription storage item.
     * 
     * @param subscriptionItem
     */
    public void save(SubscriptionStorageItem subscriptionItem) {
        LOG.debug("Performing subscription item save");
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
                    LOG.error("Failed to obtain a session from the sessionFactory");
                }
            } else {
                LOG.error("Session factory was null");
            }
        } finally {
            if (trans != null) {
                try {
                    trans.commit();
                } catch (Throwable t) {
                    LOG.error("Failed to commit transaction: " + t.getMessage(), t);
                }
            }
            if (sess != null) {
                try {
                    sess.close();
                } catch (Throwable t) {
                    LOG.error("Failed to close session: " + t.getMessage(), t);
                }
            }
        }

        LOG.debug("Completed subscription item save");
    }

    /**
     * Retrieve a subscription storage item by identifier
     * 
     * @param recordId Subscription database identifier
     * @return Retrieved subscription
     */
    public SubscriptionStorageItem findById(String recordId) {
        LOG.debug("Performing subscription retrieve using id: " + recordId);
        SubscriptionStorageItem subscription = null;
        Session sess = null;
        try {
            SessionFactory fact = HibernateUtil.getSessionFactory();
            if (fact != null) {
                sess = fact.openSession();
                if (sess != null) {
                    subscription = (SubscriptionStorageItem) sess.get(SubscriptionStorageItem.class, recordId);
                } else {
                    LOG.error("Failed to obtain a session from the sessionFactory");
                }
            } else {
                LOG.error("Session factory was null");
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("Completed subscription retrieve by id. Result was " + ((subscription == null) ? "not " : "")
                        + "found");
            }
        } finally {
            if (sess != null) {
                try {
                    sess.close();
                } catch (Throwable t) {
                    LOG.error("Failed to close session: " + t.getMessage(), t);
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
    @SuppressWarnings({ "unchecked", "unchecked" })
    public List<SubscriptionStorageItem> findBySubscriptionId(String subscriptionId) {
        LOG.debug("Performing subscription retrieve using subscription id: " + subscriptionId);
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
                    LOG.error("Failed to obtain a session from the sessionFactory");
                }
            } else {
                LOG.error("Session factory was null");
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("Completed subscription retrieve by subscription id. Results found: "
                        + ((subscriptions == null) ? "0" : Integer.toString(subscriptions.size())));
            }
        } finally {
            if (sess != null) {
                try {
                    sess.close();
                } catch (Throwable t) {
                    LOG.error("Failed to close session: " + t.getMessage(), t);
                }
            }
        }
        return subscriptions;
    }

    public List<SubscriptionStorageItem> findByRootTopic(String rootTopic, String producer) {
        LOG.debug("Performing subscription retrieve using rootTopic='" + rootTopic + "';producer='" + producer + "'");

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
                    LOG.error("Failed to obtain a session from the sessionFactory");
                }
            } else {
                LOG.error("Session factory was null");
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("Completed subscription retrieve by subscription id. Results found: "
                        + ((subscriptions == null) ? "0" : Integer.toString(subscriptions.size())));
            }
        } finally {
            if (sess != null) {
                try {
                    sess.close();
                } catch (Throwable t) {
                    LOG.error("Failed to close session: " + t.getMessage(), t);
                }
            }
        }
        return subscriptions;
    }

    public List<SubscriptionStorageItem> findByProducer(String producer) {
        LOG.debug("Performing subscription retrieve using producer='" + producer + "'");

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
                    LOG.error("Failed to obtain a session from the sessionFactory");
                }
            } else {
                LOG.error("Session factory was null");
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("Completed subscription retrieve by subscription id. Results found: "
                        + ((subscriptions == null) ? "0" : Integer.toString(subscriptions.size())));
            }
        } finally {
            if (sess != null) {
                try {
                    sess.close();
                } catch (Throwable t) {
                    LOG.error("Failed to close session: " + t.getMessage(), t);
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
        LOG.debug("Performing subscription retrieve using parent subscription id: " + parentSubscriptionId);
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
                    LOG.error("Failed to obtain a session from the sessionFactory");
                }
            } else {
                LOG.error("Session factory was null");
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("Completed subscription retrieve by parent subscription id. Results found: "
                        + ((subscriptions == null) ? "0" : Integer.toString(subscriptions.size())));
            }
        } finally {
            if (sess != null) {
                try {
                    sess.close();
                } catch (Throwable t) {
                    LOG.error("Failed to close session: " + t.getMessage(), t);
                }
            }
        }
        return subscriptions;
    }

    /**
     * Retrieve subscriptions that have a subscription reference that contains the provided string
     * 
     * @param subRefFragment
     * @return All subscriptions
     */
    public List<SubscriptionStorageItem> retrieveCloseBySubscriptionReference(String subRefFragment) {
        LOG.debug("Performing retrieve all subscriptions");
        List<SubscriptionStorageItem> subscriptions = null;
        Session sess = null;
        try {
            SessionFactory fact = HibernateUtil.getSessionFactory();
            if (fact != null) {
                sess = fact.openSession();
                if (sess != null) {
                    // Criteria subRefCriteria = sess.;
                    // subscription = (SubscriptionStorageItem) sess.get(SubscriptionStorageItem.class, subscriptionId);
                } else {
                    LOG.error("Failed to obtain a session from the sessionFactory");
                }
            } else {
                LOG.error("Session factory was null");
            }
        } finally {
            if (sess != null) {
                try {
                    sess.close();
                } catch (Throwable t) {
                    LOG.error("Failed to close session: " + t.getMessage(), t);
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
        LOG.debug("Performing subscription storage item delete");

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
                    LOG.error("Failed to obtain a session from the sessionFactory");
                }
            } else {
                LOG.error("Session factory was null");
            }
        } finally {
            if (trans != null) {
                try {
                    trans.commit();
                } catch (Throwable t) {
                    LOG.error("Failed to commit transaction: " + t.getMessage(), t);
                }
            }
            if (sess != null) {
                try {
                    sess.close();
                } catch (Throwable t) {
                    LOG.error("Failed to close session: " + t.getMessage(), t);
                }
            }
        }
        LOG.debug("Completed subscription storage item delete");
    }

    public int subscriptionCount() {
        int subscriptionCount = 0;
        LOG.debug("Performing subscriptionCount");
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
                    LOG.error("Failed to obtain a session from the sessionFactory");
                }
            } else {
                LOG.error("Session factory was null");
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("Completed subscriptionCount " + subscriptionCount);
            }
        } finally {
            if (sess != null) {
                try {
                    sess.close();
                } catch (Throwable t) {
                    LOG.error("Failed to close session: " + t.getMessage(), t);
                }
            }
        }
        return subscriptionCount;
    }

    public void emptyRepository() {
        LOG.debug("Performing emptyRepository");
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
                    LOG.error("Failed to obtain a session from the sessionFactory");
                }
            } else {
                LOG.error("Session factory was null");
            }
        } finally {
            if (trans != null) {
                try {
                    trans.commit();
                } catch (Throwable t) {
                    LOG.error("Failed to commit transaction: " + t.getMessage(), t);
                }
            }
            if (sess != null) {
                try {
                    sess.close();
                } catch (Throwable t) {
                    LOG.error("Failed to close session: " + t.getMessage(), t);
                }
            }
        }
    }
}
