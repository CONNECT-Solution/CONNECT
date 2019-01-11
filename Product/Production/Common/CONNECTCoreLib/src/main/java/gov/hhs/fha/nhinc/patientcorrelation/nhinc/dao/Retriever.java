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

import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.model.CorrelatedIdentifiers;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.model.QualifiedPatientIdentifier;
import gov.hhs.fha.nhinc.persistence.HibernateUtilFactory;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Expression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author rayj
 */
public class Retriever {

    private static final Logger LOG = LoggerFactory.getLogger(Retriever.class);

    public static List<QualifiedPatientIdentifier> retrievePatientCorrelation(
            QualifiedPatientIdentifier qualifiedPatientIdentifier, List<String> includeOnlyAssigningAuthorities) {
        List<QualifiedPatientIdentifier> qualifiedPatientIdentifiers = retrievePatientCorrelation(
                qualifiedPatientIdentifier);
        LOG.info("unfiltered list = " + qualifiedPatientIdentifiers.size() + " record(s)");
        qualifiedPatientIdentifiers = filterByIncludeList(qualifiedPatientIdentifiers, includeOnlyAssigningAuthorities);
        LOG.info("filtered list = " + qualifiedPatientIdentifiers.size() + " record(s)");
        return qualifiedPatientIdentifiers;
    }

    private static List<QualifiedPatientIdentifier> filterByIncludeList(
            List<QualifiedPatientIdentifier> qualifiedPatientIdentifiers,
            List<String> includeOnlyAssigningAuthorities) {
        List<QualifiedPatientIdentifier> filteredQualifiedPatientIdentifiers;

        if (NullChecker.isNotNullish(includeOnlyAssigningAuthorities)) {
            filteredQualifiedPatientIdentifiers = new ArrayList<>();
            for (QualifiedPatientIdentifier qualifiedPatientIdentifier : qualifiedPatientIdentifiers) {
                if (isAssigningAuthorityInList(qualifiedPatientIdentifier, includeOnlyAssigningAuthorities)) {
                    filteredQualifiedPatientIdentifiers.add(qualifiedPatientIdentifier);
                }
            }
        } else {
            filteredQualifiedPatientIdentifiers = qualifiedPatientIdentifiers;
        }
        return filteredQualifiedPatientIdentifiers;
    }

    private static boolean isAssigningAuthorityInList(QualifiedPatientIdentifier qualifiedPatientIdentifier,
            List<String> assigningAuthorities) {
        boolean found = false;
        for (String assigningAuthority : assigningAuthorities) {
            if (qualifiedPatientIdentifier.getAssigningAuthorityId().contentEquals(assigningAuthority)) {
                found = true;
                break;
            }
        }

        return found;
    }

