/**
 *
 */
package gov.hhs.fha.nhinc.patientlocationquery.dao;

import gov.hhs.fha.nhinc.patientcorrelation.nhinc.model.RecordLocatorService;
import gov.hhs.fha.nhinc.persistence.HibernateUtilFactory;
import gov.hhs.fha.nhinc.util.GenericDBUtils;
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
public class RecordLocationServiceDao {

    private static final Logger LOG = LoggerFactory.getLogger(RecordLocationServiceDao.class);

    public List<RecordLocatorService> getAllPatientsBy(String rlsId) {
        LOG.debug("Beginning Patient Location Query");

        List<RecordLocatorService> recLocService = new ArrayList<>();
        Session sess = null;
        try {
            sess = getSession();
            if (sess != null) {

                Criteria criteria = sess.createCriteria(RecordLocatorService.class);

                if (rlsId != null) {
                    criteria.add(Restrictions.eq("rlsId", rlsId));
                }

                recLocService = criteria.list();

                LOG.debug("Completed retrieve of Patient Location Query. {} results returned.", recLocService.size());
            } else {
                LOG.error("Failed to obtain a session from the sessionFactory");
            }

        } finally {
            GenericDBUtils.closeSession(sess);
        }
        return recLocService;
    }

    protected static Session getSession() {
        Session session = null;
        try {
            session = HibernateUtilFactory.getDocRepoHibernateUtil().getSessionFactory().openSession();
        } catch (HibernateException e) {
            LOG.error("Fail to openSession: {}, {}", e.getMessage(), e);
        }
        return session;
    }
}
