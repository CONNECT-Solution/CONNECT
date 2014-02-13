/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.direct.addressparsing;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author svalluripalli
 */
public class ToAddressParserFactoryTest {

    /**
     * Test of getToParser method, of class ToAddressParserFactory.
     */
    @Test
    public void testGetToParser() {
        ToAddressParserFactory instance = new ToAddressParserFactory();
        ToAddressParser result = instance.getToParser();
        assertTrue(result instanceof ToAddressParser);
    }
}