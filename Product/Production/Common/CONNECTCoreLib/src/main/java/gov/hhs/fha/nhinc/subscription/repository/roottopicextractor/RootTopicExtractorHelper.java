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
package gov.hhs.fha.nhinc.subscription.repository.roottopicextractor;

import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.subscription.repository.service.SubscriptionRepositoryException;
import gov.hhs.fha.nhinc.xmlCommon.XmlUtility;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.ls.LSException;

/**
 * 
 * @author rayj
 */
public class RootTopicExtractorHelper {

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory
            .getLog(RootTopicExtractorHelper.class);
    private static final String startDelimter = "{";
    private static final String endDelimter = "}";

    public static String removeNamespaceHolder(String xml) {
        String newXml = xml;
        if (NullChecker.isNotNullish(xml)) {
            newXml = xml.replaceAll("\\{([^\\}]*)\\}", "");
        }
        return newXml;
    }

    public static String ReplaceNamespacePrefixesWithNamespaces(String xml, Node node)
            throws SubscriptionRepositoryException {
        log.debug("begin ReplaceNamespacePrefixesWithNamespaces for xml string: '" + xml + "'");
        log.debug("node='" + XmlUtility.serializeElementIgnoreFaults((Element) node) + "'");
        int positionOfDelimiter = xml.indexOf(":");
        String prefix;
        String value;
        String namespaceValue;
        String newValue;
        if (positionOfDelimiter > 0) {
            prefix = xml.substring(0, positionOfDelimiter);
            log.debug("prefix='" + prefix + "'");
            value = xml.substring(positionOfDelimiter + 1);
            log.debug("value='" + value + "'");
            namespaceValue = node.lookupNamespaceURI(prefix);
            log.debug("namespaceValue='" + namespaceValue + "'");
            if (namespaceValue == null) {
                log.warn("Unable to determine namespace for prefix '" + prefix + "'");
                if (supportUndefinedNamespacePrefix()) {
                    namespaceValue = null;
                } else {
                    // todo: throw better exception
                    throw new SubscriptionRepositoryException("Unable to determine namespace for prefix '" + prefix
                            + "'.  Prefix not defined in source node.");
                }
            }

            newValue = "{" + namespaceValue + "}" + value;
            xml = newValue;
            log.debug("updated xml='" + xml + "'");
        }

        log.debug("end ReplaceNamespacePrefixesWithNamespaces");
        return xml;
    }

    private static boolean supportUndefinedNamespacePrefix() {
        return true;
    }
}
