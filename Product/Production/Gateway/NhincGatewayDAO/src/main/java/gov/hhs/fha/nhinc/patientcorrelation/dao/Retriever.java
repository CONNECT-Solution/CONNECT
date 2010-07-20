/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.patientcorrelation.dao;

import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.patientcorrelation.model.CorrelatedIdentifiers;
import gov.hhs.fha.nhinc.patientcorrelation.model.QualifiedPatientIdentifier;
import gov.hhs.fha.nhinc.patientcorrelation.persistence.HibernateUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Expression;

/**
 *
 * @author rayj
 */
public class Retriever {

    static Log log = LogFactory.getLog(Retriever.class);

    public static List<QualifiedPatientIdentifier> retrievePatientCorrelation(QualifiedPatientIdentifier qualifiedPatientIdentifier, List<String> includeOnlyAssigningAuthorities) {
        List<QualifiedPatientIdentifier> qualifiedPatientIdentifiers = retrievePatientCorrelation(qualifiedPatientIdentifier);
        log.info("unfiltered list = " + qualifiedPatientIdentifiers.size() + " record(s)");
        qualifiedPatientIdentifiers = filterByIncludeList(qualifiedPatientIdentifiers, includeOnlyAssigningAuthorities);
        log.info("filtered list = " + qualifiedPatientIdentifiers.size() + " record(s)");
        return qualifiedPatientIdentifiers;
    }

