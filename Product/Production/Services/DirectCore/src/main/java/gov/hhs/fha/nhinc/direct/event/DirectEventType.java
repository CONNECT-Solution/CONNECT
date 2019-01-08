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

/**
 * Enumeration of event types for direct event logging.
 */
public enum DirectEventType {

    /**
     * Begin sending outbound direct message.
     */
    BEGIN_OUTBOUND_DIRECT,
    /**
     * End sending outbound direct message.
     */
    END_OUTBOUND_DIRECT,
    /**
     * Begin handling an inbound direct (non-MDN) message.
     */
    BEGIN_INBOUND_DIRECT,
    /**
     * End handling an inbound direct (non-MDN) message.
     */
    END_INBOUND_DIRECT,
    /**
     * Begin sending an outbound MDN message.
     */
    BEGIN_OUTBOUND_MDN,
    /**
     * End sending an outbound MDN message.
     */
    END_OUTBOUND_MDN,
    /**
     * Begin handling an inbound MDN message.
     */
    BEGIN_INBOUND_MDN,
    /**
     * End handling an inbound MDN message.
     */
    END_INBOUND_MDN,
    /**
     * Begin handling an inbound Processed MDN message.
     */
    BEGIN_INBOUND_MDN_PROCESSED,
    /**
     * End handling an inbound processed MDN message.
     */
    END_INBOUND_MDN_PROCESSED,
    /**
     * Begin handling an inbound dispatched MDN message.
     */
    BEGIN_INBOUND_MDN_DISPATCHED,
    /**
     * End handling an inbound dispatched MDN message.
     */
    END_INBOUND_MDN_DISPATCHED,
    /**
     * Begin handling an inbound Failed MDN message.
     */
    BEGIN_INBOUND_MDN_FAILED,
    /**
     * End handling an inbound Failed MDN message.
     */
    END_INBOUND_MDN_FAILED,
    /**
     * Begin handling an Outbound Processed MDN message.
     */
    BEGIN_OUTBOUND_MDN_PROCESSED,
    /**
     * End handling an Outbound Processed MDN message.
     */
    END_OUTBOUND_MDN_PROCESSED,
    /**
     * Begin handling an Outbound Dispatched MDN message.
     */
    BEGIN_OUTBOUND_MDN_DISPATCHED,
    /**
     * Begin handling an Outbound Dispatched MDN message.
     */
    END_OUTBOUND_MDN_DISPATCHED,
    /**
     * Begin handling an Outbound Failed MDN message.
     */
    BEGIN_OUTBOUND_MDN_FAILED,
    /**
     * End handling an Outbound Failed MDN message.
     */
    END_OUTBOUND_MDN_FAILED,
    /**
     * Once an Successful Edge notification is sent.
     */
    DIRECT_EDGE_NOTIFICATION_SUCCESSFUL,
    /**
     * Once an Failed Edge notification is sent.
     */
    DIRECT_EDGE_NOTIFICATION_FAILED,
    /**
     * An error or exception condition was generated in the Direct Module.
     */
    DIRECT_ERROR;

}
