package gov.hhs.fha.nhinc.directconfig.dao.helpers;

import gov.hhs.fha.nhinc.directconfig.persistence.HibernateUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class DaoUtils {

    private static final Log log = LogFactory.getLog(DaoUtils.class);

    public static String toParamString(List<String> list) {
        StringBuffer nameListBuffer = null;
        String nameList = null;

        if (list != null) {
            for (String name : list) {
                if (nameListBuffer == null) {
                    nameListBuffer = new StringBuffer("(");
                } else {
                    nameListBuffer.append(", ");
                }

                nameListBuffer.append("'").append(name.toUpperCase(Locale.getDefault())).append("'");
            }

            if (nameListBuffer != null) {
                nameListBuffer.append(")");

                nameList = nameListBuffer.toString();
            }
        }

        log.debug("Name List: " + nameList);

        return nameList;
    }

    public static String toIdString(List<Long> list) {
        StringBuffer idListBuffer = null;
        String idList = null;

        if (list != null) {
            for (Long id : list) {
                if (idListBuffer == null) {
                    idListBuffer = new StringBuffer("(");
                } else {
                    idListBuffer.append(", ");
                }

                idListBuffer.append(id);
            }

            if (idListBuffer != null) {
                idListBuffer.append(")");

                idList = idListBuffer.toString();
            }
        }

        log.debug("ID List: " + idList);

        return idList;
    }

    public static String toParamString(Collection<String> list) {
        return toParamString(new ArrayList<String>(list));
    }

    public static String toIdString(Collection<Long> list) {
        return toIdString(new ArrayList<Long>(list));
    }

    public static Session getSession() {
        Session session = null;
        SessionFactory fact = HibernateUtil.getSessionFactory();

        if (fact != null) {
            session = fact.openSession();
        } else {
            log.error("No Session available - SessionFactory is null.");
        }

        return session;
    }

    /*
     * TODO: Comments
     */
    public static void closeSession(Session session) {
        if (session != null) {
            try {
                session.close();
            } catch (Exception e) {
                log.error("Failed to close session: " + e.getMessage(), e);
            }
        }
    }

    /*
     * TODO: Comments
     */
    public static void rollbackTransaction(Transaction tx) {
        if (tx != null) {
            try {
                log.error("Failed to commit transaction, attempting rollback...");
                tx.rollback();
            } catch (Exception e) {
                log.error("Failed to rollback transaction: " + e.getMessage(), e);
            }
        }
    }
}
