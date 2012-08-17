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
package gov.hhs.fha.nhinc.assemblymanager.dao;

import gov.hhs.fha.nhinc.assemblymanager.AssemblyConstants;
import gov.hhs.fha.nhinc.common.dao.QueryDAO;
import gov.hhs.fha.nhinc.assemblymanager.dao.model.DasConfig;
import gov.hhs.fha.nhinc.common.dao.PersistentServiceFactory;
import java.util.Collection;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author kim
 */
public class PropertiesDAO extends QueryDAO {

   private static PropertiesDAO dao = null;

   protected PropertiesDAO(String pUnit) {
      super(pUnit);
   }

   public static PropertiesDAO getInstance() {
      synchronized (PropertiesDAO.class) {
         if (dao == null) {
            dao = new PropertiesDAO(AssemblyConstants.DAS_PU_VALUE);
         }
      }

      return dao;
   }

   public String getAttributeValue(String attName, boolean active) {
      String value = "";

      if (attName != null && attName.length() > 1) {
         EntityManager em = null;
         PersistentServiceFactory psf = null;
         try {
            psf = PersistentServiceFactory.getInstance(AssemblyConstants.DAS_PU_VALUE);
            em = psf.getEntityManager();
            Query query = em.createNamedQuery("DasConfig.findByAttName_ActiveYn");
            query.setParameter("attName", attName.trim());
            query.setParameter("activeYn", (active ? 'Y' : 'N'));
            DasConfig att = (DasConfig) query.getSingleResult();
            if (att != null) {
               value = att.getAttValue();
            }
         } catch (javax.persistence.NoResultException nr) {
            log.error("Attribute " + attName + " not found.", nr);
         } catch (Exception ex) {
            log.error("Error encountered for \"DasConfig.findByAttName_ActiveYn\"", ex);
         }
      }
      return value;
   }

   public DasConfig getAttribute(String attName, boolean active) {
      if (attName != null && attName.length() > 1) {
         EntityManager em = null;
         PersistentServiceFactory psf = null;

         try {
            psf = PersistentServiceFactory.getInstance(AssemblyConstants.DAS_PU_VALUE);
            em = psf.getEntityManager();
            Query query = em.createNamedQuery("DasConfig.findByAttName_ActiveYn");
            query.setParameter("attName", attName.trim());
            if (active) {
               query.setParameter("activeYn", "Y");
            } else {
               query.setParameter("activeYn", "N");
            }
            return (DasConfig) query.getSingleResult();
         } catch (javax.persistence.NoResultException nr) {
            log.error("Attribute " + attName + " not found.", nr);
         } catch (Exception ex) {
            log.error("Error encountered for \"DasConfig.findByAttName_ActiveYn\"", ex);
         }
      }

      return null;
   }

   public DasConfig[] getAttributes(boolean active) {

      EntityManager em = null;
         PersistentServiceFactory psf = null;
      try {
         psf = PersistentServiceFactory.getInstance(AssemblyConstants.DAS_PU_VALUE);
            em = psf.getEntityManager();
         Query query = em.createNamedQuery("DasConfig.findByActiveYn");
         //query.setHint("toplink.refresh", "true");
         if (active) {
            query.setParameter("activeYn", "Y");
         } else {
            query.setParameter("activeYn", "N");
         }
         Collection<DasConfig> results = query.getResultList();

         log.info("# of DAS attributes=" + results.size());

         if (results != null) {
            return (DasConfig[]) results.toArray(new DasConfig[0]);
         }
      } catch (Exception ex) {
         log.error("Error encountered for \"DasConfig.findByActiveYn\"", ex);
      }

      return null;
   }
}
