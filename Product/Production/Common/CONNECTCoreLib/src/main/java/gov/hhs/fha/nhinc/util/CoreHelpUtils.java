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
package gov.hhs.fha.nhinc.util;

import static gov.hhs.fha.nhinc.nhinclib.NhincConstants.ADAPTER_PROPERTY_FILE_NAME;
import static gov.hhs.fha.nhinc.nhinclib.NhincConstants.AUDIT_LOGGING_PROPERTY_FILE;
import static gov.hhs.fha.nhinc.nhinclib.NhincConstants.CA_AUTHORITY_PROPERTY_FILE;
import static gov.hhs.fha.nhinc.nhinclib.NhincConstants.GATEWAY_PROPERTY_FILE;

import gov.hhs.fha.nhinc.common.loadtestdatamanagement.AddressType;
import gov.hhs.fha.nhinc.common.loadtestdatamanagement.DocumentMetadataType;
import gov.hhs.fha.nhinc.common.loadtestdatamanagement.IdentifierType;
import gov.hhs.fha.nhinc.common.loadtestdatamanagement.PatientType;
import gov.hhs.fha.nhinc.common.loadtestdatamanagement.PersonNameType;
import gov.hhs.fha.nhinc.docrepository.adapter.model.DocumentMetadata;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.patientdb.model.Address;
import gov.hhs.fha.nhinc.patientdb.model.Patient;
import gov.hhs.fha.nhinc.patientdb.model.Personname;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;


/**
 * @author ttang
 *
 */
public class CoreHelpUtils {
    private static final Logger LOG = LoggerFactory.getLogger(CoreHelpUtils.class);
    private static final String ALGORITHM = "RSA";
    private CoreHelpUtils() {
    }

    public static <T> void debugApplicationContext(Class<T> from, ApplicationContext context) {
        debugApplicationContext(from, context, false);
    }

    public static <T> void debugApplicationContext(Class<T> from, ApplicationContext context, boolean logBean) {
        if (null != context) {
            LOG.debug("debug--applicationContext {}: name:{}, id:{}, hash:{}, size:{}, parent: {}", from,
                context.getApplicationName(), context.getId(), context.hashCode(), context.getBeanDefinitionNames().length,
                context.getParent() != null ? context.getParent().hashCode() : "no-parent");
            if (logBean && LOG.isDebugEnabled()) {
                LOG.debug("debug--Beans-name: {}", Arrays.toString(context.getBeanDefinitionNames()));
            }
        } else {
            LOG.debug("debug--applicationContext is-null");
        }
    }

    public static <T> void logInfoServiceProcess(Class<T> from) {
        LOG.info("Flag service processing debug: {}", from);
    }

    public static XMLGregorianCalendar getXMLGregorianCalendarFrom(Date date) {
        if (date != null) {
            GregorianCalendar gregorianCalendar = new GregorianCalendar();
            gregorianCalendar.setTime(date);
            try {
                XMLGregorianCalendar cal = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
                LOG.trace("{}-{}-{} {}:{}:{} {}", cal.getMonth(), cal.getDay(), cal.getYear(), cal.getHour(),
                    cal.getMinute(), cal.getSecond(), cal.getTimezone());
                return cal;
            } catch (DatatypeConfigurationException ex) {
                LOG.error("Unable to convert date {} ", ex.getLocalizedMessage(), ex);
            }
        }
        return null;
    }

    public static <T> List<T> getUniqueList(List<T> fromList) {
        Set<T> uniqueList = new HashSet<>();
        uniqueList.addAll(fromList);
        List<T> retList = new ArrayList<>();
        retList.addAll(uniqueList);
        return retList;
    }

    public static Date getDate(XMLGregorianCalendar xmlDate) {
        return null != xmlDate ? xmlDate.toGregorianCalendar().getTime() : null;
    }

    public static List<String> returnSort(List<String> list) {
        Collections.sort(list);
        return list;
    }

    public static Date getDate(String dateString) {
        if (StringUtils.isNotBlank(dateString)) {
            try {
                return new SimpleDateFormat(NhincConstants.DATE_PARSE_FORMAT).parse(dateString);
            } catch (ParseException ex) {
                LOG.error("Error while parsing date.");
            }
        }
        return null;
    }

