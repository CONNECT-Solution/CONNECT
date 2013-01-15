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
package gov.hhs.fha.nhinc.adapter.busorchestration.nhinadapteserviceejb;

import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Reference AdapterConstants - Many from ServiceNameConstants
 *
 * Most these should be consolidated between projects into a common library
 *
 * @author Jerry Goodnough
 */
public class AdapterConstants {

    private static Log log = LogFactory.getLog(AdapterConstants.class);
    /**
     * Constant for the Document Assembler Service Name
     */
    public final static String DOCUMENT_ASSEMBLY = "documentassembly";
    /**
     * Constant for the Document Manager Service Name
     */
    public final static String DOCUMENT_MANAGER = "documentmanager";
    /**
     *  Constant for the Document Class Code
     */
    public final static String XDSDocumentEntryClassCode =
        "$XDSDocumentEntryClassCode";
    /**
     * Constant for the Clinically Unique Hash metadata element
     */
    public final static String XDSClinicallyUniqueHash = "urn:gov:hhs:fha:nhinc:xds:clinicalUniqueHash";
    /**
     * Constant for the Service Start Time metadata element
     */
    public final static String XDSStartTime = "serviceStartTime";
    /**
     * Constant for the Service Stop Time metadata element
     */
    public final static String XDSStopTime = "serviceStopTime";
    /**
     * Constant for the Dynamic Document Usage metadata element
     */
    public final static String XDSHasBeenAccessed = "urn:gov:hhs:fha:nhinc:xds:hasBeenAccessed";
    public static final String REPOSITORY_PROPERTY_FILE = "repository";
    public static final String DYNAMIC_DOCUMENT_REPOSITORY_ID_PROP = "dynamicDocumentRepositoryId";
    public static final String XDS_REPOSITORY_ID = "repositoryUniqueId";
    public static final String XDS_REPOSITORY_ID_QUERY = "$XDSRepositoryUniqueId";
    public final static String C32_DOCUMENT = getC32_DocumentClassCode();
    public final static String ED_DISCHARGE_SUMMARY = getC62_DocumentClassCode();
    public final static String RADIOLOGY_REPORT = getC62_RadiologyDocumentClassCode();
    public final static String DOCASSEMBLY_PROPERTY_FILE = "docassembly";
    //document assembly endpoint
    public final static String ADAPTER_DOCUMENT_ASSEMBLY_ENDPOINT_PROP = "DAS_ADAPTER_DOCASSEMBLY_ENDPOINT";
    public final static String ADAPTER_DOCUMENT_ASSEMBLY_ENDPOINT = getAdapterDocumentAssemblyEndpoint();
    //document manager endpoint
    public final static String DOCUMENT_MANAGER_ENDPOINT_PROP = "DAS_DOCMGRSERVICE_ENDPOINT";
    public final static String DOCUMENT_MANAGER_ENDPOINT = getDocumentManagerEndpoint();

    private final static String getC32_DocumentClassCode() {
        String classCode = null;
        try {
            classCode = PropertyAccessor.getInstance().getProperty(DOCASSEMBLY_PROPERTY_FILE, "C32_CLASS_CODE");
        } catch (PropertyAccessException e) {
            log.error("Failed to read C32 document class code" + e.getMessage());
        }

        return classCode;
    }

    private final static String getC62_DocumentClassCode() {
        String classCode = null;
        try {
            classCode = PropertyAccessor.getInstance().getProperty(DOCASSEMBLY_PROPERTY_FILE, "C62_CLASS_CODE");
        } catch (PropertyAccessException e) {
            log.error("Failed to read C62 document class code" + e.getMessage());
        }

        return classCode;
    }

    private final static String getC62_RadiologyDocumentClassCode() {
        String classCode = null;
        try {
            classCode = PropertyAccessor.getInstance().getProperty(DOCASSEMBLY_PROPERTY_FILE, "C62_RR_CLASS_CODE");
        } catch (PropertyAccessException e) {
            log.error("Failed to read C62 Radiology document class code" + e.getMessage());
        }

        return classCode;
    }

    //return AdapterDocumentAssembly endpoint from docassembly properties file
    private final static String getAdapterDocumentAssemblyEndpoint() {
        String endpoint = null;
        try {
            endpoint = PropertyAccessor.getInstance().getProperty(DOCASSEMBLY_PROPERTY_FILE, ADAPTER_DOCUMENT_ASSEMBLY_ENDPOINT_PROP);
        } catch (PropertyAccessException e) {
            log.error("Failed to read C62 Radiology document class code" + e.getMessage());
        }

        return endpoint;
    }

    //return Document Manager endpoint from docassembly properties file
    private final static String getDocumentManagerEndpoint() {
        String docMgrEndpoint = null;
        try {
            docMgrEndpoint = PropertyAccessor.getInstance().getProperty(DOCASSEMBLY_PROPERTY_FILE, DOCUMENT_MANAGER_ENDPOINT_PROP);
        } catch (PropertyAccessException e) {
            log.error("Failed to read C62 Radiology document class code" + e.getMessage());
        }

        return docMgrEndpoint;
    }
}
