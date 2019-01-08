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
package gov.hhs.fha.nhinc.direct;

import gov.hhs.fha.nhinc.direct.event.DirectEventLogger;
import gov.hhs.fha.nhinc.direct.event.DirectEventType;
import javax.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Exceptions for {@link MimeMessageBuilder}.
 */
public class DirectException extends RuntimeException {

    private static final long serialVersionUID = 4636463959045310435L;
    private static final Logger LOG = LoggerFactory.getLogger(DirectException.class);
    private static DirectEventLogger directEventLogger;

    /**
     * Constructor.
     *
     * @param message for the exception
     * @param cause chained exception
     * @param mimeMessage associated with the exception for event logging.
     */
    public DirectException(String message, Throwable cause, MimeMessage mimeMessage) {
        super(message, cause);
        LOG.error(message, cause);
    }

    /**
     * Constructor.
     *
     * @param message for the exception
     * @param mimeMessage associated with the exception for event logging.
     */
    public DirectException(String message, MimeMessage mimeMessage) {
        super(message);
        LOG.error(message);
        directEventLogger.log(DirectEventType.DIRECT_ERROR, mimeMessage, message);
    }

    /**
     * Constructor.
     *
     * @param message for the exception
     * @param cause chained exception
     */
    public DirectException(String message, Throwable cause) {
        super(message, cause);
        LOG.error(message, cause);
        directEventLogger.log(DirectEventType.DIRECT_ERROR, message + cause.getMessage());
    }

    /**
     * Constructor.
     *
     * @param message for the exception
     */
    public DirectException(String message) {
        super(message);
        LOG.error(message);
        directEventLogger.log(DirectEventType.DIRECT_ERROR, message);
    }

    /**
     * Statically inject a {@link DirectEventLogger}.
     *
     * @param directEventLogger the directEventLogger to set
     */
    public static void setDirectEventLogger(DirectEventLogger directEventLogger) {
        DirectException.directEventLogger = directEventLogger;
    }
}
