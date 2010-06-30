/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.transform.subdisc;

import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import java.io.Serializable;
import java.util.TimeZone;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.JAXBElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.*;

/**
 *
 * @author Jon Hoppesch
 */
public class HL7DataTransformHelper {

    private static Log log = LogFactory.getLog(HL7DataTransformHelper.class);

    public static II IIFactory(String root) {
        return IIFactory(root, null, null);
    }

    public static II IIFactory(String root, String extension) {
        return IIFactory(root, extension, null);
    }

    public static II IIFactory(String root, String extension, String assigningAuthorityName) {
        II ii = new II();
        if (NullChecker.isNotNullish(root)) {
            log.debug("Setting root attribute of II to " + root);
            ii.setRoot(root);
        }
        if (NullChecker.isNotNullish(extension)) {
            log.debug("Setting extension attribute of II to " + extension);
            ii.setExtension(extension);
        }
        if (NullChecker.isNotNullish(assigningAuthorityName)) {
            log.debug("Setting assigning authority attribute of II to " + assigningAuthorityName);
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
            log.debug("Setting the code attribute of CS " + code);
            cs.setCode(code);
        }

        return cs;
    }

    public static CE CEFactory(String code) {
        CE ce = new CE();

        if (NullChecker.isNotNullish(code)) {
            log.debug("Setting the code attribute of CE " + code);
            ce.setCode(code);
        }

        return ce;
    }

    public static CD CDFactory(String code) {
        return CDFactory(code, null);
    }

    public static CD CDFactory(String code, String codeSystem) {
        CD cd = new CD();

        if (NullChecker.isNotNullish(code)) {
            log.debug("Setting the code attribute of CD " + code);
            cd.setCode(code);
        }

        if (NullChecker.isNotNullish(codeSystem)) {
            log.debug("Setting the code system attribute of CD: " + codeSystem);
            cd.setCodeSystem(codeSystem);
        }

        return cd;
    }

    public static TSExplicit TSExplicitFactory(String value) {
        TSExplicit ts = new TSExplicit();

        ts.setValue(value);

        return ts;
    }

    public static TSExplicit CreationTimeFactory() {
        String timestamp = "";
        TSExplicit creationTime = new TSExplicit();

        try {
            GregorianCalendar today = new GregorianCalendar(TimeZone.getTimeZone("GMT"));

            timestamp = String.valueOf(today.get(GregorianCalendar.YEAR)) +
                    String.valueOf(today.get(GregorianCalendar.MONTH) + 1) +
                    String.valueOf(today.get(GregorianCalendar.DAY_OF_MONTH)) +
                    String.valueOf(today.get(GregorianCalendar.HOUR_OF_DAY)) +
                    String.valueOf(today.get(GregorianCalendar.MINUTE)) +
                    String.valueOf(today.get(GregorianCalendar.SECOND));
        } catch (Exception e) {
            log.error("Exception when creating XMLGregorian Date");
            log.error(" message: " + e.getMessage());
        }

        if (NullChecker.isNotNullish(timestamp)) {
            log.debug("Setting the creation timestamp to " + timestamp);
            creationTime.setValue(timestamp);
        }

        return creationTime;
    }

    public static ENExplicit createEnExplicit(String firstName,
            String middleName, String lastName, String title, String suffix)
    {
        org.hl7.v3.ObjectFactory factory = new org.hl7.v3.ObjectFactory();
        ENExplicit enName = (ENExplicit) (factory.createENExplicit());
        List enNamelist = enName.getContent();
                             

        if(NullChecker.isNotNullish(lastName))
        {
            EnExplicitFamily familyName = createEnExplicitFamily(lastName);
            enNamelist.add(factory.createENExplicitFamily(familyName));
        }
        if(NullChecker.isNotNullish(firstName))
        {
            EnExplicitGiven givenName = createEnExplicitGiven(firstName);
            enNamelist.add(factory.createENExplicitGiven(givenName));
        }
        if(NullChecker.isNotNullish(middleName))
        {
            EnExplicitGiven givenName2 = createEnExplicitGiven(middleName);
            enNamelist.add(factory.createENExplicitGiven(givenName2));
        }
        if(NullChecker.isNotNullish(title))
        {
            EnExplicitPrefix prefix = createEnExplicitPrefix(title);
            enNamelist.add(factory.createENExplicitPrefix(prefix));
        }
        if(NullChecker.isNotNullish(suffix))
        {
            EnExplicitSuffix enSuffix = createEnExplicitSuffix(suffix);
            enNamelist.add(factory.createENExplicitSuffix(enSuffix));
        }

        return enName;
    }

