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
package gov.hhs.fha.nhinc.transform.subdisc;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.JAXBElement;
import org.hl7.v3.ADExplicit;
import org.hl7.v3.CD;
import org.hl7.v3.CE;
import org.hl7.v3.CS;
import org.hl7.v3.ENExplicit;
import org.hl7.v3.EnExplicitFamily;
import org.hl7.v3.EnExplicitGiven;
import org.hl7.v3.EnExplicitPrefix;
import org.hl7.v3.EnExplicitSuffix;
import org.hl7.v3.II;
import org.hl7.v3.PNExplicit;
import org.hl7.v3.TELExplicit;
import org.hl7.v3.TSExplicit;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Test;

/**
 * @author achidambaram
 *
 */
public class HL7DataTransformHelperTest {

    @Test
    public void testIIFactoryWithRoot() {
        String root = "1.1";
        HL7DataTransformHelper helper = new HL7DataTransformHelper();
        II ii = new II();
        ii = helper.IIFactory(root);
        assertEquals(ii.getRoot(), "1.1");
        assertNull(ii.getAssigningAuthorityName());
        assertNull(ii.getExtension());
    }

    @Test
    public void testIIFactoryWithRootAndExtension() {
        String root = "1.1";
        String extension = "1.16.17.19";
        HL7DataTransformHelper helper = new HL7DataTransformHelper();
        II ii = new II();
        ii = helper.IIFactory(root, extension);
        assertEquals(ii.getRoot(), "1.1");
        assertNull(ii.getAssigningAuthorityName());
        assertEquals(ii.getExtension(), "1.16.17.19");
    }

    @Test
    public void IIFactory() {
        String root = "1.1";
        String extension = "1.16.17.19";
        HL7DataTransformHelper helper = new HL7DataTransformHelper();
        II ii = new II();
        ii = helper.IIFactory(root, extension);
        assertEquals(ii.getRoot(), "1.1");
        assertEquals(ii.getExtension(), "1.16.17.19");
        assertNull(ii.getAssigningAuthorityName());
    }

    @Test
    public void testIIFactory() {
        String root = "1.1";
        String extension = "1.16.17.19";
        String assigningAuthorityName = "CONNECT";
        HL7DataTransformHelper helper = new HL7DataTransformHelper();
        II ii = new II();
        ii = helper.IIFactory(root, extension, assigningAuthorityName);
        assertEquals(ii.getAssigningAuthorityName(), assigningAuthorityName);
        assertEquals(ii.getExtension(), extension);
        assertEquals(ii.getRoot(), "1.1");
    }

    @Test
    public void testIIFactoryWhenRootNull() {
        String root = null;
        String extension = "1.16.17.19";
        String assigningAuthorityName = "CONNECT";
        HL7DataTransformHelper helper = new HL7DataTransformHelper();
        II ii = new II();
        ii = helper.IIFactory(root, extension, assigningAuthorityName);
        assertEquals(ii.getAssigningAuthorityName(), assigningAuthorityName);
        assertEquals(ii.getExtension(), extension);
        assertNull(ii.getRoot());
    }

    @Test
    public void testIIFactoryWhenAANull() {
        String root = "1.1";
        String extension = "1.16.17.19";
        String assigningAuthorityName = null;
        HL7DataTransformHelper helper = new HL7DataTransformHelper();
        II ii = new II();
        ii = helper.IIFactory(root, extension, assigningAuthorityName);
        assertNull(ii.getAssigningAuthorityName());
        assertEquals(ii.getExtension(), extension);
        assertEquals(ii.getRoot(), "1.1");
    }

    @Test
    public void testIIFactoryWhenExtensionNull() {
        String root = "1.1";
        String extension = null;
        String assigningAuthorityName = "CONNECT";
        HL7DataTransformHelper helper = new HL7DataTransformHelper();
        II ii = new II();
        ii = helper.IIFactory(root, extension, assigningAuthorityName);
        assertEquals(ii.getAssigningAuthorityName(), "CONNECT");
        assertNull(ii.getExtension());
        assertEquals(ii.getRoot(), "1.1");
    }

    @Test
    public void IIFactoryCreateNull() {
        HL7DataTransformHelper helper = new HL7DataTransformHelper();
        II ii = new II();
        ii = helper.IIFactoryCreateNull();
        assertEquals(ii.getNullFlavor().get(0), "NA");
    }

    @Test
    public void CSFactory() {
        String code = "CONNECT";
        CS cs = new CS();
        HL7DataTransformHelper helper = new HL7DataTransformHelper();
        cs = helper.CSFactory(code);
        assertEquals(cs.getCode(), "CONNECT");
    }

