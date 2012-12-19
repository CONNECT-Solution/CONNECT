/**
 * 
 */
package gov.hhs.fha.nhinc.direct.edge.proxy;

import gov.hhs.fha.nhinc.direct.DirectException;
import gov.hhs.fha.nhinc.mail.MailSender;

import javax.mail.internet.MimeMessage;

import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

/**
 * @author mweaver
 *
 */
public class DirectEdgeProxySmtpImpl implements DirectEdgeProxy {

    private final MailSender internalMailSender;
    
    /**
     * @param internalMailSender used to send mail messages.
     */
    public DirectEdgeProxySmtpImpl(MailSender internalMailSender) {
        this.internalMailSender = internalMailSender;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RegistryResponseType provideAndRegisterDocumentSetB(MimeMessage message) {
        try {
            internalMailSender.send(message.getAllRecipients(), message);
        } catch (Exception e) {
            throw new DirectException("Error sending inbound direct message to smtp edge client.", e, message);
        }
        return null;
    }
    
}
