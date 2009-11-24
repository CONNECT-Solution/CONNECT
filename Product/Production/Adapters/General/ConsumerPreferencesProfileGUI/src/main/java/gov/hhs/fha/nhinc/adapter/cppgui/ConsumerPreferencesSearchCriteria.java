/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapter.cppgui;

/**
 *
 * @author patlollav
 */
public class ConsumerPreferencesSearchCriteria
{
    private String patientID;
    private String assigningAuthorityID;

    public String getAssigningAuthorityID() {
        return assigningAuthorityID;
    }

    public void setAssigningAuthorityID(String assigningAuthorityID) {
        this.assigningAuthorityID = assigningAuthorityID;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }
    

}
