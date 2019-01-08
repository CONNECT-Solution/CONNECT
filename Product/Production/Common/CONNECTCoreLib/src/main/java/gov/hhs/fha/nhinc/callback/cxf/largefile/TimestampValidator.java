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
package gov.hhs.fha.nhinc.callback.cxf.largefile;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import java.util.Date;
import org.apache.cxf.message.Message;
import org.apache.wss4j.common.ext.WSSecurityException;
import org.apache.wss4j.common.ext.WSSecurityException.ErrorCode;
import org.apache.wss4j.dom.engine.WSSConfig;
import org.apache.wss4j.dom.handler.RequestData;
import org.apache.wss4j.dom.validate.Credential;
import org.apache.wss4j.dom.validate.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class replaces the default WSS4J TimestampValidator (org.apache.ws.security.validate.TimestampValidator). The
 * validation logic was copied from that class but modified to use a passed in time from the Message Context. This
 * passed in time needs to be set by a CXF interceptor before validation is to occur. In addition, this class will
 * prefer configuration from the CONNECT gateway properties file instead of CXF.
 *
 * @author akong
 *
 */
public class TimestampValidator implements Validator {

    private static final Logger LOG = LoggerFactory.getLogger(TimestampValidator.class);
    protected static final long INVALID_LONG_VALUE = -1;

    /**
     * Validate the credential argument. It must contain a non-null Timestamp.
     *
     * @param credential the Credential to be validated
     * @param data the RequestData associated with the request
     * @return
     * @throws WSSecurityException on a failed validation
     */
    @Override
    public Credential validate(Credential credential, RequestData data) throws WSSecurityException {
        if (credential == null || credential.getTimestamp() == null) {
            throw new WSSecurityException(ErrorCode.FAILURE, "noCredential");
        }
        WSSConfig wssConfig = data.getWssConfig();
        if (wssConfig == null) {
            throw new WSSecurityException(ErrorCode.FAILURE, "WSSConfig cannot be null");
        }

        CONNECTTimestamp timeStamp = new CONNECTTimestamp(credential.getTimestamp());

        validate(timeStamp, data);

        return credential;
    }

    /**
     * Validate whether the security semantics have expired
     */
    private void validate(CONNECTTimestamp timeStamp, RequestData data) throws WSSecurityException {
        boolean timeStampStrict = getTimeStampStrict();

        // The default is 300 seconds (5 minutes)
        int timeStampTTL = data.getTimeStampTTL();
        long timeStampTTLProperty = getTimeStampTTL();
        if (timeStampTTLProperty >= 0) {
            timeStampTTL = (int) timeStampTTLProperty;
        }

        long futureTimeToLiveProperty = getTimeStampFutureTTL();
        // The default is 60 seconds.
        int futureTimeToLive = data.getTimeStampFutureTTL();
        if (futureTimeToLiveProperty >= 0) {
            futureTimeToLive = (int) futureTimeToLiveProperty;
        }

        Date invocationTime = (Date) ((Message) data.getMsgContext()).get(TimestampInterceptor.INVOCATION_TIME_KEY);
        // Validate whether the security semantics have expired
        if (timeStampStrict && timeStamp.isExpired(invocationTime)
            || !timeStamp.verifyCreated(timeStampTTL, futureTimeToLive, invocationTime)) {
            throw new WSSecurityException(ErrorCode.MESSAGE_EXPIRED, "invalidTimestamp",
                new Object[] { "The security semantics of the message have expired" });
        }
    }

    protected long getTimeStampTTL() {
        try {
            return PropertyAccessor.getInstance().getPropertyLong(NhincConstants.GATEWAY_PROPERTY_FILE,
                NhincConstants.TIMESTAMP_TIME_TO_LIVE);
        } catch (PropertyAccessException pae) {
            LOG.error("Failed to determine timestamp time to live in gateway.properties, will use default values: {}",
                pae.getLocalizedMessage(), pae);
        }

        return INVALID_LONG_VALUE;
    }

    protected long getTimeStampFutureTTL() {
        try {
            return PropertyAccessor.getInstance().getPropertyLong(NhincConstants.GATEWAY_PROPERTY_FILE,
                NhincConstants.TIMESTAMP_FUTURE_TIME_TO_LIVE);
        } catch (PropertyAccessException pae) {
            LOG.error("Failed to determine timestamp future TTL in gateway.properties, will use default value: {}",
                pae.getLocalizedMessage(), pae);
        }

        return INVALID_LONG_VALUE;
    }

    protected boolean getTimeStampStrict() {
        try {
            return PropertyAccessor.getInstance().getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE,
                NhincConstants.TIMESTAMP_STRICT);
        } catch (PropertyAccessException pae) {
            LOG.error("Failed to determine timestamp strict in gateway.properties, will use default value: {}",
                pae.getLocalizedMessage(), pae);
        }

        return true;
    }

}
