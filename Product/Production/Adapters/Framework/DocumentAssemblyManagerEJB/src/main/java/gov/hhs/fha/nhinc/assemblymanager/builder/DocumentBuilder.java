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
package gov.hhs.fha.nhinc.assemblymanager.builder;

import gov.hhs.fha.nhinc.assemblymanager.AssemblyConstants;

import gov.hhs.fha.nhinc.assemblymanager.dao.DocumentTypeDAO;

import gov.hhs.fha.nhinc.assemblymanager.dao.PropertiesDAO;

import gov.hhs.fha.nhinc.assemblymanager.dao.model.DocumentType;

import gov.hhs.fha.nhinc.assemblymanager.utils.DocumentIdGenerator;

import gov.hhs.fha.nhinc.template.model.CdaTemplate;

import java.util.Calendar;

import java.util.Date;

import java.util.List;

import org.apache.commons.logging.Log;

import org.apache.commons.logging.LogFactory;

import org.hl7.v3.II;

import org.hl7.v3.ObjectFactory;

import org.hl7.v3.POCDMT000040Organization;

import org.hl7.v3.ONExplicit;

/**
 * 
 * 
 * 
 * @author kim
 */

public abstract class DocumentBuilder {

    protected ObjectFactory objectFactory = null;

    protected String orgOID = "";

    protected String orgName = "";

    protected String patientId = null;

    protected List<CdaTemplate> templates = null; // templates for section

    protected DocumentType documentType = null;

    public DocumentBuilder() {

        initialize();

    }

    public DocumentBuilder(List<CdaTemplate> templates) {

        initialize();

        this.templates = templates;

    }

    public DocumentBuilder(String id) {

        initialize();

        this.patientId = id;

    }

    private void initialize() {

        if (objectFactory == null) {

            objectFactory = new ObjectFactory();

        }

        orgOID = PropertiesDAO.getInstance().getAttributeValue(AssemblyConstants.ORGANIZATION_OID, true);

        orgName = PropertiesDAO.getInstance().getAttributeValue(AssemblyConstants.ORGANIZATION_NAME, true);

    }

    public String getPatientId() {

        return patientId;

    }

    public void setPatientId(String id) {

        this.patientId = id;

    }

    protected Date getCreatedDTM() {

        return Calendar.getInstance().getTime();

    }

    protected String createDocumentId() {

        return DocumentIdGenerator.generateDocumentId();

    }

    protected II getOrganization() {

        II id = new II();

        id.setRoot(orgOID);

        return id;

    }

    public void setDocumentType(String docType) {

        try {

            documentType = DocumentTypeDAO.getInstance().getDocumentType(docType);

        } catch (Exception ex) {

            documentType = new DocumentType();

            documentType.setTypeId(docType);

        }

    }

    public DocumentType getDocumentType() {

        return documentType;

    }

    protected POCDMT000040Organization getRepresentedOrganization() {

        POCDMT000040Organization org = new POCDMT000040Organization();

        org.getId().add(getOrganization());

        ONExplicit onName = objectFactory.createONExplicit();

        onName.getContent().add(orgName);

        org.getName().add(onName);

        return org;

    }

}
