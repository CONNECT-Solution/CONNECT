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
package gov.hhs.fha.nhinc.perfrepo.dao;

import gov.hhs.fha.nhinc.common.entityperformancelogquery.CountDataType;
import gov.hhs.fha.nhinc.common.entityperformancelogquery.DetailDataType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.perfrepo.persistance.HibernateUtil;
import gov.hhs.fha.nhinc.perfrepo.model.Perfrepository;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.criterion.Expression;

/**
 * PerfrepositoryDao Class provides methods to query and update Performance Data to/from MySQL Database using Hibernate
 * 
 * @author richard.ettema
 */
public class PerfrepositoryDao {

    private static Log log = LogFactory.getLog(PerfrepositoryDao.class);
    private static PerfrepositoryDao perfDAO = new PerfrepositoryDao();
    private static final String DIRECTION_INBOUND = "inbound";
    private static final String DIRECTION_OUTBOUND = "outbound";
    private static final String DIRECTION_ERROR = "error";
    private static final String PERF_EXPECTED_INBOUND = "performanceLogInbound";
    private static final String PERF_EXPECTED_OUTBOUND = "performanceLogOutbound";
    private static final String PERF_EXPECTED_ERRORS = "performanceLogErrors";

    /**
     * Constructor
     */
    private PerfrepositoryDao() {
        log.info("PerfrepositoryDao - Initialized");
    }

    /**
     * Singleton instance returned...
     * 
     * @return PerfrepositoryDao
     */
    public static PerfrepositoryDao getPerfrepositoryDaoInstance() {
        log.debug("getPerfrepositoryDaoInstance()..");
        return perfDAO;
    }

    /**
     * Insert a single <code>Perfrepository</code> record. The generated id will be available in the perfRecord.
     * 
     * @param perfRecord
     * @return boolean
     */
    public boolean insertPerfrepository(Perfrepository perfRecord) {
        log.debug("PerfrepositoryDAO.insertPerfrepository() - Begin");
        Session session = null;
        Transaction tx = null;
        boolean result = true;

        if (perfRecord != null) {
            try {
                SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
                session = sessionFactory.openSession();
                tx = session.beginTransaction();
                log.info("Inserting Record...");

                session.persist(perfRecord);

                log.info("Perfrepository Inserted seccussfully...");
                tx.commit();
            } catch (Exception e) {
                result = false;
                if (tx != null) {
                    tx.rollback();
                }
                log.error("Exception during insertion caused by :" + e.getMessage(), e);
            } finally {
                // Actual event_log insertion will happen at this step
                if (session != null) {
                    session.close();
                }
            }
        }
        log.debug("PerfrepositoryDAO.insertPerfrepository() - End");
        return result;
    }

    /**
     * Update a single <code>Perfrepository</code> record.
     * 
     * @param perfRecord
     * @return boolean
     */
    public boolean updatePerfrepository(Perfrepository perfRecord) {
        log.debug("PerfrepositoryDAO.updatePerfrepository() - Begin");
        Session session = null;
        Transaction tx = null;
        boolean result = true;

        if (perfRecord != null) {
            try {
                SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
                session = sessionFactory.openSession();
                tx = session.beginTransaction();
                log.info("Updating Record...");

                session.saveOrUpdate(perfRecord);

                log.info("Perfrepository Updated seccussfully...");
                tx.commit();
            } catch (Exception e) {
                result = false;
                if (tx != null) {
                    tx.rollback();
                }
                log.error("Exception during update caused by :" + e.getMessage(), e);
            } finally {
                // Actual event_log update will happen at this step
                if (session != null) {
                    session.close();
                }
            }
        }
        log.debug("PerfrepositoryDAO.updatePerfrepository() - End");
        return result;
    }