    public static List<QualifiedPatientIdentifier> retrievePatientCorrelation(
            QualifiedPatientIdentifier qualifiedPatientIdentifier) {
        LOG.debug("-- Begin CorrelatedIdentifiersDao.retrieveAllPatientCorrelation() ---");

        if (qualifiedPatientIdentifier == null) {
            throw new IllegalArgumentException("Missing required parameter: qualifiedPatientIdentifier");
        } else if (NullChecker.isNullish(qualifiedPatientIdentifier.getAssigningAuthorityId())) {
            throw new IllegalArgumentException(
                    "Missing required parameter: qualifiedPatientIdentifier.getAssigningAuthorityId");
        } else if (NullChecker.isNullish(qualifiedPatientIdentifier.getPatientId())) {
            throw new IllegalArgumentException("Missing required parameter: qualifiedPatientIdentifier.getPatientId");
        }

        CorrelatedIdentifiers criteria;

        criteria = new CorrelatedIdentifiers();
        criteria.setPatientId(qualifiedPatientIdentifier.getPatientId());
        criteria.setPatientAssigningAuthorityId(qualifiedPatientIdentifier.getAssigningAuthorityId());
        List<CorrelatedIdentifiers> result1 = retrievePatientCorrelation(criteria);

        criteria = new CorrelatedIdentifiers();
        criteria.setCorrelatedPatientId(qualifiedPatientIdentifier.getPatientId());
        criteria.setCorrelatedPatientAssigningAuthorityId(qualifiedPatientIdentifier.getAssigningAuthorityId());
        List<CorrelatedIdentifiers> result2 = retrievePatientCorrelation(criteria);

        List<CorrelatedIdentifiers> existingCorrelatedIdentifiers = unionList(result1, result2);

        List<QualifiedPatientIdentifier> resultQualifiedPatientIdentifiers = new ArrayList<>();
        for (CorrelatedIdentifiers correlatedIdentifiers : existingCorrelatedIdentifiers) {
            QualifiedPatientIdentifier resultQualifiedPatientIdentifier;

            resultQualifiedPatientIdentifier = new QualifiedPatientIdentifier();
            resultQualifiedPatientIdentifier
                    .setAssigningAuthority(correlatedIdentifiers.getPatientAssigningAuthorityId());
            resultQualifiedPatientIdentifier.setPatientId(correlatedIdentifiers.getPatientId());
            if (!AreSame(qualifiedPatientIdentifier, resultQualifiedPatientIdentifier)) {
                resultQualifiedPatientIdentifiers.add(resultQualifiedPatientIdentifier);
            }

            resultQualifiedPatientIdentifier = new QualifiedPatientIdentifier();
            resultQualifiedPatientIdentifier
                    .setAssigningAuthority(correlatedIdentifiers.getCorrelatedPatientAssigningAuthorityId());
            resultQualifiedPatientIdentifier.setPatientId(correlatedIdentifiers.getCorrelatedPatientId());

            if (!AreSame(qualifiedPatientIdentifier, resultQualifiedPatientIdentifier)) {
                resultQualifiedPatientIdentifiers.add(resultQualifiedPatientIdentifier);
            }

        }

        LOG.debug("Checking Expirations");

        LOG.info("resultQualifiedPatientIdentifiers=" + resultQualifiedPatientIdentifiers.size() + " record(s)");

        LOG.debug("-- End CorrelatedIdentifiersDao.retrieveAllPatientCorrelation() ---");
        return resultQualifiedPatientIdentifiers;
    }

    private static boolean AreSame(QualifiedPatientIdentifier a, QualifiedPatientIdentifier b) {
        return a.getAssigningAuthorityId().contentEquals(b.getAssigningAuthorityId())
                && a.getPatientId().contentEquals(b.getPatientId());
    }

    private static List<CorrelatedIdentifiers> unionList(List<CorrelatedIdentifiers> list1,
            List<CorrelatedIdentifiers> list2) {
        if (list1 == null) {
            list1 = new ArrayList<>();
        }

        for (CorrelatedIdentifiers correlatedIdentifiers : list2) {
            list1.add(correlatedIdentifiers);
        }

        return list1;
    }

    public static boolean doesCorrelationExist(CorrelatedIdentifiers correlatedIdentifers) {
        boolean exists;

        CorrelatedIdentifiers criteria;

        List<CorrelatedIdentifiers> existingCorrelations;
        criteria = correlatedIdentifers;
        existingCorrelations = retrievePatientCorrelation(criteria);
        exists = NullChecker.isNotNullish(existingCorrelations);

        if (!exists) {
            criteria = new CorrelatedIdentifiers();
            criteria.setPatientId(correlatedIdentifers.getCorrelatedPatientId());
            criteria.setPatientAssigningAuthorityId(correlatedIdentifers.getCorrelatedPatientAssigningAuthorityId());
            criteria.setCorrelatedPatientId(correlatedIdentifers.getPatientId());
            criteria.setCorrelatedPatientAssigningAuthorityId(correlatedIdentifers.getPatientAssigningAuthorityId());
            if (NullChecker.isNotNullish(correlatedIdentifers.getrlsId())) {
            criteria.setRlsId(correlatedIdentifers.getrlsId());
            }
            existingCorrelations = retrievePatientCorrelation(criteria);
            exists = NullChecker.isNotNullish(existingCorrelations);
        }

        LOG.debug("correlation exists? = " + exists);
        return exists;
    }

