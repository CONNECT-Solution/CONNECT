/**
 * 
 */
package gov.hhs.fha.nhinc.cxf.extraction;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;

import org.w3c.dom.Element;

/**
 * @author mweaver
 *
 */
public interface SAMLExtractorDOM {
    public AssertionType extractSAMLAssertion(final Element element);
}
