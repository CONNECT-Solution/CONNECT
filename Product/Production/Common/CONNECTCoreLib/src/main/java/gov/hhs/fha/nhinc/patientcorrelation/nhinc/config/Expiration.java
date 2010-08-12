/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientcorrelationservice.config;

/**
 *
 * @author dunnek
 */
public class Expiration {
    private String aa = "";
    private int dur;
    private String units;

    public Expiration(String assigningAuthority, String durationUnits, int duration)
    {
        aa = assigningAuthority;
        dur = duration;
        units = durationUnits;
    }
    public String getAssigningAuthority()
    {
        return aa;
    }
    public int getDuration()
    {
        return dur;
    }
    public String getUnits()
    {
        return units;
    }

}