    @Test
    public void CSFactoryWhenCodeNull() {
        String code = null;
        CS cs = new CS();
        HL7DataTransformHelper helper = new HL7DataTransformHelper();
        cs = helper.CSFactory(code);
        assertNull(cs.getCode());
    }

    @Test
    public void CEFactory() {
        String code = "CONNECT";
        CE ce = new CE();
        HL7DataTransformHelper helper = new HL7DataTransformHelper();
        ce = helper.CEFactory(code);
        assertEquals(ce.getCode(), code);
    }

    @Test
    public void CEFactoryWhenCodeNull() {
        String code = null;
        CE ce = new CE();
        HL7DataTransformHelper helper = new HL7DataTransformHelper();
        ce = helper.CEFactory(code);
        assertNull(ce.getCode());
    }

    @Test
    public void CDFactory() {
        String code = "CONNECT";
        String codeSystem = "CONNECTDomain";
        String displayName = "CONNECT CD";
        CD cd = new CD();
        HL7DataTransformHelper helper = new HL7DataTransformHelper();
        cd = helper.CDFactory(code, codeSystem, displayName);
        assertEquals(cd.getCode(), "CONNECT");
        assertEquals(cd.getCodeSystem(), "CONNECTDomain");
        assertEquals(cd.getDisplayName(), "CONNECT CD");
    }

    @Test
    public void CDFactoryWhenCodeNull() {
        String code = null;
        String codeSystem = "CONNECTDomain";
        String displayName = "CONNECT CD";
        CD cd = new CD();
        HL7DataTransformHelper helper = new HL7DataTransformHelper();
        cd = helper.CDFactory(code, codeSystem, displayName);
        assertNull(cd.getCode());
        assertEquals(cd.getCodeSystem(), "CONNECTDomain");
        assertEquals(cd.getDisplayName(), "CONNECT CD");
    }

    @Test
    public void CDFactoryWithCode() {
        String code = "CONNECT";
        CD cd = new CD();
        HL7DataTransformHelper helper = new HL7DataTransformHelper();
        cd = helper.CDFactory(code);
        assertEquals(cd.getCode(), "CONNECT");
        assertNull(cd.getCodeSystem());
        assertNull(cd.getDisplayName());
    }

    @Test
    public void CDFactoryWIthCodeAndCodeSystem() {
        String code = "CONNECT";
        String codeSystem = "CONNECTDomain";
        CD cd = new CD();
        HL7DataTransformHelper helper = new HL7DataTransformHelper();
        cd = helper.CDFactory(code, codeSystem);
        assertEquals(cd.getCode(), "CONNECT");
        assertEquals(cd.getCodeSystem(), "CONNECTDomain");
        assertNull(cd.getDisplayName());
    }

    @Test
    public void TSExplicitFactory() {
        String value = "CONNECT";
        HL7DataTransformHelper helper = new HL7DataTransformHelper();
        TSExplicit ts = new TSExplicit();
        ts = helper.TSExplicitFactory(value);
        assertEquals(ts.getValue(), "CONNECT");
    }

