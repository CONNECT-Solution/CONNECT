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
 * 
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