    public static ENExplicit ConvertPNToEN(PNExplicit pnName) {
        org.hl7.v3.ObjectFactory factory = new org.hl7.v3.ObjectFactory();
        ENExplicit enName = (ENExplicit) (factory.createENExplicit());
        List enNamelist = enName.getContent();
        EnExplicitFamily familyName = new EnExplicitFamily();
        EnExplicitGiven givenName = new EnExplicitGiven();

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
        PNExplicit result = (PNExplicit) (factory.createPNExplicit());
        List namelist = result.getContent();
        String lastName = "";
        String firstName = "";

        EnExplicitFamily explicitFamilyName = null;
        EnExplicitGiven explicitGivenName = null;


        for (Object item : value.getContent()) {
            if (item instanceof EnFamily) {
                EnFamily familyName = (EnFamily) item;
                lastName = familyName.getRepresentation().value();

                explicitFamilyName = createEnExplicitFamily(lastName);
                namelist.add(factory.createPNExplicitFamily(explicitFamilyName));
                log.debug("Added family name" + lastName);
            } else if (item instanceof EnGiven) {
                EnGiven givenName = (EnGiven) item;
                firstName = givenName.getRepresentation().value();

                explicitGivenName = createEnExplicitGiven(firstName);
                namelist.add(factory.createPNExplicitGiven(explicitGivenName));
                log.debug("Added given name" + firstName);
            }else if (item instanceof EnPrefix) {
                EnPrefix enPrefix = (EnPrefix)item;
                String prefix = enPrefix.getRepresentation().value();

                EnExplicitPrefix explicitPrefix = createEnExplicitPrefix(prefix);
                
                namelist.add(factory.createPNExplicitPrefix(explicitPrefix));
                log.debug("Added prefix" + prefix);
            }else if (item instanceof JAXBElement) {
                JAXBElement newItem = (JAXBElement) item;
                if (newItem.getValue() instanceof EnExplicitFamily) {
                    EnExplicitFamily familyName = (EnExplicitFamily) newItem.getValue();
                    lastName = familyName.getContent();

                    explicitFamilyName = new EnExplicitFamily();
                    explicitFamilyName.setPartType("FAM");
                    explicitFamilyName.setContent(lastName);
                    namelist.add(factory.createPNExplicitFamily(explicitFamilyName));
                    log.debug("Added family name" + lastName);
                } else if (newItem.getValue() instanceof EnExplicitGiven) {
                    EnExplicitGiven givenName = (EnExplicitGiven) newItem.getValue();
                    firstName = givenName.getContent();

                    explicitGivenName = new EnExplicitGiven();
                    explicitGivenName.setPartType("GIV");
                    explicitGivenName.setContent(firstName);
                    namelist.add(factory.createPNExplicitGiven(explicitGivenName));
                    log.debug("Added given name" + firstName);
                }
            }

        }

        return result;
    }

