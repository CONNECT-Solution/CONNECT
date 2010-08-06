/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.admindistribution.entity.proxy;
import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.admindistribution.entity.EntityAdminDistributionOrchImpl;
/**
 *
 * @author dunnek
 */
public class EntityAdminDistJavaImpl implements EntityAdminDistProxy{

    public void sendAlertMessage(EDXLDistribution body, AssertionType assertion, NhinTargetCommunitiesType target)
    {
        new EntityAdminDistributionOrchImpl().sendAlertMessage(body, assertion, target);
    }    
}
