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

 Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer
 in the documentation and/or other materials provided with the distribution.
 3. Neither the name of the The NHIN Direct Project (nhindirect.org) nor the names of its contributors may be used to endorse or promote
 products derived from this software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS
 BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 THE POSSIBILITY OF SUCH DAMAGE.
 */
package gov.hhs.fha.nhinc.directconfig.dao.impl;

import gov.hhs.fha.nhinc.directconfig.dao.CertificateDao;
import gov.hhs.fha.nhinc.directconfig.dao.helpers.DaoUtils;
import gov.hhs.fha.nhinc.directconfig.entity.Certificate;
import gov.hhs.fha.nhinc.directconfig.entity.Certificate.CertContainer;
import gov.hhs.fha.nhinc.directconfig.entity.helpers.EntityStatus;
import gov.hhs.fha.nhinc.directconfig.exception.CertificateException;
import gov.hhs.fha.nhinc.directconfig.exception.ConfigurationStoreException;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

/**
 * Implementing class for Certificate DAO methods.
 *
 * @author ppyette
 */
@Repository
public class CertificateDaoImpl implements CertificateDao {

    private static final Log LOG = LogFactory.getLog(CertificateDaoImpl.class);

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings({ "unchecked" })
    public Certificate load(String owner, String thumbprint) {
        Session session = null;
        Query query;

        List<Certificate> results;
        Certificate cert = null;

        try {
            session = DaoUtils.getSession();

            query = session.getNamedQuery("getCertificates");

            query.setParameter("thumbprint", thumbprint);

            if (owner != null) {
                owner = owner.toUpperCase(Locale.getDefault());
            }

            query.setParameter("owner", owner);

            results = query.list();

            if (CollectionUtils.isNotEmpty(results)) {
                cert = results.iterator().next();
                LOG.debug("Certificate found");
            }
        } finally {
            DaoUtils.closeSession(session);
        }

        return cert;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings({ "unchecked" })
    public List<Certificate> list(List<Long> idList) {
        List<Certificate> results = Collections.emptyList();

        if (CollectionUtils.isNotEmpty(idList)) {
            Session session = null;
            Query query;

            try {
                session = DaoUtils.getSession();

                query = session.getNamedQuery("getCertificatesByIds");
                query.setParameterList("idList", idList);

                results = query.list();

                if (results == null) {
                    results = Collections.emptyList();
                }

                LOG.debug("Certificates found: " + results.size());
            } finally {
                DaoUtils.closeSession(session);
            }
        }

        return results;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings({ "unchecked" })
    public List<Certificate> list(String owner) {
        List<Certificate> results = null;

        Session session = null;
        Query query;

        try {
            session = DaoUtils.getSession();

            query = session.getNamedQuery("getCertificatesByOwner");
            query.setParameter("owner", owner);

            results = query.list();

            if (results == null) {
                results = Collections.emptyList();
            }

            LOG.debug("Certificates found: " + results.size());
        } finally {
            DaoUtils.closeSession(session);
        }

        return results;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(Certificate cert) {
        if (cert != null) {
            Session session = null;
            Transaction tx = null;

            cert.setId(null);
            cert.setCreateTime(Calendar.getInstance());

            try {
                CertContainer container = null;
                X509Certificate xcert = null;

                container = cert.toCredential();
                xcert = container.getCert();

                if (cert.getValidStartDate() == null && xcert != null) {
                    Calendar startDate = Calendar.getInstance();
                    startDate.setTime(xcert.getNotBefore());
                    cert.setValidStartDate(startDate);
                }

                if (cert.getValidEndDate() == null && xcert != null) {
                    Calendar endDate = Calendar.getInstance();
                    endDate.setTime(xcert.getNotAfter());
                    cert.setValidEndDate(endDate);
                }

                if (cert.getStatus() == null) {
                    cert.setStatus(EntityStatus.NEW);
                }

                cert.setPrivateKey(container.getKey() != null);

                session = DaoUtils.getSession();

                LOG.debug("Saving anchor");

                tx = session.beginTransaction();
                session.persist(cert);
                tx.commit();

            } catch (CertificateException | HibernateException e) {
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
    public void save(List<Certificate> certList) {
        if (CollectionUtils.isNotEmpty(certList)) {
            for (Certificate cert : certList) {
                save(cert);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setStatus(List<Long> certificateIDs, EntityStatus status) {
        List<Certificate> certs = list(certificateIDs);

        if (CollectionUtils.isNotEmpty(certs)) {
            Session session = null;
            Transaction tx = null;

            LOG.debug("Setting status on " + certs.size() + " certs");

            try {
                session = DaoUtils.getSession();

                tx = session.beginTransaction();

                for (Certificate cert : certs) {
                    cert.setStatus(status);
                    session.merge(cert);
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
    public void setStatus(String owner, EntityStatus status) {
        List<Certificate> certs = list(owner);

        if (CollectionUtils.isNotEmpty(certs)) {
            Session session = null;
            Transaction tx = null;

            LOG.debug("Setting status on " + certs.size() + " certs");

            try {
                session = DaoUtils.getSession();

                tx = session.beginTransaction();

                for (Certificate cert : certs) {
                    cert.setStatus(status);
                    session.merge(cert);
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
    public void delete(List<Long> idList) {
        if (CollectionUtils.isNotEmpty(idList)) {
            Session session = null;
            Transaction tx = null;
            Query query;

            int count;

            try {
                session = DaoUtils.getSession();

                tx = session.beginTransaction();

                query = session.createQuery("DELETE FROM Certificate c WHERE c.id IN (:idList)");
                query.setParameterList("idList", idList);

                count = query.executeUpdate();
                tx.commit();

                LOG.debug("Deleted " + count + " Certificates");

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
    public void delete(String owner) {
        if (owner != null) {
            Session session = null;
            Transaction tx = null;
            Query query;

            int count;

            try {
                session = DaoUtils.getSession();

                tx = session.beginTransaction();

                query = session.createQuery("DELETE FROM Certificate c WHERE UPPER(c.owner) = :owner");
                query.setParameter("owner", owner.toUpperCase(Locale.getDefault()));

                count = query.executeUpdate();
                tx.commit();

                LOG.debug("Deleted " + count + " Certificates");

            } catch (Exception e) {
                DaoUtils.rollbackTransaction(tx, e);
                throw new ConfigurationStoreException(e);
            } finally {
                DaoUtils.closeSession(session);
            }
        }
    }
}
