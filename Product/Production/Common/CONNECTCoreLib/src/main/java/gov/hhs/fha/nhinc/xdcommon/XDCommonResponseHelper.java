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
package gov.hhs.fha.nhinc.xdcommon;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

/**
 * @author mweaver
 *
 */
public class XDCommonResponseHelper {

    public static final String CONNECT_LOCATION = "CONNECT";

    public enum ErrorCodes {
        XDSRegistryError, XDSRepositoryError
    }

    public RegistryResponseType createSuccess() {
        RegistryResponseType response = new RegistryResponseType();
        response.setStatus(NhincConstants.NHINC_ADHOC_QUERY_SUCCESS_RESPONSE);
        return response;
    }

    public RegistryResponseType createError(String message) {
        return createRegistryResponse(message, ErrorCodes.XDSRegistryError, CONNECT_LOCATION);
    }

    public RegistryResponseType createError(String message, ErrorCodes code) {
        return createRegistryResponse(message, code, CONNECT_LOCATION);
    }

    public RegistryResponseType createError(String message, ErrorCodes code, String location) {
        return createRegistryResponse(message, code, location);
    }

    public RegistryResponseType createError(Throwable e) {
        return createRegistryResponse(e.getLocalizedMessage(), ErrorCodes.XDSRegistryError,
                ExceptionUtils.getFullStackTrace(e));
    }

    private RegistryResponseType createRegistryResponse(String error, ErrorCodes code, String location) {
        RegistryResponseType response = new RegistryResponseType();
        response.setStatus(NhincConstants.XDR_ACK_FAILURE_STATUS_MSG);

        RegistryErrorList registryErrorList = new RegistryErrorList();
        RegistryError registryError = new RegistryError();
        registryError.setCodeContext(error);
        registryError.setErrorCode(StringUtils.trim(code.toString()));
        registryError.setLocation(StringUtils.trim(location));
        registryError.setSeverity(NhincConstants.XDS_REGISTRY_ERROR_SEVERITY_ERROR);
        registryErrorList.getRegistryError().add(registryError);
        response.setRegistryErrorList(registryErrorList);
        return response;
    }

}
