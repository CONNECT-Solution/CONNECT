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
package gov.hhs.fha.nhinc.audit;

/**
 * Constants shared between all AuditLogger implementations.
 *
 * @author achidamb
 */
public class AuditTransformsConstants {

    public static final Integer EVENT_OUTCOME_INDICATOR_SUCCESS = 0;
    public static final String ACTIVE_PARTICIPANT_USER_ID_SOURCE = "anonymous";
    public static final String ACTIVE_PARTICIPANT_ROLE_CODE_SOURCE_DISPLAY_NAME = "Source";
    public static final String ACTIVE_PARTICIPANT_ROLE_CODE_DESTINATION_DISPLAY_NAME = "Destination";
    public static final String ACTIVE_PARTICIPANT_UNKNOWN_IP_ADDRESS = "unknown";
    public static final String ACTIVE_PARTICIPANT_ROLE_CODE_DEST = "110152";
    public static final String ACTIVE_PARTICIPANT_ROLE_CODE_SOURCE = "110153";
    public static final String ACTIVE_PARTICIPANT_CODE_SYSTEM_NAME = "DCM";
    public static final Short NETWORK_ACCESSOR_PT_TYPE_CODE_NAME = 1;
    public static final Short NETWORK_ACCESSOR_PT_TYPE_CODE_IP = 2;

}
