/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
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
package gov.hhs.fha.nhinc.subscription.repository.data.test;

import org.junit.Test;
import static org.junit.Assert.*;
import gov.hhs.fha.nhinc.subscription.repository.data.ReferenceParameter;
import org.junit.Ignore;

/**
 * Unit test for the ReferenceParameter class
 * 
 * @author Neil Webb
 */
@Ignore
public class ReferenceParameterTest {
    @Test
    public void testGettersAndSetters() {
        System.out.println("Begin testGettersAndSetters");
        try {
            String namespace = "namespace";
            String namespacePrefix = "prefix";
            String elementName = "elementName";
            String value = "elementValue";

            ReferenceParameter refParam = new ReferenceParameter();
            refParam.setNamespace(namespace);
            refParam.setNamespacePrefix(namespacePrefix);
            refParam.setElementName(elementName);
            refParam.setValue(value);

            assertEquals("Namespace", namespace, refParam.getNamespace());
            assertEquals("Namespace prefix", namespacePrefix, refParam.getNamespacePrefix());
            assertEquals("Element name", elementName, refParam.getElementName());
            assertEquals("Value", value, refParam.getValue());
        } catch (Throwable t) {
            t.printStackTrace();
            fail(t.getMessage());
        }
        System.out.println("End testGettersAndSetters");
    }

