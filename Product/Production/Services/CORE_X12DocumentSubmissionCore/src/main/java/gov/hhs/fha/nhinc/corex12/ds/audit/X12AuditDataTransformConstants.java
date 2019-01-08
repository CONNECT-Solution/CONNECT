/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.corex12.ds.audit;

/**
 * Constants used in Core X12 Audit logging
 *
 * @author nsubrama
 */
public class X12AuditDataTransformConstants {

    public static final String EVENT_ID_CODE_X12 = "110112";
    public static final String EVENT_ID_CODE_SYS_NAME = "DCM";
    public static final String EVENT_ID_DISPLAY_NAME_X12_EXPORT = "Export";
    public static final String EVENT_ID_DISPLAY_NAME_X12_IMPORT = "Import";
    public static final String EVENT_ID_CODE_SYS_CODE_X12 = "X12";
    public static final String EVENT_ID_CODE_SYS_NAME_X12 = "CAQH CORE Transactions";
    public static final String EVENT_ACTION_CODE_READ = "R";
    public static final String EVENT_ACTION_CODE_CREATE = "C";
    public static final short EVENT_OUTCOME_INDICATOR_SUCCESS = 0;
    public static final String EVENT_ID_DISPLAY_NAME_X12REALTIME = "NwHIN CAQH CORE X12 Document Submission";
    public static final String CORE_X12_NAMESPACE_URI = "urn:org:caqh:soap:wsdl:corerule2_2_0";
    public static final String CORE_X12_JAXB_CONTEXT = "org.caqh.soap.wsdl.corerule2_2_0";
    public static final String CORE_X12_REQUEST_LOCALPART = "COREEnvelopeRealTimeRequest";
    public static final String CORE_X12_RESPONSE_LOCALPART = "COREEnvelopeRealTimeResponse";
    public static final String CORE_X12_BATCH_REQUEST_LOCALPART = "COREEnvelopeBatchSubmission";
    public static final String CORE_X12_BATCH_RESPONSE_LOCALPART = "COREEnvelopeBatchSubmissionResponse";
    public static final String CAQH_X12_CONNECTIVITY_CODED_SYS_NAME = "CAQH CORE Connectivity Metadata";
    public static final String CAQH_X12_CONNECTIVITY_CODED_SYS_DISPLAY_NAME = "Payload Identifier";
    public static final short PARTICIPANT_OBJ_TYPE_CODE_ROLE_X12 = 20;
    public static final short PARTICIPANT_OJB_TYPE_CODE_SYSTEM = 2;
}
