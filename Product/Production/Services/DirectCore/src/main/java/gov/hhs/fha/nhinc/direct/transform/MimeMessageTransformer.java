/**
 * 
 */
package gov.hhs.fha.nhinc.direct.transform;

import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nhindirect.xd.transform.MimeXdsTransformer;
import org.nhindirect.xd.transform.exception.TransformationException;
import org.nhindirect.xd.transform.impl.DefaultMimeXdsTransformer;

import gov.hhs.fha.nhinc.direct.DirectException;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;

/**
 * @author mweaver
 *
 */
public class MimeMessageTransformer {
    
    private static final Log LOG = LogFactory.getLog(MimeMessageTransformer.class);
    private MimeXdsTransformer transformer = null;
    private String errorMessage = "Error transforming message to XDR";
    
    public MimeMessageTransformer() {
        transformer = getMimeXdsTransformer();
    }
    
    public ProvideAndRegisterDocumentSetRequestType transform(MimeMessage message) {
        ProvideAndRegisterDocumentSetRequestType request = null;
        try {
            request = transformer.transform(message);
        } catch (TransformationException e) {
            LOG.error(errorMessage, e);
            throw new DirectException(errorMessage, e);
        }
        return request;
    }
    
    private MimeXdsTransformer getMimeXdsTransformer() {
        return new DefaultMimeXdsTransformer();
    }

}