    @Test
    public void creationTimeFactory() {
        TSExplicit ts = new TSExplicit();
        HL7DataTransformHelper helper = new HL7DataTransformHelper();
        ts = helper.creationTimeFactory();
        assertNotNull(ts.getValue());
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void createEnExplicit() {
        String firstName = "Gallow";
        String middleName = "Matt";
        String lastName = "Younger";
        String title = "Patient";
        String suffix = "S";
        EnExplicitFamily familyName = null;
        EnExplicitGiven givenName;
        EnExplicitPrefix prefix = null;
        EnExplicitSuffix suf = null;
        HL7DataTransformHelper helper = new HL7DataTransformHelper();
        ENExplicit enName = helper.createEnExplicit(firstName, middleName, lastName, title, suffix);
        List<Serializable> enList = enName.getContent();
        Iterator<Serializable> iter = enList.iterator();

        while (iter.hasNext()) {
            Serializable contentItem = iter.next();
            if (contentItem instanceof JAXBElement) {
                JAXBElement oJAXBElement = (JAXBElement) contentItem;
                if (oJAXBElement.getValue() instanceof EnExplicitFamily) {
                    familyName = (EnExplicitFamily) oJAXBElement.getValue();
                }
                if (oJAXBElement.getValue() instanceof EnExplicitGiven) {
                    givenName = (EnExplicitGiven) oJAXBElement.getValue();
                    System.out.println(givenName.getPartType());
                }
                if (oJAXBElement.getValue() instanceof EnExplicitGiven) {
                    givenName = (EnExplicitGiven) oJAXBElement.getValue();
                    System.out.println(givenName.getPartType());
                }
                if (oJAXBElement.getValue() instanceof EnExplicitPrefix) {
                    prefix = (EnExplicitPrefix) oJAXBElement.getValue();
                }
                if (oJAXBElement.getValue() instanceof EnExplicitSuffix) {
                    suf = (EnExplicitSuffix) oJAXBElement.getValue();
                }
            }
        }
        assertEquals(familyName.getContent(), lastName);
        assertEquals(prefix.getContent(), title);
        assertEquals(suf.getContent(), suffix);
    }

    @Test
    public void createEnExplicitWhenFamilyNameNull() {
        String firstName = "Gallow";
        String middleName = "Matt";
        String lastName = null;
        String title = "Patient";
        String suffix = "S";
        EnExplicitFamily familyName = null;
        EnExplicitGiven givenName = null;
        EnExplicitPrefix prefix = null;
        EnExplicitSuffix suf = null;
        HL7DataTransformHelper helper = new HL7DataTransformHelper();
        ENExplicit enName = helper.createEnExplicit(firstName, middleName, lastName, title, suffix);
        List<Serializable> choice = enName.getContent();
        Iterator<Serializable> iterSerialObjects = choice.iterator();

        while (iterSerialObjects.hasNext()) {
            Serializable contentItem = iterSerialObjects.next();
            if (contentItem instanceof JAXBElement) {
                JAXBElement oJAXBElement = (JAXBElement) contentItem;
                if (oJAXBElement.getValue() instanceof EnExplicitFamily) {
                    familyName = (EnExplicitFamily) oJAXBElement.getValue();
                }
                if (oJAXBElement.getValue() instanceof EnExplicitGiven) {
                    givenName = (EnExplicitGiven) oJAXBElement.getValue();
                }
                if (oJAXBElement.getValue() instanceof EnExplicitGiven) {
                    givenName = (EnExplicitGiven) oJAXBElement.getValue();
                }
                if (oJAXBElement.getValue() instanceof EnExplicitPrefix) {
                    prefix = (EnExplicitPrefix) oJAXBElement.getValue();
                }
                if (oJAXBElement.getValue() instanceof EnExplicitSuffix) {
                    suf = (EnExplicitSuffix) oJAXBElement.getValue();

                }
            }
        }
        assertNull(familyName);
        assertEquals(prefix.getContent(), title);
        assertEquals(suf.getContent(), suffix);
    }

    @Test
    public void createEnExplicitWhenPrefixNull() {
        String firstName = "Gallow";
        String middleName = "Matt";
        String lastName = "Younger";
        String title = null;
        String suffix = "S";
        EnExplicitFamily familyName = null;
        EnExplicitGiven givenName = null;
        EnExplicitPrefix prefix = null;
        EnExplicitSuffix suf = null;
        HL7DataTransformHelper helper = new HL7DataTransformHelper();
        ENExplicit enName = helper.createEnExplicit(firstName, middleName, lastName, title, suffix);
        List<Serializable> choice = enName.getContent();
        Iterator<Serializable> iterSerialObjects = choice.iterator();

        while (iterSerialObjects.hasNext()) {
            Serializable contentItem = iterSerialObjects.next();
            if (contentItem instanceof JAXBElement) {
                JAXBElement oJAXBElement = (JAXBElement) contentItem;
                if (oJAXBElement.getValue() instanceof EnExplicitFamily) {
                    familyName = (EnExplicitFamily) oJAXBElement.getValue();
                }
                if (oJAXBElement.getValue() instanceof EnExplicitGiven) {
                    givenName = (EnExplicitGiven) oJAXBElement.getValue();
                }
                if (oJAXBElement.getValue() instanceof EnExplicitGiven) {
                    givenName = (EnExplicitGiven) oJAXBElement.getValue();
                }
                if (oJAXBElement.getValue() instanceof EnExplicitPrefix) {
                    prefix = (EnExplicitPrefix) oJAXBElement.getValue();
                }
                if (oJAXBElement.getValue() instanceof EnExplicitSuffix) {
                    suf = (EnExplicitSuffix) oJAXBElement.getValue();

                }
            }
        }
        assertEquals(familyName.getContent(), lastName);
        assertNull(prefix);
        assertEquals(suf.getContent(), suffix);
    }

    @Test
    public void createEnExplicitWhenSuffixNull() {
        String firstName = "Gallow";
        String middleName = "Matt";
        String lastName = "Younger";
        String title = "Mr";
        String suffix = null;
        EnExplicitFamily familyName = null;
        EnExplicitGiven givenName = null;
        EnExplicitPrefix prefix = null;
        EnExplicitSuffix suf = null;
        HL7DataTransformHelper helper = new HL7DataTransformHelper();
        ENExplicit enName = helper.createEnExplicit(firstName, middleName, lastName, title, suffix);
        List<Serializable> choice = enName.getContent();
        Iterator<Serializable> iterSerialObjects = choice.iterator();

        while (iterSerialObjects.hasNext()) {
            Serializable contentItem = iterSerialObjects.next();
            if (contentItem instanceof JAXBElement) {
                JAXBElement oJAXBElement = (JAXBElement) contentItem;
                if (oJAXBElement.getValue() instanceof EnExplicitFamily) {
                    familyName = (EnExplicitFamily) oJAXBElement.getValue();
                }
                if (oJAXBElement.getValue() instanceof EnExplicitGiven) {
                    givenName = (EnExplicitGiven) oJAXBElement.getValue();
                }
                if (oJAXBElement.getValue() instanceof EnExplicitGiven) {
                    givenName = (EnExplicitGiven) oJAXBElement.getValue();
                }
                if (oJAXBElement.getValue() instanceof EnExplicitPrefix) {
                    prefix = (EnExplicitPrefix) oJAXBElement.getValue();
                }
                if (oJAXBElement.getValue() instanceof EnExplicitSuffix) {
                    suf = (EnExplicitSuffix) oJAXBElement.getValue();

                }
            }
        }
        assertEquals(familyName.getContent(), lastName);
        assertEquals(prefix.getContent(), title);
        assertNull(suf);
    }

    @Test
    public void createADExplicit() {
        String street = "12601 FairLakes Circle";
        String city = "Fairfax";
        String state = "VA";
        String zip = "22102";
        HL7DataTransformHelper helper = new HL7DataTransformHelper();
        ADExplicit result = helper.createADExplicit(street, city, state, zip);
        assertNotNull(result.getUse());
        assertEquals(result.getUse().size(), 4);
    }

    @Test
    public void createADExplicitWithStreet1() {
        String street = "12601 FairLakes Circle";
        String street1 = "100 suite";
        String city = "Fairfax";
        String state = "VA";
        String zip = "22102";
        HL7DataTransformHelper helper = new HL7DataTransformHelper();
        ADExplicit result = helper.createADExplicit(street, street1, city, state, zip);
        assertNotNull(result.getUse());
        assertEquals(result.getUse().size(), 5);
    }

    @Test
    public void createTELExplicit() {
        String value = "210-340-6780";
        TELExplicit te = new TELExplicit();
        HL7DataTransformHelper helper = new HL7DataTransformHelper();
        te = helper.createTELExplicit(value);
        assertEquals(te.getValue(), value);
    }

    @Test
    public void convertPNToEN() {
        HL7DataTransformHelper helper = new HL7DataTransformHelper();
        String firstName = "Gallow";
        String lastName = "Younger";
        String middleName = "Matt";
        String prefix = "Mr";
        String suffix = "s";
        EnExplicitFamily familyName = null;
        EnExplicitGiven givenName = null;
        EnExplicitPrefix prefixEx = null;
        EnExplicitSuffix sufEx = null;
        PNExplicit pnName = helper.createPNExplicit(firstName, middleName, lastName, prefix, suffix);
        ENExplicit enName = helper.convertPNToEN(pnName);
        List<Serializable> enList = enName.getContent();
        Iterator<Serializable> iter = enList.iterator();

        while (iter.hasNext()) {
            Serializable contentItem = iter.next();
            if (contentItem instanceof JAXBElement) {
                JAXBElement oJAXBElement = (JAXBElement) contentItem;
                if (oJAXBElement.getValue() instanceof EnExplicitFamily) {
                    familyName = (EnExplicitFamily) oJAXBElement.getValue();
                }
                if (oJAXBElement.getValue() instanceof EnExplicitGiven) {
                    givenName = (EnExplicitGiven) oJAXBElement.getValue();
                }
                if (oJAXBElement.getValue() instanceof EnExplicitGiven) {
                    givenName = (EnExplicitGiven) oJAXBElement.getValue();
                }
                if (oJAXBElement.getValue() instanceof EnExplicitPrefix) {
                    prefixEx = (EnExplicitPrefix) oJAXBElement.getValue();
                }
                if (oJAXBElement.getValue() instanceof EnExplicitSuffix) {
                    sufEx = (EnExplicitSuffix) oJAXBElement.getValue();

                }
            }
        }
        assertEquals(familyName.getContent(), lastName);
        assertEquals(prefixEx.getContent(), prefix);
        assertEquals(sufEx.getContent(), suffix);
    }

}
