/**
 * 
 */
package gov.hhs.fha.nhinc.direct.edge.proxy;

import gov.hhs.fha.nhinc.direct.DirectClient;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

import org.nhindirect.stagent.MessageEnvelope;
import org.nhindirect.stagent.mail.Message;

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
    public RegistryResponseType provideAndRegisterDocumentSetB(MessageEnvelope message) {
        Message msg = message.getMessage();
        intDirectClient.send(msg);
        return null;
    }
    
    public void setInternalDirectClient(DirectClient client) {
        intDirectClient = client;
    }

}