    /**
     * This method does a query to the database to get a Performance Log record based on a known id.
     * 
     * @param id
     * @return Perfrepository
     */
    public Perfrepository getPerfrepository(Long id) {
        log.debug("PerfrepositoryDao.getPerfrepository() - Begin");

        if (id == null) {
            log.info("-- id Parameter is required for Performance Query --");
            log.debug("PerfrepositoryDAO.getPerfrepository() - End");
            return null;
        }

        Session session = null;
        List<Perfrepository> queryList = null;
        Perfrepository foundRecord = null;
        try {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            session = sessionFactory.openSession();
            log.info("Getting Records");

            // Build the criteria
            Criteria aCriteria = session.createCriteria(Perfrepository.class);

            aCriteria.add(Expression.eq("id", id));

            queryList = aCriteria.list();

            if (queryList != null && queryList.size() > 0) {
                foundRecord = queryList.get(0);
            }
        } catch (Exception e) {
            log.error("Exception in getPerfrepository() occured due to :" + e.getMessage(), e);
        } finally {
            // Flush and close session
            if (session != null) {
                session.flush();
                session.close();
            }
        }
        log.debug("PerfrepositoryDAO.getPerfrepository() - End");
        return foundRecord;
    }

    /**
     * This method does a query to the database to get Performance Log records based on a datetime range.
     * 
     * @param beginTime
     * @param endTime
     * @return List
     */
    public List<Perfrepository> getPerfrepositoryRange(Timestamp beginTime, Timestamp endTime) {
        log.debug("PerfrepositoryDao.getAuditRepositoryOnCriteria() - Begin");

        if (beginTime == null || endTime == null) {
            log.info("-- Range Parameters are required for Performance Query --");
            log.debug("PerfrepositoryDao.queryAuditRepositoryOnCriteria() - End");
            return null;
        }

        Session session = null;
        List<Perfrepository> queryList = null;
        try {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            session = sessionFactory.openSession();
            log.info("Getting Records");

            // Build the criteria
            Criteria aCriteria = session.createCriteria(Perfrepository.class);

            aCriteria.add(Expression.between("starttime", beginTime, endTime));

            queryList = aCriteria.list();
        } catch (Exception e) {
            log.error("Exception in getPerfrepositoryRange() occured due to :" + e.getMessage(), e);
        } finally {
            // Flush and close session
            if (session != null) {
                session.flush();
                session.close();
            }
        }
        log.debug("PerfrepositoryDao.queryAuditRepositoryOnCriteria() - End");
        return queryList;
    }

    /**
     * <p>
     * This method does a query to the database to get the count statistic data from the Performance Log records based
     * on a datetime range.
     * <p>
     * Please note: The returned List will always be populated with three(3) instances of the <code>CountDataType</code>
     * class; one for each direction: Inbound, Outbound, Error
     * 
     * @param beginTime
     * @param endTime
     * @return List
     */
    public List<CountDataType> getPerfrepositoryCountRange(Timestamp beginTime, Timestamp endTime) {
        log.debug("PerfrepositoryDao.getPerfrepositoryCountRange() - Begin");

        if (beginTime == null || endTime == null) {
            log.info("-- Range Parameters are required for Performance Query --");
            log.debug("PerfrepositoryDao.getPerfrepositoryCountRange() - End");
            return null;
        }

        Session session = null;
        List<CountDataType> queryList = null;
        try {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            session = sessionFactory.openSession();
            log.info("Getting Records");

            // Build the query
            Query sqlQuery = session
                    .createSQLQuery(
                            "SELECT direction, COUNT(direction) AS countval FROM perfrepo.perfrepository WHERE time BETWEEN ? AND ? GROUP BY direction")
                    .addScalar("direction", Hibernate.STRING).addScalar("countval", Hibernate.LONG);

            List<Object[]> result = sqlQuery.setTimestamp(0, beginTime).setTimestamp(1, endTime).list();

            if (result != null && result.size() > 0) {
                String[] directionArray = new String[result.size()];
                Long[] countvalArray = new Long[result.size()];
                int counter = 0;
                for (Object[] row : result) {
                    directionArray[counter] = row[0].toString();
                    countvalArray[counter] = new Long(row[1].toString());
                    counter++;
                }

                // Initialize the three(3) count data directions
                CountDataType inboundData = new CountDataType();
                inboundData.setType(DIRECTION_INBOUND);
                inboundData.setCount(new Long(0));
                inboundData.setExpected(getPerfMonitorExpectedInbound());
                CountDataType outboundData = new CountDataType();
                outboundData.setType(DIRECTION_OUTBOUND);
                outboundData.setCount(new Long(0));
                outboundData.setExpected(getPerfMonitorExpectedOutbound());
                CountDataType errorData = new CountDataType();
                errorData.setType(DIRECTION_ERROR);
                errorData.setCount(new Long(0));
                errorData.setExpected(getPerfMonitorExpectedErrors());

                queryList = new ArrayList<CountDataType>();
                for (int i = 0; i < directionArray.length; i++) {
                    if (directionArray[i].equalsIgnoreCase(DIRECTION_INBOUND)) {
                        inboundData.setCount(countvalArray[i]);
                    } else if (directionArray[i].equalsIgnoreCase(DIRECTION_OUTBOUND)) {
                        outboundData.setCount(countvalArray[i]);
                    } else if (directionArray[i].equalsIgnoreCase(DIRECTION_ERROR)) {
                        errorData.setCount(countvalArray[i]);
                    }
                }

                queryList.add(inboundData);
                queryList.add(outboundData);
                queryList.add(errorData);
            }
        } catch (Exception e) {
            log.error("Exception in getPerfrepositoryCountRange() occured due to :" + e.getMessage(), e);
        } finally {
            // Flush and close session
            if (session != null) {
                session.flush();
                session.close();
            }
        }
        log.debug("PerfrepositoryDao.getPerfrepositoryCountRange() - End");
        return queryList;
    }

