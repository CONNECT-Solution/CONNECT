/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.xdr.adapter;
import javax.xml.ws.WebServiceContext;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterProvideAndRegisterDocumentSetRequestType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author dunnek
 */
public class AdapterComponentXDRImpl {
    private static Log log = null;
    public AdapterComponentXDRImpl()
    {
        log = createLogger();
    }
    public RegistryResponseType provideAndRegisterDocumentSetb(AdapterProvideAndRegisterDocumentSetRequestType body) {
        log.debug("Begin provideAndRegisterDocumentSetb()");
        return createPositiveAck();
    }
    protected Log createLogger()
    {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }
    private RegistryResponseType createPositiveAck()
    {
        RegistryResponseType result= new RegistryResponseType();

        result.setStatus("Success");

        return result;
    }
    
}
