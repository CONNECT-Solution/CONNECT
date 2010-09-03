/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.patientcorrelation.nhinc.dao;

import gov.hhs.fha.nhinc.patientcorrelation.nhinc.model.CorrelatedIdentifiers;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.model.QualifiedPatientIdentifier;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.persistence.HibernateUtil;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;

/**
 *
 * @author rayj
 */
public class Storer {

    static Log log = LogFactory.getLog(Storer.class);

    public static void addPatientCorrelation(CorrelatedIdentifiers correlatedIdentifers) {
        log.info("patient correlation add requested");
        if (!Retriever.doesCorrelationExist(correlatedIdentifers)) {
            localAddPatientCorrelation(correlatedIdentifers);
        } else if(correlatedIdentifers.getCorrelationExpirationDate() != null)
        {
            log.info("updating expiration date");
            localUpdatePatientCorrelation(correlatedIdentifers);
        }
        else{
            log.info("Correlation already exists, no store needed");
        }
    }

    private static void localUpdatePatientCorrelation(CorrelatedIdentifiers correlatedIdentifers)
    {
        log.debug("-- Begin CorrelatedIdentifiersDao.localUpdatePatientCorrelation() ---");
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
        log.debug("-- End CorrelatedIdentifiersDao.localUpdatePatientCorrelation() ---");
    }
    private static void localAddPatientCorrelation(CorrelatedIdentifiers correlatedIdentifers) {
        log.debug("-- Begin CorrelatedIdentifiersDao.addPatientCorrelation() ---");
        Session sess = null;
        Transaction trans = null;

        try {
            SessionFactory fact = HibernateUtil.getSessionFactory();
            if (fact != null) {
                sess = fact.openSession();
                trans = sess.beginTransaction();
                sess.saveOrUpdate(correlatedIdentifers);
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
        log.debug("-- End CorrelatedIdentifiersDao.addPatientCorrelation() ---");
    }

    public static  void removePatientCorrelation(CorrelatedIdentifiers correlatedIdentifers) {
        log.debug("-- Begin CorrelatedIdentifiersDao.removePatientCorrelation() ---");
        Session sess = null;
        Transaction trans = null;
        boolean result = false;
        String param1 = correlatedIdentifers.getPatientAssigningAuthorityId();
        String param2 = correlatedIdentifers.getPatientId();
        String param3 = correlatedIdentifers.getCorrelatedPatientAssigningAuthorityId();
        String param4 = correlatedIdentifers.getCorrelatedPatientId();
        String sql = "delete from correlatedidentifiers where ((PatientAssigningAuthorityId='" + param1 + "' and PatientId='" + param2 + "' and CorrelatedPatientAssignAuthId='" +
                param3 + "' and CorrelatedPatientId='" + param4 + "') or (PatientAssigningAuthorityId='" + param3 + "' and PatientId='" + param4 + "' and CorrelatedPatientAssignAuthId='" +
                param1 + "' and CorrelatedPatientId='" + param2 + "'))";
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
                    log.error("Unable to create session...");
                }
            } else {
                log.error("Unable to create Factory...");
            }
        } catch (HibernateException exp) {
            exp.printStackTrace();
        } finally {
            if (trans != null && trans.isActive()) {
                try {
                    trans.rollback();
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
        log.debug("-- End CorrelatedIdentifiersDao.removePatientCorrelation() ---");
    }
}
