/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * Copyright (c) 2011, Conemaugh Valley Memorial Hospital
 *
 * This source is subject to the Conemaugh public license.  Please see the
 * license.txt file for more information.
 *
 * All other rights reserved.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
 * CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT
 * SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.common.dao;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author kim
 */
public class PersistentServiceFactory {

   //@PersistenceContext(unitName = "docassemblyPU")
   protected EntityManager em;

   //@PersistenceUnit(unitName = "docassemblyPU")
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
