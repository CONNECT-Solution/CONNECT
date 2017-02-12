/*
 * Copyright (c) 2009-2016, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.openSAML.extraction;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import gov.hhs.fha.nhinc.callback.openSAML.OpenSAML2ComponentBuilder;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.junit.Test;
import org.opensaml.core.xml.XMLObject;
import org.opensaml.saml.saml2.core.Attribute;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author sjarral
 *
 */
public class AttributeHelperTest {

    /**
     * Test method for
     * {@link gov.hhs.fha.nhinc.openSAML.extraction.AttributeHelper#extractNameParts(org.opensaml.saml.saml2.core.Attribute, gov.hhs.fha.nhinc.common.nhinccommon.AssertionType)}.
     */
    @Test
    public final void testExtractNameParts() {

        // Test for exception when Attribute value passed in Attribute is Null
        AttributeHelper helper = new AttributeHelper();
        Attribute attrib = mock(Attribute.class);
        List<XMLObject> attrVals = new ArrayList<>();
        when(attrib.getAttributeValues()).thenReturn(attrVals);
        AssertionType assertOut = mock(AssertionType.class);
        helper.extractNameParts(attrib, assertOut);

    }

    /**
     * Test method for
     * {@link gov.hhs.fha.nhinc.openSAML.extraction.AttributeHelper#extractNhinCodedElement(org.opensaml.saml.saml2.core.Attribute, java.lang.String)}.
     */
    @Test
    public final void testExtractAttributeValueString() {

        // Test for exceptions if attribute is Null
        AttributeHelper helper = new AttributeHelper();
        Attribute attrib = mock(Attribute.class);
        helper.extractAttributeValueString(attrib);

    }

    @Test
    public final void testExtractNhinCodedElement() {

        // Test for exception when Attrib value passed in Attribute is Null
        AttributeHelper helper = new AttributeHelper();
        Attribute attrib = mock(Attribute.class);
        List<XMLObject> attrVals = new ArrayList<>();
        when(attrib.getAttributeValues()).thenReturn(attrVals);
        String CodeId = null;
        helper.extractNhinCodedElement(attrib, CodeId);

        // Test for exception when Attrib value is not null and child node of Attrib value is set
        final String purposeCode2 = "purposeCode";
        final String purposeSystem2 = "purposeSystem";
        final String purposeSystemName2 = "purposeSystemName";
        final String purposeDisplay2 = "purposeDisplay";
        final Attribute attribute = OpenSAML2ComponentBuilder.getInstance().createPurposeOfUseAttribute(purposeCode2,
            purposeSystem2, purposeSystemName2, purposeDisplay2);
        try {
            attribute.getAttributeValues().get(0).setDOM(getElementForSamlFile(getTestFilePath("complete_saml.xml")));
        } catch (Exception e) {
            e.printStackTrace();
        }

        helper.extractNhinCodedElement(attribute, CodeId);

    }

    /**
     * Gets Saml File.
     *
     * @param filename the name of the file to retrieve
     * @return
     */

    private File getSamlFile(String samlFileName) {
        URI uri = null;
        try {
            uri = this.getClass().getResource(samlFileName).toURI();
        } catch (URISyntaxException e) {
            fail("Could not build URI for filepath. " + e.getMessage());
        }
        return new File(uri);
    }

    /**
     * Gets the Element for Saml File.
     *
     * @param filename the name of the file to retrieve
     * @return
     */
    private Element getElementForSamlFile(String samlFileName) throws Exception {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(getSamlFile(samlFileName));
        return document.getDocumentElement();
    }

    /**
     * Gets the file path to the test resource.
     *
     * @param filename the name of the file to retrieve
     * @return
     */
    private String getTestFilePath(String filename) {
        // the first "/" is intentionally not using File.separator due to differences in how windows and unix based
        // operating systems handle the class.getResource method. Please see GATEWAY-2873 for more details.
        return "/" + "testing_saml" + File.separator + filename;
    }

}
