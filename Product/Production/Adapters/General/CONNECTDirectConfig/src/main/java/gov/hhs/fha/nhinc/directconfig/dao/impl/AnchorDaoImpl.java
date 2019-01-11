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

import gov.hhs.fha.nhinc.directconfig.dao.AnchorDao;
import gov.hhs.fha.nhinc.directconfig.dao.helpers.DaoUtils;
import gov.hhs.fha.nhinc.directconfig.entity.Anchor;
import gov.hhs.fha.nhinc.directconfig.entity.helpers.EntityStatus;
import gov.hhs.fha.nhinc.directconfig.exception.CertificateException;
import gov.hhs.fha.nhinc.directconfig.exception.ConfigurationStoreException;

import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

/**
 * Implementing class for Anchor DAO methods.
 *
 * @author ppyette
 */
@Repository
public class AnchorDaoImpl implements AnchorDao {

    private static final Log log = LogFactory.getLog(AnchorDaoImpl.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public Anchor load(String owner) {
        // Direct RI comment:
        // not sure what this will accomplish... multiple anchors are always possible for an owner
        Collection<Anchor> anchors = list(Arrays.asList(owner));
        Anchor anchor = null;

        if (CollectionUtils.isNotEmpty(anchors)) {
            anchor = anchors.iterator().next();
        }

        return anchor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Anchor> listAll() {
        List<Anchor> results = null;

        Session session = null;

        try {
            session = DaoUtils.getSession();

            results = session.getNamedQuery("getAllAnchors").list();

            if (results == null) {
                results = new ArrayList<>();
            }

            log.debug("Anchors found: " + results.size());

        } finally {
            DaoUtils.closeSession(session);
        }

        return results;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Anchor> list(List<String> owners) {
        List<Anchor> results = null;

        if (CollectionUtils.isEmpty(owners)) {
            results = listAll();
        } else {
            Session session = null;
            Query query;

            try {
                session = DaoUtils.getSession();

                query = session.getNamedQuery("getAnchorsByOwner");
                query.setParameterList("ownerList", owners);

                results = query.list();

                if (results == null) {
                    results = new ArrayList<>();
                }

                log.debug("Anchors found: " + results.size());

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
    public void add(Anchor anchor) {
        if (anchor != null) {
            Session session = null;
            Transaction tx = null;

            anchor.setId(null);
            anchor.setCreateTime(Calendar.getInstance());

            try {
                X509Certificate cert = anchor.toCertificate();

                if (anchor.getValidStartDate() == null) {
                    Calendar startDate = Calendar.getInstance();
                    startDate.setTime(cert.getNotBefore());
                    anchor.setValidStartDate(startDate);
                }

                if (anchor.getValidEndDate() == null) {
                    Calendar endDate = Calendar.getInstance();
                    endDate.setTime(cert.getNotAfter());
                    anchor.setValidEndDate(endDate);
                }

                if (anchor.getStatus() == null) {
                    anchor.setStatus(EntityStatus.NEW);
                }

                session = DaoUtils.getSession();

                log.debug("Saving anchor");

                tx = session.beginTransaction();
                session.persist(anchor);
                tx.commit();

            } catch (CertificateException e) {
                log.error("Could not convert anchor data to X509Certificate");
                DaoUtils.rollbackTransaction(tx, e);
                throw new ConfigurationStoreException(e);
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
    public void save(Anchor anchor) {
        // Deleted due to non-terminating circular reference
        throw new ConfigurationStoreException(new NotImplementedException());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(List<Anchor> anchorList) {
        // Deleted due to non-terminating circular reference
        throw new ConfigurationStoreException(new NotImplementedException());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Anchor> listByIds(List<Long> anchorIds) {
        List<Anchor> results = null;

        if (CollectionUtils.isEmpty(anchorIds)) {
            results = Collections.emptyList();
        } else {
            Session session = null;
            Query query;

            try {
                session = DaoUtils.getSession();

                query = session.getNamedQuery("getAnchorsByIds");
                query.setParameterList("idList", anchorIds);

                results = query.list();

                if (results == null) {
                    results = new ArrayList<>();
                }

                log.debug("Anchors found: " + results.size());

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
    public void setStatus(List<Long> anchorIDs, EntityStatus status) {
        List<Anchor> anchors = listByIds(anchorIDs);

        if (CollectionUtils.isNotEmpty(anchors)) {
            Session session = null;
            Transaction tx = null;

            try {
                session = DaoUtils.getSession();

                tx = session.beginTransaction();

                for (Anchor anchor : anchors) {
                    anchor.setStatus(status);
                    session.merge(anchor);
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
        if (owner != null) {
            Session session = null;
            Transaction tx = null;

            List<String> owners = new ArrayList<>();
            owners.add(owner);

            List<Anchor> anchors = list(owners);

            if (CollectionUtils.isNotEmpty(anchors)) {
                try {
                    session = DaoUtils.getSession();

                    tx = session.beginTransaction();

                    for (Anchor anchor : anchors) {
                        anchor.setStatus(status);
                        session.merge(anchor);
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
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(List<Long> ids) {
        if (CollectionUtils.isNotEmpty(ids)) {
            Session session = null;
            Transaction tx = null;
            Query query;

            int count;

            try {
                session = DaoUtils.getSession();

                tx = session.beginTransaction();

                query = session.createQuery("DELETE FROM Anchor a WHERE a.id IN (:idList)");
                query.setParameterList("idList", ids);

                count = query.executeUpdate();
                tx.commit();

                log.debug("Deleted " + count + " Anchors");

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

                query = session.createQuery("DELETE FROM Anchor a WHERE UPPER(a.owner) = :owner");
                query.setParameter("owner", owner.toUpperCase(Locale.getDefault()));

                count = query.executeUpdate();
                tx.commit();

                log.debug("Deleted " + count + " Anchors");

            } catch (Exception e) {
                DaoUtils.rollbackTransaction(tx, e);
                throw new ConfigurationStoreException(e);
            } finally {
                DaoUtils.closeSession(session);
            }
        }
    }
}
