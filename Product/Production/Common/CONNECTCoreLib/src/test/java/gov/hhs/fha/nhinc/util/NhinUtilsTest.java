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

import gov.hhs.fha.nhinc.callback.opensaml.CertificateManagerImpl;
import gov.hhs.fha.nhinc.common.nhinccommon.CeType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import java.util.HashMap;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author tjafri
 */
public class NhinUtilsTest {

    private static final String KEY_STORE_PATH = "src/test/resources/gov/hhs/fha/nhinc/callback/gateway.jks";
    private static final String TRUST_STORE_PATH = "src/test/resources/gov/hhs/fha/nhinc/callback/cacerts.jks";
    private static CertificateManagerImpl certManager;
    private static final String CERT_SUBJECT_DN = "CN=host1";

    @BeforeClass
    public static void setup() {
        final HashMap<String, String> keyStoreMap = new HashMap<>();
        keyStoreMap.put(CertificateManagerImpl.KEY_STORE_KEY, KEY_STORE_PATH);
        keyStoreMap.put(CertificateManagerImpl.KEY_STORE_PASSWORD_KEY, "changeit");
        keyStoreMap.put(CertificateManagerImpl.KEY_STORE_TYPE_KEY, "JKS");

        final HashMap<String, String> trustStoreMap = new HashMap<>();
        trustStoreMap.put(CertificateManagerImpl.TRUST_STORE_KEY, TRUST_STORE_PATH);
        trustStoreMap.put(CertificateManagerImpl.TRUST_STORE_PASSWORD_KEY, "changeit");
        trustStoreMap.put(CertificateManagerImpl.TRUST_STORE_TYPE_KEY, "JKS");

        certManager = (CertificateManagerImpl) CertificateManagerImpl.getInstance(keyStoreMap, trustStoreMap);
    }

    @Test
    public void testSAMLSubjectDNWithNullUserName() {
        NhinUtils nhinUtils = NhinUtils.getInstance();
        nhinUtils.setCertificateManager(certManager);
        String subjectDN = nhinUtils.getSAMLSubjectNameDN(getNhinTarget(), getUserInfo(null));
        assertNotNull(subjectDN);
        assertEquals("UserName does not match the certificate subject DN", CERT_SUBJECT_DN, subjectDN);
    }

    @Test
    public void testSAMLSubjectDNWithInvalidUserName() {
        NhinUtils nhinUtils = NhinUtils.getInstance();
        nhinUtils.setCertificateManager(certManager);
        String subjectDN = nhinUtils.getSAMLSubjectNameDN(getNhinTarget(), getUserInfo("testUser"));
        assertNotNull(subjectDN);
        assertEquals("UserName does not match the certificate subject DN", CERT_SUBJECT_DN, subjectDN);
    }

    @Test
    public void testSAMLSubjectDNWithValidUserName() {
        NhinUtils nhinUtils = NhinUtils.getInstance();
        nhinUtils.setCertificateManager(certManager);
        String subjectDN = nhinUtils.getSAMLSubjectNameDN(getNhinTarget(), getUserInfo("CN=testUser"));
        assertNotNull(subjectDN);
        assertEquals("UserName does not match with one supplied in assertion", "CN=testUser", subjectDN);
    }

    protected UserType getUserInfo(String userName) {
        UserType userType = new UserType();
        userType.setOrg(createHomeCommunityType());
        userType.setPersonName(createPersonNameType());
        userType.setRoleCoded(createCeType());
        userType.setUserName(userName);

        return userType;
    }

    protected CeType createCeType() {
        CeType ceType = new CeType();
        ceType.setCode("Code");
        ceType.setCodeSystem("CodeSystem");
        ceType.setCodeSystemVersion("1.1");
        ceType.setDisplayName("DisplayName");
        return ceType;
    }

    protected HomeCommunityType createHomeCommunityType() {
        HomeCommunityType homeCommunityType = new HomeCommunityType();
        homeCommunityType.setHomeCommunityId("1.1");
        homeCommunityType.setName("DOD");
        homeCommunityType.setDescription("This is DOD Gateway");
        return homeCommunityType;
    }

    protected PersonNameType createPersonNameType() {
        PersonNameType personNameType = new PersonNameType();
        personNameType.setFamilyName("Tamney");
        personNameType.setFullName("Erica");
        personNameType.setGivenName("Jasmine");
        personNameType.setPrefix("Ms");
        return personNameType;
    }

    protected NhinTargetSystemType getNhinTarget() {
        NhinTargetSystemType targetSystem = new NhinTargetSystemType();
        targetSystem.setHomeCommunity(createTargetHomeCommunityType());
        return targetSystem;
    }

    protected HomeCommunityType createTargetHomeCommunityType() {
        HomeCommunityType homeCommunityType = new HomeCommunityType();
        homeCommunityType.setHomeCommunityId("2.2");
        homeCommunityType.setName("SSA");
        homeCommunityType.setDescription("This is DOD Gateway");
        return homeCommunityType;
    }
}
