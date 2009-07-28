/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.hiem.dte;

import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.xmlCommon.XmlUtility;
import gov.hhs.fha.nhinc.xmlCommon.XpathHelper;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.Element;

/**
 *
 * @author rayj
 */
public class TargetBuilder {

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(TargetBuilder.class);

    public NhinTargetSystemType buildSubscriptionManagerTarget(String subscriptionReferenceXml) throws XPathExpressionException {
        String xpathQuery = "//*[local-name()='Address']";
        NhinTargetSystemType target = null;
        Element subscriptionReferenceAddressElement;
        subscriptionReferenceAddressElement = (Element) XpathHelper.performXpathQuery(subscriptionReferenceXml, xpathQuery);
        String subscriptionReferenceAddress = XmlUtility.getNodeValue(subscriptionReferenceAddressElement);
        target = new NhinTargetSystemType();
        target.setUrl(subscriptionReferenceAddress);
        return target;
    }
}
