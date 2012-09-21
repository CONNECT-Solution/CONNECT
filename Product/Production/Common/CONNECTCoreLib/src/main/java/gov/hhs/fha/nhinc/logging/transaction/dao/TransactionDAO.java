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
package gov.hhs.fha.nhinc.logging.transaction.dao;

import java.util.List;

import gov.hhs.fha.nhinc.logging.transaction.model.TransactionRepo;
import gov.hhs.fha.nhinc.logging.transaction.persistance.HibernateUtil;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;

/**
 * TransactionDAO provides methods to query and update the transrepo database.
 * @author jasonasmith
 *
 */
public class TransactionDAO {
	
	private static Log log = LogFactory.getLog(TransactionDAO.class);
	private static TransactionDAO transDAOInstance = new TransactionDAO();
	
	/**
	 * Constructor
	 */
	private TransactionDAO(){
		log.info("TransactionDAO initialized");
	}
	
	/**
	 * Returns the instance of the DAO according to the Singleton Pattern
	 * @return
	 */
	public static TransactionDAO getTransactionDAOInstance(){
		log.debug("getTransactionDAOInstance()...");
		return transDAOInstance;
	}
	
	/**
	 * Inserts a single TransactionRepo object into the database, returns boolean on
	 * success or failure.
	 * @param transactionRepo
	 * @return
	 */
	public boolean insertIntoTransactionRepo(TransactionRepo transactionRepo){
		
		log.debug("TransactionDAO.insertIntoTransactionRepo() - Begin");
        Session session = null;
        Transaction tx = null;
        boolean result = true;

        if (transactionRepo != null) {
            try {
                SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
                session = sessionFactory.openSession();
                tx = session.beginTransaction();
                log.info("Inserting Record...");

                session.persist(transactionRepo);

                log.info("TransactionRepo Inserted successfully...");
                tx.commit();
            } catch (Exception e) {
                result = false;
                if (tx != null) {
                    tx.rollback();
                }
                log.error("Exception during insertion caused by :" + e.getMessage(), e);
            } finally {
                if (session != null) {
                    session.close();
                }
            }
        }
        log.debug("TransactionDAO.insertIntoTransactionRepo() - End");
        return result;
	}
	
	/**
	 * Queries the database for a transaction record using the messageId.
	 * @param messageId
	 * @return
	 */
	public String getTransactionId(String messageId){
		log.debug("TransactionDAO.getTransactinId() - Begin");

        if (NullChecker.isNullish(messageId)) {
            log.info("-- MessageId Parameter is required for Transaction Query --");
            log.debug("TransactionDAO.getTransactinId() - End");
            return null;
        }
        
        Session session = null;
        List<TransactionRepo> queryList = null;
        TransactionRepo foundRecord = null;
        String transactionId = null;
        
        try {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            session = sessionFactory.openSession();
            log.info("Getting Records");

            Criteria aCriteria = session.createCriteria(TransactionRepo.class);
            
            aCriteria.add(Expression.eq("messageId", messageId));

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
        
        if(foundRecord != null)
        	transactionId = foundRecord.getTransactionId();
        
        return transactionId;
	}
	
	

}
