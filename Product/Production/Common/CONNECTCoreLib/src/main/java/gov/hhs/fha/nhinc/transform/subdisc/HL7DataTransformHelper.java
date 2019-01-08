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

import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import java.io.Serializable;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
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
import org.hl7.v3.EnFamily;
import org.hl7.v3.EnGiven;
import org.hl7.v3.EnPrefix;
import org.hl7.v3.II;
import org.hl7.v3.PNExplicit;
import org.hl7.v3.TELExplicit;
import org.hl7.v3.TSExplicit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jon Hoppesch
 */
public class HL7DataTransformHelper {

    private static final Logger LOG = LoggerFactory.getLogger(HL7DataTransformHelper.class);

    public static II IIFactory(String root) {
        return IIFactory(root, null, null);
    }

    public static II IIFactory(String root, String extension) {
        return IIFactory(root, extension, null);
    }

    public static II IIFactory(String root, String extension, String assigningAuthorityName) {
        II ii = new II();
        if (NullChecker.isNotNullish(root)) {
            LOG.debug("Setting root attribute of II --> root value is not null");
            ii.setRoot(HomeCommunityMap.formatHomeCommunityId(root));
        }
        if (NullChecker.isNotNullish(extension)) {
            LOG.debug("Setting extension attribute of II --> Extension value is not null.");
            ii.setExtension(extension);
        }
        if (NullChecker.isNotNullish(assigningAuthorityName)) {
            LOG.debug("Setting assigning authority attribute of II to " + assigningAuthorityName);
            ii.setAssigningAuthorityName(assigningAuthorityName);
        }
        return ii;
    }

    public static II IIFactoryCreateNull() {
        II ii = new II();
        ii.getNullFlavor().add(HL7Constants.NULL_FLAVOR);
        return ii;
    }

    public static CS CSFactory(String code) {
        CS cs = new CS();

        if (NullChecker.isNotNullish(code)) {
            LOG.debug("Setting the code attribute of CS " + code);
            cs.setCode(code);
        }

        return cs;
    }

    public static CE CEFactory(String code) {
        CE ce = new CE();

        if (NullChecker.isNotNullish(code)) {
            LOG.debug("Setting the code attribute of CE --> code is not null");
            ce.setCode(code);
        }

        return ce;
    }

    public static CD CDFactory(String code) {
        return CDFactory(code, null);
    }

    public static CD CDFactory(String code, String codeSystem) {
        return CDFactory(code, codeSystem, null);
    }

    public static CD CDFactory(String code, String codeSystem, String displayName) {
        CD cd = new CD();

        if (NullChecker.isNotNullish(code)) {
            LOG.debug("Setting the code attribute of CD " + code);
            cd.setCode(code);
        }

        if (NullChecker.isNotNullish(codeSystem)) {
            LOG.debug("Setting the code system attribute of CD: " + codeSystem);
            cd.setCodeSystem(codeSystem);
        }

        if (NullChecker.isNotNullish(displayName)) {
            LOG.debug("Setting the display name attribute of CD: " + displayName);
            cd.setDisplayName(displayName);
        }

        return cd;
    }

    public static TSExplicit TSExplicitFactory(String value) {
        TSExplicit ts = new TSExplicit();

        ts.setValue(value);

        return ts;
    }

    public static TSExplicit creationTimeFactory() {
        String timestamp = "";
        TSExplicit creationTime = new TSExplicit();

        try {
            GregorianCalendar today = new GregorianCalendar(TimeZone.getTimeZone("GMT"));

            timestamp = String.valueOf(today.get(GregorianCalendar.YEAR))
                    + String.valueOf(today.get(GregorianCalendar.MONTH) + 1)
                    + String.valueOf(today.get(GregorianCalendar.DAY_OF_MONTH))
                    + String.valueOf(today.get(GregorianCalendar.HOUR_OF_DAY))
                    + String.valueOf(today.get(GregorianCalendar.MINUTE))
                    + String.valueOf(today.get(GregorianCalendar.SECOND));
        } catch (Exception e) {
            LOG.error("Exception when creating XMLGregorian Date: {}", e.getLocalizedMessage(), e);
        }

        if (NullChecker.isNotNullish(timestamp)) {
            LOG.debug("Setting the creation timestamp to " + timestamp);
            creationTime.setValue(timestamp);
        }

        return creationTime;
    }

