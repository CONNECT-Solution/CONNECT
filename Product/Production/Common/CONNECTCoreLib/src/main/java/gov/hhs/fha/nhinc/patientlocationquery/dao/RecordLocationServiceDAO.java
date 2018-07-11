/**
 *
 */
package gov.hhs.fha.nhinc.patientlocationquery.dao;

import gov.hhs.fha.nhinc.patientcorrelation.nhinc.model.RecordLocatorService;
import gov.hhs.fha.nhinc.persistence.HibernateUtilFactory;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ptambellini
 *
 */
public class RecordLocationServiceDAO {

    private static final Logger LOG = LoggerFactory.getLogger(RecordLocationServiceDAO.class);

    private RecordLocationServiceDAO() {

    }

    public static List<RecordLocatorService> getAllPatientsBy(String requestedPatientId) {
        LOG.debug("Beginning Patient Location Query");

        List<RecordLocatorService> recLocService = new ArrayList<>();

        try (Session sess = getSession()){

            if (sess != null) {

                Criteria criteria = sess.createCriteria(RecordLocatorService.class);

                if (requestedPatientId != null) {
                    criteria.add(Restrictions.eq("requestedPatientId", requestedPatientId));
                }

                recLocService = criteria.list();

                LOG.debug("Completed retrieve of Patient Location Query. {} results returned.", recLocService.size());
            } else {
                LOG.error("Failed to obtain a session from the sessionFactory");
            }

        }
        return recLocService;
    }

    protected static Session getSession() {
        Session session = null;
        try {
            session = HibernateUtilFactory.getPatientCorrHibernateUtil()
                .getSessionFactory()
                .openSession();
        } catch (HibernateException e) {
            LOG.error("Fail to openSession: {}, {}", e.getMessage(), e);
        }
        return session;
    }

}
