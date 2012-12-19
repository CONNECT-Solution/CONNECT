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
package gov.hhs.fha.nhinc.callback.cxf.largefile;

import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.ws.security.WSSecurityException;
import org.apache.ws.security.message.token.Timestamp;
import org.w3c.dom.Element;

/**
 * @author akong
 * 
 */
public class CONNECTTimestamp extends Timestamp {

    private static final Logger LOG = Logger.getLogger(CONNECTTimestamp.class);

    /**
     * {@inheritDoc}
     */
    public CONNECTTimestamp(Element timestampElement) throws WSSecurityException {
        super(timestampElement);
    }

    /**
     * Initializes the class with the given timestamp.
     * 
     * @param timestamp
     * @throws WSSecurityException
     */
    public CONNECTTimestamp(Timestamp timestamp) throws WSSecurityException {
        super(timestamp.getElement());
    }

    /**
     * Return true if the current Timestamp is expired, meaning if the "Expires" value is before the current time. It
     * returns false if there is no Expires value.
     * 
     * @param invocationDate the passed in invocation date to compare the expiration date with. if null, current time
     *            will be used.
     */
    public boolean isExpired(Date invocationDate) {
        if (expiresDate != null) {
            if (invocationDate == null) {
                invocationDate = new Date();
            }

            return expiresDate.before(invocationDate);
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
        if (invocationDate == null) {
            invocationDate = new Date();
        }

        long invocationTime = invocationDate.getTime();
        if (futureTimeToLive > 0) {
            invocationDate.setTime(invocationTime + futureTimeToLive * 1000);
        }
        // Check to see if the created time is in the future
        if (createdDate != null && createdDate.after(invocationDate)) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Validation of Timestamp: The message was created in the future!");
            }
            return false;
        }

        // Calculate the time that is allowed for the message to travel
        invocationTime -= timeToLive * 1000;
        invocationDate.setTime(invocationTime);

        // Validate the time it took the message to travel
        if (createdDate != null && createdDate.before(invocationDate)) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Validation of Timestamp: The message was created too long ago");
            }
            return false;
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Validation of Timestamp: Everything is ok");
        }
        return true;
    }

}
