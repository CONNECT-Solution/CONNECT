/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
 * All rights reserved. 
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met: 
 *     * Redistributions of source code must retain the above 
 *       copyright notice, this list of conditions and the following disclaimer. 
 *     * Redistributions in binary form must reproduce the above copyright 
 *       notice, this list of conditions and the following disclaimer in the documentation 
 *       and/or other materials provided with the distribution. 
 *     * Neither the name of the United States Government nor the 
 *       names of its contributors may be used to endorse or promote products 
 *       derived from this software without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY 
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */
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
import org.oasis_open.docs.wsn.bw_2.SubscribeCreationFailedFault;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import gov.hhs.fha.nhinc.hiem.processor.faults.SoapFaultFactory;

/**
 * Utility for extracting a patient id
 * 
 * @author Neil Webb
 */
public class PatientIdExtractor {
    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory
            .getLog(PatientIdExtractor.class);

    /**
     * Extract a patient identifier from an element
     * 
     * @param subscribeElement Element containing subscribe message
     * @param topicConfig topic configuration containing patient id location and rules.
     * @return Extracted patient identifier
     * @throws org.oasis_open.docs.wsn.bw_2.SubscribeCreationFailedFault
     */
    public QualifiedSubjectIdentifierType extractPatientIdentifier(Element subscribeElement,
            TopicConfigurationEntry topicConfig) throws SubscribeCreationFailedFault {
        log.debug("begin extractPatientIdentifier");
        QualifiedSubjectIdentifierType patientIdentifier = null;
        if (log.isDebugEnabled()) {
            log.debug("Subscribe Patient id location: " + topicConfig.getPatientIdentifierSubscribeLocation());
            log.debug("Subscribe element: " + XmlUtility.serializeElementIgnoreFaults(subscribeElement));
        }
        String serializedPatientIdentifier = extractPatientId(subscribeElement,
                topicConfig.getPatientIdentifierSubscribeLocation());
        log.debug("Extracted patient identifier: " + serializedPatientIdentifier);

        if (NullChecker.isNotNullish(serializedPatientIdentifier)) {
            patientIdentifier = new QualifiedSubjectIdentifierType();
            patientIdentifier.setAssigningAuthorityIdentifier(PatientIdFormatUtil
                    .parseCommunityId(serializedPatientIdentifier));
            patientIdentifier.setSubjectIdentifier(PatientIdFormatUtil.parsePatientId(serializedPatientIdentifier));
            log.debug("Extracted Patient id: " + patientIdentifier.getSubjectIdentifier());
            log.debug("Extracted Assigning Authority: " + patientIdentifier.getAssigningAuthorityIdentifier());
        }

        if ((patientIdentifier == null) && topicConfig.isPatientRequired()) {
            throw new SoapFaultFactory().getPatientNotInSubscribeMessage();
        }
        return patientIdentifier;
    }

    private String extractPatientId(Element subscribeElement, String patientIdentifierLocation) {
        log.debug("Begin extractPatientId");
        String patientId = null;
        if (NullChecker.isNotNullish(patientIdentifierLocation)) {
            try {
                Node targetNode = XpathHelper.performXpathQuery(subscribeElement, patientIdentifierLocation);
                if (targetNode != null) {
                    patientId = XmlUtility.getNodeValue(targetNode);
                }
            } catch (XPathExpressionException ex) {
                Logger.getLogger(PatientIdExtractor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        log.debug("End extractPatientId - patient id: '" + patientId + "'");
        return patientId;
    }
}