    /**
     * This method does a query to the database to get the detail statistic data from the Performance Log records based
     * on a datetime range.
     * 
     * @param beginTime
     * @param endTime
     * @return List
     */
    public List<DetailDataType> getPerfrepositoryDetailRange(Timestamp beginTime, Timestamp endTime) {
        log.debug("PerfrepositoryDao.getPerfrepositoryDetailRange() - Begin");

        if (beginTime == null || endTime == null) {
            log.info("-- Range Parameters are required for Performance Query --");
            log.debug("PerfrepositoryDao.getPerfrepositoryDetailRange() - End");
            return null;
        }

        Session session = null;
        List<DetailDataType> queryList = null;
        try {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            session = sessionFactory.openSession();
            log.info("Getting Records");

            // Build the query
            Query sqlQuery = session
                    .createSQLQuery(
                            "SELECT servicetype, messagetype, direction, AVG(duration) AS avgduration, MIN(duration) AS minduration, MAX(duration) AS maxduration, COUNT(direction) AS countval FROM perfrepo.perfrepository WHERE UPPER(direction) <> 'ERROR' AND starttime BETWEEN ? AND ? GROUP BY servicetype, messagetype, direction")
                    .addScalar("servicetype", Hibernate.STRING).addScalar("messagetype", Hibernate.STRING)
                    .addScalar("direction", Hibernate.STRING).addScalar("avgduration", Hibernate.DOUBLE)
                    .addScalar("minduration", Hibernate.LONG).addScalar("maxduration", Hibernate.LONG)
                    .addScalar("countval", Hibernate.LONG);

            List<Object[]> result = sqlQuery.setTimestamp(0, beginTime).setTimestamp(1, endTime).list();

            if (result != null && result.size() > 0) {
                String[] servicetypeArray = new String[result.size()];
                String[] messagetypeArray = new String[result.size()];
                String[] directionArray = new String[result.size()];
                BigDecimal[] avgdurationArray = new BigDecimal[result.size()];
                Long[] mindurationArray = new Long[result.size()];
                Long[] maxdurationArray = new Long[result.size()];
                Long[] countvalArray = new Long[result.size()];
                int counter = 0;
                for (Object[] row : result) {
                    servicetypeArray[counter] = row[0].toString();
                    messagetypeArray[counter] = row[1].toString();
                    directionArray[counter] = row[2].toString();
                    avgdurationArray[counter] = new BigDecimal(row[3].toString());
                    mindurationArray[counter] = new Long(row[4].toString());
                    maxdurationArray[counter] = new Long(row[5].toString());
                    countvalArray[counter] = new Long(row[6].toString());
                    counter++;
                }

                queryList = new ArrayList<DetailDataType>();
                for (int i = 0; i < directionArray.length; i++) {
                    DetailDataType detailData = new DetailDataType();
                    detailData.setServiceType(servicetypeArray[i]);
                    detailData.setMessageType(messagetypeArray[i]);
                    detailData.setDirection(directionArray[i]);
                    detailData.setAvgDuration(avgdurationArray[i]);
                    detailData.setMinDuration(mindurationArray[i]);
                    detailData.setMaxDuration(maxdurationArray[i]);
                    detailData.setCount(countvalArray[i]);
                    queryList.add(detailData);
                }
            }
        } catch (Exception e) {
            log.error("Exception in getPerfrepositoryDetailRange() occured due to :" + e.getMessage(), e);
        } finally {
            // Flush and close session
            if (session != null) {
                session.flush();
                session.close();
            }
        }
        log.debug("PerfrepositoryDao.getPerfrepositoryDetailRange() - End");
        return queryList;
    }

