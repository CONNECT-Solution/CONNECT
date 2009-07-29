/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.interoptest;

import javax.xml.stream.XMLStreamReader;
import org.w3c.dom.Element;

/**
 *
 * @author Admin
 */
public class InteropTestValidator implements com.sun.xml.wss.impl.callback.SAMLAssertionValidator
{
    public void validate(Element arg0) throws SAMLValidationException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void validate(XMLStreamReader arg0) throws SAMLValidationException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
