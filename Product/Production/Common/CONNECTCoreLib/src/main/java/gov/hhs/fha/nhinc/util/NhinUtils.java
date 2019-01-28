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

import gov.hhs.fha.nhinc.callback.opensaml.CertificateManager;
import gov.hhs.fha.nhinc.callback.opensaml.CertificateManagerException;
import gov.hhs.fha.nhinc.callback.opensaml.CertificateManagerImpl;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommon.ObjectFactory;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import gov.hhs.fha.nhinc.exchangemgr.ExchangeManagerHelper;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.UDDI_SPEC_VERSION;
import java.security.cert.X509Certificate;
import javax.naming.InvalidNameException;
import javax.naming.Name;
import javax.naming.ldap.LdapName;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ttang
 *
 */
public class NhinUtils {

    private static final Logger LOG = LoggerFactory.getLogger(NhinUtils.class);
    private static final NhinUtils INSTANCE = new NhinUtils();
    private static CertificateManager certMgr;

    private NhinUtils() {
        NhinUtils.certMgr = CertificateManagerImpl.getInstance();
    }

    public static NhinUtils getInstance() {
        return INSTANCE;
    }

    /**
     * set user spec version if target doesn't have it
     *
     * @param targets NhinTargetCommunitiesType
     * @param version service spec version
     */
    public void setTargetCommunitiesVersion(NhinTargetCommunitiesType targets, UDDI_SPEC_VERSION version) {
        if (targets == null) {
            targets = new ObjectFactory().createNhinTargetCommunitiesType();
        }

        if (StringUtils.isBlank(targets.getUseSpecVersion())) {
            targets.setUseSpecVersion(version.toString());
        }
    }

    /**
     * <p>
     * This method extract userName from assertion-->userInfo element. If it is not a valid DN, it pulls the subject DN
     * name from certificate based on exchangeName
     *
     * @param target
     * @param userInfo
     * @return String - a valid DN string
     */
    public String getSAMLSubjectNameDN(NhinTargetSystemType target, UserType userInfo) {
        if (userInfo == null || StringUtils.isBlank(userInfo.getUserName()) || !checkDistinguishedName(userInfo.
            getUserName())) {
            String alias = ExchangeManagerHelper.getExchangeAlias(target.getExchangeName());
            try {
                X509Certificate cert = getCertificateManager().getCertificateBy(alias);
                return cert.getSubjectX500Principal().getName();
            } catch (CertificateManagerException ex) {
                LOG.error("Unable to load certificate with alias {}", alias, ex);
                return null;
            }
        }
        return userInfo.getUserName().trim();
    }

    public String getSAMLSubjectNameDN(X509Certificate x509Cert, String userName) {
        String subjectDN = userName;
        if ((StringUtils.isBlank(userName) || !checkDistinguishedName(userName)) && null != x509Cert
            && null != x509Cert.getSubjectX500Principal()) {
            subjectDN = x509Cert.getSubjectX500Principal().getName();
        }
        return subjectDN;
    }

    public boolean checkDistinguishedName(String userName) {
        Name name = null;
        try {
            name = new LdapName(userName);
        } catch (InvalidNameException ex) {
            LOG.debug("DN check exception {}", ex.getLocalizedMessage(), ex);
            LOG.warn("Invalid distinguished name {}", userName);
        }
        return name != null;
    }

    private static CertificateManager getCertificateManager() {
        return certMgr;
    }

    protected static void setCertificateManager(CertificateManager certMgr) {
        NhinUtils.certMgr = certMgr;
    }
}