    /**
     * This method does a query to the database to get the error statistic data from the Performance Log records based
     * on a datetime range.
     * 
     * @param beginTime
     * @param endTime
     * @return List
     */
    public List<DetailDataType> getPerfrepositoryErrorRange(Timestamp beginTime, Timestamp endTime) {
        log.debug("PerfrepositoryDao.getPerfrepositoryErrorRange() - Begin");

        if (beginTime == null || endTime == null) {
            log.info("-- Range Parameters are required for Performance Query --");
            log.debug("PerfrepositoryDao.getPerfrepositoryErrorRange() - End");
            return null;
        }

        Session session = null;
        List<DetailDataType> queryList = null;
        try {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            session = sessionFactory.openSession();
            log.info("Getting Records");

            // Build the query
            Query sqlQuery = session
                    .createSQLQuery(
                            "SELECT servicetype, messagetype, direction, AVG(duration) AS avgduration, MIN(duration) AS minduration, MAX(duration) AS maxduration, COUNT(direction) AS countval FROM perfrepo.perfrepository WHERE UPPER(direction) = 'ERROR' AND starttime BETWEEN ? AND ? GROUP BY servicetype, messagetype, direction")
                    .addScalar("servicetype", Hibernate.STRING).addScalar("messagetype", Hibernate.STRING)
                    .addScalar("direction", Hibernate.STRING).addScalar("avgduration", Hibernate.DOUBLE)
                    .addScalar("minduration", Hibernate.LONG).addScalar("maxduration", Hibernate.LONG)
                    .addScalar("countval", Hibernate.LONG);

            List<Object[]> result = sqlQuery.setTimestamp(0, beginTime).setTimestamp(1, endTime).list();

            if (result != null && result.size() > 0) {
                String[] servicetypeArray = new String[result.size()];
                String[] messagetypeArray = new String[result.size()];
                String[] directionArray = new String[result.size()];
                BigDecimal[] avgdurationArray = new BigDecimal[result.size()];
                Long[] mindurationArray = new Long[result.size()];
                Long[] maxdurationArray = new Long[result.size()];
                Long[] countvalArray = new Long[result.size()];
                int counter = 0;
                for (Object[] row : result) {
                    servicetypeArray[counter] = row[0].toString();
                    messagetypeArray[counter] = row[1].toString();
                    directionArray[counter] = row[2].toString();
                    avgdurationArray[counter] = new BigDecimal(row[3].toString());
                    mindurationArray[counter] = new Long(row[4].toString());
                    maxdurationArray[counter] = new Long(row[5].toString());
                    countvalArray[counter] = new Long(row[6].toString());
                    counter++;
                }

                queryList = new ArrayList<DetailDataType>();
                for (int i = 0; i < directionArray.length; i++) {
                    DetailDataType detailData = new DetailDataType();
                    detailData.setServiceType(servicetypeArray[i]);
                    detailData.setMessageType(messagetypeArray[i]);
                    detailData.setDirection(directionArray[i]);
                    detailData.setAvgDuration(avgdurationArray[i]);
                    detailData.setMinDuration(mindurationArray[i]);
                    detailData.setMaxDuration(maxdurationArray[i]);
                    detailData.setCount(countvalArray[i]);
                    queryList.add(detailData);
                }
            }
        } catch (Exception e) {
            log.error("Exception in getPerfrepositoryErrorRange() occured due to :" + e.getMessage(), e);
        } finally {
            // Flush and close session
            if (session != null) {
                session.flush();
                session.close();
            }
        }
        log.debug("PerfrepositoryDao.getPerfrepositoryErrorRange() - End");
        return queryList;
    }

