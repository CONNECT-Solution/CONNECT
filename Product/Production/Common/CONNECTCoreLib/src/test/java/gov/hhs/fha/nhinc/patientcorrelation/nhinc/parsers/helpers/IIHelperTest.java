/**
 * 
 */
package gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.helpers;

import static org.junit.Assert.assertEquals;

import org.hl7.v3.II;
import org.junit.Test;

/**
 * @author achidambaram
 *
 */
public class IIHelperTest {
    
    @SuppressWarnings("static-access")
    @Test
    public void  testIIFactoryCreateNull() {
        String nullFlavor = "NA";
        IIHelper II = new IIHelper();
        II ii = new II();
        ii = II.IIFactoryCreateNull(nullFlavor);
        assertEquals(ii.getNullFlavor().get(0),"NA");
    }
    
    @Test
    public void testIIFactoryCreateNullwithoutArg() {
        String nullFlavor = "NA";
        IIHelper II = new IIHelper();
        II ii = new II();
        ii = II.IIFactoryCreateNull();
        assertEquals(ii.getNullFlavor().get(0), "NA");  
        
    }
    
    @Test
    public void testIIFactory() {
        String root = "1.1";
        String extension ="D123401";
        IIHelper II = new IIHelper();
        II ii = new II();
        ii = II.IIFactory(root, extension);
        assertEquals(ii.getExtension(), "D123401");
        assertEquals(ii.getRoot(), "1.1");
    }
        
}
