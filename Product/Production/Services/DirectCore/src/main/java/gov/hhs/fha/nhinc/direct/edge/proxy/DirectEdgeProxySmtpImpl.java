/**
 * 
 */
package gov.hhs.fha.nhinc.direct.edge.proxy;

import gov.hhs.fha.nhinc.direct.DirectException;
import gov.hhs.fha.nhinc.mail.MailClient;

import javax.mail.internet.MimeMessage;

import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

/**
 * @author mweaver
 *
 */
public class DirectEdgeProxySmtpImpl implements DirectEdgeProxy {

    private final MailClient internalMailClient;
    
    /**
     * @param internalMailClient
     */
    public DirectEdgeProxySmtpImpl(MailClient internalMailClient) {
        super();
        this.internalMailClient = internalMailClient;
    }

    /* (non-Javadoc)
     * @see gov.hhs.fha.nhinc.direct.edge.proxy.DirectEdgeProxy#provideAndRegisterDocumentSetB(org.nhindirect.stagent.MessageEnvelope)
     */
    @Override
    public RegistryResponseType provideAndRegisterDocumentSetB(MimeMessage message) {
        try {
            internalMailClient.send(message.getAllRecipients(), message);
        } catch (Exception e) {
            throw new DirectException("Error sending inbound direct message to smtp edge client.", e, message);
        }
        return null;
    }
    
}