    /**
     * Return gateway property key perf.monitor.expected.inbound value
     * 
     * @return String gateway property value
     */
    private static Long getPerfMonitorExpectedInbound() {
        Long inbound = null;
        try {
            // Use CONNECT utility class to access gateway.properties
            String inboundString = PropertyAccessor.getInstance().getProperty(NhincConstants.GATEWAY_PROPERTY_FILE,
                    PERF_EXPECTED_INBOUND);
            if (inboundString != null) {
                inbound = new Long(inboundString);
            }
        } catch (PropertyAccessException pae) {
            log.error("Error: Failed to retrieve " + PERF_EXPECTED_INBOUND + " from property file: "
                    + NhincConstants.GATEWAY_PROPERTY_FILE);
            log.error(pae.getMessage());
            inbound = null;
        } catch (NumberFormatException nfe) {
            log.error("Error: Failed to convert " + PERF_EXPECTED_INBOUND + " from property file: "
                    + NhincConstants.GATEWAY_PROPERTY_FILE);
            log.error(nfe.getMessage());
            inbound = null;
        }
        return inbound;
    }

    /**
     * Return gateway property key perf.monitor.expected.outbound value
     * 
     * @return String gateway property value
     */
    private static Long getPerfMonitorExpectedOutbound() {
        Long outbound = null;
        try {
            // Use CONNECT utility class to access gateway.properties
            String outboundString = PropertyAccessor.getInstance().getProperty(NhincConstants.GATEWAY_PROPERTY_FILE,
                    PERF_EXPECTED_OUTBOUND);
            if (outboundString != null) {
                outbound = new Long(outboundString);
            }
        } catch (PropertyAccessException pae) {
            log.error("Error: Failed to retrieve " + PERF_EXPECTED_OUTBOUND + " from property file: "
                    + NhincConstants.GATEWAY_PROPERTY_FILE);
            log.error(pae.getMessage());
            outbound = null;
        } catch (NumberFormatException nfe) {
            log.error("Error: Failed to convert " + PERF_EXPECTED_INBOUND + " from property file: "
                    + NhincConstants.GATEWAY_PROPERTY_FILE);
            log.error(nfe.getMessage());
            outbound = null;
        }
        return outbound;
    }

    /**
     * Return gateway property key perf.monitor.expected.errors value
     * 
     * @return String gateway property value
     */
    private static Long getPerfMonitorExpectedErrors() {
        Long errors = null;
        try {
            // Use CONNECT utility class to access gateway.properties
            String errorsString = PropertyAccessor.getInstance().getProperty(NhincConstants.GATEWAY_PROPERTY_FILE,
                    PERF_EXPECTED_ERRORS);
            if (errorsString != null) {
                errors = new Long(errorsString);
            }
        } catch (PropertyAccessException pae) {
            log.error("Error: Failed to retrieve " + PERF_EXPECTED_ERRORS + " from property file: "
                    + NhincConstants.GATEWAY_PROPERTY_FILE);
            log.error(pae.getMessage());
            errors = null;
        } catch (NumberFormatException nfe) {
            log.error("Error: Failed to convert " + PERF_EXPECTED_INBOUND + " from property file: "
                    + NhincConstants.GATEWAY_PROPERTY_FILE);
            log.error(nfe.getMessage());
            errors = null;
        }
        return errors;
    }

}
