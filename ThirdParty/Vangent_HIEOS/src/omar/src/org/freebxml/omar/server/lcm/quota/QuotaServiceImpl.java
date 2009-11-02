/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/lcm/quota/QuotaServiceImpl.java,v 1.13 2006/08/24 20:42:25 farrukh_najmi Exp $
 * ====================================================================
 */
/*
$Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/lcm/quota/QuotaServiceImpl.java,v 1.13 2006/08/24 20:42:25 farrukh_najmi Exp $
*/
package org.freebxml.omar.server.lcm.quota;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.freebxml.omar.common.BindingUtility;
import javax.xml.registry.RegistryException;
import org.freebxml.omar.common.exceptions.QuotaExceededException;
import org.freebxml.omar.server.common.RegistryProperties;
import org.freebxml.omar.server.persistence.PersistenceManagerFactory;
import org.freebxml.omar.server.repository.RepositoryManager;
import org.freebxml.omar.server.repository.RepositoryManagerFactory;

import org.oasis.ebxml.registry.bindings.query.ResponseOption;
import org.oasis.ebxml.registry.bindings.query.ReturnType;
import org.oasis.ebxml.registry.bindings.rim.ObjectRef;

import java.util.Iterator;
import java.util.List;


public class QuotaServiceImpl {
    private static QuotaServiceImpl instance = null;
    private static long quotaLimit; // in MB
    private static long quotaLimitInBytes;
    private static final Log log = LogFactory.getLog(QuotaServiceImpl.class);

    public synchronized static QuotaServiceImpl getInstance() {
        if (instance == null) {
            instance = new QuotaServiceImpl();
            quotaLimit = Integer.parseInt(RegistryProperties.getInstance()
                                                            .getProperty("omar.repository.quota"));
            quotaLimitInBytes = quotaLimit * 1024 * 1024;
        }

        return instance;
    }

    /**
    *     @throws QuotaExceededException if the size of already submitted items exceeds
    *     the quota specified in ebxmlrr.properties. It simply counts the number of bytes
    *     of previously submitted items and throws the exception on *next* request. So
    *     a single request can submit an item whose size is bigger than the quota limit.
    *     @throws RegistryException if there is a IOException when getting the items sizes.
    */
    public void checkQuota(String userId)
        throws QuotaExceededException, RegistryException {
        //TODO: Fix after AuditableEvents have been fixed for V3
        /*
        try {            
            // Get the id of items already submitted by this user
            String sql = "SELECT eo.* FROM ExtrinsicObject eo WHERE id IN " +
                "(SELECT ae.registryObject FROM AuditableEvent ae WHERE ae.eventType='Created' AND " +
                "ae.user_='" + userId + "')";

            //System.err.println(sql);
            ResponseOption responseOption = BindingUtility.getInstance().queryFac.createResponseOption();
            responseOption.setReturnType(ReturnType.OBJECT_REF);

            List results = PersistenceManagerFactory.getInstance()
                                                    .getPersistenceManager()
                                                    .executeSQLQuery(sql,
                    responseOption, "ExtrinsicObject", new java.util.ArrayList());

            // Get the total size of the repository items already submitted
            Iterator resultsIter = results.iterator();
            List itemsIds = new java.util.ArrayList();

            while (resultsIter.hasNext()) {
                ObjectRef objectRef = (ObjectRef) resultsIter.next();
                itemsIds.add(objectRef.getId());

                //System.err.println(objectRef.getId());    
            }

            RepositoryManager repManager = RepositoryManagerFactory.getInstance()
                                                                   .getRepositoryManager();
            long totalSizeOfSubmittedItems = repManager.getItemsSize(itemsIds);

            //System.err.println("Size: " + totalSizeOfSubmittedItems);
            if (totalSizeOfSubmittedItems > quotaLimitInBytes) {
                throw new QuotaExceededException(userId, quotaLimit);
            }
        } catch (javax.xml.bind.JAXBException e) {
            log.error(e);
        }
         */
    }
}
