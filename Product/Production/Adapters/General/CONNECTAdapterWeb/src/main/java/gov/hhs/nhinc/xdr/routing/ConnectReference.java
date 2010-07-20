/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.nhinc.xdr.routing;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import gov.hhs.fha.nhinc.xdr.adapter.XDRHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 *
 * @author dunnek
 */
public class ConnectReference implements XDRRouting{
    private static Log log = null;

    public ConnectReference()
    {
        log = createLogger();
    }
    public RegistryResponseType provideAndRegisterDocumentSetB(ProvideAndRegisterDocumentSetRequestType request, AssertionType assertion)
    {
        log.info("Inside Connect Reference provideAndRegisterDocumentSetB()");
        XDRHelper helper = new XDRHelper();

       return helper.createPositiveAck();
    }
    protected Log createLogger()
    {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }
}
