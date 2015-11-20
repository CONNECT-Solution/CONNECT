/*
 * Copyright (c) 2009-2015, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.common.connectionmanager.dao;

import gov.hhs.fha.nhinc.common.connectionmanager.model.AssigningAuthorityToHomeCommunityMapping;
import gov.hhs.fha.nhinc.common.connectionmanager.persistence.HibernateUtil;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 *
 * @author svalluripalli
 */
public class AssigningAuthorityHomeCommunityMappingDAO {

    private static final Logger LOG = LoggerFactory.getLogger(AssigningAuthorityHomeCommunityMappingDAO.class);

    /**
     * This method retrieves and returns a AssigningAuthority for an Home Community...
     *
     * @param homeCommunityId
     * @return String
     */
    public String getAssigningAuthority(String homeCommunityId) {
        LOG.trace("--Begin AssigningAuthorityHomeCommunityMappingDAO.getACommunityIdForAssigningAuthority() ---");
        String assigningAuthId = "";
        List<String> aaids = getAssigningAuthoritiesByHomeCommunity(homeCommunityId);
        if (aaids.size() > 1) {
            assigningAuthId = aaids.get(0);
        }
        LOG.trace("--End AssigningAuthorityHomeCommunityMappingDAO.getACommunityIdForAssigningAuthority() ---");
        return assigningAuthId;
    }

    /**
     * returns List of Assigning Authorities for a given Home Community Id
     *
     * @param homeCommId
     * @return List
     */
    @SuppressWarnings("unchecked")
    public List<String> getAssigningAuthoritiesByHomeCommunity(String homeCommunityId) {
        LOG.trace("-- Begin AssigningAuthorityHomeCommunityMappingDAO.getAssigningAuthoritiesByHomeCommunity() ---");
        Session sess = null;
        List<String> listOfAAs = new ArrayList<String>();
        if (homeCommunityId != null && !homeCommunityId.equals("")) {
            SessionFactory fact = HibernateUtil.getSessionFactory();
            try {
                sess = fact.openSession();
                if (sess != null) {
                    Query namedQuery = sess.getNamedQuery("findAAByHomeCommunityId");
                    namedQuery.setParameter("homeCommunityId", homeCommunityId);
                    for (AssigningAuthorityToHomeCommunityMapping mapping : (List<AssigningAuthorityToHomeCommunityMapping>) namedQuery.list()) {
                        listOfAAs.add(mapping.getAssigningAuthorityId());
                    }

                } else {
                    LOG.error("Unable create Hibernate Sessions");
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
        } else {
            LOG.error("Please provide a valid homeCommunityId");
        }
        if (LOG.isTraceEnabled()) {
            LOG.trace("-- End AssigningAuthorityHomeCommunityMappingDAO.getAssigningAuthoritiesByHomeCommunity() ---");
            LOG.trace("getAssigningAuthoritiesByHomeCommunity - listOfAAs.size: " + listOfAAs.size());
        }
        return listOfAAs;
    }

    /**
     * This method retrieves Home Community for an Assigning Authority...
     *
     * @param assigningAuthority
     */
    public String getHomeCommunityId(String assigningAuthority) {
        LOG.debug("--Begin AssigningAuthorityHomeCommunityMappingDAO.getAllCommunityIdsForAllAssigningAuthorities() ---");
        String homeCommunity = "";
        if (assigningAuthority != null && !assigningAuthority.equals("")) {
            Session sess = null;
            SessionFactory fact = HibernateUtil.getSessionFactory();
            try {
                sess = fact.openSession();
                if (sess != null) {
                    Query namedQuery = sess.getNamedQuery("findAAByAAId");
                    namedQuery.setParameter("assigningAuthorityId", assigningAuthority);
                    List<AssigningAuthorityToHomeCommunityMapping> l = namedQuery.list();
                    if (l != null && l.size() > 0) {
                        homeCommunity = l.get(0).getHomeCommunityId();
                    }
                } else {
                    LOG.error("Unable to create session");
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
        } else {
            LOG.error("Enter correct assigning authority");
        }
        LOG.debug("--End AssigningAuthorityHomeCommunityMappingDAO.getAllCommunityIdsForAllAssigningAuthorities() ---");
        return homeCommunity;
    }

    /**
     * This method stores Assigning Authority To Home Community Mapping...
     *
     * @param homeCommunityId
     * @param assigningAuthority
     */
    public boolean storeMapping(String homeCommunityId, String assigningAuthority) {
        LOG.debug("--Begin AssigningAuthorityHomeCommunityMappingDAO.storeAssigningAuthorityAndHomeCommunity() ---");
        System.out
                .println("--Begin AssigningAuthorityHomeCommunityMappingDAO.storeAssigningAuthorityAndHomeCommunity() ---");
        boolean success = false;
        AssigningAuthorityToHomeCommunityMapping mappingInfo = null;
        Transaction trans = null;
        Session sess = null;
        if (homeCommunityId != null && !homeCommunityId.equals("") && assigningAuthority != null
                && !assigningAuthority.equals("")) {
            SessionFactory fact = HibernateUtil.getSessionFactory();
            try {
                sess = fact.openSession();
                if (sess != null) {
                    Query namedQuery = sess.getNamedQuery("findAAByAAIdAndHomeCommunityId");
                    namedQuery.setParameter("assigningAuthorityId", assigningAuthority);
                    namedQuery.setParameter("homeCommunityId", homeCommunityId);
                    List<AssigningAuthorityToHomeCommunityMapping> l = namedQuery.list();

                    if (l != null && l.size() > 0) {
                        LOG.info("Assigning Authority and Home Community pair already present in the repository");
                    } else {
                        mappingInfo = new AssigningAuthorityToHomeCommunityMapping();
                        mappingInfo.setAssigningAuthorityId(assigningAuthority);
                        mappingInfo.setHomeCommunityId(homeCommunityId);
                        trans = sess.beginTransaction();
                        sess.saveOrUpdate(mappingInfo);
                        success = true;
                    }
                } else {
                    LOG.error("Unable to create session information");
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
        } else {
            LOG.error("Invalid data entered, Enter Valid data to store");
        }
        LOG.debug("--End AssigningAuthorityHomeCommunityMappingDAO.storeAssigningAuthorityAndHomeCommunity() ---");
        System.out
                .println("--End AssigningAuthorityHomeCommunityMappingDAO.storeAssigningAuthorityAndHomeCommunity() ---");
        return success;
    }
}
