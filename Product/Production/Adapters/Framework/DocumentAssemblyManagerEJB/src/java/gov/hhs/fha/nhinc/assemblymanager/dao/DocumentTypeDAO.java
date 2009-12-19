/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.assemblymanager.dao;

import gov.hhs.fha.nhinc.common.dao.QueryDAO;
import gov.hhs.fha.nhinc.assemblymanager.AssemblyConstants;
import gov.hhs.fha.nhinc.assemblymanager.dao.model.DocumentType;
import gov.hhs.fha.nhinc.common.dao.PersistentServiceFactory;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author kim
 */
public class DocumentTypeDAO extends QueryDAO {

   private static DocumentTypeDAO dao = null;

   protected DocumentTypeDAO(String pUnit) {
      super(pUnit);
   }

   public static DocumentTypeDAO getInstance() {
      synchronized (DocumentTypeDAO.class) {
         if (dao == null) {
            dao = new DocumentTypeDAO(AssemblyConstants.DAS_PU_VALUE);
         }
      }

      return dao;
   }

   public DocumentType getDocumentType(String typeId) throws Exception {
      DocumentType docType = null;

      if (typeId != null && typeId.length() > 1) {
         EntityManager em = null;
         PersistentServiceFactory psf = null;
         try {
            psf = PersistentServiceFactory.getInstance(AssemblyConstants.DAS_PU_VALUE);
            em = psf.getEntityManager();
            Query query = em.createNamedQuery("DocumentType.findByTypeId");
            query.setParameter("typeId", typeId.trim());

            docType = (DocumentType) query.getSingleResult();
         } catch (Exception ex) {
            log.error("Error encountered for \"DocumentType.findByTypeId\"", ex);
            throw ex;
         }
      }

      return docType;
   }

   public List<DocumentType> getDocumentTypes() {
      List<DocumentType> docTypeList = null;
      EntityManager em = null;
      PersistentServiceFactory psf = null;
      
      try {
         psf = PersistentServiceFactory.getInstance(AssemblyConstants.DAS_PU_VALUE);
         em = psf.getEntityManager();
         Query query = em.createNamedQuery("DocumentType.findAll");
         docTypeList = query.getResultList();
      } catch (Exception ex) {
         log.error("Error encountered for \"DocumentType.findAll\"", ex);
      }

      return docTypeList;
   }

   public boolean isValidDocumentType(String typeId) {
      boolean status = false;
      EntityManager em = null;
      PersistentServiceFactory psf = null;

      if (typeId != null && typeId.length() > 1) {
         try {
            psf = PersistentServiceFactory.getInstance(AssemblyConstants.DAS_PU_VALUE);
            em = psf.getEntityManager();
            DocumentType docType = em.find(DocumentType.class, typeId);
            if (docType != null && docType.getActive()) {
               status = true;
            }
         } catch (javax.persistence.NoResultException nr) {
            log.error("Document Type \"" + typeId + "\" not found.", nr);
         } catch (Exception ex) {
            log.error("Error encountered for \"DocumentType.find\"", ex);
         }
      }

      return status;
   }
}
