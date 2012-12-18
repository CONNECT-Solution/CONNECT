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
package gov.hhs.fha.nhinc.patientcorrelation.nhinc.dao;

import gov.hhs.fha.nhinc.patientcorrelation.nhinc.model.CorrelatedIdentifiers;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.persistence.HibernateUtil;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 * 
 * @author rayj
 */
public class Storer {

    private static final Logger LOG = Logger.getLogger(Storer.class);

    public static void addPatientCorrelation(CorrelatedIdentifiers correlatedIdentifers) {
        LOG.info("patient correlation add requested");
        if (!Retriever.doesCorrelationExist(correlatedIdentifers)) {
            localAddPatientCorrelation(correlatedIdentifers);
        } else if (correlatedIdentifers.getCorrelationExpirationDate() != null) {
            LOG.info("updating expiration date");
            localUpdatePatientCorrelation(correlatedIdentifers);
        } else {
            LOG.info("Correlation already exists, no store needed");
        }
    }

    private static void localUpdatePatientCorrelation(CorrelatedIdentifiers correlatedIdentifers) {
        LOG.debug("-- Begin CorrelatedIdentifiersDao.localUpdatePatientCorrelation() ---");
        Session sess = null;
        Transaction trans = null;
        CorrelatedIdentifiers singleRecord = Retriever.retrieveSinglePatientCorrelation(correlatedIdentifers);

        singleRecord.setCorrelationExpirationDate(correlatedIdentifers.getCorrelationExpirationDate());

        try {
            SessionFactory fact = HibernateUtil.getSessionFactory();
            if (fact != null) {
                sess = fact.openSession();
                trans = sess.beginTransaction();
                sess.saveOrUpdate(singleRecord);
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
        LOG.debug("-- End CorrelatedIdentifiersDao.localUpdatePatientCorrelation() ---");
    }

    private static void localAddPatientCorrelation(CorrelatedIdentifiers correlatedIdentifers) {
        LOG.debug("-- Begin CorrelatedIdentifiersDao.addPatientCorrelation() ---");
        Session sess = null;
        Transaction trans = null;

        try {
            SessionFactory fact = HibernateUtil.getSessionFactory();
            if (fact != null) {
                sess = fact.openSession();
                trans = sess.beginTransaction();
                sess.saveOrUpdate(correlatedIdentifers);
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
        LOG.debug("-- End CorrelatedIdentifiersDao.addPatientCorrelation() ---");
    }

    public static void removePatientCorrelation(CorrelatedIdentifiers correlatedIdentifers) {
        LOG.debug("-- Begin CorrelatedIdentifiersDao.removePatientCorrelation() ---");
        Session sess = null;
        Transaction trans = null;
        boolean result = false;
        String param1 = correlatedIdentifers.getPatientAssigningAuthorityId();
        String param2 = correlatedIdentifers.getPatientId();
        String param3 = correlatedIdentifers.getCorrelatedPatientAssigningAuthorityId();
        String param4 = correlatedIdentifers.getCorrelatedPatientId();
        String sql = "delete from correlatedidentifiers where ((PatientAssigningAuthorityId='" + param1
                + "' and PatientId='" + param2 + "' and CorrelatedPatientAssignAuthId='" + param3
                + "' and CorrelatedPatientId='" + param4 + "') or (PatientAssigningAuthorityId='" + param3
                + "' and PatientId='" + param4 + "' and CorrelatedPatientAssignAuthId='" + param1
                + "' and CorrelatedPatientId='" + param2 + "'))";
        try {
            SessionFactory fact = HibernateUtil.getSessionFactory();
            if (fact != null) {
                System.out.println("Factory Created...");
                sess = fact.openSession();
                if (sess != null) {
                    trans = sess.beginTransaction();
                    int rowsDeleted = sess.createSQLQuery(sql).executeUpdate();
                    trans.commit();
                    if (rowsDeleted != 0) {
                        result = true;
                    }
                } else {
                    LOG.error("Unable to create session...");
                }
            } else {
                LOG.error("Unable to create Factory...");
            }
        } catch (HibernateException exp) {
            exp.printStackTrace();
        } finally {
            if (trans != null && trans.isActive()) {
                try {
                    trans.rollback();
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
        LOG.debug("-- End CorrelatedIdentifiersDao.removePatientCorrelation() ---");
    }
}
