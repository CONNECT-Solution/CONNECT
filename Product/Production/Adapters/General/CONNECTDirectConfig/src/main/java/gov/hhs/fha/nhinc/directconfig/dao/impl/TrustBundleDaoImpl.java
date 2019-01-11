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
/*
 Copyright (c) 2010, NHIN Direct Project
 All rights reserved.

 Authors:
 Greg Meyer      gm2552@cerner.com

 Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

 Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer
 in the documentation and/or other materials provided with the distribution.  Neither the name of the The NHIN Direct Project (nhindirect.org).
 nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS
 BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 THE POSSIBILITY OF SUCH DAMAGE.
 */
package gov.hhs.fha.nhinc.directconfig.dao.impl;

import gov.hhs.fha.nhinc.directconfig.dao.DomainDao;
import gov.hhs.fha.nhinc.directconfig.dao.TrustBundleDao;
import gov.hhs.fha.nhinc.directconfig.dao.helpers.DaoUtils;
import gov.hhs.fha.nhinc.directconfig.entity.Domain;
import gov.hhs.fha.nhinc.directconfig.entity.TrustBundle;
import gov.hhs.fha.nhinc.directconfig.entity.TrustBundleAnchor;
import gov.hhs.fha.nhinc.directconfig.entity.TrustBundleDomainReltn;
import gov.hhs.fha.nhinc.directconfig.entity.helpers.BundleRefreshError;
import gov.hhs.fha.nhinc.directconfig.exception.CertificateException;
import gov.hhs.fha.nhinc.directconfig.exception.ConfigurationStoreException;

import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

import javax.persistence.NoResultException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

/**
 * Implementation of the TrustBundleDao interface
 * In case the parameter passes as null, do not convert into empty String to allow AutomatedDirectTest
 * SoapUI test to finish
 * @author Greg Meyer
 * @since 1.2
 */
@Repository
public class TrustBundleDaoImpl implements TrustBundleDao {

    @Autowired
    protected DomainDao domainDao;

    private static final Log LOG = LogFactory.getLog(TrustBundleDaoImpl.class);