    public static PNExplicit CreatePNExplicit(String firstName, String lastName) {
        log.debug("begin CreatePNExplicit");
        log.debug("firstName = " + firstName + "; lastName = " + lastName);
        org.hl7.v3.ObjectFactory factory = new org.hl7.v3.ObjectFactory();
        PNExplicit name = (PNExplicit) (factory.createPNExplicit());
        List namelist = name.getContent();

        if (NullChecker.isNotNullish(lastName)) {
            EnExplicitFamily familyName = new EnExplicitFamily();
            familyName.setPartType("FAM");
            familyName.setContent(lastName);
            log.info("Setting Patient Lastname: " + lastName);
            namelist.add(factory.createPNExplicitFamily(familyName));
        }

        if (NullChecker.isNotNullish(firstName)) {
            EnExplicitGiven givenName = new EnExplicitGiven();
            givenName.setPartType("GIV");
            givenName.setContent(firstName);
            log.info("Setting Patient Firstname: " + firstName);
            namelist.add(factory.createPNExplicitGiven(givenName));
        }

        log.debug("end CreatePNExplicit");
        return name;
    }

    public static PNExplicit CreatePNExplicit(String firstName, String middleName, String lastName) {
        log.debug("begin CreatePNExplicit");
        log.debug("firstName = " + firstName + "; lastName = " + lastName);
        org.hl7.v3.ObjectFactory factory = new org.hl7.v3.ObjectFactory();
        PNExplicit name = CreatePNExplicit(firstName, lastName);
        List namelist = name.getContent();

        if (NullChecker.isNotNullish(middleName)) {
            EnExplicitGiven givenName = new EnExplicitGiven();
            givenName.setPartType("GIV");
            givenName.setContent(middleName);
            log.info("Setting Patient Firstname: " + middleName);
            namelist.add(factory.createPNExplicitGiven(givenName));
        }

        log.debug("end CreatePNExplicit");
        return name;
    }
    public static PNExplicit CreatePNExplicit(String firstName, String middleName, String lastName, String title, String suffix)
    {
        PNExplicit result = CreatePNExplicit(firstName, middleName, lastName);
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
    public static ADExplicit CreateADExplicit(boolean notOrdered, String street, String street1, String city, String state, String zip) {
        ADExplicit result = new ADExplicit();

        result.setIsNotOrdered(notOrdered);

        result.getUse().add(street);
        result.getUse().add(street1);
        result.getUse().add(city);
        result.getUse().add(state);
        result.getUse().add(zip);
        return result;
    }
    public static ADExplicit CreateADExplicit(boolean notOrdered, String street, String city, String state, String zip) {
        ADExplicit result = new ADExplicit();

        result.setIsNotOrdered(notOrdered);

        result.getUse().add(street);
        result.getUse().add(city);
        result.getUse().add(state);
        result.getUse().add(zip);
        return result;
    }

    public static ADExplicit CreateADExplicit(String street, String city, String state, String zip) {
        return CreateADExplicit(false, street, city, state, zip);
    }
    public static ADExplicit CreateADExplicit(String street, String street1, String city, String state, String zip) {
        return CreateADExplicit(false, street, street1, city, state, zip);
    }
    public static TELExplicit createTELExplicit(String value) {
        TELExplicit result = new TELExplicit();

        result.setValue(value);

        return result;
    }
    private static EnExplicitFamily createEnExplicitFamily(String lastName)
    {
        EnExplicitFamily familyName = new EnExplicitFamily();
        familyName.setPartType("FAM");
        familyName.setContent(lastName);

        return familyName;
    }
    private static EnExplicitGiven createEnExplicitGiven(String givenName)
    {
        EnExplicitGiven result = new EnExplicitGiven();

        result.setPartType("GIV");
        result.setContent(givenName);

        return result;
    }
    private static EnExplicitPrefix createEnExplicitPrefix(String prefix)
    {
        EnExplicitPrefix explicitPrefix = new EnExplicitPrefix();
        explicitPrefix.setPartType("PFX");
        explicitPrefix.setContent(prefix);

        return explicitPrefix;
    }
    private static EnExplicitSuffix createEnExplicitSuffix(String suffix)
    {
        EnExplicitSuffix result = new EnExplicitSuffix();
        result.setPartType("SFX");
        result.setContent(suffix);

        return result;
    }

}