    @Test
    public void testEquals() {
        System.out.println("Begin testEquals");
        try {
            // Equals - All equal
            ReferenceParameter refParam1 = new ReferenceParameter();
            refParam1.setNamespace("namespace1");
            refParam1.setNamespacePrefix("namespacePrefix1");
            refParam1.setElementName("elementName1");
            refParam1.setValue("value1");
            ReferenceParameter refParam2 = new ReferenceParameter();
            refParam2.setNamespace("namespace1");
            refParam2.setNamespacePrefix("namespacePrefix1");
            refParam2.setElementName("elementName1");
            refParam2.setValue("value1");
            assertTrue("Equals - All equal", refParam1.equals(refParam2));

            // Equals - Only namespace
            refParam1 = new ReferenceParameter();
            refParam1.setNamespace("namespace1");
            refParam2 = new ReferenceParameter();
            refParam2.setNamespace("namespace1");
            assertTrue("Equals - Only namespace", refParam1.equals(refParam2));

            // Equals - Only namespace prefix
            refParam1 = new ReferenceParameter();
            refParam1.setNamespacePrefix("namespacePrefix1");
            refParam2 = new ReferenceParameter();
            refParam2.setNamespacePrefix("namespacePrefix1");
            assertTrue("Equals - Only namespace prefix", refParam1.equals(refParam2));

            // Equals - Only element name
            refParam1 = new ReferenceParameter();
            refParam1.setElementName("elementName1");
            refParam2 = new ReferenceParameter();
            refParam2.setElementName("elementName1");
            assertTrue("Equals - Only element name", refParam1.equals(refParam2));

            // Equals - Only value
            refParam1 = new ReferenceParameter();
            refParam1.setValue("value1");
            refParam2 = new ReferenceParameter();
            refParam2.setValue("value1");
            assertTrue("Equals - Only value", refParam1.equals(refParam2));

            // Equals - 1,2
            refParam1 = new ReferenceParameter();
            refParam1.setNamespace("namespace1");
            refParam1.setNamespacePrefix("namespacePrefix1");
            refParam2 = new ReferenceParameter();
            refParam2.setNamespace("namespace1");
            refParam2.setNamespacePrefix("namespacePrefix1");
            assertTrue("Equals - 1,2", refParam1.equals(refParam2));

            // Equals - 1,3
            refParam1 = new ReferenceParameter();
            refParam1.setNamespace("namespace1");
            refParam1.setElementName("elementName1");
            refParam2 = new ReferenceParameter();
            refParam2.setNamespace("namespace1");
            refParam2.setElementName("elementName1");
            assertTrue("Equals - 1,3", refParam1.equals(refParam2));

            // Equals - 1,4
            refParam1 = new ReferenceParameter();
            refParam1.setNamespace("namespace1");
            refParam1.setValue("value1");
            refParam2 = new ReferenceParameter();
            refParam2.setNamespace("namespace1");
            refParam2.setValue("value1");
            assertTrue("Equals - 1,4", refParam1.equals(refParam2));

            // Equals - 2,3
            refParam1 = new ReferenceParameter();
            refParam1.setNamespacePrefix("namespacePrefix1");
            refParam1.setElementName("elementName1");
            refParam2 = new ReferenceParameter();
            refParam2.setNamespacePrefix("namespacePrefix1");
            refParam2.setElementName("elementName1");
            assertTrue("Equals - 2,3", refParam1.equals(refParam2));

            // Equals - 2,4
            refParam1 = new ReferenceParameter();
            refParam1.setNamespacePrefix("namespacePrefix1");
            refParam1.setValue("value1");
            refParam2 = new ReferenceParameter();
            refParam2.setNamespacePrefix("namespacePrefix1");
            refParam2.setValue("value1");
            assertTrue("", refParam1.equals(refParam2));

            // Equals - 3,4
            refParam1 = new ReferenceParameter();
            refParam1.setElementName("elementName1");
            refParam1.setValue("value1");
            refParam2 = new ReferenceParameter();
            refParam2.setElementName("elementName1");
            refParam2.setValue("value1");
            assertTrue("Equals - 3,4", refParam1.equals(refParam2));

            // Equals - 1,2,3
            refParam1 = new ReferenceParameter();
            refParam1.setNamespace("namespace1");
            refParam1.setNamespacePrefix("namespacePrefix1");
            refParam1.setElementName("elementName1");
            refParam2 = new ReferenceParameter();
            refParam2.setNamespace("namespace1");
            refParam2.setNamespacePrefix("namespacePrefix1");
            refParam2.setElementName("elementName1");
            assertTrue("Equals - 1,2,3", refParam1.equals(refParam2));

            // Equals - 1,2,4
            refParam1 = new ReferenceParameter();
            refParam1.setNamespace("namespace1");
            refParam1.setNamespacePrefix("namespacePrefix1");
            refParam1.setValue("value1");
            refParam2 = new ReferenceParameter();
            refParam2.setNamespace("namespace1");
            refParam2.setNamespacePrefix("namespacePrefix1");
            refParam2.setValue("value1");
            assertTrue("Equals - 1,2,4", refParam1.equals(refParam2));

            // Equals - 2,3,4
            refParam1 = new ReferenceParameter();
            refParam1.setNamespacePrefix("namespacePrefix1");
            refParam1.setElementName("elementName1");
            refParam1.setValue("value1");
            refParam2 = new ReferenceParameter();
            refParam2.setNamespacePrefix("namespacePrefix1");
            refParam2.setElementName("elementName1");
            refParam2.setValue("value1");
            assertTrue("Equals - 2,3,4", refParam1.equals(refParam2));

            // Equals - 1,3,4
            refParam1 = new ReferenceParameter();
            refParam1.setNamespace("namespace1");
            refParam1.setElementName("elementName1");
            refParam1.setValue("value1");
            refParam2 = new ReferenceParameter();
            refParam2.setNamespace("namespace1");
            refParam2.setElementName("elementName1");
            refParam2.setValue("value1");
            assertTrue("Equals - 1,3,4", refParam1.equals(refParam2));

            // Equals - All null
            refParam1 = new ReferenceParameter();
            refParam2 = new ReferenceParameter();
            assertTrue("Equals - All null", refParam1.equals(refParam2));

            // Not equal - All populated, namespace different
            refParam1 = new ReferenceParameter();
            refParam1.setNamespace("namespace1");
            refParam1.setNamespacePrefix("namespacePrefix1");
            refParam1.setElementName("elementName1");
            refParam1.setValue("value1");
            refParam2 = new ReferenceParameter();
            refParam2.setNamespace("namespace2");
            refParam2.setNamespacePrefix("namespacePrefix1");
            refParam2.setElementName("elementName1");
            refParam2.setValue("value1");
            assertFalse("Not equal - All populated, namespace different", refParam1.equals(refParam2));

            // Not equal - All populated, namespace prefix different
            // refParam1 = new ReferenceParameter();
            // refParam1.setNamespace("namespace1");
            // refParam1.setNamespacePrefix("namespacePrefix1");
            // refParam1.setElementName("elementName1");
            // refParam1.setValue("value1");
            // refParam2 = new ReferenceParameter();
            // refParam2.setNamespace("namespace1");
            // refParam2.setNamespacePrefix("namespacePrefix2");
            // refParam2.setElementName("elementName1");
            // refParam2.setValue("value1");
            // assertFalse("Not equal - All populated, namespace prefix different", refParam1.equals(refParam2));

            // Not equal - All populated, element name different
            refParam1 = new ReferenceParameter();
            refParam1.setNamespace("namespace1");
            refParam1.setNamespacePrefix("namespacePrefix1");
            refParam1.setElementName("elementName1");
            refParam1.setValue("value1");
            refParam2 = new ReferenceParameter();
            refParam2.setNamespace("namespace1");
            refParam2.setNamespacePrefix("namespacePrefix1");
            refParam2.setElementName("elementName2");
            refParam2.setValue("value1");
            assertFalse("Not equal - All populated, element name different", refParam1.equals(refParam2));

            // Not equal - All populated, value different
            refParam1 = new ReferenceParameter();
            refParam1.setNamespace("namespace1");
            refParam1.setNamespacePrefix("namespacePrefix1");
            refParam1.setElementName("elementName1");
            refParam1.setValue("value1");
            refParam2 = new ReferenceParameter();
            refParam2.setNamespace("namespace1");
            refParam2.setNamespacePrefix("namespacePrefix1");
            refParam2.setElementName("elementName1");
            refParam2.setValue("value2");
            assertFalse("Not equal - All populated, value different", refParam1.equals(refParam2));

            // Not equal - 1 full, 2 missing namespace
            refParam1 = new ReferenceParameter();
            refParam1.setNamespace("namespace1");
            refParam1.setNamespacePrefix("namespacePrefix1");
            refParam1.setElementName("elementName1");
            refParam1.setValue("value1");
            refParam2 = new ReferenceParameter();
            refParam2.setNamespacePrefix("namespacePrefix1");
            refParam2.setElementName("elementName1");
            refParam2.setValue("value1");
            assertFalse("Not equal - 1 full, 2 missing namespace", refParam1.equals(refParam2));

            // Not equal - 1 full, 2 missing namespace prefix
            // refParam1 = new ReferenceParameter();
            // refParam1.setNamespace("namespace1");
            // refParam1.setNamespacePrefix("namespacePrefix1");
            // refParam1.setElementName("elementName1");
            // refParam1.setValue("value1");
            // refParam2 = new ReferenceParameter();
            // refParam2.setNamespace("namespace1");
            // refParam2.setElementName("elementName1");
            // refParam2.setValue("value1");
            // assertFalse("Not equal - 1 full, 2 missing namespace prefix", refParam1.equals(refParam2));

            // Not equal - 1 full, 2 missing element name
            refParam1 = new ReferenceParameter();
            refParam1.setNamespace("namespace1");
            refParam1.setNamespacePrefix("namespacePrefix1");
            refParam1.setElementName("elementName1");
            refParam1.setValue("value1");
            refParam2 = new ReferenceParameter();
            refParam2.setNamespace("namespace1");
            refParam2.setNamespacePrefix("namespacePrefix1");
            refParam2.setValue("value1");
            assertFalse("Not equal - 1 full, 2 missing element name", refParam1.equals(refParam2));

            // Not equal - 1 full, 2 missing value
            refParam1 = new ReferenceParameter();
            refParam1.setNamespace("namespace1");
            refParam1.setNamespacePrefix("namespacePrefix1");
            refParam1.setElementName("elementName1");
            refParam1.setValue("value1");
            refParam2 = new ReferenceParameter();
            refParam2.setNamespace("namespace1");
            refParam2.setNamespacePrefix("namespacePrefix1");
            refParam2.setElementName("elementName1");
            assertFalse("Not equal - 1 full, 2 missing value", refParam1.equals(refParam2));

            // Not equal - 2 full, 1 missing namespace
            refParam1 = new ReferenceParameter();
            refParam1.setNamespacePrefix("namespacePrefix1");
            refParam1.setElementName("elementName1");
            refParam1.setValue("value1");
            refParam2 = new ReferenceParameter();
            refParam2.setNamespace("namespace1");
            refParam2.setNamespacePrefix("namespacePrefix1");
            refParam2.setElementName("elementName1");
            refParam2.setValue("value1");
            assertFalse("Not equal - 2 full, 1 missing namespace", refParam1.equals(refParam2));

            // Not equal - 2 full, 1 missing namespace prefix
            // refParam1 = new ReferenceParameter();
            // refParam1.setNamespace("namespace1");
            // refParam1.setElementName("elementName1");
            // refParam1.setValue("value1");
            // refParam2 = new ReferenceParameter();
            // refParam2.setNamespace("namespace1");
            // refParam2.setNamespacePrefix("namespacePrefix1");
            // refParam2.setElementName("elementName1");
            // refParam2.setValue("value1");
            // assertFalse("Not equal - 2 full, 1 missing namespace prefix", refParam1.equals(refParam2));

            // Not equal - 2 full, 1 missing element name
            refParam1 = new ReferenceParameter();
            refParam1.setNamespace("namespace1");
            refParam1.setNamespacePrefix("namespacePrefix1");
            refParam1.setValue("value1");
            refParam2 = new ReferenceParameter();
            refParam2.setNamespace("namespace1");
            refParam2.setNamespacePrefix("namespacePrefix1");
            refParam2.setElementName("elementName1");
            refParam2.setValue("value1");
            assertFalse("Not equal - 2 full, 1 missing element name", refParam1.equals(refParam2));

            // Not equal - 2 full, 1 missing value
            refParam1 = new ReferenceParameter();
            refParam1.setNamespace("namespace1");
            refParam1.setNamespacePrefix("namespacePrefix1");
            refParam1.setElementName("elementName1");
            refParam2 = new ReferenceParameter();
            refParam2.setNamespace("namespace1");
            refParam2.setNamespacePrefix("namespacePrefix1");
            refParam2.setElementName("elementName1");
            refParam2.setValue("value1");
            assertFalse("Not equal - 2 full, 1 missing value", refParam1.equals(refParam2));

        } catch (Throwable t) {
            t.printStackTrace();
            fail(t.getMessage());
        }
        System.out.println("End testEquals");
    }
}