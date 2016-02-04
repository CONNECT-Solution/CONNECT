/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.admingui.util;

/**
 *
 * @author achidamb
 */
public class GUIConstants {

    public static enum EVENT_NAMES {

        PatientDiscovery("PD"), PatientDiscoveryDeferredReq("PDDefReq"), PatientDiscoveryDeferredResp("PDDefResp"),
        QueryForDocuments("QD"), RetrieveDocuments("RD"), DocSubmission("DS"), DocSubmissionDeferredReq("DSDefReq"),
        DocSubmissionDeferredResp("DSDefResp"), AdminDistribution("AD");

        public String abbServiceName = null;

        EVENT_NAMES(String value) {
            this.abbServiceName = value;
        }

        public String getAbbServiceName() {
            return this.abbServiceName;
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
}
