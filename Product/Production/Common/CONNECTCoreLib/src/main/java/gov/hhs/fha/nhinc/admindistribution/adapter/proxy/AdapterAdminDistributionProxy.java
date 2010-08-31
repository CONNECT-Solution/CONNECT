/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.admindistribution.adapter.proxy;
import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;

/**
 *
 * @author dunnek
 */
public interface AdapterAdminDistributionProxy {

    public void sendAlertMessage(EDXLDistribution body, AssertionType assertion);

}
