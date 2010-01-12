/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.common.dao;

import gov.hhs.fha.nhinc.assemblymanager.AssemblyConstants;
import gov.hhs.fha.nhinc.common.dao.PersistentServiceFactory;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author kim
 */
public class QueryDAO {

   protected static Log log = LogFactory.getLog(QueryDAO.class);

   //@PersistenceContext(unitName="docassemblyPU")
   //protected EntityManagerFactory emf;
   private static PersistentServiceFactory factory = null;

   public QueryDAO(String pUnit) {
      factory = PersistentServiceFactory.getInstance(pUnit);
   }

   public PersistentServiceFactory getFactory() {
      return factory;
   }
}
