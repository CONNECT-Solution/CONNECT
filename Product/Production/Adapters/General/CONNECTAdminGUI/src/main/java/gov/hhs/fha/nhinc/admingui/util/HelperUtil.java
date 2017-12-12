/*
 * Copyright (c) 2009-2017, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.admingui.util;

import com.google.gson.Gson;
import gov.hhs.fha.nhinc.docrepository.adapter.model.Document;
import gov.hhs.fha.nhinc.patientdb.model.Address;
import gov.hhs.fha.nhinc.patientdb.model.Patient;
import gov.hhs.fha.nhinc.patientdb.model.Personname;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Tran Tang
 *
 */
public class HelperUtil {
    private static final Logger LOG = LoggerFactory.getLogger(HelperUtil.class);

    /*
     * Utility class-private constructor
     */
    private HelperUtil() {
    }

    public static boolean isId(Long id) {
        return id != null && id.longValue() > 0L;
    }

    // CONVERT-METHODS
    public static Timestamp toTimestamp(Date date) {
        if (date != null) {
            return new Timestamp(date.getTime());
        } else {
            return null;
        }
    }

    public static Date toDate(Timestamp timestamp) {
        if (timestamp != null) {
            return new Date(timestamp.getTime());
        } else {
            return null;
        }
    }

    public static String toJsonString(Object object) {
        return new Gson().toJson(object);
    }

    public static <T> T lastItem(List<T> items) {
        T item = null;
        if (CollectionUtils.isNotEmpty(items)) {
            item = items.get(items.size() - 1);
        }
        return item;
    }

    public static void updateDocumentBy(Document doc, Patient patient) {
        doc.setPatientId(patient.getPatientIdentifierIso());
        doc.setSourcePatientId(patient.getPatientIdentifierIso());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        // setPIDs
        Personname personname = HelperUtil.lastItem(patient.getPersonnames());
        if (personname != null) {
            doc.setPid5(MessageFormat.format("{0}^{1}^^^", personname.getLastName(), personname.getFirstName()));
        }
        Timestamp dateOfBirth = patient.getDateOfBirth();
        if (dateOfBirth != null) {
            doc.setPid7(sdf.format(dateOfBirth));
        }
        doc.setPid8(patient.getGender());

        Address address = HelperUtil.lastItem(patient.getAddresses());
        if (address != null) {
            doc.setPid11(MessageFormat.format("{0}^^{1}^{2}^{3}^", address.getStreet1(), address.getCity(),
                address.getState(), address.getPostal()));
        }
        if (HelperUtil.isId(patient.getPatientId())) {
            doc.setPatientRecordId(patient.getPatientId());
        }
    }

    public static <T> T firstItem(List<T> items) {
        T item = null;
        if (CollectionUtils.isNotEmpty(items)) {
            item = items.get(0);
        }
        return item;
    }

