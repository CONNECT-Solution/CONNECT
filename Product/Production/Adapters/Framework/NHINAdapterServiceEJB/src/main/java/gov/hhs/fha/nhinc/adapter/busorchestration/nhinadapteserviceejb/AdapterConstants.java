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

    private final static String getC32_DocumentClassCode(){
        String classCode = null;
        try
        {
            classCode = PropertyAccessor.getProperty("docassembly", "C32_CLASS_CODE");
        }
        catch(PropertyAccessException e){
            log.error("Failed to read C32 document class code" + e.getMessage());
        }

        return classCode;
    }

    private final static String getC62_DocumentClassCode(){
        String classCode = null;
        try
        {
            classCode = PropertyAccessor.getProperty("docassembly", "C62_CLASS_CODE");
        }
        catch(PropertyAccessException e){
            log.error("Failed to read C32 document class code" + e.getMessage());
        }

        return classCode;
    }
}
