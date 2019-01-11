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
package gov.hhs.fha.nhinc.docquery;

import gov.hhs.fha.nhinc.document.DocumentConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.util.MessageGeneratorUtils;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;

/**
 * @author akong
 *
 */
public class DQMessageGeneratorUtils extends MessageGeneratorUtils {

    private static final DQMessageGeneratorUtils INSTANCE = new DQMessageGeneratorUtils();

    DQMessageGeneratorUtils() {
    }

    /**
     * Returns the singleton instance of this class.
     *
     * @return the singleton instance
     */
    public static DQMessageGeneratorUtils getInstance() {
        return INSTANCE;
    }

    /**
     * Create a AdhocQueryResponse with severity set to error.
     *
     * @param codeContext The codecontext defines the reason of failure of AdhocQueryRequest.
     * @param errorCode The ErrorCode that needs to be set to the AdhocQueryResponse (Errorcodes are defined in spec).
     * @param status - the status of the message
     * @return the generated AdhocQueryResponse message
     */
    public AdhocQueryResponse createAdhocQueryErrorResponse(String codeContext, String errorCode, String status) {
        RegistryErrorList regErrList = new RegistryErrorList();
        regErrList.setHighestSeverity(NhincConstants.XDS_REGISTRY_ERROR_SEVERITY_ERROR);

        regErrList.getRegistryError().add(createRegistryError(codeContext, errorCode));

        AdhocQueryResponse response = new AdhocQueryResponse();
        response.setRegistryErrorList(regErrList);
        response.setStatus(status);
        response.setRegistryObjectList(new RegistryObjectListType());

        return response;
    }

    /**
     * Creates a registry error with the passed in codeContext and errorCode.
     *
     * @param codeContext The codecontext defines the reason of failure of AdhocQueryRequest.
     * @param errorCode The ErrorCode that needs to be set to the AdhocQueryResponse (Errorcodes are defined in spec).
     * @return the generated RegistryError
     */
    public RegistryError createRegistryError(String codeContext, String errorCode) {
        RegistryError regErr = new RegistryError();
        regErr.setCodeContext(codeContext);
        regErr.setErrorCode(errorCode);
        regErr.setSeverity(NhincConstants.XDS_REGISTRY_ERROR_SEVERITY_ERROR);
        return regErr;
    }

    /**
     * Creates a Policy failed error AdhocQueryResponse.
     *
     * @return the generated AdhocQueryResponse message
     */
    public AdhocQueryResponse createPolicyErrorResponse() {
        return createAdhocQueryErrorResponse("Policy check failed.", DocumentConstants.XDS_ERRORCODE_REPOSITORY_ERROR,
            DocumentConstants.XDS_QUERY_RESPONSE_STATUS_FAILURE);
    }

    /**
     * Create a AdhocQueryResponse failure with errorCode XDSRepositoryError.
     *
     * @param codeContext The codecontext defines the reason of failure of AdhocQueryRequest.
     * @return the generated AdhocQueryResponse message
     */
    public AdhocQueryResponse createRepositoryErrorResponse(String codeContext) {
        return createAdhocQueryErrorResponse(codeContext, DocumentConstants.XDS_ERRORCODE_REPOSITORY_ERROR,
            DocumentConstants.XDS_QUERY_RESPONSE_STATUS_FAILURE);
    }

}
