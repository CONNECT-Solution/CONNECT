/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.hiem.consumerreference;

import gov.hhs.fha.nhinc.hiem.dte.SoapUtil;
import gov.hhs.fha.nhinc.xmlCommon.XmlUtility;
import java.util.ArrayList;
import java.util.List;
import javax.xml.soap.SOAPHeader;
import org.w3c.dom.Element;

/**
 *
 * @author rayj
 */
public class ReferenceParametersElements   {

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(SoapUtil.class);
    private List<Element> elements = null;

    public ReferenceParametersElements() {
        elements = new ArrayList();
    }

    public List<Element> getElements() {
        return elements;
    }
}
