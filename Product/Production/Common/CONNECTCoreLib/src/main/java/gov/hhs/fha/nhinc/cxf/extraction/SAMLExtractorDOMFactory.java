/**
 * 
 */
package gov.hhs.fha.nhinc.cxf.extraction;

import gov.hhs.fha.nhinc.openSAML.extraction.OpenSAMLAssertionExtractorImpl;

/**
 * @author mweaver
 *
 */
public class SAMLExtractorDOMFactory {
    
    public SAMLExtractorDOMFactory() {
        
    }
    
    public SAMLExtractorDOM getExtractor() {
        return new OpenSAMLAssertionExtractorImpl();
    }

}
