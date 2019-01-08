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
package gov.hhs.fha.nhinc.docrepository.adapter.model;

import gov.hhs.fha.nhinc.nhinclib.NullChecker;

/**
 * Query parameter for a single event code.
 *
 * @author Neil Webb
 */
public class EventCodeParam {

    private String eventCode;
    private String eventCodeScheme;

    public String getEventCode() {
        return eventCode;
    }

    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }

    public String getEventCodeScheme() {
        return eventCodeScheme;
    }

    public void setEventCodeScheme(String eventCodeScheme) {
        this.eventCodeScheme = eventCodeScheme;
    }

    @Override
    public int hashCode() {
        int hashCode = 0;
        if (NullChecker.isNotNullish(eventCode)) {
            hashCode = eventCode.hashCode();
            if (NullChecker.isNotNullish(eventCodeScheme)) {
                hashCode += eventCodeScheme.hashCode();
            }
        }
        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!this.getClass().equals(obj.getClass())) {
            return false;
        }
        EventCodeParam toCheck = (EventCodeParam) obj;

        if (getEventCode() == null && toCheck.getEventCode() != null) {
            return false;
        } else if (getEventCode() != null && !getEventCode().equals(toCheck.getEventCode())) {
            return false;
        }

        if (getEventCodeScheme() == null && toCheck.getEventCodeScheme() != null) {
            return false;
        } else if (getEventCodeScheme() != null && !getEventCodeScheme().equals(toCheck.getEventCodeScheme())) {
            return false;
        }
        return true;
    }

}
