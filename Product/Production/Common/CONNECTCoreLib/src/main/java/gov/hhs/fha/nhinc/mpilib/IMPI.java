/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.mpilib;

/**
 *
 * @author rayj
 */
public interface IMPI {
    Patient AddUpdate(Patient value);
    Patients Search(Patient searchParams, boolean AllowSearchByDemographics);
    Patients Search(Patient searchParams);
}
