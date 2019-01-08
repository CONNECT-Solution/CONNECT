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
package gov.hhs.fha.nhinc.patientcorrelation.nhinc.dao;

import static gov.hhs.fha.nhinc.util.GenericDBUtils.closeSession;

import gov.hhs.fha.nhinc.patientcorrelation.nhinc.model.CorrelatedIdentifiers;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.persistence.HibernateUtil;
import gov.hhs.fha.nhinc.persistence.HibernateUtilFactory;
import gov.hhs.fha.nhinc.util.GenericDBUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author rayj
 */
public class Storer {

    private static final Logger LOG = LoggerFactory.getLogger(Storer.class);

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

        CorrelatedIdentifiers singleRecord = Retriever.retrieveSinglePatientCorrelation(correlatedIdentifers);
        singleRecord.setCorrelationExpirationDate(correlatedIdentifers.getCorrelationExpirationDate());
        GenericDBUtils.save(getSession(), singleRecord);

        LOG.debug("-- End CorrelatedIdentifiersDao.localUpdatePatientCorrelation() ---");
    }

    private static void localAddPatientCorrelation(CorrelatedIdentifiers correlatedIdentifers) {
        LOG.debug("-- Begin CorrelatedIdentifiersDao.addPatientCorrelation() ---");
        GenericDBUtils.save(getSession(), correlatedIdentifers);
        LOG.debug("-- End CorrelatedIdentifiersDao.addPatientCorrelation() ---");
    }

    public static void removePatientCorrelation(CorrelatedIdentifiers correlatedIdentifers) {
        LOG.debug("-- Begin CorrelatedIdentifiersDao.removePatientCorrelation() ---");
        Session sess = null;
        Transaction trans = null;

        String deleteCorrelatedIdentifiersSQL = "delete from correlatedidentifiers "
            + "where ((PatientAssigningAuthorityId = :patientAssigningAuthority " + "and PatientId= :patientId "
            + "and CorrelatedPatientAssignAuthId= :correlatedPatientAssignAuthId "
            + "and CorrelatedPatientId= :correlatedPatientId) "
            + "or (PatientAssigningAuthorityId= :correlatedPatientAssignAuthId "
            + "and PatientId= :correlatedPatientId "
            + "and CorrelatedPatientAssignAuthId= :patientAssigningAuthority "
            + "and CorrelatedPatientId= :patientId))";

        // get the SQL bind values
        String paramPatientAssigningAuthority = correlatedIdentifers.getPatientAssigningAuthorityId();
        String paramPatientId = correlatedIdentifers.getPatientId();
        String paramCorrelatedPatientAssignAuthId = correlatedIdentifers.getCorrelatedPatientAssigningAuthorityId();
        String paramCorrelatedPatientId = correlatedIdentifers.getCorrelatedPatientId();
        try {
            sess = getSession();
            if (null != sess) {
                trans = sess.beginTransaction();
                Query query = sess.createSQLQuery(deleteCorrelatedIdentifiersSQL);
                query.setString("patientAssigningAuthority", paramPatientAssigningAuthority);
                query.setString("patientId", paramPatientId);
                query.setString("correlatedPatientAssignAuthId", paramCorrelatedPatientAssignAuthId);
                query.setString("correlatedPatientId", paramCorrelatedPatientId);
                // delete the rows
                int rowsDeleted = query.executeUpdate();
                // commit the trasaction
                trans.commit();
                if (rowsDeleted != 0) {
                    LOG.debug("Total Rows Deleted from table correlatedidentifiers -->" + rowsDeleted);
                }
            }
        } catch (HibernateException | NullPointerException ex) {
            GenericDBUtils.rollbackTransaction(trans);
            LOG.error("error encounter during the removePatientCorrelation: {}", ex.getLocalizedMessage(), ex);
        } finally {
            closeSession(sess);
        }
        LOG.debug("-- End CorrelatedIdentifiersDao.removePatientCorrelation() ---");
    }

    protected static Session getSession() {
        HibernateUtil util = HibernateUtilFactory.getPatientCorrHibernateUtil();
        if (null == util) {
            LOG.error("Session factory was null");
            return null;
        }

        SessionFactory fact = null;
        fact = util.getSessionFactory();
        if (null == fact) {
            LOG.error("Failed to obtain a session from the sessionFactory");
            return null;
        }

        return fact.openSession();
    }

}
