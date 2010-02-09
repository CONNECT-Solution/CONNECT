/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.xdr.proxy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.xml.ws.WebServiceContext;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType;

/**
 *
 * @author dunnek
 */
public class NhincProxyXDRSecuredImpl {
    private Log log = null;

    public NhincProxyXDRSecuredImpl()
    {
        log = createLogger();
    }
    public RegistryResponseType provideAndRegisterDocumentSetB(
            RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType body, WebServiceContext context) {
        log.debug("begin provideAndRegisterDocumentSetB()");

        return new RegistryResponseType();
    }
    protected Log createLogger()
    {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }
}