    public static ENExplicit convertPNToEN(PNExplicit pnName) {
        org.hl7.v3.ObjectFactory factory = new org.hl7.v3.ObjectFactory();
        ENExplicit enName = factory.createENExplicit();
        List enNamelist = enName.getContent();
        EnExplicitFamily familyName;
        EnExplicitGiven givenName;

        List<Serializable> choice = pnName.getContent();
        Iterator<Serializable> iterSerialObjects = choice.iterator();

        while (iterSerialObjects.hasNext()) {
            Serializable contentItem = iterSerialObjects.next();

            if (contentItem instanceof JAXBElement) {
                JAXBElement oJAXBElement = (JAXBElement) contentItem;

                if (oJAXBElement.getValue() instanceof EnExplicitFamily) {
                    familyName = (EnExplicitFamily) oJAXBElement.getValue();
                    enNamelist.add(factory.createENExplicitFamily(familyName));
                } else if (oJAXBElement.getValue() instanceof EnExplicitGiven) {
                    givenName = (EnExplicitGiven) oJAXBElement.getValue();
                    enNamelist.add(factory.createENExplicitGiven(givenName));
                } else if (oJAXBElement.getValue() instanceof EnExplicitPrefix) {
                    EnExplicitPrefix explicitPrefix = (EnExplicitPrefix) oJAXBElement.getValue();
                    enNamelist.add(factory.createENExplicitPrefix(explicitPrefix));
                } else if (oJAXBElement.getValue() instanceof EnExplicitSuffix) {
                    EnExplicitSuffix explicitSuffix = (EnExplicitSuffix) oJAXBElement.getValue();
                    enNamelist.add(factory.createENExplicitSuffix(explicitSuffix));
                }
            }
        }

        return enName;
    }

    public static PNExplicit convertENtoPN(ENExplicit value) {
        org.hl7.v3.ObjectFactory factory = new org.hl7.v3.ObjectFactory();
        PNExplicit result = factory.createPNExplicit();
        List namelist = result.getContent();
        String lastName;
        String firstName;

        EnExplicitFamily explicitFamilyName;
        EnExplicitGiven explicitGivenName;

        for (Object item : value.getContent()) {
            if (item instanceof EnFamily) {
                EnFamily familyName = (EnFamily) item;
                lastName = familyName.getRepresentation().value();

                explicitFamilyName = createEnExplicitFamily(lastName);
                namelist.add(factory.createPNExplicitFamily(explicitFamilyName));
                LOG.debug("Added family name" + lastName);
            } else if (item instanceof EnGiven) {
                EnGiven givenName = (EnGiven) item;
                firstName = givenName.getRepresentation().value();

                explicitGivenName = createEnExplicitGiven(firstName);
                namelist.add(factory.createPNExplicitGiven(explicitGivenName));
                LOG.debug("Added given name" + firstName);
            } else if (item instanceof EnPrefix) {
                EnPrefix enPrefix = (EnPrefix) item;
                String prefix = enPrefix.getRepresentation().value();

                EnExplicitPrefix explicitPrefix = createEnExplicitPrefix(prefix);

                namelist.add(factory.createPNExplicitPrefix(explicitPrefix));
                LOG.debug("Added prefix" + prefix);
            } else if (item instanceof JAXBElement) {
                JAXBElement newItem = (JAXBElement) item;
                if (newItem.getValue() instanceof EnExplicitFamily) {
                    EnExplicitFamily familyName = (EnExplicitFamily) newItem.getValue();
                    lastName = familyName.getContent();

                    explicitFamilyName = new EnExplicitFamily();
                    explicitFamilyName.setPartType("FAM");
                    explicitFamilyName.setContent(lastName);
                    namelist.add(factory.createPNExplicitFamily(explicitFamilyName));
                    LOG.debug("Added family name" + lastName);
                } else if (newItem.getValue() instanceof EnExplicitGiven) {
                    EnExplicitGiven givenName = (EnExplicitGiven) newItem.getValue();
                    firstName = givenName.getContent();

                    explicitGivenName = new EnExplicitGiven();
                    explicitGivenName.setPartType("GIV");
                    explicitGivenName.setContent(firstName);
                    namelist.add(factory.createPNExplicitGiven(explicitGivenName));
                    LOG.debug("Added given name" + firstName);
                }
            }

        }

        return result;
    }

    public static ENExplicit createEnExplicit(String firstName, String middleName, String lastName, String title,
            String suffix) {
        org.hl7.v3.ObjectFactory factory = new org.hl7.v3.ObjectFactory();
        ENExplicit enName = factory.createENExplicit();
        List enNamelist = enName.getContent();

        if (NullChecker.isNotNullish(lastName)) {
            EnExplicitFamily familyName = createEnExplicitFamily(lastName);
            enNamelist.add(factory.createENExplicitFamily(familyName));
        }
        if (NullChecker.isNotNullish(firstName)) {
            EnExplicitGiven givenName = createEnExplicitGiven(firstName);
            enNamelist.add(factory.createENExplicitGiven(givenName));
        }
        if (NullChecker.isNotNullish(middleName)) {
            EnExplicitGiven givenName2 = createEnExplicitGiven(middleName);
            enNamelist.add(factory.createENExplicitGiven(givenName2));
        }
        if (NullChecker.isNotNullish(title)) {
            EnExplicitPrefix prefix = createEnExplicitPrefix(title);
            enNamelist.add(factory.createENExplicitPrefix(prefix));
        }
        if (NullChecker.isNotNullish(suffix)) {
            EnExplicitSuffix enSuffix = createEnExplicitSuffix(suffix);
            enNamelist.add(factory.createENExplicitSuffix(enSuffix));
        }

        return enName;
    }

