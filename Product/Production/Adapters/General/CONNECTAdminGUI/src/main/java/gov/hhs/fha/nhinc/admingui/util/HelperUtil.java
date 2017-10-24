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
import gov.hhs.fha.nhinc.patientdb.model.Patient;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Tran Tang
 *
 */
public class HelperUtil {
    private static final Logger LOG = LoggerFactory.getLogger(HelperUtil.class);
    private static final String ENCRYPTION_KEY = "MZygpewJsCpRrfOr";
    private static final String CRYPTO_VALUE = "Blowfish";

    /*
     * Utility class-private constructor
     */
    private HelperUtil() {
    }

    /**
     * Populate the gender lookup data list. This logic needs to be moved to a Utility or to the application bean.
     *
     */
    public static Map<String, String> populteGenderList() {
        Map<String, String> localGenderList = new HashMap<>();
        localGenderList.put("Male", "M");
        localGenderList.put("Female", "F");
        localGenderList.put("Undifferentiated", "UN");
        return localGenderList;
    }

    public static boolean isId(Long id) {
        return id != null && id.longValue() > 0L;
    }

    // CONVERT-METHODS
    public static Timestamp toTimestamp(Date date){
        return new Timestamp(date.getTime());
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

    public static void addMessageError(String messageId, String theMessage) {
        FacesContext.getCurrentInstance().addMessage(messageId,
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", theMessage));
    }

    public static void addMessageInfo(String messageId, String theMessage) {
        FacesContext.getCurrentInstance().addMessage(messageId,
            new FacesMessage(FacesMessage.SEVERITY_INFO, "INFO", theMessage));
    }

    public static String encryptToKey(String strClearText, String strKey) {
        String strData="";
        if (StringUtils.isNotEmpty(strClearText)) {
            try {
                SecretKeySpec skeyspec = new SecretKeySpec(strKey.getBytes(), CRYPTO_VALUE);
                Cipher cipher = Cipher.getInstance(CRYPTO_VALUE);
                cipher.init(Cipher.ENCRYPT_MODE, skeyspec);
                byte[] encrypted=cipher.doFinal(strClearText.getBytes());
                strData=new String(encrypted);

            } catch (Exception e) {
                LOG.error("Encryption Error while encryptToKey: {} ", e.getMessage(), e);
            }
        }
        return strData;
    }

    public static String decryptToKey(String strEncrypted, String strKey) {
        String strData="";
        if (StringUtils.isNotEmpty(strEncrypted)) {
            try {
                SecretKeySpec skeyspec = new SecretKeySpec(strKey.getBytes(), CRYPTO_VALUE);
                Cipher cipher = Cipher.getInstance(CRYPTO_VALUE);
                cipher.init(Cipher.DECRYPT_MODE, skeyspec);
                byte[] decrypted=cipher.doFinal(strEncrypted.getBytes());
                strData=new String(decrypted);
            } catch (Exception e) {
                LOG.error("Encryption Error while decryptToKey: {} ", e.getMessage(), e);
            }
        }
        return strData;
    }

    public static String encrypt(String strClearText) {
        return encryptToKey(strClearText, ENCRYPTION_KEY);
    }

    public static String decrypt(String strEncrypted) {
        return decryptToKey(strEncrypted, ENCRYPTION_KEY);
    }

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

    public static Map<String, String> populateListPatientId(List<Patient> listPatient) {
        String formatValue = "{0}^^^&{1}&ISO";
        String formatDisplay = "{0} {1}^^^&{2}&ISO";

        Map<String, String> listPatientId = new HashMap<>();
        for (Patient rec : listPatient) {
            if(rec.getLastIdentifier() != null){
                listPatientId.put(
                    MessageFormat.format(formatDisplay, rec.getFirstName(), rec.getLastName(),
                        rec.getLastIdentifier().getOrganizationId()),
                    MessageFormat.format(formatValue, rec.getLastIdentifier().getId(),
                        rec.getLastIdentifier().getOrganizationId()));
            }
        }

        return listPatientId;
    }

    public static Map<String, String> populateListStatusType() {
        Map<String, String> popList = new HashMap<>();
        popList.put("Approved", "urn:oasis:names:tc:ebxml-regrep:StatusType:Approved");
        popList.put("Deprecated", "urn:oasis:names:tc:ebxml-regrep:StatusType:Deprecated");
        popList.put("Submitted", "urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted");

        return popList;
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
}

