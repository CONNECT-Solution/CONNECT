/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientcorrelationservice.config;
import gov.hhs.fha.nhinc.patientcorrelationservice.config.Expiration;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dunnek
 */
public class ExpirationConfiguration {
    private int defExpiration;
    private String defUnits;
    private List<Expiration> expirationList;

    public ExpirationConfiguration()
    {
        defUnits = "";
        defExpiration = -1;
        expirationList = new ArrayList<Expiration>();
    }
    public ExpirationConfiguration(int defaultExpiration, String defaultUnits)
    {
       defExpiration = defaultExpiration;
       defUnits = defaultUnits;
       expirationList = new ArrayList<Expiration>();
    }
    public List<Expiration> getExpirations()
    {
        return expirationList;
    }
    public int getDefaultDuration()
    {
        return defExpiration;
    }
    public String getDefaultUnits()
    {
        return defUnits;
    }

}
