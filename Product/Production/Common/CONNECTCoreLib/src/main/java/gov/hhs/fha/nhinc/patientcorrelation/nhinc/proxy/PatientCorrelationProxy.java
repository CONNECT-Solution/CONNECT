/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientcorrelation.nhinc.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import org.hl7.v3.AddPatientCorrelationResponseType;
import org.hl7.v3.PRPAIN201301UV02;
import org.hl7.v3.PRPAIN201309UV02;
import org.hl7.v3.RetrievePatientCorrelationsResponseType;

/**
 *
 * @author jhoppesc
 */
public interface PatientCorrelationProxy {

    public RetrievePatientCorrelationsResponseType retrievePatientCorrelations(PRPAIN201309UV02 request, AssertionType assertion);

    public AddPatientCorrelationResponseType addPatientCorrelation(PRPAIN201301UV02 request, AssertionType assertion);

}
