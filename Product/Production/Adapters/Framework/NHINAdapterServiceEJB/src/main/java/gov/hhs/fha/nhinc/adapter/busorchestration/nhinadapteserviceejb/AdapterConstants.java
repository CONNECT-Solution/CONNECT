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
package gov.hhs.fha.nhinc.adapter.busorchestration.nhinadapteserviceejb;

/**
 * Reference AdapterConstants - Many from ServiceNameConstants
 * 
 * Most these should be consolidated between projects into a common library
 *
 * @author Jerry Goodnough
 */
public class AdapterConstants {
    /**
     * Constant for the Document Assembler Service Name
     */
    public final static String DOCUMENT_ASSEMBLY="documentassembly";
    /**
     * Constant for the Document Manager Service Name
     */
    public final static String DOCUMENT_MANAGER="documentmanager";

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
     * Constant for the Dynamic Document Usage metadata element
     */
    public final static String XDSHasBeenAccessed = "urn:gov:hhs:fha:nhinc:xds:hasBeenAccessed";

    public static final String REPOSITORY_PROPERTY_FILE = "repository";
    public static final String DYNAMIC_DOCUMENT_REPOSITORY_ID_PROP = "dynamicDocumentRepositoryId";

    public static final String XDS_REPOSITORY_ID = "repositoryUniqueId";
    public static final String XDS_REPOSITORY_ID_QUERY = "$XDSRepositoryUniqueId";
    
    public final static String C32_DOCUMENT = "34133-9";
}
