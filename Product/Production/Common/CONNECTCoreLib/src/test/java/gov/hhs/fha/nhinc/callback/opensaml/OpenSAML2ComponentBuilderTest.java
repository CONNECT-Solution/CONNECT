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
package gov.hhs.fha.nhinc.callback.opensaml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import gov.hhs.fha.nhinc.callback.opensaml.OpenSAML2ComponentBuilder;

import java.util.List;
import java.util.UUID;
import javax.xml.namespace.QName;
import org.junit.Test;
import org.opensaml.core.xml.XMLObject;
import org.opensaml.core.xml.schema.XSAny;
import org.opensaml.core.xml.util.AttributeMap;
import org.opensaml.saml.saml2.core.Assertion;
import org.opensaml.saml.saml2.core.Attribute;

/**
 * @author achidamb
 *
 */
public class OpenSAML2ComponentBuilderTest {

    @Test
    public void generateEvAssertionWithoutUnderScore() {
        final String uuid = "2345";
        final Assertion assertion = OpenSAML2ComponentBuilder.getInstance().createAssertion(uuid);
        assertEquals(uuid, assertion.getID());
    }

    @Test
    public void generateEvAssertionWithUnderScore() {
        final String uuid = "_".concat(String.valueOf(UUID.randomUUID()));
        final Assertion assertion = OpenSAML2ComponentBuilder.getInstance().createAssertion(uuid);
        assertEquals(uuid, assertion.getID());
    }

    /**
     * Test namespace uri for purpose use.
     */
    @Test
    public void testNamespaceUriForPurposeUse() {
        boolean foundType = false;
        final String purposeCode = "purposeCode";
        final String purposeSystem = "purposeSystem";
        final String purposeSystemName = "purposeSystemName";
        final String purposeDisplay = "purposeDisplay";
        final String type = "hl7:CE";

        final Attribute attribute = OpenSAML2ComponentBuilder.getInstance().createPurposeOfUseAttribute(purposeCode,
                purposeSystem, purposeSystemName, purposeDisplay);
        final List<XMLObject> attributeValue = attribute.getAttributeValues();
        for (final XMLObject value : attributeValue) {
            for (final XMLObject valueElement : value.getOrderedChildren()) {
                if (valueElement instanceof XSAny) {
                    final XSAny role = (XSAny) valueElement;
                    final AttributeMap map = role.getUnknownAttributes();
                    assertNotNull(map.get(new QName("http://www.w3.org/2001/XMLSchema-instance", "type", "xsi")));
                    assertEquals(type, map.get(new QName("http://www.w3.org/2001/XMLSchema-instance", "type", "xsi")));
                    foundType = true;
                }
            }
        }
        assertFalse(!foundType);
    }

    /**
     * Test namespace uri for role.
     */
    @Test
    public void testNamespaceUriForRole() {
        boolean foundType = false;
        final String userCode = "12345";
        final String userSystem = "1.2.34.56";
        final String userSystemName = "CANCER-Research";
        final String userDisplay = "Public Health";
        final String type = "hl7:CE";

        final Attribute attribute = OpenSAML2ComponentBuilder.getInstance().createSubjectRoleAttribute(userCode, userSystem,
                userSystemName, userDisplay);
        final List<XMLObject> attributeValue = attribute.getAttributeValues();
        for (final XMLObject value : attributeValue) {
            for (final XMLObject valueElement : value.getOrderedChildren()) {
                if (valueElement instanceof XSAny) {
                    final XSAny role = (XSAny) valueElement;
                    final AttributeMap map = role.getUnknownAttributes();
                    assertNotNull(map.get(new QName("http://www.w3.org/2001/XMLSchema-instance", "type", "xsi")));
                    assertEquals(type, map.get(new QName("http://www.w3.org/2001/XMLSchema-instance", "type", "xsi")));
                    foundType = true;
                }
            }
        }
        assertFalse(!foundType);
    }

}
