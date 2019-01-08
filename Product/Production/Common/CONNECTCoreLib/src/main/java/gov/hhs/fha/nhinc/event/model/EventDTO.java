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
package gov.hhs.fha.nhinc.event.model;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ttang
 *
 */
public class EventDTO extends DatabaseEvent {
    private static final Logger LOG = LoggerFactory.getLogger(EventDTO.class);
    public static final boolean OMIT_DESCRIPTION = true;

    private String exceptionType;
    private String version;

    public String getExceptionType() {
        return exceptionType;
    }

    public void setExceptionType(String exceptionType) {
        this.exceptionType = exceptionType;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public static EventDTO convertFrom(DatabaseEvent event) {
        return convertFrom(event, !OMIT_DESCRIPTION);
    }

    public static EventDTO convertFrom(DatabaseEvent event, boolean omitDescription) {
        EventDTO dto = new EventDTO();
        if (null != event) {
            dto.setId(event.getId());
            if (!omitDescription) {
                dto.setDescription(event.getDescription());
            }
            dto.setMessageID(event.getMessageID());
            dto.setTransactionID(event.getTransactionID());
            dto.setServiceType(event.getServiceType());
            dto.setInitiatorHcid(event.getInitiatorHcid());
            dto.setRespondingHcid(event.getRespondingHcid());
            dto.setEventName(event.getEventName());
            dto.setEventTime(event.getEventTime());

            try {
                JSONObject json = new JSONObject(event.getDescription());

                if (json.has("version")) {
                    dto.setVersion(json.getString("version"));
                } else {
                    dto.setVersion("N/A");
                }

                if (json.has("exceptionClass")) {
                    dto.setExceptionType(json.getString("exceptionClass"));
                }

            } catch (JSONException ex) {
                LOG.error("error converting json-object: {}", ex.getMessage(), ex);
            }
        }
        return dto;
    }
}
