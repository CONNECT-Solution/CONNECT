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

import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

import javax.persistence.NoResultException;
import org.hibernate.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;

import gov.hhs.fha.nhinc.directconfig.entity.Domain;
import gov.hhs.fha.nhinc.directconfig.entity.TrustBundle;
import gov.hhs.fha.nhinc.directconfig.entity.TrustBundleAnchor;
import gov.hhs.fha.nhinc.directconfig.entity.TrustBundleDomainReltn;
import gov.hhs.fha.nhinc.directconfig.entity.helpers.BundleRefreshError;
import gov.hhs.fha.nhinc.directconfig.exception.ConfigurationStoreException;
import gov.hhs.fha.nhinc.directconfig.dao.DomainDao;
import gov.hhs.fha.nhinc.directconfig.dao.TrustBundleDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of the TrustBundleDao interface
 * @author Greg Meyer
 * @since 1.2
 */
@Service
public class TrustBundleDaoImpl implements TrustBundleDao {
    private static final Log log = LogFactory.getLog(TrustBundleDaoImpl.class);

    /*
     * Provided by Spring application context.
     */
    @Autowired
    protected SessionFactory sessionFactory;

    @Autowired
    protected DomainDao domainDao;

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
    @Transactional(readOnly = true)
    public Collection<TrustBundle> getTrustBundles() throws ConfigurationStoreException {
        Collection<TrustBundle> rs;

        try {
            Query select = sessionFactory.getCurrentSession().createQuery("SELECT tb from TrustBundle tb");

            rs = select.list();
            if (rs.size() == 0)
                return Collections.emptyList();
        } catch (Exception e) {
            throw new ConfigurationStoreException("Failed to execute trust bundle DAO query.", e);
        }

        // make sure the anchors are loaded
        for (TrustBundle bundle : rs) {
            if (!bundle.getTrustBundleAnchors().isEmpty()) {
                for (TrustBundleAnchor anchor : bundle.getTrustBundleAnchors()) {
                    anchor.getData();
                }
            }
        }

        return rs;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public TrustBundle getTrustBundleByName(String bundleName) throws ConfigurationStoreException {
        try {
            Query select = sessionFactory.getCurrentSession().createQuery("SELECT tb from TrustBundle tb WHERE UPPER(tb.bundleName) = ?");
            select.setParameter(0, bundleName.toUpperCase(Locale.getDefault()));

            TrustBundle rs = (TrustBundle)select.uniqueResult();

            // make sure the anchors are loaded
            if (!rs.getTrustBundleAnchors().isEmpty()) {
                for (TrustBundleAnchor anchor : rs.getTrustBundleAnchors()) {
                    anchor.getData();
                }
            }

            return rs;
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            throw new ConfigurationStoreException("Failed to execute trust bundle DAO query.", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public TrustBundle getTrustBundleById(long id) throws ConfigurationStoreException {
        try {
            Query select = sessionFactory.getCurrentSession().createQuery("SELECT tb from TrustBundle tb WHERE tb.id = ?");
            select.setParameter(0, id);

            TrustBundle rs = (TrustBundle)select.uniqueResult();

            // make sure the anchors are loaded
            if (!rs.getTrustBundleAnchors().isEmpty()) {
                for (TrustBundleAnchor anchor : rs.getTrustBundleAnchors()) {
                    anchor.getData();
                }
            }

            return rs;
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            throw new ConfigurationStoreException("Failed to execute trust bundle DAO query.", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = false)
    public void addTrustBundle(TrustBundle bundle) throws ConfigurationStoreException {
        try {
            final TrustBundle existingBundle = this.getTrustBundleByName(bundle.getBundleName());

            if (existingBundle != null) {
                throw new ConfigurationStoreException("Trust bundle " + bundle.getBundleName() + " already exists");
            }

            bundle.setId(null);
            bundle.setCreateTime(Calendar.getInstance(Locale.getDefault()));

            sessionFactory.getCurrentSession().persist(bundle);
        } catch (ConfigurationStoreException cse) {
            throw cse;
        } catch (Exception e) {
            throw new ConfigurationStoreException("Failed to add trust bundle " + bundle.getBundleName(), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = false)
    public void updateTrustBundleAnchors(long trustBundleId, Calendar attemptTime, Collection<TrustBundleAnchor> newAnchorSet,
            String bundleCheckSum)
            throws ConfigurationStoreException {

        try {
            final TrustBundle existingBundle = this.getTrustBundleById(trustBundleId);

            if (existingBundle == null) {
                throw new ConfigurationStoreException("Trust bundle does not exist");
            }

            // blow away all the existing bundles
            final Query delete = sessionFactory.getCurrentSession().createQuery("DELETE from TrustBundleAnchor tba where tba.trustBundle = ?");
            delete.setParameter(0, existingBundle);
            delete.executeUpdate();

            // now update the bundle
            existingBundle.setCheckSum(bundleCheckSum);
            existingBundle.setTrustBundleAnchors(newAnchorSet);
            existingBundle.setLastRefreshAttempt(attemptTime);
            existingBundle.setLastSuccessfulRefresh(Calendar.getInstance(Locale.getDefault()));

            sessionFactory.getCurrentSession().persist(existingBundle);
        } catch (ConfigurationStoreException cse) {
            throw cse;
        } catch (Exception e) {
            throw new ConfigurationStoreException("Failed to update anchors in trust bundle.", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = false)
    public void updateLastUpdateError(long trustBundleId, Calendar attemptTime, BundleRefreshError error)
            throws ConfigurationStoreException {

        try {
            final TrustBundle existingBundle = this.getTrustBundleById(trustBundleId);

            if (existingBundle == null) {
                throw new ConfigurationStoreException("Trust bundle does not exist");
            }

            existingBundle.setLastRefreshAttempt(attemptTime);
            existingBundle.setLastRefreshError(error);

            sessionFactory.getCurrentSession().persist(existingBundle);
        } catch (ConfigurationStoreException cse) {
            throw cse;
        } catch (Exception e) {
            throw new ConfigurationStoreException("Failed to update bundle last refresh error.", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = false)
    @Override
    public void deleteTrustBundles(long[] trustBundleIds) throws ConfigurationStoreException {
        if (trustBundleIds == null || trustBundleIds.length == 0) {
            return;
        }

        for (long id : trustBundleIds) {
            try {
                final TrustBundle bundle = this.getTrustBundleById(id);

                this.disassociateTrustBundleFromDomains(id);

                sessionFactory.getCurrentSession().delete(bundle);
            } catch (ConfigurationStoreException e) {
                log.warn(e.getMessage(), e);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = false)
    public void updateTrustBundleSigningCertificate(long trustBundleId, X509Certificate signingCert)
            throws ConfigurationStoreException {

        try {
            final TrustBundle existingBundle = this.getTrustBundleById(trustBundleId);

            if (existingBundle == null) {
                throw new ConfigurationStoreException("Trust bundle does not exist");
            }

            if (signingCert == null) {
                existingBundle.setSigningCertificateData(null);
            } else {
                existingBundle.setSigningCertificateData(signingCert.getEncoded());
            }

            sessionFactory.getCurrentSession().persist(existingBundle);
        } catch (ConfigurationStoreException cse) {
            throw cse;
        } catch (Exception e) {
            throw new ConfigurationStoreException("Failed to update bundle last refresh error.", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = false)
    public void updateTrustBundleAttributes(long trustBundleId, String bundleName, String bundleUrl, X509Certificate signingCert,
            int refreshInterval) throws ConfigurationStoreException {

        try {
            final TrustBundle existingBundle = this.getTrustBundleById(trustBundleId);

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

            sessionFactory.getCurrentSession().persist(existingBundle);
        } catch (ConfigurationStoreException cse) {
            throw cse;
        } catch (Exception e) {
            throw new ConfigurationStoreException("Failed to update bundle last refresh error.", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = false)
    public void associateTrustBundleToDomain(long domainId, long trustBundleId, boolean incoming,
            boolean outgoing) throws ConfigurationStoreException {

        // make sure the domain exists
        final Domain domain = domainDao.getDomain(domainId);

        if (domain == null) {
            throw new ConfigurationStoreException("Domain with id " + domainId + " does not exist");
        }

        // make sure the trust bundle exists
        final TrustBundle trustBundle = this.getTrustBundleById(trustBundleId);

        if (trustBundle == null) {
            throw new ConfigurationStoreException("Trust bundle with id " + trustBundle + " does not exist");
        }

        try {
            final TrustBundleDomainReltn domainTrustBundleAssoc = new TrustBundleDomainReltn();
            domainTrustBundleAssoc.setDomain(domain);
            domainTrustBundleAssoc.setTrustBundle(trustBundle);
            domainTrustBundleAssoc.setIncoming(incoming);
            domainTrustBundleAssoc.setOutgoing(outgoing);

            sessionFactory.getCurrentSession().persist(domainTrustBundleAssoc);
        } catch (Exception e) {
            throw new ConfigurationStoreException("Failed to associate trust bundle to domain.", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = false)
    public void disassociateTrustBundleFromDomain(long domainId, long trustBundleId) throws ConfigurationStoreException {
        // make sure the domain exists
        final Domain domain = domainDao.getDomain(domainId);

        if (domain == null) {
            throw new ConfigurationStoreException("Domain with id " + domainId + " does not exist");
        }

        // make sure the trust bundle exists
        final TrustBundle trustBundle = this.getTrustBundleById(trustBundleId);

        if (trustBundle == null) {
            throw new ConfigurationStoreException("Trust bundle with id " + trustBundle + " does not exist");
        }

        try {
            final Query select = sessionFactory.getCurrentSession().createQuery("SELECT tbd from TrustBundleDomainReltn tbd where tbd.domain  = ? " +
                    " and tbd.trustBundle = ?");

            select.setParameter(0, domain);
            select.setParameter(1, trustBundle);

            final TrustBundleDomainReltn reltn = (TrustBundleDomainReltn)select.uniqueResult();

            sessionFactory.getCurrentSession().merge(reltn);
            sessionFactory.getCurrentSession().delete(reltn);
        } catch (NoResultException e) {
            throw new ConfigurationStoreException("Association between domain id " + domainId + " and trust bundle id"
                     + trustBundleId + " does not exist", e);
        } catch (Exception e) {
            throw new ConfigurationStoreException("Failed to delete trust bundle to domain relation.", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = false)
    public void disassociateTrustBundlesFromDomain(long domainId) throws ConfigurationStoreException {
        // make sure the domain exists
        final Domain domain = domainDao.getDomain(domainId);

        if (domain == null) {
            throw new ConfigurationStoreException("Domain with id " + domainId + " does not exist");
        }

        try {
            final Query delete = sessionFactory.getCurrentSession().createQuery("DELETE from TrustBundleDomainReltn tbd where tbd.domain  = ?");

            delete.setParameter(0, domain);
            delete.executeUpdate();
        } catch (Exception e) {
            throw new ConfigurationStoreException("Failed to dissaccociate trust bundle from domain id ." + domainId, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = false)
    public void disassociateTrustBundleFromDomains(long trustBundleId) throws ConfigurationStoreException {
        // make sure the trust bundle exists
        final TrustBundle trustBundle = this.getTrustBundleById(trustBundleId);

        if (trustBundle == null) {
            throw new ConfigurationStoreException("Trust bundle with id " + trustBundle + " does not exist");
        }

        try {
            final Query delete = sessionFactory.getCurrentSession().createQuery("DELETE from TrustBundleDomainReltn tbd where tbd.trustBundle  = ?");

            delete.setParameter(0, trustBundle);
            delete.executeUpdate();
        } catch (Exception e) {
            throw new ConfigurationStoreException("Failed to dissaccociate domains from trust bundle id ." + trustBundleId, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    @Transactional(readOnly = true)
    public Collection<TrustBundleDomainReltn> getTrustBundlesByDomain(long domainId) throws ConfigurationStoreException {
        // make sure the domain exists
        final Domain domain = domainDao.getDomain(domainId);

        if (domain == null) {
            throw new ConfigurationStoreException("Domain with id " + domainId + " does not exist");
        }

        Collection<TrustBundleDomainReltn> retVal = null;

        try {
            final Query select = sessionFactory.getCurrentSession().createQuery("SELECT tbd from TrustBundleDomainReltn tbd where tbd.domain = ?");
            select.setParameter(0, domain);

            retVal = (Collection<TrustBundleDomainReltn>)select.list();

            if (retVal.size() == 0) {
                return Collections.emptyList();
            }

            for (TrustBundleDomainReltn reltn : retVal) {
                if (!reltn.getTrustBundle().getTrustBundleAnchors().isEmpty()) {
                    for (TrustBundleAnchor anchor : reltn.getTrustBundle().getTrustBundleAnchors()) {
                        anchor.getData();
                    }
                }
            }
        } catch (Exception e) {
            throw new ConfigurationStoreException("Failed to execute trust bundle relation DAO query.", e);
        }

        return retVal;
    }
}
