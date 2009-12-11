/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.template.dao;

import gov.hhs.fha.nhinc.common.dao.PersistentServiceFactory;
import gov.hhs.fha.nhinc.template.TemplateManager;
import gov.hhs.fha.nhinc.template.model.CdaTemplate;
import gov.hhs.fha.nhinc.template.model.DocSection;
import gov.hhs.fha.nhinc.template.model.SectionModule;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author kim
 */
public class TemplateManagerDAO implements TemplateManager {

   public final static String TEMPLATE_MANAGER_PU = "TemplateManagerPU";
   private static PersistentServiceFactory factory = null;
   protected static Log log = LogFactory.getLog(TemplateManagerDAO.class);
   
   //@PersistenceContext(unitName="TemplateManagerPU") EntityManager manager;
   //private PersistenceManager serviceFactory = null;
   private static TemplateManager dao = null;

   protected TemplateManagerDAO(String pUnit) {
      factory = PersistentServiceFactory.getInstance(pUnit);
   }

   public static TemplateManager getInstance() {
      synchronized (TemplateManagerDAO.class) {
         if (dao == null) {
            dao = new TemplateManagerDAO(TEMPLATE_MANAGER_PU);
         }
      }

      return dao;
   }

   @Override
   public List<CdaTemplate> getSectionTemplatesForDocument(String loincCode, boolean active) {

      EntityManager em = null;
      List<CdaTemplate> templates = new ArrayList<CdaTemplate>();
      PersistentServiceFactory psf = null;

      if (loincCode != null || loincCode.trim().length() > 0) {
         try {
            psf = PersistentServiceFactory.getInstance(TEMPLATE_MANAGER_PU);
            em = psf.getEntityManager();
            //Query query = em.createQuery(SELECT_SECTION_TEMPLATES);
            Query query = em.createNamedQuery("DocSection.findByLoincCode");
            query.setParameter("loincCode", loincCode);
            query.setParameter("activeYn", active ? 'Y' : 'N');
            Collection<DocSection> results = query.getResultList();

            Iterator<DocSection> iter = results.iterator();
            for (DocSection d : results) {
               //em.refresh(d);
               log.debug(d.toString());
               templates.add(d.getCdaTemplate1());
            }

            return templates.subList(0, results.size());
         } catch (javax.persistence.NoResultException nr) {
            log.error("No templates found for loincCode=" + loincCode);
         } finally {
            em.close();
         }
      } else {
         log.error("docType can not be null or empty");
      }

      return templates;
   }

   @Override
   public List<CdaTemplate> getModuleTemplatesForSection(int id, boolean active) {

      EntityManager em = null;
      List<CdaTemplate> templates = new ArrayList<CdaTemplate>();
      PersistentServiceFactory psf = null;

      try {
         psf = PersistentServiceFactory.getInstance(TEMPLATE_MANAGER_PU);
         em = psf.getEntityManager();
         Query query = em.createNamedQuery("SectionModule.findBySectionIdActiveYn");
         query.setParameter("sectionId", id);
         query.setParameter("activeYn", active ? 'Y' : 'N');
         Collection<SectionModule> results = query.getResultList();

         Iterator<SectionModule> iter = results.iterator();
         for (SectionModule d : results) {
            //em.refresh(d);
            log.debug(d.toString());
            templates.add(d.getCdaTemplate1());
         }

         return templates.subList(0, results.size());
      } catch (javax.persistence.NoResultException nr) {
         log.error("No templates found for section=" + id);
      } finally {
         em.close();
      }

      return templates;
   }

   @Override
   public CdaTemplate getTemplateForDocument(String loincCode) {

      CdaTemplate result = null;
      EntityManager em = null;
      PersistentServiceFactory psf = null;
      
      try {
         psf = PersistentServiceFactory.getInstance(TEMPLATE_MANAGER_PU);
         em = psf.getEntityManager();
         Query query = em.createNamedQuery("CdaTemplate.findByLoincCode");
         query.setParameter("loincCode", loincCode);
         result = (CdaTemplate) query.getSingleResult();
      } catch (javax.persistence.NoResultException nr) {
         log.error("No template found for document=" + loincCode);
      } finally {
         em.close();
      }

      return result;
   }

   @Override
   public List<CdaTemplate> getTemplates() {

      List<CdaTemplate> results = new ArrayList<CdaTemplate>();
      EntityManager em = null;
      PersistentServiceFactory psf = null;
      
      try {
         psf = PersistentServiceFactory.getInstance(TEMPLATE_MANAGER_PU);
         em = psf.getEntityManager();
         Query query = em.createNamedQuery("CdaTemplate.findAll");
         results = query.getResultList();
      } catch (javax.persistence.NoResultException nr) {
         log.error("No templates found!");
      } finally {
         em.close();
      }

      return results;
   }
}
