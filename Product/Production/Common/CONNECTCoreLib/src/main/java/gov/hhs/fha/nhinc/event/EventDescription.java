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

import java.util.List;

/**
 * Interface defining data expected to be in Event.description.
 *
 * @author bhumphrey
 *
 */
public interface EventDescription {

    public String getMessageId();

    /**
     *
     * @return
     */
    public String getTimeStamp();

    /**
     * Message service type (with version).
     *
     * @return
     */
    public String getServiceType();

    /**
     * transaction id (this is called "correlation id" in the requirements doc)
     *
     * @return
     */
    public String getTransactionId();

    /**
     * Payload Types (C32, C62 etc.).
     *
     * @return
     */
    public List<String> getPayloadTypes();

    /**
     * Payload Sizes (message specific).
     *
     * @return
     */
    public List<String> getPayloadSizes();

    /**
     * HCID for Initiatinggateway.
     *
     * @return
     */
    public String getInitiatingHCID();

    /**
     * HCID(s) for Responding gateway(s).
     *
     * @return
     */
    public List<String> getRespondingHCIDs();

    /**
     *
     * @return
     */
    public String getNPI();

    /**
     * Message status (Success/Failure). Returns a list to support response objects that allow multiple responses (ie,
     * queries).
     *
     * @return
     */
    public List<String> getStatuses();

    /**
     * Error code(s) when failed.
     *
     * @return
     */
    public List<String> getErrorCodes();

    public String getVersion();

    /**
     * Returns a list of response message ids. Can be an empty list (eg [] ).
     *
     * @return
     */
    public List<String> getResponseMsgIdList();

}
