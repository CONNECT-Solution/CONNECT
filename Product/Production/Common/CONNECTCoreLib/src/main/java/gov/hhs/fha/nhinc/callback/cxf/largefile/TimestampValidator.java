/*
 * Copyright (c) 2009-2015, United States Government, as represented by the Secretary of Health and Human Services.
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.cxf.message.Message;
import org.apache.ws.security.WSSConfig;
import org.apache.ws.security.WSSecurityException;
import org.apache.ws.security.handler.RequestData;
import org.apache.ws.security.validate.Credential;
import org.apache.ws.security.validate.Validator;

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
     * @throws WSSecurityException on a failed validation
     */
    public Credential validate(Credential credential, RequestData data) throws WSSecurityException {
        if (credential == null || credential.getTimestamp() == null) {
            throw new WSSecurityException(WSSecurityException.FAILURE, "noCredential");
        }
        if (data.getWssConfig() == null) {
            throw new WSSecurityException("WSSConfig cannot be null");
        }
        WSSConfig wssConfig = data.getWssConfig();
        boolean timeStampStrict = true;
        int timeStampTTL = 300;
        int futureTimeToLive = 60;
        if (wssConfig != null) {
            timeStampTTL = wssConfig.getTimeStampTTL();
            futureTimeToLive = wssConfig.getTimeStampFutureTTL();
        }

        long timeStampTTLProperty = getTimeStampTTL();
        if (timeStampTTLProperty >= 0) {
            timeStampTTL = (int) timeStampTTLProperty;
        }

        long futureTimeToLiveProperty = getTimeStampFutureTTL();
        if (futureTimeToLiveProperty >= 0) {
            futureTimeToLive = (int) futureTimeToLiveProperty;
        }
        timeStampStrict = getTimeStampStrict();

        CONNECTTimestamp timeStamp = new CONNECTTimestamp(credential.getTimestamp());

        Date invocationTime = (Date) ((Message) data.getMsgContext()).get(TimestampInterceptor.INVOCATION_TIME_KEY);

        // Validate whether the security semantics have expired
        if ((timeStampStrict && timeStamp.isExpired(invocationTime))
                || !timeStamp.verifyCreated(timeStampTTL, futureTimeToLive, invocationTime)) {
            throw new WSSecurityException(WSSecurityException.MESSAGE_EXPIRED, "invalidTimestamp",
                    new Object[] { "The security semantics of the message have expired" });
        }
        return credential;
    }

    protected long getTimeStampTTL() {
        try {
            return PropertyAccessor.getInstance().getPropertyLong(NhincConstants.GATEWAY_PROPERTY_FILE,
                    NhincConstants.TIMESTAMP_TIME_TO_LIVE);
        } catch (PropertyAccessException pae) {
            LOG.info("Failed to determine timestamp time to live in gateway.properties.  Will use default values.");
        }

        return INVALID_LONG_VALUE;
    }

    protected long getTimeStampFutureTTL() {
        try {
            return PropertyAccessor.getInstance().getPropertyLong(NhincConstants.GATEWAY_PROPERTY_FILE,
                    NhincConstants.TIMESTAMP_FUTURE_TIME_TO_LIVE);
        } catch (PropertyAccessException pae) {
            LOG.info("Failed to determine timestamp future time to live in gateway.properties.  Will use default values.");
        }

        return INVALID_LONG_VALUE;
    }

    protected boolean getTimeStampStrict() {
        try {
            return PropertyAccessor.getInstance().getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE,
                    NhincConstants.TIMESTAMP_STRICT);
        } catch (PropertyAccessException pae) {
            LOG.info("Failed to determine timestamp strict in gateway.properties.  Will use default values.");
        }

        return true;
    }

}
