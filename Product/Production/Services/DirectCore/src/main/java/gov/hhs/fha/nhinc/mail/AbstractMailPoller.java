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
package gov.hhs.fha.nhinc.mail;

import gov.hhs.fha.nhinc.direct.messagemonitoring.util.MessageMonitoringUtil;

/**
 * Uses a mail client and handler to poll and handle mail messages from a server.
 */
public abstract class AbstractMailPoller {

    private final MailReceiver mailReceiver;
    private final MessageHandler messageHandler;

    /**
     * @param mailClient of the server to be polled.
     * @param messageHandler handles messages returned by the poller.
     */
    public AbstractMailPoller(MailReceiver mailReceiver, MessageHandler messageHandler) {
        super();
        this.mailReceiver = mailReceiver;
        this.messageHandler = messageHandler;
    }

    /**
     * Poll the mail server for new messages and handle them.
     */
    public void poll() {
        try {
            //Update the Agent Settings Cache value
            MessageMonitoringUtil.updateAgentSettingsCacheTimeoutValue();
            mailReceiver.handleMessages(messageHandler);
        } catch (MailClientException e) {
            handleException(e);
        }
    }

    /**
     * Handle an exception thrown during message handling.
     * @param e exception to be handled.
     */
    public abstract void handleException(MailClientException e);
}
