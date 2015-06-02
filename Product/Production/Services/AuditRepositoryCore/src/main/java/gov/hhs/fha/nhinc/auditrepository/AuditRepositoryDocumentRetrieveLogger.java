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
package gov.hhs.fha.nhinc.auditrepository;

import gov.hhs.fha.nhinc.common.auditlog.DocRetrieveMessageType;
import gov.hhs.fha.nhinc.common.auditlog.DocRetrieveResponseMessageType;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;

public interface AuditRepositoryDocumentRetrieveLogger {

    /**
     * This method will create the generic Audit Log Message from a document retrieve request.
     *
     * @param message The Document Retrieve Request message to be audit logged.
     * @param direction The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logDocRetrieve(DocRetrieveMessageType message, String direction, String _interface);

    /**
     * This method will create the generic Audit Log Message from a document retrieve request.
     *
     * @param message The Document Retrieve Request message to be audit logged.
     * @param direction The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @param responseCommunityID The response Community ID
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logDocRetrieve(DocRetrieveMessageType message, String direction, String _interface,
            String responseCommunityID);

    /**
     * This method will create the generic Audit Log Message from a document retrieve response.
     *
     * @param message The Document Retrieve Response message to be audit logged.
     * @param direction The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logDocRetrieveResult(DocRetrieveResponseMessageType message, String direction,
            String _interface);

    /**
     * This method will create the generic Audit Log Message from a document retrieve response.
     *
     * @param message The Document Retrieve Response message to be audit logged.
     * @param direction The direction this message is going (Inbound or Outbound)
     * @param _interface The interface this message is being received/sent on (Entity, Adapter, or Nhin)
     * @param requestCommunityID The Request Community ID
     * @return A generic audit log message that can be passed to the Audit Repository
     */
    public LogEventRequestType logDocRetrieveResult(DocRetrieveResponseMessageType message, String direction,
            String _interface, String requestCommunityID);

}