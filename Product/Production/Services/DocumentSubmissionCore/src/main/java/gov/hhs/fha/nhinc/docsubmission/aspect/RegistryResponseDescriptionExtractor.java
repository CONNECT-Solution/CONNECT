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
package gov.hhs.fha.nhinc.docsubmission.aspect;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import gov.hhs.fha.nhinc.event.builder.ErrorExtractor;
import java.util.ArrayList;
import java.util.List;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

/**
 * @author akong
 *
 */
public class RegistryResponseDescriptionExtractor {

    private static final ErrorExtractor ERROR_EXTRACTOR = new ErrorExtractor();

    /**
     * Extracts the statuses in the RegistryResponse;
     *
     * @param response the RegistryResponse whose values are to be extracted
     * @return a list of string containing the statuses
     */
    public ImmutableList<String> getStatuses(RegistryResponseType response) {
        ImmutableList<String> statuses = ImmutableList.of();
        if (hasStatus(response)) {
            statuses = ImmutableList.of(response.getStatus());
        }

        return statuses;
    }

    /**
     * Extracts the error codes in the RegistryResponse;
     *
     * @param response the RegistryResponse whose values are to be extracted
     * @return a list of string containing the error codes
     */
    public List<String> getErrorCodes(RegistryResponseType response) {
        List<String> errorCodes = new ArrayList<>();
        if (hasErrorList(response)) {
            errorCodes = Lists.transform(response.getRegistryErrorList().getRegistryError(), ERROR_EXTRACTOR);
        }

        return errorCodes;
    }

    private boolean hasStatus(RegistryResponseType response) {
        return response != null && response.getStatus() != null;
    }

    private boolean hasErrorList(RegistryResponseType response) {
        return response != null && response.getRegistryErrorList() != null;
    }

}
