/*
 * Copyright (c) 2009-2018, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.admingui.util;

/**
 *
 * @author achidamb
 */
public class GUIConstants {

    private GUIConstants() {

    }

    public enum EVENT_NAMES {

        PATIENT_DISCOVERY("PD"), PATIENT_DISCOVERY_DEF_REQ("PDDefReq"), PATIENT_DISCOVERY_DEF_RESP("PDDefResp"),
        QUERY_FOR_DOCUMENTS("QD"), RETRIEVE_DOCUMENTS("RD"), DOCUMENT_SUBMISSION("DS"), DOCUMENT_SUBMISISON_DEF_REQ("DSDefReq"),
        DOCUMENT_SUBMISSION_DEF_RESP("DSDefResp"), ADMIN_DIST("AD"), CORE_X12DS_REALTIME("X12RealTime"),
        CORE_X12DS_BATCH_REQ("X12BatchReq"), CORE_X12DS_BATCH_RESP("X12BatchResp"),
        DOCUMENT_DATA_SUBMISSION("DDS"), PATIENT_LOCATION_QUERY("PLQ");

        private String abbServiceName = null;

        EVENT_NAMES(String value) {
            abbServiceName = value;
        }

        public String getAbbServiceName() {
            return abbServiceName;
        }

        public static EVENT_NAMES fromValueString(String valueString) {
            if (valueString != null) {
                for (EVENT_NAMES enumValue : EVENT_NAMES.values()) {
                    if (valueString.equals(enumValue.abbServiceName)) {
                        return enumValue;
                    }
                }
            }
            throw new IllegalArgumentException("No enum constant " + valueString);
        }
    }

    public enum COLOR_CODING_CSS {
        RED("RED"), GREEN("GREEN"),
        YELLOW("YELLOW");

        private final String colorCodingCertExpiry;

        private COLOR_CODING_CSS(final String color) {
            colorCodingCertExpiry = color;
        }

        @Override
        public String toString() {
            return colorCodingCertExpiry;
        }
    }

}
