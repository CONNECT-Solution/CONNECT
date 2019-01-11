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
package gov.hhs.fha.nhinc.opensaml.extraction;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.CeType;
import gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.opensaml.core.xml.XMLObject;
import org.opensaml.core.xml.schema.impl.XSStringImpl;
import org.opensaml.core.xml.schema.impl.XSAnyImpl;
import org.opensaml.saml.saml2.core.Attribute;

/**
 * @author sjarral
 *
 */
public class AttributeHelperTest {
    private AttributeHelper helper;

    @Before
    public void setup() {
        helper = new AttributeHelper();
    }

    /**
     * Test method for
     * {@link gov.hhs.fha.nhinc.openSAML.extraction.AttributeHelper#extractNameParts(org.opensaml.saml.saml2.core.Attribute, gov.hhs.fha.nhinc.common.nhinccommon.AssertionType)}.
     */
    @Test
    public final void testExtractNameParts() {

        Attribute attrib = mock(Attribute.class);
        List<XMLObject> attrVals = new ArrayList<>();
        XSStringImpl mockStringValue = mock(XSStringImpl.class);
        when(mockStringValue.getValue()).thenReturn("ONC Team");
        attrVals.add(mockStringValue);
        when(attrib.getAttributeValues()).thenReturn(attrVals);
        AssertionType assertOut = mock(AssertionType.class);
        PersonNameType mockPersonNameType = new PersonNameType();
        UserType userType = new UserType();
        userType.setPersonName(mockPersonNameType);
        when(assertOut.getUserInfo()).thenReturn(userType);
        helper.extractNameParts(attrib, assertOut);
        PersonNameType result = assertOut.getUserInfo().getPersonName();
        Assert.assertEquals("ONC Team", result.getFullName());

    }

    /**
     * Test method for
     * {@link gov.hhs.fha.nhinc.openSAML.extraction.AttributeHelper#extractNhinCodedElement(org.opensaml.saml.saml2.core.Attribute, java.lang.String)}.
     */
    @Test
    public final void testExtractAttributeValueString() {

        // Test for exceptions if attribute is Null
        Attribute attrib = mock(Attribute.class);
        List<XMLObject> attrVals = new ArrayList<>();
        XSStringImpl mockStringValue = mock(XSStringImpl.class);
        when(mockStringValue.getValue()).thenReturn("ONC Team");
        attrVals.add(mockStringValue);
        when(attrib.getAttributeValues()).thenReturn(attrVals);
        String result = helper.extractAttributeValueString(attrib);
        Assert.assertEquals("ONC Team", result);

    }
    
    @Test
    public final void testExtractAttributeValueAnyString() {
        // Test for exceptions if attribute is Null
        final String testValue = "ONC Team";
        Attribute attrib = mock(Attribute.class);
        List<XMLObject> attrVals = new ArrayList<>();
        XSAnyImpl mockAnyValue = mock(XSAnyImpl.class);
        when(mockAnyValue.getTextContent()).thenReturn(testValue);
        attrVals.add(mockAnyValue);
        when(attrib.getAttributeValues()).thenReturn(attrVals);
        String result = helper.extractAttributeValueString(attrib);
        Assert.assertEquals(testValue, result);
    }

    @Test
    public final void testExtractNhinCodedElement() {

        // Test for exception when Attrib value passed in Attribute is Null

        Attribute attrib = mock(Attribute.class);
        List<XMLObject> attrVals = new ArrayList<>();

        when(attrib.getAttributeValues()).thenReturn(attrVals);
        String CodeId = null;
        CeType ceTypeResult = helper.extractNhinCodedElement(attrib, CodeId);
        Assert.assertEquals("", ceTypeResult.getCode());


    }


}
