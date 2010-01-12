/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.assemblymanager.dao.persistence;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;

/**
 *
 * @author kim
 */
public class PersistentServiceFactory {

   @PersistenceContext(unitName = "docassemblyPU")
   protected EntityManager em;

   @PersistenceUnit(unitName = "docassemblyPU")
   protected EntityManagerFactory emf;
   
   private String puName = null;
   private static Map instances = new HashMap();

   public static PersistentServiceFactory getInstance(String puName) {
      synchronized (PersistentServiceFactory.class) {
         if (!instances.containsKey(puName)) {
            try {
               PersistentServiceFactory instance =
                       new PersistentServiceFactory(puName);
               instance.puName = puName;
               instances.put(puName, instance);
               return instance;
            } catch (Exception ex) {
               ex.printStackTrace();
               return null;
            }
         }
      }

      return (PersistentServiceFactory) instances.get(puName);
   }

   private PersistentServiceFactory(String puName) throws Exception {
      try {
         this.emf = Persistence.createEntityManagerFactory(puName);
      } catch (Exception e) {
         throw e;
      }
   }

   public EntityManager getEntityManager() {
      if (this.emf == null || !this.emf.isOpen()) {
         this.emf = Persistence.createEntityManagerFactory(this.puName);
      }

      if (this.em == null || !this.em.isOpen()) {
         this.em = this.emf.createEntityManager();
      }
      return this.em;
   }

   public void closeEntityManagerFactory() {
      if (this.emf != null) {
         this.emf.close();
         this.emf = null;
      }
   }

   public void closeEntityManager() {
      if (this.em != null) {
         this.em.close();
         this.em = null;
      }
   }

   public void closeAll() {
      closeEntityManagerFactory();
      closeEntityManager();
   }
}
