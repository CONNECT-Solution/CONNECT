package gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.PRPAIN201309UV.helpers;

import static org.junit.Assert.assertEquals;
import gov.hhs.fha.nhinc.common.nhinccommon.QualifiedSubjectIdentifierType;

import org.hl7.v3.II;
import org.junit.Test;

/**
 * @author achidambaram
 *
 */
public class IIHelperTest {
    
    @Test
    public void IIFactoryCreateNull() {
        II ii = new II();
        IIHelper helper = new IIHelper();
        String nullFlavor = "NOT Available";
        ii = helper.IIFactoryCreateNull(nullFlavor);
        assertEquals(ii.getNullFlavor().get(0), "NA");
    }
    
    @Test
    public void testIIFactoryCreateNull() {
        II ii = new II();
        IIHelper helper = new IIHelper();
        ii = helper.IIFactoryCreateNull();
        assertEquals(ii.getNullFlavor().get(0), "NA");
    }
    
    @Test
    public void testIIFactory() {
        II ii = new II();
        IIHelper helper = new IIHelper();
        String root = "1.1";
        String extension = "1.16.17.19";
        ii= helper.IIFactory(root, extension);
        assertEquals(ii.getExtension(), extension);
        assertEquals(ii.getRoot(), root);
    }
    
    @Test
    public void IIFactory() {
        II ii = new II();
        IIHelper helper = new IIHelper();
        ii = helper.IIFactory(createQualifiedSubjectIdentifierType());
        assertEquals(ii.getExtension(), "D123401");
        assertEquals(ii.getRoot(), "1.1");
    }
    
    private QualifiedSubjectIdentifierType createQualifiedSubjectIdentifierType() {
        QualifiedSubjectIdentifierType identifier = new QualifiedSubjectIdentifierType();
        identifier.setAssigningAuthorityIdentifier("1.1");
        identifier.setSubjectIdentifier("D123401");
        return identifier;
    }
      
}
