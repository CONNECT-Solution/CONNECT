/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(RootTopicExtractorHelper.class);
    private static final String startDelimter = "{";
    private static final String endDelimter = "}";

    public static String removeNamespaceHolder(String xml) {
        String newXml = xml;
        if (NullChecker.isNotNullish(xml)) {
            newXml = xml.replaceAll("\\{([^\\}]*)\\}", "" );
        }
        return newXml;
    }

    public static String ReplaceNamespacePrefixesWithNamespaces(String xml, Node node) throws SubscriptionRepositoryException {
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
                    //todo: throw better exception
                    throw new SubscriptionRepositoryException("Unable to determine namespace for prefix '" + prefix + "'.  Prefix not defined in source node.");
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
