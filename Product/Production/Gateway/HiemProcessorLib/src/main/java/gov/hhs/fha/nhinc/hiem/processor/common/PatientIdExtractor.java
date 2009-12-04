package gov.hhs.fha.nhinc.hiem.processor.common;

import gov.hhs.fha.nhinc.common.nhinccommon.QualifiedSubjectIdentifierType;
import gov.hhs.fha.nhinc.hiem.configuration.topicconfiguration.TopicConfigurationEntry;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.util.format.PatientIdFormatUtil;
import gov.hhs.fha.nhinc.xmlCommon.XmlUtility;
import gov.hhs.fha.nhinc.xmlCommon.XpathHelper;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.xpath.XPathExpressionException;
import org.oasis_open.docs.wsn.bw_2.ResourceUnknownFault;
import org.oasis_open.docs.wsn.bw_2.SubscribeCreationFailedFault;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import gov.hhs.fha.nhinc.hiem.processor.faults.SoapFaultFactory;

/**
 * Utility for extracting a patient id
 *
 * @author Neil Webb
 */
public class PatientIdExtractor
{
    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(PatientIdExtractor.class);

    /**
     * Extract a patient identifier from an element
     *
     * @param subscribeElement Element containing subscribe message
     * @param topicConfig topic configuration containing patient id location and rules.
     * @return Extracted patient identifier
     * @throws org.oasis_open.docs.wsn.bw_2.ResourceUnknownFault
     * @throws org.oasis_open.docs.wsn.bw_2.SubscribeCreationFailedFault
     */
    public QualifiedSubjectIdentifierType extractPatientIdentifier(Element subscribeElement, TopicConfigurationEntry topicConfig) throws ResourceUnknownFault, SubscribeCreationFailedFault
    {
        log.debug("begin extractPatientIdentifier");
        QualifiedSubjectIdentifierType patientIdentifier = null;
        if(log.isDebugEnabled())
        {
            log.debug("Subscribe Patient id location: " + topicConfig.getPatientIdentifierSubscribeLocation());
            log.debug("Subscribe element: " + XmlUtility.serializeElementIgnoreFaults(subscribeElement));
        }
        String serializedPatientIdentifier = extractPatientId(subscribeElement, topicConfig.getPatientIdentifierSubscribeLocation());
        log.debug("Extracted patient identifier: " + serializedPatientIdentifier);

        if(NullChecker.isNotNullish(serializedPatientIdentifier))
        {
            patientIdentifier = new QualifiedSubjectIdentifierType();
            patientIdentifier.setAssigningAuthorityIdentifier(PatientIdFormatUtil.parseCommunityId(serializedPatientIdentifier));
            patientIdentifier.setSubjectIdentifier(PatientIdFormatUtil.parsePatientId(serializedPatientIdentifier));
            log.debug("Extracted Patient id: " + patientIdentifier.getSubjectIdentifier());
            log.debug("Extracted Assigning Authority: " + patientIdentifier.getAssigningAuthorityIdentifier());
        }

        if ((patientIdentifier == null) && topicConfig.isPatientRequired())
        {
            throw new SoapFaultFactory().getPatientNotInSubscribeMessage();
        }
        return patientIdentifier;
    }

    private String extractPatientId(Element subscribeElement, String patientIdentifierLocation)
    {
        log.debug("Begin extractPatientId");
        String patientId = null;
        if(NullChecker.isNotNullish(patientIdentifierLocation))
        {
            try
            {
                Node targetNode = XpathHelper.performXpathQuery(subscribeElement, patientIdentifierLocation);
                if(targetNode != null)
                {
                    patientId = XmlUtility.getNodeValue(targetNode);
                }
            }
            catch (XPathExpressionException ex)
            {
                Logger.getLogger(PatientIdExtractor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        log.debug("End extractPatientId - patient id: '" + patientId + "'");
        return patientId;
    }
}