    public static CorrelatedIdentifiers retrieveSinglePatientCorrelation(CorrelatedIdentifiers correlatedIdentifers) {
        List<CorrelatedIdentifiers> resultSet;
        CorrelatedIdentifiers result = new CorrelatedIdentifiers();

        resultSet = retrievePatientCorrelation(correlatedIdentifers);

        /*
         * AEGIS.net, Inc. (c) 2010 - Interop Test Platform If empty resultSet, attempt retrieve again with reversed ids
         */
        if (resultSet != null && resultSet.isEmpty()) {
            CorrelatedIdentifiers criteria = new CorrelatedIdentifiers();
            criteria.setPatientId(correlatedIdentifers.getCorrelatedPatientId());
            criteria.setPatientAssigningAuthorityId(correlatedIdentifers.getCorrelatedPatientAssigningAuthorityId());
            criteria.setCorrelatedPatientId(correlatedIdentifers.getPatientId());
            criteria.setCorrelatedPatientAssigningAuthorityId(correlatedIdentifers.getPatientAssigningAuthorityId());

            resultSet = retrievePatientCorrelation(criteria);
        }
        /* AEGIS.net, Inc. (c) 2010 - Interop Test Platform */

        if (resultSet != null) {
            if (resultSet.size() > 1) {
                LOG.warn("return more than 1 result");
            }
            result = resultSet.get(0);
        }

        return result;

    }

    private static List<CorrelatedIdentifiers> retrievePatientCorrelation(CorrelatedIdentifiers correlatedIdentifers) {
        SessionFactory fact;
        Session sess = null;
        List<CorrelatedIdentifiers> result = null;

        try {
            fact = HibernateUtilFactory.getPatientCorrHibernateUtil().getSessionFactory();
            sess = fact.openSession();

            Criteria criteria;

            criteria = sess.createCriteria(CorrelatedIdentifiers.class);

            if (NullChecker.isNotNullish(correlatedIdentifers.getPatientAssigningAuthorityId())) {
                LOG.debug("Retrieving by patientAssigningAuthorityId="
                        + correlatedIdentifers.getPatientAssigningAuthorityId());
                criteria.add(Expression.eq("patientAssigningAuthorityId",
                        correlatedIdentifers.getPatientAssigningAuthorityId()));
            }
            if (NullChecker.isNotNullish(correlatedIdentifers.getPatientId())) {
                LOG.debug("Retrieving by patientId=" + correlatedIdentifers.getPatientId());
                criteria.add(Expression.eq("patientId", correlatedIdentifers.getPatientId()));
            }
            if (NullChecker.isNotNullish(correlatedIdentifers.getCorrelatedPatientAssigningAuthorityId())) {
                LOG.debug("Retrieving by correlatedPatientAssigningAuthorityId="
                        + correlatedIdentifers.getCorrelatedPatientAssigningAuthorityId());
                criteria.add(Expression.eq("correlatedPatientAssigningAuthorityId",
                        correlatedIdentifers.getCorrelatedPatientAssigningAuthorityId()));
            }
            if (NullChecker.isNotNullish(correlatedIdentifers.getCorrelatedPatientId())) {
                LOG.debug("Retrieving by correlatedPatientId=" + correlatedIdentifers.getCorrelatedPatientId());
                criteria.add(Expression.eq("correlatedPatientId", correlatedIdentifers.getCorrelatedPatientId()));
            }
            result = removeExpiredCorrelations(criteria.list());

            LOG.debug("Found " + result.size() + " record(s)");
        } finally {
            if (sess != null) {
                try {
                    sess.close();
                } catch (HibernateException he) {
                    LOG.error("Failed to close session: " + he.getMessage(), he);
                }

            }
        }

        return result;
    }

    /**
     * This method removes expired records from the list of records returned from the database and also removes the
     * expired records from the database.
     *
     * @param result List of correlationIdentifiers objects returned from the database
     * @return Returns a list of correlationIdentifiers that have not expired
     */
    private static List<CorrelatedIdentifiers> removeExpiredCorrelations(List<CorrelatedIdentifiers> result) {
        List<CorrelatedIdentifiers> modifiedResult = new ArrayList<>();
        Date now = new Date();

        if (result != null) {
            // loop through list and remove the expired correlations from list then from db
            for (CorrelatedIdentifiers correlatedIdentifiers : result) {
                // do not delete a record if there isn't an expiration date.
                if (correlatedIdentifiers.getCorrelationExpirationDate() == null
                        || now.before(correlatedIdentifiers.getCorrelationExpirationDate())) {
                    LOG.debug("patient correlation record has not expired");
                    modifiedResult.add(correlatedIdentifiers);
                } else {
                    LOG.debug("...removing expired patient correlation record...");
                    // remove expired record from database
                    Storer.removePatientCorrelation(correlatedIdentifiers);
                }
            }
        }

        return modifiedResult;
    }

}
