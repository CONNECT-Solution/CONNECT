/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.xdr;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;

/**
 *
 * @author dunnek
 */
public class NhinXDRImpl
{
    public static final String XDR_RESPONSE_SUCCESS = "Success";
    public static final String XDR_RESPONSE_FAILURE = "Failure";
    private static Log log = LogFactory.getLog(NhinXDRImpl.class);

public RegistryResponseType documentRepositoryProvideAndRegisterDocumentSetB(ProvideAndRegisterDocumentSetRequestType body,WebServiceContext context ) {
     RegistryResponseType result;

     log.debug("Entering NhinXDRImpl.documentRepositoryProvideAndRegisterDocumentSetB");
     result = createPositiveAck();

     return result;

}

    private RegistryResponseType createPositiveAck()
    {
        RegistryResponseType result= new RegistryResponseType();

        result.setStatus(XDR_RESPONSE_SUCCESS);

        return result;
    }
}