    public static void updateDocumentBy(DocumentMetadata doc, Patient patient) {
        doc.setPatientId(patient.getPatientIdentifierIso());
        doc.setSourcePatientId(patient.getPatientIdentifierIso());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        // setPIDs
        Personname personname = lastItem(patient.getPersonnames());
        if (personname != null) {
            doc.setPid5(MessageFormat.format("{0}^{1}^^^", personname.getLastName(), personname.getFirstName()));
        }
        Timestamp dateOfBirth = patient.getDateOfBirth();
        if (dateOfBirth != null) {
            doc.setPid7(sdf.format(dateOfBirth));
        }
        doc.setPid8(patient.getGender());

        Address address = lastItem(patient.getAddresses());
        if (address != null) {
            doc.setPid11(MessageFormat.format("{0}^^{1}^{2}^{3}^", address.getStreet1(), address.getCity(),
                address.getState(), address.getPostal()));
        }
        if (isId(patient.getPatientId())) {
            doc.setPatientRecordId(patient.getPatientId());
        }
    }

    public static void updateDocumentBy(DocumentMetadataType doc, PatientType patientType) {
        doc.setPatientId(getPatientIdentifierIso(patientType));
        doc.setSourcePatientId(getPatientIdentifierIso(patientType));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        // setPIDs
        PersonNameType personname = lastItem(patientType.getPersonNameList());
        if (personname != null) {
            doc.setPid5(MessageFormat.format("{0}^{1}^^^", personname.getLastName(), personname.getFirstName()));
        }
        Date dateOfBirth = getDate(patientType.getDateOfBirth());
        if (dateOfBirth != null) {
            doc.setPid7(sdf.format(dateOfBirth));
        }
        doc.setPid8(patientType.getGender());

        AddressType address = lastItem(patientType.getAddressList());
        if (address != null) {
            doc.setPid11(MessageFormat.format("{0}^^{1}^{2}^{3}^", address.getStreet1(), address.getCity(),
                address.getState(), address.getPostal()));
        }
        if (isId(patientType.getPatientId())) {
            doc.setPatientRecordId(patientType.getPatientId());
        }
    }

    public static <T> T lastItem(List<T> items) {
        T item = null;
        if (CollectionUtils.isNotEmpty(items)) {
            item = items.get(items.size() - 1);
        }
        return item;
    }

    public static <T> T firstItem(List<T> items) {
        T item = null;
        if (CollectionUtils.isNotEmpty(items)) {
            item = items.get(0);
        }
        return item;
    }

    public static boolean isId(Long id) {
        return id != null && id.longValue() > 0L;
    }

    public static String getPatientIdentifier(PatientType patient) {
        PersonNameType name = lastItem(patient.getPersonNameList());
        IdentifierType identifier = lastItem(patient.getIdentifierList());
        if (null != name && null != identifier) {
            return MessageFormat.format("{0} {1} - {2}", name.getFirstName(), name.getLastName(), identifier.getId());
        }
        return null;
    }

    public static String getPatientIdentifierIso(PatientType patient) {
        IdentifierType identifier = lastItem(patient.getIdentifierList());
        if (null != identifier) {
            return MessageFormat.format("{0}^^^&{1}&ISO", identifier.getId(), identifier.getOrganizationId());
        }
        return null;
    }

    public static <R, T> List<R> castListType(List<T> patientList) {
        List<R> list = new ArrayList<>();
        for (T patient : patientList) {
            list.add((R) patient);
        }
        return list;
    }

    public static <K, V> boolean isCollectionEmpty(Map<K, V> map) {
        return !isCollectionNotEmpty(map);
    }

    public static <K, V> boolean isCollectionNotEmpty(Map<K, V> map) {
        return null != map && !map.isEmpty();
    }

    public static String formatDate(String dateFormat, Date date) {
        if (StringUtils.isNotBlank(dateFormat) && null != date) {
            return new SimpleDateFormat(dateFormat).format(date);
        }
        return "";
    }

    public static KeyPair generateKeyPair(int keysize, SecureRandom sr) throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
        if (null != sr) {
            keyGen.initialize(keysize, sr);
        } else {
            keyGen.initialize(keysize);
        }
        return keyGen.generateKeyPair();
    }

    public static Certificate[] getCertificateChain(Certificate... certs) {
        return certs;
    }

    public static void saveJksTo(KeyStore keystore, String storePass, String storeLoc) throws UtilException {
        try (FileOutputStream os = new FileOutputStream(storeLoc)) {
            keystore.store(os, storePass.toCharArray());
        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
            throw new UtilException("Unable to save to the tempKeystore.", e);
        }
    }

    public static boolean checkPropertyList(String file) {
        return file.equalsIgnoreCase(GATEWAY_PROPERTY_FILE) || file.equalsIgnoreCase(ADAPTER_PROPERTY_FILE_NAME)
            || file.equalsIgnoreCase(AUDIT_LOGGING_PROPERTY_FILE) || file.equalsIgnoreCase(CA_AUTHORITY_PROPERTY_FILE);
    }

}
