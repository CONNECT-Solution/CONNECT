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
package gov.hhs.fha.nhinc.docretrieve;

import gov.hhs.fha.nhinc.document.DocumentConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

/**
 * @author akong
 *
 */
public class MessageGenerator {

    private static MessageGenerator instance = new MessageGenerator();

    /**
     * @return the singleton instance of DocRetrieveFileUtils
     */
    public static MessageGenerator getInstance() {
        return instance;
    }

    /**
     * Creates a Response with a RegistryError with a Repository Error Code.
     *
     * @param codeContext
     * @return
     */
    public RetrieveDocumentSetResponseType createRegistryResponseError(String codeContext) {

        RetrieveDocumentSetResponseType response = new RetrieveDocumentSetResponseType();

        RegistryResponseType regResp = new RegistryResponseType();
        regResp.setStatus(DocumentConstants.XDS_RETRIEVE_RESPONSE_STATUS_FAILURE);

        RegistryError registryError = new RegistryError();
        registryError.setCodeContext(codeContext);
        registryError.setErrorCode(DocumentConstants.XDS_RETRIEVE_ERRORCODE_REPOSITORY_ERROR);
        registryError.setSeverity(NhincConstants.XDS_REGISTRY_ERROR_SEVERITY_ERROR);

        regResp.setRegistryErrorList(new RegistryErrorList());
        regResp.getRegistryErrorList().getRegistryError().add(registryError);

        response.setRegistryResponse(regResp);

        return response;
    }
}
