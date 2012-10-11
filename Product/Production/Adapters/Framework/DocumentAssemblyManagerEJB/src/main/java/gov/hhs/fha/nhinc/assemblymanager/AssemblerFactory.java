/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
 * All rights reserved. 
 * Copyright (c) 2011, Conemaugh Valley Memorial Hospital
 * This source is subject to the Conemaugh public license.  Please see the
 * license.txt file for more information.
 * All other rights reserved.
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
package gov.hhs.fha.nhinc.assemblymanager;

import gov.hhs.fha.nhinc.assemblymanager.builder.C32DocumentBuilder;
import gov.hhs.fha.nhinc.assemblymanager.builder.C62DocumentBuilder;
import gov.hhs.fha.nhinc.template.dao.TemplateManagerDAO;

import gov.hhs.fha.nhinc.template.model.CdaTemplate;

import java.util.List;

import org.apache.commons.logging.Log;

import org.apache.commons.logging.LogFactory;

/**
 * 
 * 
 * 
 * @author kim
 */
public class AssemblerFactory {

    private static Log log = LogFactory.getLog(AssemblerFactory.class);

    public static C32DocumentBuilder C32Builder(String docType) {
        List<CdaTemplate> templates;
        C32DocumentBuilder builder = null;
        try {
            templates = TemplateManagerDAO.getInstance().getSectionTemplatesForDocument(docType, true);
            log.debug(templates.size() + " templates for document type " + docType);

            builder = new C32DocumentBuilder(templates);
        } catch (Exception ex) {
            log.error("No templates located - error: " + ex.getMessage());
            builder = new C32DocumentBuilder();
        }

        builder.setDocumentType(docType);
        return builder;
    }

    public static C62DocumentBuilder c62Builder(String docType) {
        C62DocumentBuilder builder = null;
        builder = new C62DocumentBuilder();

        builder.setDocumentType(docType);
        return builder;
    }
}
