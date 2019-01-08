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

import java.util.Date;
import org.apache.wss4j.common.bsp.BSPEnforcer;
import org.apache.wss4j.common.ext.WSSecurityException;
import org.apache.wss4j.dom.message.token.Timestamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

/**
 * @author akong
 *
 */
public class CONNECTTimestamp extends Timestamp {

    private static final Logger LOG = LoggerFactory.getLogger(CONNECTTimestamp.class);

    /**
     * {@inheritDoc}
     */
    public CONNECTTimestamp(Element timestampElement) throws WSSecurityException {
        super(timestampElement, new BSPEnforcer());

    }

    /**
     * Initializes the class with the given timestamp.
     *
     * @param timestamp
     * @throws WSSecurityException
     */
    public CONNECTTimestamp(Timestamp timestamp) throws WSSecurityException {
        super(timestamp.getElement(), new BSPEnforcer());
    }

    /**
     * Return true if the current Timestamp is expired, meaning if the "Expires" value is before the current time. It
     * returns false if there is no Expires value.
     *
     * @param invocationDate the passed in invocation date to compare the expiration date with. if null, current time
     *            will be used.
     */
    public boolean isExpired(Date invocationDate) {
        if (getExpires() != null) {
            return getExpires().before(invocationDate == null ? new Date() : invocationDate);
        }
        return false;
    }

    /**
     * Return true if the "Created" value is before the current time minus the timeToLive argument, and if the Created
     * value is not "in the future".
     *
     * @param timeToLive the value in seconds for the validity of the Created time
     * @param futureTimeToLive the value in seconds for the future validity of the Created time
     * @param invocationDate the passed in invocation date to compare the expiration date with. if null, current time
     *            will be used.
     * @return true if the timestamp is before (now-timeToLive), false otherwise
     */
    public boolean verifyCreated(int timeToLive, int futureTimeToLive, Date invocationDate) {
        Date currentInvocationDate = invocationDate == null ? new Date() : invocationDate;

        long invocationTime = currentInvocationDate.getTime();
        if (futureTimeToLive > 0) {
            currentInvocationDate.setTime(invocationTime + futureTimeToLive * 1000);
        }

        // Check to see if the created time is in the future
        if (!verifyCreatedAfter(currentInvocationDate)) {
            return false;
        }

        // Calculate the time that is allowed for the message to travel
        invocationTime -= timeToLive * 1000;
        currentInvocationDate.setTime(invocationTime);

        // Validate the time it took the message to travel
        if (!verifyCreatedBefore(currentInvocationDate)) {
            return false;
        }

        LOG.info("Validation of Timestamp: Everything is ok");
        return true;
    }

    /**
     * Checks if created time is in the future
     */
    private boolean verifyCreatedAfter(Date invocationDate) {
        // Check to see if the created time is in the future
        if (getCreated() != null && getCreated().after(invocationDate)) {
            LOG.info("Validation of Timestamp: The message was created in the future!");
            return false;
        } else {
            return true;
        }
    }

    /**
     * Checks if created time is in the past
     */
    private boolean verifyCreatedBefore(Date invocationDate) {
        // Validate the time it took the message to travel
        if (getCreated() != null && getCreated().before(invocationDate)) {
            LOG.info("Validation of Timestamp: The message was created too long ago");
            return false;
        } else {
            return true;
        }
    }

}
