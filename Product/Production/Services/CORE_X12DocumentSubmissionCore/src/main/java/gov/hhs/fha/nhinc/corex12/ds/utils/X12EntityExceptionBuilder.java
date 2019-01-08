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
package gov.hhs.fha.nhinc.corex12.ds.utils;

/**
 *
 * @author svalluripalli, cmay
 */
public class X12EntityExceptionBuilder extends AbstractX12ExceptionBuilder {

    private static final String ENTITY_ERROR_CODE = "Sender";
    private static final String ENTITY_ERROR_MESSAGE = "NwHIN target communities not specified in entity request";
    private static final String ENTITY_PAYLOAD_TYPE = "CoreEnvelopeError";

    /**
     * Private constructor to prevent instantiation
     */
    private X12EntityExceptionBuilder() {
    }

    public static X12EntityExceptionBuilder getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {

        public static final X12EntityExceptionBuilder INSTANCE = new X12EntityExceptionBuilder();
    }

    @Override
    protected String getErrorCode() {
        return ENTITY_ERROR_CODE;
    }

    @Override
    protected String getErrorMessage() {
        return ENTITY_ERROR_MESSAGE;
    }

    @Override
    protected String getPayloadType() {
        return ENTITY_PAYLOAD_TYPE;
    }
}