    /**
     * Empty constructor
     */
    public TrustBundleDaoImpl() {
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public Collection<TrustBundle> getTrustBundles() throws ConfigurationStoreException {
        Collection<TrustBundle> results = null;

        Session session = null;
        Query query;

        try {
            session = DaoUtils.getSession();

            query = session.getNamedQuery("getAllTrustBundles");

            results = query.list();

            if (results.isEmpty()) {
                results = Collections.emptyList();
            } else {
                for (TrustBundle bundle : results) {
                    loadAnchorData(bundle.getTrustBundleAnchors());
                }
            }

        } catch (Exception e) {
            throw new ConfigurationStoreException("Failed to execute trust bundle DAO query.", e);
        } finally {
            DaoUtils.closeSession(session);
        }

        return results;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TrustBundle getTrustBundleByName(String bundleName) throws ConfigurationStoreException {
        TrustBundle result = null;

        Session session = null;
        Query query;

        try {
            session = DaoUtils.getSession();

            query = session.getNamedQuery("getTrustBundleByName");
            query.setParameter("bundleName", StringUtils.hasText(bundleName)? bundleName.toUpperCase(Locale.getDefault()) : null);

            result = (TrustBundle) query.uniqueResult();

            if (result != null) {
                loadAnchorData(result.getTrustBundleAnchors());
            }

        } catch (NoResultException e) {
            LOG.warn("No results found: " + e.getLocalizedMessage());
            LOG.trace("No results found: " + e.getLocalizedMessage(), e);
            result = null;
        } catch (Exception e) {
            LOG.warn("Could not execute query to retrieve trust bundle: ", e);
            throw new ConfigurationStoreException("Failed to execute trust bundle DAO query.", e);
        } finally {
            DaoUtils.closeSession(session);
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TrustBundle getTrustBundleById(long id) throws ConfigurationStoreException {
        TrustBundle result = null;

        Session session = null;
        Query query;

        try {
            session = DaoUtils.getSession();

            query = session.getNamedQuery("getTrustBundleById");
            query.setParameter("id", id);

            result = (TrustBundle) query.uniqueResult();

            if (result != null) {
                loadAnchorData(result.getTrustBundleAnchors());
            }

        } catch (NoResultException e) {
            LOG.warn("No results found: " + e.getLocalizedMessage());
            LOG.trace("No results found: " + e.getLocalizedMessage(), e);
            result = null;
        } catch (Exception e) {
            throw new ConfigurationStoreException("Failed to execute trust bundle DAO query.", e);
        } finally {
            DaoUtils.closeSession(session);
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addTrustBundle(TrustBundle bundle) throws ConfigurationStoreException {
        Session session = null;
        Transaction tx = null;

        try {
            final TrustBundle existingBundle = getTrustBundleByName(bundle.getBundleName());

            if (existingBundle != null) {
                throw new ConfigurationStoreException("Trust bundle " + bundle.getBundleName() + " already exists");
            }

            bundle.setId(null);
            bundle.setCreateTime(Calendar.getInstance(Locale.getDefault()));

            session = DaoUtils.getSession();

            tx = session.beginTransaction();
            session.persist(bundle);
            tx.commit();

        } catch (ConfigurationStoreException cse) {
            DaoUtils.rollbackTransaction(tx, cse);
            throw cse;
        } catch (HibernateException e) {
            DaoUtils.rollbackTransaction(tx, e);
            throw new ConfigurationStoreException("Failed to add trust bundle " + bundle.getBundleName(), e);
        } finally {
            DaoUtils.closeSession(session);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateTrustBundleAnchors(long trustBundleId, Calendar attemptTime,
            Collection<TrustBundleAnchor> newAnchorSet, String bundleCheckSum) throws ConfigurationStoreException {

        Session session = null;
        Transaction tx = null;

        try {
            final TrustBundle existingBundle = getTrustBundleById(trustBundleId);

            if (existingBundle == null) {
                throw new ConfigurationStoreException("Trust bundle does not exist");
            }

            deleteTrustBundleAnchors(existingBundle);

            LOG.debug("Preparing to update Trust Bundle Anchors.");
            session = DaoUtils.getSession();

            // now update the bundle
            tx = session.beginTransaction();

            // persist TrustBundleAnchors separately
            existingBundle.setTrustBundleAnchors(null);

            existingBundle.setCheckSum(bundleCheckSum);
            existingBundle.setLastRefreshAttempt(attemptTime);
            existingBundle.setLastSuccessfulRefresh(Calendar.getInstance(Locale.getDefault()));

            session.saveOrUpdate(existingBundle);
            tx.commit();

            saveAnchors(newAnchorSet);

        } catch (ConfigurationStoreException cse) {
            DaoUtils.rollbackTransaction(tx, cse);
            throw cse;
        } catch (HibernateException e) {
            DaoUtils.rollbackTransaction(tx, e);
            throw new ConfigurationStoreException("Failed to update Trust Bundle Anchors: " + e.getLocalizedMessage(),
                    e);
        } finally {
            DaoUtils.closeSession(session);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateLastUpdateError(long trustBundleId, Calendar attemptTime, BundleRefreshError error)
            throws ConfigurationStoreException {

        Session session = null;
        Transaction tx = null;

        try {
            final TrustBundle existingBundle = getTrustBundleById(trustBundleId);

            if (existingBundle == null) {
                throw new ConfigurationStoreException("Trust bundle does not exist");
            }

            session = DaoUtils.getSession();

            tx = session.beginTransaction();

            if (attemptTime != null) {
                existingBundle.setLastRefreshAttempt(attemptTime);
            }

            if (error != null) {
                existingBundle.setLastRefreshError(error);
            }

            session.merge(existingBundle);
            tx.commit();

        } catch (ConfigurationStoreException cse) {
            DaoUtils.rollbackTransaction(tx, cse);
            throw cse;
        } catch (HibernateException e) {
            DaoUtils.rollbackTransaction(tx, e);
            throw new ConfigurationStoreException("Failed to update bundle last refresh error.", e);
        } finally {
            DaoUtils.closeSession(session);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteTrustBundles(long[] trustBundleIds) throws ConfigurationStoreException {
        if (trustBundleIds != null && trustBundleIds.length > 0) {
            Session session = null;
            Transaction tx = null;

            try {
                session = DaoUtils.getSession();

                tx = session.beginTransaction();

                for (long id : trustBundleIds) {
                    try {
                        session.delete(getTrustBundleById(id));
                    } catch (ConfigurationStoreException e) {
                        LOG.warn("Unable to delete Trust Bundle #" + id + ": " + e.getLocalizedMessage(), e);
                    }
                }

                tx.commit();

            } catch (Exception e) {
                DaoUtils.rollbackTransaction(tx, e);
                throw new ConfigurationStoreException(e);
            } finally {
                DaoUtils.closeSession(session);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateTrustBundleSigningCertificate(long trustBundleId, X509Certificate signingCert)
            throws ConfigurationStoreException {

        Session session = null;
        Transaction tx = null;

        try {
            final TrustBundle existingBundle = getTrustBundleById(trustBundleId);

            if (existingBundle == null) {
                throw new ConfigurationStoreException("Trust bundle does not exist");
            }

            if (signingCert == null) {
                existingBundle.setSigningCertificateData(null);
            } else {
                existingBundle.setSigningCertificateData(signingCert.getEncoded());
            }

            session = DaoUtils.getSession();

            tx = session.beginTransaction();
            session.merge(existingBundle);
            tx.commit();

        } catch (ConfigurationStoreException cse) {
            DaoUtils.rollbackTransaction(tx, cse);
            throw cse;
        } catch (CertificateException | CertificateEncodingException | HibernateException e) {
            DaoUtils.rollbackTransaction(tx, e);
            throw new ConfigurationStoreException("Failed to update bundle last refresh error.", e);
        } finally {
            DaoUtils.closeSession(session);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateTrustBundleAttributes(long trustBundleId, String bundleName, String bundleUrl,
            X509Certificate signingCert, int refreshInterval) throws ConfigurationStoreException {

        Session session = null;
        Transaction tx = null;

        try {
            final TrustBundle existingBundle = getTrustBundleById(trustBundleId);

            if (existingBundle == null) {
                throw new ConfigurationStoreException("Trust bundle does not exist");
            }

            if (signingCert == null) {
                existingBundle.setSigningCertificateData(null);
            } else {
                existingBundle.setSigningCertificateData(signingCert.getEncoded());
            }

            existingBundle.setRefreshInterval(refreshInterval);

            if (bundleName != null && !bundleName.isEmpty()) {
                existingBundle.setBundleName(bundleName);
            }

            if (bundleUrl != null && !bundleUrl.isEmpty()) {
                existingBundle.setBundleURL(bundleUrl);
            }

            session = DaoUtils.getSession();

            tx = session.beginTransaction();
            session.merge(existingBundle);
            tx.commit();

        } catch (ConfigurationStoreException cse) {
            DaoUtils.rollbackTransaction(tx, cse);
            throw cse;
        } catch (CertificateException | CertificateEncodingException | HibernateException e) {
            DaoUtils.rollbackTransaction(tx, e);
            throw new ConfigurationStoreException("Failed to update bundle last refresh error.", e);
        } finally {
            DaoUtils.closeSession(session);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void associateTrustBundleToDomain(long domainId, long trustBundleId, boolean incoming, boolean outgoing)
            throws ConfigurationStoreException {

        Session session = null;
        Transaction tx = null;

        // make sure the domain exists
        final Domain domain = domainDao.getDomain(domainId);

        if (domain == null) {
            throw new ConfigurationStoreException("Domain with id " + domainId + " does not exist");
        }

        // make sure the trust bundle exists
        final TrustBundle trustBundle = getTrustBundleById(trustBundleId);

        if (trustBundle == null) {
            throw new ConfigurationStoreException("Trust bundle with id " + trustBundleId + " does not exist");
        }

        try {
            final TrustBundleDomainReltn domainTrustBundleAssoc = new TrustBundleDomainReltn();

            domainTrustBundleAssoc.setDomain(domain);
            domainTrustBundleAssoc.setTrustBundle(trustBundle);
            domainTrustBundleAssoc.setIncoming(incoming);
            domainTrustBundleAssoc.setOutgoing(outgoing);

            session = DaoUtils.getSession();

            tx = session.beginTransaction();
            session.persist(domainTrustBundleAssoc);
            tx.commit();

        } catch (Exception e) {
            DaoUtils.rollbackTransaction(tx, e);
            throw new ConfigurationStoreException("Failed to associate trust bundle to domain.", e);
        } finally {
            DaoUtils.closeSession(session);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disassociateTrustBundleFromDomain(long domainId, long trustBundleId)
            throws ConfigurationStoreException {
        Session session = null;
        Transaction tx = null;

        // make sure the domain exists
        final Domain domain = domainDao.getDomain(domainId);

        if (domain == null) {
            throw new ConfigurationStoreException("Domain with id " + domainId + " does not exist");
        }

        // make sure the trust bundle exists
        final TrustBundle trustBundle = getTrustBundleById(trustBundleId);

        if (trustBundle == null) {
            throw new ConfigurationStoreException("Trust bundle with id " + trustBundle + " does not exist");
        }

        try {
            session = DaoUtils.getSession();

            tx = session.beginTransaction();
            final Query select = session.getNamedQuery("getTrustBundleDomainReltn");

            select.setParameter("domain", domain);
            select.setParameter("trustBundle", trustBundle);

            final TrustBundleDomainReltn reltn = (TrustBundleDomainReltn) select.uniqueResult();

            session.merge(reltn);
            session.delete(reltn);
            tx.commit();

        } catch (NoResultException e) {
            DaoUtils.rollbackTransaction(tx, e);
            throw new ConfigurationStoreException(
                    "No association between Domain ID " + domainId + " and Trust Bundle ID" + trustBundleId, e);
        } catch (Exception e) {
            DaoUtils.rollbackTransaction(tx, e);
            throw new ConfigurationStoreException("Failed to delete trust bundle to domain relation.", e);
        } finally {
            DaoUtils.closeSession(session);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disassociateTrustBundlesFromDomain(long domainId) throws ConfigurationStoreException {
        Session session = null;
        Transaction tx = null;

        // make sure the domain exists
        final Domain domain = domainDao.getDomain(domainId);

        if (domain == null) {
            throw new ConfigurationStoreException("Domain with id " + domainId + " does not exist");
        }

        try {
            session = DaoUtils.getSession();

            tx = session.beginTransaction();
            final Query delete = session
                    .createQuery("DELETE FROM TrustBundleDomainReltn tbd WHERE tbd.domain = :domain");

            delete.setParameter("domain", domain);
            delete.executeUpdate();
            tx.commit();

        } catch (Exception e) {
            DaoUtils.rollbackTransaction(tx, e);
            throw new ConfigurationStoreException("Failed to dissociate trust bundle from domain id " + domainId, e);
        } finally {
            DaoUtils.closeSession(session);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disassociateTrustBundleFromDomains(long trustBundleId) throws ConfigurationStoreException {
        Session session = null;
        Transaction tx = null;

        // make sure the trust bundle exists
        final TrustBundle trustBundle = getTrustBundleById(trustBundleId);

        if (trustBundle == null) {
            throw new ConfigurationStoreException("TrustBundle with id " + trustBundle + " does not exist");
        }

        try {
            session = DaoUtils.getSession();

            tx = session.beginTransaction();
            final Query delete = session
                    .createQuery("DELETE FROM TrustBundleDomainReltn tbd WHERE tbd.trustBundle = ?");

            delete.setParameter(0, trustBundle);
            delete.executeUpdate();
            tx.commit();

        } catch (Exception e) {
            DaoUtils.rollbackTransaction(tx, e);
            throw new ConfigurationStoreException(
                    "Failed to disassociate domains from TrustBundle with id " + trustBundleId, e);
        } finally {
            DaoUtils.closeSession(session);
        }
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public Collection<TrustBundleDomainReltn> getTrustBundlesByDomain(long domainId)
            throws ConfigurationStoreException {
        Session session = null;

        // make sure the domain exists
        final Domain domain = domainDao.getDomain(domainId);

        if (domain == null) {
            throw new ConfigurationStoreException("Domain with id " + domainId + " does not exist");
        }

        Collection<TrustBundleDomainReltn> results = null;

        try {
            session = DaoUtils.getSession();

            final Query select = session.getNamedQuery("getTrustBundleDomainReltnByDomain");
            select.setParameter("domain", domain);

            results = select.list();

            if (results.isEmpty()) {
                results = Collections.emptyList();
            } else {
                for (TrustBundleDomainReltn reltn : results) {
                    loadAnchorData(reltn.getTrustBundle().getTrustBundleAnchors());
                }
            }

        } catch (Exception e) {
            throw new ConfigurationStoreException("Failed to execute trust bundle relation DAO query.", e);
        } finally {
            DaoUtils.closeSession(session);
        }

        return results;
    }

    /**
     *
     * @param tba Collection of TrustBundleAnchor to be loaded
     */
    private void loadAnchorData(Collection<TrustBundleAnchor> tba) {
        // make sure the anchors are loaded
        if (CollectionUtils.isNotEmpty(tba)) {
            for (TrustBundleAnchor anchor : tba) {
                anchor.getData();
            }
        }
    }

    private void deleteTrustBundleAnchors(TrustBundle existingBundle) {
        Session session = null;
        Transaction tx = null;

        try {
            session = DaoUtils.getSession();

            tx = session.beginTransaction();

            // blow away all the existing bundles
            final Query delete = session
                    .createQuery("DELETE FROM TrustBundleAnchor tba WHERE tba.trustBundle = :existingBundle");
            delete.setParameter("existingBundle", existingBundle);
            int count = delete.executeUpdate();

            LOG.debug("Deleted " + count + " Trust Bundle Anchors");

            tx.commit();

        } catch (Exception e) {
            DaoUtils.rollbackTransaction(tx, e);
            throw new ConfigurationStoreException("Failed to update Trust Bundle Anchors: " + e.getLocalizedMessage(),
                    e);
        } finally {
            DaoUtils.closeSession(session);
        }
    }

    private void saveAnchors(Collection<TrustBundleAnchor> anchors) {
        Session session = null;
        Transaction tx = null;

        try {
            session = DaoUtils.getSession();

            LOG.debug("Saving " + anchors.size() + " Trust Bundle Anchors");

            tx = session.beginTransaction();

            for (TrustBundleAnchor tba : anchors) {
                session.save(tba);
            }

            tx.commit();

        } catch (Exception e) {
            DaoUtils.rollbackTransaction(tx, e);
            throw new ConfigurationStoreException(e);
        } finally {
            DaoUtils.closeSession(session);
        }
    }
}
