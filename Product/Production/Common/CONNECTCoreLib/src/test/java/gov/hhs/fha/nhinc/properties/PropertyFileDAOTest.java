/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;
import java.util.Set;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;

/**
 * @author akong
 *
 */
public class PropertyFileDAOTest {

    private final static String TEST_PROPERTIES_NAME = "gatewaytest.properties";

    protected Mockery context = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    @Test
    public void testLoadPropertyFile() throws PropertyAccessException {
        PropertyFileDAO propDAO = loadTestProperties();

        assertTrue(propDAO.getProperty(TEST_PROPERTIES_NAME, "booleanTrueTest").equals("true"));
    }

    @Test(expected = PropertyAccessException.class)
    public void testLoadPropertyFile_FileDoesNotExists() throws PropertyAccessException {
        File propertyFile = new File("/config/PropertyAccessorTest/doesnotexist");
        PropertyFileDAO propDAO = new PropertyFileDAO();
        propDAO.loadPropertyFile(propertyFile, "doesnotexist");
    }

    @Test
    public void testGetProperty() throws PropertyAccessException {
        PropertyFileDAO propDAO = loadTestProperties();

        assertTrue(propDAO.getProperty(TEST_PROPERTIES_NAME, "stringTest").equals("test123"));
        assertNull(propDAO.getProperty(TEST_PROPERTIES_NAME, "nonexistantfield"));
        assertNull(propDAO.getProperty("nonexistant", "stringTest"));
    }

    @Test
    public void testGetPropertyBoolean() throws PropertyAccessException {
        PropertyFileDAO propDAO = loadTestProperties();

        assertTrue(propDAO.getPropertyBoolean(TEST_PROPERTIES_NAME, "booleanTrueTest"));
        assertTrue(propDAO.getPropertyBoolean(TEST_PROPERTIES_NAME, "booleanTrueTest2"));
        assertFalse(propDAO.getPropertyBoolean(TEST_PROPERTIES_NAME, "booleanFalseTest"));
        assertFalse(propDAO.getPropertyBoolean(TEST_PROPERTIES_NAME, "booleanFalseTest2"));
    }

    @Test(expected = PropertyAccessException.class)
    public void testGetPropertyBoolean_EmptyBooleanValue() throws PropertyAccessException {
        PropertyFileDAO propDAO = loadTestProperties();

        propDAO.getPropertyBoolean(TEST_PROPERTIES_NAME, "booleanErrorTest");
    }

    @Test(expected = PropertyAccessException.class)
    public void testGetPropertyBoolean_MissingField() throws PropertyAccessException {
        PropertyFileDAO propDAO = loadTestProperties();

        propDAO.getPropertyBoolean(TEST_PROPERTIES_NAME, "nonexistantfield");
    }

    @Test(expected = PropertyAccessException.class)
    public void testGetPropertyBoolean_NoPropertyFile() throws PropertyAccessException {
        PropertyFileDAO propDAO = loadTestProperties();

        propDAO.getPropertyBoolean("nonexistant", "nonexistantfield");
    }

    @Test
    public void testGetPropertyLong() throws PropertyAccessException {
        PropertyFileDAO propDAO = loadTestProperties();

        assertEquals(1500, propDAO.getPropertyLong(TEST_PROPERTIES_NAME, "longTest"));
    }

    @Test(expected = PropertyAccessException.class)
    public void testGetPropertyLong_BadValue() throws PropertyAccessException {
        PropertyFileDAO propDAO = loadTestProperties();
        propDAO.getPropertyLong(TEST_PROPERTIES_NAME, "longErrorTest");
    }

    @Test(expected = PropertyAccessException.class)
    public void testGetPropertyLong_MissingField() throws PropertyAccessException {
        PropertyFileDAO propDAO = loadTestProperties();

        propDAO.getPropertyLong(TEST_PROPERTIES_NAME, "nonexistantfield");
    }

    @Test(expected = PropertyAccessException.class)
    public void testGetPropertyLong_NoPropertyFile() throws PropertyAccessException {
        PropertyFileDAO propDAO = loadTestProperties();

        propDAO.getPropertyLong("nonexistant", "nonexistantfield");
    }

    @Test
    public void testGetPropertyNames() throws PropertyAccessException {
        PropertyFileDAO propDAO = loadTestProperties();

        Set<String> properties = propDAO.getPropertyNames(TEST_PROPERTIES_NAME);
        assertEquals(9, properties.size());
        assertTrue(properties.contains("stringTest"));

        properties = propDAO.getPropertyNames("nonexistant");
        assertNull(properties);
    }

    @Test
    public void testGetPropertyNames_NoPropertyFile() throws PropertyAccessException {
        PropertyFileDAO propDAO = loadTestProperties();

        Set<String> properties = propDAO.getPropertyNames("nonexistant");
        assertNull(properties);
    }

    @Test
    public void testGetProperties() throws PropertyAccessException {
        PropertyFileDAO propDAO = loadTestProperties();

        Properties properties = propDAO.getProperties(TEST_PROPERTIES_NAME);
        assertTrue(properties.getProperty("stringTest").equals("test123"));
    }

    private File getFile(String filename) {
        URL url = this.getClass().getResource(filename);
        try {
            return new File(url.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private PropertyFileDAO loadTestProperties() throws PropertyAccessException {
        File propertyFile = getFile("/config/PropertyAccessorTest/gatewaytest.properties");
        PropertyFileDAO propDAO = new PropertyFileDAO();

        propDAO.loadPropertyFile(propertyFile, TEST_PROPERTIES_NAME);

        return propDAO;
    }

    @Test
    public void testGetPropertyWithComma() throws PropertyAccessException {
        PropertyFileDAO propDAO = loadTestProperties();

        assertTrue(propDAO.getProperty(TEST_PROPERTIES_NAME, "stringWithCommaTest").equals("test1,test2,test3,test4"));
    }
}
