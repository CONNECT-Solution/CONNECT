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
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URL;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;

/**
 * @author akong
 *
 */
public class PropertyAccessorFileUtilitiesTest {

    private static String EXPECTED_PROPERTY_FILE_LOCATION;

    protected Mockery context = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    @Test
    public void testGetPropertyFileLocation() {
        PropertyAccessorFileUtilities fileUtilities = createPropertyAccessorFileUtilities();

        String fileDir = fileUtilities.getPropertyFileLocation();
        assertTrue(fileDir.endsWith(File.separator));
        assertEquals(EXPECTED_PROPERTY_FILE_LOCATION, fileDir);
    }

    @Test
    public void testGetPropertyFileLocation_WithFileName() {
        PropertyAccessorFileUtilities fileUtilities = createPropertyAccessorFileUtilities();

        String fileLocation = fileUtilities.getPropertyFileLocation("gateway");
        assertEquals(EXPECTED_PROPERTY_FILE_LOCATION + "gateway.properties", fileLocation);
    }

    @Test
    public void testSetPropertyFileLocation() {
        PropertyAccessorFileUtilities fileUtilities = createPropertyAccessorFileUtilities();

        fileUtilities.setPropertyFileLocation("/test");
        assertTrue(fileUtilities.getPropertyFileLocation().endsWith(File.separator));
    }

    @Test
    public void testGetPropertyFileURL() {
        PropertyAccessorFileUtilities fileUtilities = createPropertyAccessorFileUtilities();

        String fileUrl = fileUtilities.getPropertyFileURL();
        assertEquals("file:///" + EXPECTED_PROPERTY_FILE_LOCATION, fileUrl);
    }

    @Test
    public void testNoSystemVariableSet() {
        System.clearProperty("nhinc.properties.dir");

        PropertyAccessorFileUtilities fileUtilities = new PropertyAccessorFileUtilities();
        assertEquals("", fileUtilities.getPropertyFileLocation());
    }

    private PropertyAccessorFileUtilities createPropertyAccessorFileUtilities() {
        URL url = this.getClass().getResource("/config");
        System.setProperty("nhinc.properties.dir", url.getFile());

        EXPECTED_PROPERTY_FILE_LOCATION = url.getFile() + File.separator;
        return new PropertyAccessorFileUtilities();
    }

}
