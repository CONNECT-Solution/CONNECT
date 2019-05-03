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
package gov.hhs.fha.nhinc.admingui.util;

import static gov.hhs.fha.nhinc.admingui.jee.jsf.UserAuthorizationListener.USER_INFO_SESSION_ATTRIBUTE;

import com.google.gson.Gson;
import gov.hhs.fha.nhinc.admingui.model.loadtestdata.Document;
import gov.hhs.fha.nhinc.admingui.model.loadtestdata.Patient;
import gov.hhs.fha.nhinc.admingui.services.persistence.jpa.entity.UserLogin;
import gov.hhs.fha.nhinc.callback.SamlConstants;
import gov.hhs.fha.nhinc.common.loadtestdatamanagement.DocumentMetadataType;
import gov.hhs.fha.nhinc.common.loadtestdatamanagement.PatientType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.ConfigAssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import gov.hhs.fha.nhinc.configuration.GenericPortDescriptor;
import gov.hhs.fha.nhinc.exchangemgr.ExchangeManagerException;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClientFactory;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import javax.el.ELContext;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.primefaces.context.RequestContext;
import org.primefaces.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Tran Tang
 *
 */
public class HelperUtil {
    public static final String TO_DO_MARKER = "TO DO";
    private static final Logger LOG = LoggerFactory.getLogger(HelperUtil.class);
    private static final String ADMINGUI_PROPERTIES = "admingui";

    /*
     * Utility class-private constructor
     */
    private HelperUtil() {
    }

    // CONVERT-METHODS
    public static Timestamp toTimestamp(Date date) {
        if (date != null) {
            return new Timestamp(date.getTime());
        }
        return null;
    }

    public static Date toDate(Timestamp timestamp) {
        if (timestamp != null) {
            return new Date(timestamp.getTime());
        }
        return null;
    }

    public static String toJsonString(Object object) {
        return new Gson().toJson(object);
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

    /*
     * @clientId - The client identifier with which this message is associated (if any)
     */
    public static void addMessageError(String clientId, String theMessage) {
        FacesContext.getCurrentInstance().addMessage(clientId, getMsgError(theMessage));
    }

    /*
     * @clientId - The client identifier with which this message is associated (if any)
     */
    public static void addMessageInfo(String clientId, String theMessage) {
        FacesContext.getCurrentInstance().addMessage(clientId, getMsgInfo(theMessage));
    }

    public static void addMessageWarn(String clientId, String theMessage) {
        FacesContext.getCurrentInstance().addMessage(clientId, getMsgWarn(theMessage));
    }


    // populateList
    public static Map<String, String> populateListPatientId(List<Patient> listPatient) {
        Map<String, String> listPatientId = new TreeMap<>();
        for (Patient rec : listPatient) {
            if (CollectionUtils.isNotEmpty(rec.getIdentifierList())) {
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
        if (null != date) {
            return new SimpleDateFormat(dateFormat).format(date);
        }
        return "";
    }

    public static String formatDate(String dateFormat, Date date) {
        return getDate(dateFormat, date);
    }

    public static boolean execPFCommand(String cmdString) {
        return execPFCommand(cmdString, true);
    }

    public static boolean execPFCommand(String cmdString, boolean successful) {
        if (StringUtils.isBlank(cmdString)) {
            return false;
        }
        if (successful) {
            RequestContext.getCurrentInstance().execute(cmdString);
            return true;
        }
        return false;
    }

    public static boolean execPFShowDialog(String dlgWidgetVarName) {
        return execPFShowDialog(dlgWidgetVarName, true);
    }

    public static boolean execPFShowDialog(String dlgWidgetVarName, boolean successful) {
        if (StringUtils.isBlank(dlgWidgetVarName)) {
            return false;
        }
        String pfShowDialog = MessageFormat.format("PF(''{0}'').show();", dlgWidgetVarName);
        return execPFCommand(pfShowDialog, successful);
    }

    public static boolean execPFHideDialog(String dlgWidgetVarName) {
        return execPFHideDialog(dlgWidgetVarName, true);
    }

    public static boolean execPFHideDialog(String dlgWidgetVarName, boolean successful) {
        if (StringUtils.isBlank(dlgWidgetVarName)) {
            return false;
        }
        String pfHideDialog = MessageFormat.format("PF(''{0}'').hide();", dlgWidgetVarName);
        return execPFCommand(pfHideDialog, successful);
    }

    public static ConfigAssertionType buildConfigAssertion() {
        ConfigAssertionType assertion = new ConfigAssertionType();
        UserLogin user = getUser();
        if (user != null) {
            UserType configUser = new UserType();
            configUser.setUserName(user.getUserName());
            assertion.setUserInfo(configUser);
        }
        assertion.setConfigInstance(new DateTime().toString());
        assertion.setAuthMethod(SamlConstants.ADMIN_AUTH_METHOD);

        return assertion;
    }

    public static UserLogin getUser() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext != null && facesContext.getViewRoot() != null) {
            HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
            if (session != null) {
                return (UserLogin) session.getAttribute(USER_INFO_SESSION_ATTRIBUTE);
            }
        }

        return null;
    }

    public static int getHashCodeBy(Object... values) {
        return Arrays.hashCode(values);
    }

    public static <T> T getPFELExpression(String elExpression, Class<T> clazzOf) {
        FacesContext context = FacesContext.getCurrentInstance();
        ELContext elContext = context.getELContext();
        return (T) context.getApplication().getExpressionFactory()
            .createValueExpression(elContext, elExpression, clazzOf).getValue(elContext);
    }

    public static <T> List<T> convertList(JSONArray jsonArray) {
        List<T> retList = new ArrayList<>();
        if (null != jsonArray) {
            for (Object item : jsonArray) {
                retList.add((T) item);
            }
        }
        return retList;
    }

    public static String readPropertyAdminGui(String propertyName, String defaultValue) {
        return PropertyAccessor.getInstance().getProperty(ADMINGUI_PROPERTIES, propertyName, defaultValue);
    }

    /**
     * getting unsecure-connect client with generic-portDescriptor
     */
    public static <T> CONNECTClient<T> getClientUnsecure(String serviceUrl, String wsAddressingAction,
        Class<T> portTypeClass) throws ExchangeManagerException {
        ServicePortDescriptor<T> portDescriptor = new GenericPortDescriptor(wsAddressingAction, portTypeClass);
        return CONNECTClientFactory.getInstance().getCONNECTClientUnsecured(portDescriptor, serviceUrl,
            new AssertionType());
    }

    public static List<Patient> convertPatients(List<PatientType> patients) {
        List<Patient> list = new ArrayList<>();
        for(PatientType patient: patients){
            list.add(new Patient(patient));
        }
        return list;
    }

    public static List<Document> convertDocuments(List<DocumentMetadataType> documents) {
        List<Document> list = new ArrayList<>();
        for (DocumentMetadataType meta : documents) {
            list.add(new Document(meta));
        }
        return list;
    }

}