    public static PNExplicit createPNExplicit(String firstName, String lastName) {
        LOG.debug("begin CreatePNExplicit");
        org.hl7.v3.ObjectFactory factory = new org.hl7.v3.ObjectFactory();
        PNExplicit name = factory.createPNExplicit();
        List namelist = name.getContent();

        if (NullChecker.isNotNullish(lastName)) {
            EnExplicitFamily familyName = new EnExplicitFamily();
            familyName.setPartType("FAM");
            familyName.setContent(lastName);
            LOG.info("Setting Patient Lastname --> lastName is not null");
            namelist.add(factory.createPNExplicitFamily(familyName));
        }

        if (NullChecker.isNotNullish(firstName)) {
            EnExplicitGiven givenName = new EnExplicitGiven();
            givenName.setPartType("GIV");
            givenName.setContent(firstName);
            LOG.info("Setting Patient Firstname --> firstName is not null");
            namelist.add(factory.createPNExplicitGiven(givenName));
        }

        LOG.debug("end CreatePNExplicit");
        return name;
    }

    public static PNExplicit createPNExplicit(String firstName, String middleName, String lastName) {
        LOG.debug("begin CreatePNExplicit");
        LOG.debug("firstName = " + firstName + "; lastName = " + lastName);
        org.hl7.v3.ObjectFactory factory = new org.hl7.v3.ObjectFactory();
        PNExplicit name = createPNExplicit(firstName, lastName);
        List namelist = name.getContent();

        if (NullChecker.isNotNullish(middleName)) {
            EnExplicitGiven givenName = new EnExplicitGiven();
            givenName.setPartType("GIV");
            givenName.setContent(middleName);
            LOG.info("Setting Patient Firstname: " + middleName);
            namelist.add(factory.createPNExplicitGiven(givenName));
        }

        LOG.debug("end CreatePNExplicit");
        return name;
    }

    public static PNExplicit createPNExplicit(String firstName, String middleName, String lastName, String title,
            String suffix) {
        PNExplicit result = createPNExplicit(firstName, middleName, lastName);
        List namelist = result.getContent();
        org.hl7.v3.ObjectFactory factory = new org.hl7.v3.ObjectFactory();

        if (NullChecker.isNotNullish(title)) {
            EnExplicitPrefix prefix = createEnExplicitPrefix(title);

            namelist.add(factory.createPNExplicitPrefix(prefix));
        }
        if (NullChecker.isNotNullish(suffix)) {
            EnExplicitSuffix enSuffix = createEnExplicitSuffix(suffix);

            namelist.add(factory.createPNExplicitSuffix(enSuffix));
        }

        return result;
    }

    public static ADExplicit createADExplicit(boolean notOrdered, String street, String city, String state,
            String zip) {
        ADExplicit result = new ADExplicit();

        result.setIsNotOrdered(notOrdered);

        result.getUse().add(street);
        result.getUse().add(city);
        result.getUse().add(state);
        result.getUse().add(zip);
        return result;
    }

    public static ADExplicit createADExplicit(String street, String city, String state, String zip) {
        return createADExplicit(false, street, city, state, zip);
    }

    public static ADExplicit createADExplicit(boolean notOrdered, String street, String street1, String city,
            String state, String zip) {
        ADExplicit result = new ADExplicit();

        result.setIsNotOrdered(notOrdered);

        result.getUse().add(street);
        result.getUse().add(street1);
        result.getUse().add(city);
        result.getUse().add(state);
        result.getUse().add(zip);
        return result;
    }

    public static ADExplicit createADExplicit(String street, String street1, String city, String state, String zip) {
        return createADExplicit(false, street, street1, city, state, zip);
    }

    public static TELExplicit createTELExplicit(String value) {
        TELExplicit result = new TELExplicit();

        result.setValue(value);

        return result;
    }

    private static EnExplicitFamily createEnExplicitFamily(String lastName) {
        EnExplicitFamily familyName = new EnExplicitFamily();
        familyName.setPartType("FAM");
        familyName.setContent(lastName);

        return familyName;
    }

    private static EnExplicitGiven createEnExplicitGiven(String givenName) {
        EnExplicitGiven result = new EnExplicitGiven();

        result.setPartType("GIV");
        result.setContent(givenName);

        return result;
    }

    private static EnExplicitPrefix createEnExplicitPrefix(String prefix) {
        EnExplicitPrefix explicitPrefix = new EnExplicitPrefix();
        explicitPrefix.setPartType("PFX");
        explicitPrefix.setContent(prefix);

        return explicitPrefix;
    }

    private static EnExplicitSuffix createEnExplicitSuffix(String suffix) {
        EnExplicitSuffix result = new EnExplicitSuffix();
        result.setPartType("SFX");
        result.setContent(suffix);

        return result;
    }
}
