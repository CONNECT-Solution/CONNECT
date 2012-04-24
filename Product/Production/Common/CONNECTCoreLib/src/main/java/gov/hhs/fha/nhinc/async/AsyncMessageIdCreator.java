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
package gov.hhs.fha.nhinc.async;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 
 * @author JHOPPESC
 */
public class AsyncMessageIdCreator {
	private static String UUID_TAG = "urn:uudi:";

    public Map CreateRequestContextForMessageId(AssertionType assertion) {
        Map requestContext = new HashMap();

        // Set the value for the message id property in the request context
        if (assertion != null && NullChecker.isNotNullish(assertion.getMessageId())) {
            requestContext.put(NhincConstants.ASYNC_MESSAGE_ID_PROP, assertion.getMessageId());
        }

        // set the value for the message type property in the request context
        requestContext.put(NhincConstants.ASYNC_MSG_TYPE_PROP, NhincConstants.ASYNC_REQUEST_MSG_TYPE_VAL);

        return requestContext;
    }

    public Map CreateRequestContextForRelatesTo(AssertionType assertion) {
        Map requestContext = new HashMap();

        if (assertion != null && NullChecker.isNotNullish(assertion.getRelatesToList())) {
            requestContext.put(NhincConstants.ASYNC_RELATES_TO_PROP, assertion.getRelatesToList());
        }

        // set the value for the message type property in the request context
        requestContext.put(NhincConstants.ASYNC_MSG_TYPE_PROP, NhincConstants.ASYNC_RESPONSE_MSG_TYPE_VAL);

        return requestContext;
    }

    public static String generateMessageId() {
        return UUID_TAG + UUID.randomUUID().toString();
    }

}
