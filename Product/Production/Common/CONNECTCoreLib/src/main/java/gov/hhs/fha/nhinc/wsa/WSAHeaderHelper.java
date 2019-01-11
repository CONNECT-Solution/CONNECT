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
package gov.hhs.fha.nhinc.wsa;

import java.util.UUID;

/**
 * @author mweaver
 *
 */
public class WSAHeaderHelper {

    public static final String prefix = "urn:uuid:";

    public WSAHeaderHelper() {

    }

    /**
     * @param identifier
     * @return
     */
    public String addUrnUuid(String identifier) {
        if (identifier != null) {
            if (identifier.startsWith(prefix)) {
                return identifier;
            } else {
                return prefix.concat(identifier);
            }
        } else {
            return null;
        }
    }

    /**
     * Generates a message ID with the correct prefix
     *
     * @return the message ID
     */
    public String generateMessageID() {
        return addUrnUuid(UUID.randomUUID().toString());
    }

    /**
     * @param messageId
     */
    public String fixMessageIDPrefix(String messageId) {
        if (!hasProperMessageIDPrefix(messageId)) {
            if (hasPrefix(messageId, "uuid:")) {
                messageId = "urn:" + messageId;
            } else {
                messageId = prefix.concat(messageId);
            }
        }

        return messageId;
    }

    private boolean hasProperMessageIDPrefix(String messageId) {
        return messageId.trim().startsWith(prefix);
    }

    private boolean hasPrefix(String messageId, String illegalPrefix) {
        return messageId.trim().startsWith(illegalPrefix);
    }

}
