/**
 * 
 */
package gov.hhs.fha.nhinc.direct.edge.proxy;

import javax.mail.internet.MimeMessage;

import gov.hhs.fha.nhinc.direct.DirectClient;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

/**
 * @author mweaver
 *
 */
public class DirectEdgeProxySmtpImpl implements DirectEdgeProxy {

    private DirectClient intDirectClient = null;
    
    /* (non-Javadoc)
     * @see gov.hhs.fha.nhinc.direct.edge.proxy.DirectEdgeProxy#provideAndRegisterDocumentSetB(org.nhindirect.stagent.MessageEnvelope)
     */
    @Override
    public RegistryResponseType provideAndRegisterDocumentSetB(MimeMessage message) {
        intDirectClient.send(message);
        return null;
    }
    
    public void setInternalDirectClient(DirectClient client) {
        intDirectClient = client;
    }

}