    private static List<QualifiedPatientIdentifier> filterByIncludeList(List<QualifiedPatientIdentifier> qualifiedPatientIdentifiers, List<String> includeOnlyAssigningAuthorities) {
        List<QualifiedPatientIdentifier> filteredQualifiedPatientIdentifiers;

        if (NullChecker.isNotNullish(includeOnlyAssigningAuthorities)) {
            filteredQualifiedPatientIdentifiers = new ArrayList<QualifiedPatientIdentifier>();
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

    private static boolean isAssigningAuthorityInList(QualifiedPatientIdentifier qualifiedPatientIdentifier, List<String> assigningAuthorities) {
        boolean found = false;
        for (String assigningAuthority : assigningAuthorities) {
            if (qualifiedPatientIdentifier.getAssigningAuthorityId().contentEquals(assigningAuthority)) {
                found = true;
                break;
            }
        }

        return found;
    }

    public static List<QualifiedPatientIdentifier> retrievePatientCorrelation(QualifiedPatientIdentifier qualifiedPatientIdentifier) {
        log.debug("-- Begin CorrelatedIdentifiersDao.retrieveAllPatientCorrelation() ---");

        if (qualifiedPatientIdentifier == null) {
            throw new IllegalArgumentException("Missing required parameter: qualifiedPatientIdentifier");
        } else if (NullChecker.isNullish(qualifiedPatientIdentifier.getAssigningAuthorityId())) {
            throw new IllegalArgumentException("Missing required parameter: qualifiedPatientIdentifier.getAssigningAuthorityId");
        } else if (NullChecker.isNullish(qualifiedPatientIdentifier.getPatientId())) {
            throw new IllegalArgumentException("Missing required parameter: qualifiedPatientIdentifier.getPatientId");
        }

        CorrelatedIdentifiers criteria;

        criteria =
                new CorrelatedIdentifiers();
        criteria.setPatientId(qualifiedPatientIdentifier.getPatientId());
        criteria.setPatientAssigningAuthorityId(qualifiedPatientIdentifier.getAssigningAuthorityId());
        List<CorrelatedIdentifiers> result1 = retrievePatientCorrelation(criteria);

        criteria =
                new CorrelatedIdentifiers();
        criteria.setCorrelatedPatientId(qualifiedPatientIdentifier.getPatientId());
        criteria.setCorrelatedPatientAssigningAuthorityId(qualifiedPatientIdentifier.getAssigningAuthorityId());
        List<CorrelatedIdentifiers> result2 = retrievePatientCorrelation(criteria);

        List<CorrelatedIdentifiers> existingCorrelatedIdentifiers = unionList(result1, result2);

        List<QualifiedPatientIdentifier> resultQualifiedPatientIdentifiers = new ArrayList<QualifiedPatientIdentifier>();
        for (CorrelatedIdentifiers correlatedIdentifiers : existingCorrelatedIdentifiers) {
            QualifiedPatientIdentifier resultQualifiedPatientIdentifier;

            resultQualifiedPatientIdentifier =
                    new QualifiedPatientIdentifier();
            resultQualifiedPatientIdentifier.setAssigningAuthority(correlatedIdentifiers.getPatientAssigningAuthorityId());
            resultQualifiedPatientIdentifier.setPatientId(correlatedIdentifiers.getPatientId());
            if (!AreSame(qualifiedPatientIdentifier, resultQualifiedPatientIdentifier)) {
                resultQualifiedPatientIdentifiers.add(resultQualifiedPatientIdentifier);
            }

            resultQualifiedPatientIdentifier = new QualifiedPatientIdentifier();
            resultQualifiedPatientIdentifier.setAssigningAuthority(correlatedIdentifiers.getCorrelatedPatientAssigningAuthorityId());
            resultQualifiedPatientIdentifier.setPatientId(correlatedIdentifiers.getCorrelatedPatientId());

            if (!AreSame(qualifiedPatientIdentifier, resultQualifiedPatientIdentifier)) {
                resultQualifiedPatientIdentifiers.add(resultQualifiedPatientIdentifier);
            }

        }

        log.debug("Checking Expirations");

        log.info("resultQualifiedPatientIdentifiers=" + resultQualifiedPatientIdentifiers.size() + " record(s)");

        log.debug("-- End CorrelatedIdentifiersDao.retrieveAllPatientCorrelation() ---");
        return resultQualifiedPatientIdentifiers;
    }

    private static boolean AreSame(QualifiedPatientIdentifier a, QualifiedPatientIdentifier b) {
        return ((a.getAssigningAuthorityId().contentEquals(b.getAssigningAuthorityId())) && (a.getPatientId().contentEquals(b.getPatientId())));
    }

    private static List<CorrelatedIdentifiers> unionList(List<CorrelatedIdentifiers> list1, List<CorrelatedIdentifiers> list2) {
        if (list1 == null) {
            list1 = new ArrayList<CorrelatedIdentifiers>();
        }

        for (CorrelatedIdentifiers correlatedIdentifiers : list2) {
            list1.add(correlatedIdentifiers);
        }

        return list1;
    }

    public static boolean doesCorrelationExist(CorrelatedIdentifiers correlatedIdentifers) {
        boolean exists = false;

        CorrelatedIdentifiers criteria;

        List<CorrelatedIdentifiers> existingCorrelations;
        criteria =
                correlatedIdentifers;
        existingCorrelations =
                retrievePatientCorrelation(criteria);
        exists =
                NullChecker.isNotNullish(existingCorrelations);

        if (!exists) {
            criteria = new CorrelatedIdentifiers();
            criteria.setPatientId(correlatedIdentifers.getCorrelatedPatientId());
            criteria.setPatientAssigningAuthorityId(correlatedIdentifers.getCorrelatedPatientAssigningAuthorityId());
            criteria.setCorrelatedPatientId(correlatedIdentifers.getPatientId());
            criteria.setCorrelatedPatientAssigningAuthorityId(correlatedIdentifers.getPatientAssigningAuthorityId());

            existingCorrelations =
                    retrievePatientCorrelation(criteria);
            exists =
                    NullChecker.isNotNullish(existingCorrelations);
        }

        log.debug("correlation exists? = " + exists);
        return exists;
    }
    public static CorrelatedIdentifiers retrieveSinglePatientCorrelation(CorrelatedIdentifiers correlatedIdentifers)
    {
        List<CorrelatedIdentifiers> resultSet;
        CorrelatedIdentifiers result = null;

        resultSet = retrievePatientCorrelation(correlatedIdentifers);

        /*
         * AEGIS.net, Inc. (c) 2010 - Interop Test Platform
         * If empty resultSet, attempt retrieve again with reversed ids
         */
        if (resultSet != null && resultSet.size() == 0) {
            CorrelatedIdentifiers criteria = new CorrelatedIdentifiers();
            criteria.setPatientId(correlatedIdentifers.getCorrelatedPatientId());
            criteria.setPatientAssigningAuthorityId(correlatedIdentifers.getCorrelatedPatientAssigningAuthorityId());
            criteria.setCorrelatedPatientId(correlatedIdentifers.getPatientId());
            criteria.setCorrelatedPatientAssigningAuthorityId(correlatedIdentifers.getPatientAssigningAuthorityId());

            resultSet = retrievePatientCorrelation(criteria);
        }
        /* AEGIS.net, Inc. (c) 2010 - Interop Test Platform */

        if(resultSet != null)
        {
            if(resultSet.size() == 1)
            {
                result = resultSet.get(0);
            }
            else if(resultSet.size() > 1)
            {
                log.warn("return more than 1 result");
            }
        }

        return result;



    }
    private static List<CorrelatedIdentifiers> retrievePatientCorrelation(CorrelatedIdentifiers correlatedIdentifers) {
        SessionFactory fact = null;
        Session sess = null;
        List<CorrelatedIdentifiers> result = null;
//        List<CorrelatedIdentifiers> modifiedResult = null;

        try {
            fact = HibernateUtil.getSessionFactory();
            sess =
                    fact.openSession();

            Criteria criteria;

            criteria =
                    sess.createCriteria(CorrelatedIdentifiers.class);




            if (NullChecker.isNotNullish(correlatedIdentifers.getPatientAssigningAuthorityId())) {
                log.debug("Retrieving by patientAssigningAuthorityId=" + correlatedIdentifers.getPatientAssigningAuthorityId());
                criteria.add(Expression.eq("patientAssigningAuthorityId", correlatedIdentifers.getPatientAssigningAuthorityId()));
            }
            if (NullChecker.isNotNullish(correlatedIdentifers.getPatientId())) {
                log.debug("Retrieving by patientId=" + correlatedIdentifers.getPatientId());
                criteria.add(Expression.eq("patientId", correlatedIdentifers.getPatientId()));
            }
            if (NullChecker.isNotNullish(correlatedIdentifers.getCorrelatedPatientAssigningAuthorityId())) {
                log.debug("Retrieving by correlatedPatientAssigningAuthorityId=" + correlatedIdentifers.getCorrelatedPatientAssigningAuthorityId());
                criteria.add(Expression.eq("correlatedPatientAssigningAuthorityId", correlatedIdentifers.getCorrelatedPatientAssigningAuthorityId()));
            }
            if (NullChecker.isNotNullish(correlatedIdentifers.getCorrelatedPatientId())) {
                log.debug("Retrieving by correlatedPatientId=" + correlatedIdentifers.getCorrelatedPatientId());
                criteria.add(Expression.eq("correlatedPatientId", correlatedIdentifers.getCorrelatedPatientId()));
            }
            result = removeExpiredCorrelations(criteria.list());

            log.debug("Found " + result.size() + " record(s)");
        } finally {
            if (sess != null) {
                try {
                    sess.close();
                } catch (Throwable t) {
                    log.error("Failed to close session: " + t.getMessage(), t);
                }

            }
        }

        //only non-expired patient correlation records will be returned.
        //expired correlation records will be removed from the datebase.
//        modifiedResult = removeExpiredCorrelations(result);

//        return modifiedResult;
        return result;
    }

    /**
     * This method removes expired records from the list of records returned from the database
     * and also removes the expired records from the database.
     * @param result List of correlationIdentifiers objects returned from the database
     * @return Returns a list of correlationIdentifiers that have not expired
     */
    private static List<CorrelatedIdentifiers> removeExpiredCorrelations(List<CorrelatedIdentifiers> result) {
        List<CorrelatedIdentifiers> modifiedResult = new ArrayList<CorrelatedIdentifiers>();
        Date now = new Date();

        if (result != null)
        {
            //loop through list and remove the expired correlations from list then from db
            for (CorrelatedIdentifiers correlatedIdentifiers : result)
            {
                //do not delete a record if there isn't an expiration date.
                log.debug("~~~ expirationDate: " + correlatedIdentifiers.getCorrelationExpirationDate());

                if ((correlatedIdentifiers.getCorrelationExpirationDate() == null) ||
                    (now.before(correlatedIdentifiers.getCorrelationExpirationDate())))
                {
                    log.debug("patient correlation record has not expired");
                    modifiedResult.add(correlatedIdentifiers);
                }
                else
                {
                    log.debug("...removing expired patient correlation record...");
                    //remove expired record from database
                    Storer.removePatientCorrelation(correlatedIdentifiers);
                }
            }
        }

        return modifiedResult;
    }
}
