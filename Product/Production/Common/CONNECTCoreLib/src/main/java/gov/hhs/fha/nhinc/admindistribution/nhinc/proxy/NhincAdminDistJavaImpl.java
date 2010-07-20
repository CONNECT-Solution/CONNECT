/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.admindistribution.nhinc.proxy;
import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.admindistribution.nhinc.NhincAdminDistImpl;
/**
 *
 * @author dunnek
 */
public class NhincAdminDistJavaImpl implements NhincAdminDistProxy{
   private Log log = null;

    public NhincAdminDistJavaImpl()
    {
        log = createLogger();
    }
    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }
    public void sendAlertMessage(EDXLDistribution body, AssertionType assertion, NhinTargetSystemType target)
    {
        log.info("begin sendAlert");
        this.getNhincAdminDistImpl().sendAlertMessage(body, assertion, target);

    }
    protected NhincAdminDistImpl getNhincAdminDistImpl()
    {
        return new NhincAdminDistImpl();
    }
}
