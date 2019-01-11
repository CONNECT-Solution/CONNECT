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

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * @author akong
 *
 */
public class HibernateAccessorTest {

    private static final String PROPERTY_FILE_LOCATION = "/config/PropertyAccessorTest/";
    private static final String HIBERNATE_PROPERTY_FILE_NAME = "test.hibernate.cfg.xml";

    protected Mockery context = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    final PropertyAccessor mockPropertyAccessor = context.mock(PropertyAccessor.class);

    public void setMockPropertyAccessorExpectations() throws PropertyAccessException {
        context.checking(new Expectations() {
            {
                allowing(mockPropertyAccessor).getPropertyFileLocation();
                will(returnValue(getAbsolutePathFileLocation(PROPERTY_FILE_LOCATION)));
            }
        });
    }

    public void setMockPropertyAccessorExpectationsWithWrongFileLocation() throws PropertyAccessException {
        context.checking(new Expectations() {
            {
                allowing(mockPropertyAccessor).getPropertyFileLocation();
                will(returnValue(null));
            }
        });
    }


    @Test
    public void testGetAccessor() {
        HibernateAccessor hibernateAccessor = HibernateAccessor.getInstance();
        assertNotNull(hibernateAccessor);
    }

    @Test
    public void testGetHibernateFile() throws PropertyAccessException {
        HibernateAccessor hibernateAccessor = createHibernateAccessor();

        File file = hibernateAccessor.getHibernateFile(HIBERNATE_PROPERTY_FILE_NAME);
        assertTrue(file.exists());
    }

    @Test(expected=PropertyAccessException.class)
    public void testGetHibernateFile_DoesNotExist() throws PropertyAccessException{
        HibernateAccessor hibernateAccessor = createHibernateAccessor();
        hibernateAccessor.getHibernateFile("nonexistingfile");
    }

    @Test(expected=PropertyAccessException.class)
    public void testGetHibernateFile_WrongFileLocation() throws PropertyAccessException {
        HibernateAccessor hibernateAccessor = createBadHibernateAccessor();

        hibernateAccessor.getHibernateFile(HIBERNATE_PROPERTY_FILE_NAME);
    }

    private String getAbsolutePathFileLocation(String filename) {
        URL url = this.getClass().getResource(filename);

        try {
            return new File(url.toURI()).getAbsolutePath();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private HibernateAccessor createHibernateAccessor() throws PropertyAccessException {
        setMockPropertyAccessorExpectations();
        return new HibernateAccessor() {
            @Override
            protected PropertyAccessor getPropertyAccessor() {
                return mockPropertyAccessor;
            }
        };
    }

    private HibernateAccessor createBadHibernateAccessor() throws PropertyAccessException {
        setMockPropertyAccessorExpectationsWithWrongFileLocation();
        return new HibernateAccessor() {
            @Override
            protected PropertyAccessor getPropertyAccessor() {
                return mockPropertyAccessor;
            }
        };
    }
}
