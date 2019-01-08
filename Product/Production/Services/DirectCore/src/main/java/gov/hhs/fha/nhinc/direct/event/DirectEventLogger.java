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
package gov.hhs.fha.nhinc.direct.event;

import gov.hhs.fha.nhinc.event.EventManager;
import gov.hhs.fha.nhinc.event.EventRecorder;
import javax.mail.internet.MimeMessage;

/**
 * Implements event logging for direct.
 */
public class DirectEventLogger {

    private final EventRecorder eventRecorder;

    /**
     * Singleton holder for {@link DirectEventLogger}.
     */
    private static class SingletonHolder {

        public static final DirectEventLogger INSTANCE = new DirectEventLogger();

        private SingletonHolder() {
        }
    }

    /**
     * @return singleton instance of DirectEventLogger.
     */
    public static DirectEventLogger getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * Construct a Direct Event Logger using singleton event manager.
     */
    private DirectEventLogger() {
        this.eventRecorder = EventManager.getInstance();
    }

    /**
     * Log a success direct event using event logger.
     *
     * @param type direct event type.
     * @param message used to pull info from.
     */
    public void log(DirectEventType type, MimeMessage message) {
        log(type, message, null);
    }

    /**
     * Log a failed event due to exception.
     *
     * @param type of event which failed.
     * @param message used to pull info from.
     * @param exception encountered triggering event.
     */
    public void logException(DirectEventType type, MimeMessage message, Exception exception) {
        if (exception == null) {
            log(type, message, null);
        } else {
            log(type, message, exception.getLocalizedMessage());
        }
    }

    /**
     * Log a success or failed direct event using event logger.
     *
     * @param type direct event type.
     * @param message used to pull info from.
     * @param errorMsg optional error message - if not null status = error.
     */
    public void log(DirectEventType type, MimeMessage message, String errorMsg) {
        eventRecorder.recordEvent(new DirectEvent.Builder().mimeMessage(message).errorMsg(errorMsg).build(type));
    }

    /**
     * Log a success or failed direct event using event logger.
     *
     * @param type direct event type.
     * @param errorMsg optional error message - if not null status = error.
     */
    public void log(DirectEventType type, String errorMsg) {
        eventRecorder.recordEvent(new DirectEvent.Builder().errorMsg(errorMsg).build(type));
    }

}
