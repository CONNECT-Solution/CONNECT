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
package gov.hhs.fha.nhinc.event;

import gov.hhs.fha.nhinc.event.error.MessageProcessingFailedEvent;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zmelnick
 *
 */
public class Log4jEventLogger extends EventLogger {

    private static final Logger LOG = LoggerFactory.getLogger(Log4jEventLogger.class);

    public Log4jEventLogger() {
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.event.EventLogger#update(gov.hhs.fha.nhinc.event.Event, java.lang.Object)
     */
    @Override
    void recordEvent(EventManager manager, Event logEvent) {

        String description = logEvent.getDescription();
        //We want to strip the stack trace from being logged in the console if its a MessageProcessingFailedEvent
        if (logEvent instanceof MessageProcessingFailedEvent) {
            try {
                JSONObject obj = new JSONObject(description);
                obj.put("stackTrace", "[Stacktrace ommitted in server log]");
                description = obj.toString();
            } catch (JSONException e) {
               LOG.error("Error attempting to strip stacktrace.",e);
            }
        }
        LOG.info("{} has triggered. It has messageID {}, transactionID {}, and description {}",
            logEvent.getEventName(),logEvent.getMessageID(), logEvent.getTransactionID(), description);
    }

}