    public static HttpSession getHttpSession(boolean sessionBit) {
        return (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(sessionBit);
    }

    public static Map<String, Object> getHttpSessionMap() {
        return FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
    }

    // facesMessages
    public static void addFacesMessageBy(Severity msgSeverity, String msgText) {
        addFacesMessageBy(null, new FacesMessage(msgSeverity, msgText, ""));
    }

    public static void addFacesMessageBy(String ofTarget, Severity msgSeverity, String msgText) {
        addFacesMessageBy(ofTarget, new FacesMessage(msgSeverity, msgText, ""));
    }

    public static void addFacesMessageBy(FacesMessage theMsg) {
        addFacesMessageBy(null, theMsg);
    }

    public static void addFacesMessageBy(String ofTarget, FacesMessage theMsg) {
        FacesContext.getCurrentInstance().addMessage(ofTarget, theMsg);
    }

    public static FacesMessage getMsgError(String msgText) {
        return new FacesMessage(FacesMessage.SEVERITY_ERROR, msgText, "");
    }

    public static FacesMessage getMsgInfo(String msgText) {
        return new FacesMessage(FacesMessage.SEVERITY_INFO, msgText, "");
    }

    public static FacesMessage getMsgWarn(String msgText) {
        return new FacesMessage(FacesMessage.SEVERITY_WARN, msgText, "");
    }

    public static void addMessageError(String messageId, String theMessage) {
        FacesContext.getCurrentInstance().addMessage(messageId,
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", theMessage));
    }

    public static void addMessageInfo(String messageId, String theMessage) {
        FacesContext.getCurrentInstance().addMessage(messageId,
            new FacesMessage(FacesMessage.SEVERITY_INFO, "INFO", theMessage));
    }

    // populateList
    public static Map<String, String> populateListPatientId(List<Patient> listPatient) {
        Map<String, String> listPatientId = new TreeMap<>();
        for (Patient rec : listPatient) {
            if (rec.getLastIdentifier() != null) {
                listPatientId.put(rec.getPatientIdentifier(), rec.getPatientIdentifierIso());
            }
        }
        return listPatientId;
    }

    public static Map<String, String> populateListGender() {
        Map<String, String> localGenderList = new HashMap<>();

        localGenderList.put("Male", "M");
        localGenderList.put("Female", "F");
        localGenderList.put("Undifferentiated", "UN");

        return localGenderList;
    }

    public static Map<String, String> populateListStatusType() {
        Map<String, String> popList = new HashMap<>();

        popList.put("Approved", "urn:oasis:names:tc:ebxml-regrep:StatusType:Approved");
        popList.put("Deprecated", "urn:oasis:names:tc:ebxml-regrep:StatusType:Deprecated");
        popList.put("Submitted", "urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted");

        return popList;
    }

    public static Map<String, String> populateListStates() {
        Map<String, String> popList = new TreeMap<>();

        popList.put("Alabama", "AL");
        popList.put("Alaska", "AK");
        popList.put("American Samoa", "AS");
        popList.put("Arizona", "AZ");
        popList.put("Arkansas", "AR");
        popList.put("California", "CA");
        popList.put("Colorado", "CO");
        popList.put("Connecticut", "CT");
        popList.put("Delaware", "DE");
        popList.put("District Of Columbia", "DC");
        popList.put("Federated States Of Micronesia", "FM");
        popList.put("Florida", "FL");
        popList.put("Georgia", "GA");
        popList.put("Guam", "GU");
        popList.put("Hawaii", "HI");
        popList.put("Idaho", "ID");
        popList.put("Illinois", "IL");
        popList.put("Indiana", "IN");
        popList.put("Iowa", "IA");
        popList.put("Kansas", "KS");
        popList.put("Kentucky", "KY");
        popList.put("Louisiana", "LA");
        popList.put("Maine", "ME");
        popList.put("Marshall Islands", "MH");
        popList.put("Maryland", "MD");
        popList.put("Massachusetts", "MA");
        popList.put("Michigan", "MI");
        popList.put("Minnesota", "MN");
        popList.put("Mississippi", "MS");
        popList.put("Missouri", "MO");
        popList.put("Montana", "MT");
        popList.put("Nebraska", "NE");
        popList.put("Nevada", "NV");
        popList.put("New Hampshire", "NH");
        popList.put("New Jersey", "NJ");
        popList.put("New Mexico", "NM");
        popList.put("New York", "NY");
        popList.put("North Carolina", "NC");
        popList.put("North Dakota", "ND");
        popList.put("Northern Mariana Islands", "MP");
        popList.put("Ohio", "OH");
        popList.put("Oklahoma", "OK");
        popList.put("Oregon", "OR");
        popList.put("Palau", "PW");
        popList.put("Pennsylvania", "PA");
        popList.put("Puerto Rico", "PR");
        popList.put("Rhode Island", "RI");
        popList.put("South Carolina", "SC");
        popList.put("South Dakota", "SD");
        popList.put("Tennessee", "TN");
        popList.put("Texas", "TX");
        popList.put("Utah", "UT");
        popList.put("Vermont", "VT");
        popList.put("Virgin Islands", "VI");
        popList.put("Virginia", "VA");
        popList.put("Washington", "WA");
        popList.put("West Virginia", "WV");
        popList.put("Wisconsin", "WI");
        popList.put("Wyoming", "WY");

        return popList;
    }

    public static Long diffByDays(Date startDate, Date endDate) {
        if (startDate != null && endDate != null) {
            return TimeUnit.MILLISECONDS.toDays(startDate.getTime() - endDate.getTime());
        }
        return null;
    }

    public static String getDateNow() {
        return getDateNow("MM/dd/yyyy");
    }

    public static String getDateTimeNow() {
        return getDateNow("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    }

    public static String getDateNow(String dateFormat) {
        return getDate(dateFormat, new Date());
    }

    public static String getDate(String dateFormat, Date date) {
        return new SimpleDateFormat(dateFormat).format(date);
    }

}
