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
package gov.hhs.fha.nhinc.callback.opensaml;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.w3c.dom.Element;

/**
 * @author bhumphrey
 *
 */
public abstract class SAMLAssertionBuilder {

    static final String ID_PREFIX = "_";

    static final String NHIN_NS = "http://www.hhs.gov/healthit/nhin";

    // Authorization Decision Action is always set to Execute
    static final String AUTHZ_DECISION_ACTION_EXECUTE = "Execute";

    static final String AUTHN_SESSION_INDEX = "123456";

    // Valid Authorization Decision values
    static final String AUTHZ_DECISION_PERMIT = "Permit";
    static final String X509_NAME_ID = "urn:oasis:names:tc:SAML:1.1:nameid-format:X509SubjectName";
    static final String X509_AUTHN_CNTX_CLS = "urn:oasis:names:tc:SAML:2.0:ac:classes:X509";
    static final String UNSPECIFIED_AUTHN_CNTX_CLS = "urn:oasis:names:tc:SAML:2.0:ac:classes:unspecified";

    // Valid Name Identification values
    private static final String UNSPECIFIED_NAME_ID = "urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified";
    private static final String EMAIL_NAME_ID = "urn:oasis:names:tc:SAML:1.1:nameid-format:emailAddress";
    private static final String WINDOWS_NAME_ID = "urn:oasis:names:tc:SAML:1.1:nameid-format:WindowsDomainQualifiedName";
    private static final String KERBEROS_NAME_ID = "urn:oasis:names:tc:SAML:1.1:nameid-format:kerberos";
    private static final String ENTITY_NAME_ID = "urn:oasis:names:tc:SAML:1.1:nameid-format:entity";
    private static final String PERSISTENT_NAME_ID = "urn:oasis:names:tc:SAML:1.1:nameid-format:persistent";
    private static final String TRANSIENT_NAME_ID = "urn:oasis:names:tc:SAML:1.1:nameid-format:transient";

    private static final String[] VALID_NAME_ID_ARRAY = {UNSPECIFIED_NAME_ID, EMAIL_NAME_ID, X509_NAME_ID,
            WINDOWS_NAME_ID, KERBEROS_NAME_ID, ENTITY_NAME_ID, PERSISTENT_NAME_ID, TRANSIENT_NAME_ID};
    private static final List<String> VALID_NAME_LIST = Collections
        .unmodifiableList(Arrays.asList(VALID_NAME_ID_ARRAY));

    // Valid Context Class references
    private static final String INTERNET_AUTHN_CNTX_CLS = "urn:oasis:names:tc:SAML:2.0:ac:classes:InternetProtocol";
    private static final String INTERNET_SECRET_AUTHN_CNTX_CLS
    = "urn:oasis:names:tc:SAML:2.0:ac:classes:InternetProtocolPassword";
    private static final String SECRET_AUTHN_CNTX_CLS = "urn:oasis:names:tc:SAML:2.0:ac:classes:Password";
    private static final String SECRET_TRANS_AUTHN_CNTX_CLS
    = "urn:oasis:names:tc:SAML:2.0:ac:classes:PasswordProtectedTransport";
    private static final String KERBEROS_AUTHN_CNTX_CLS = "urn:oasis:names:tc:SAML:2.0:ac:classes:Kerberos";
    private static final String PREVIOUS_AUTHN_CNTX_CLS = "urn:oasis:names:tc:SAML:2.0:ac:classes:PreviousSession";
    private static final String REMOTE_AUTHN_CNTX_CLS = "urn:oasis:names:tc:SAML:2.0:ac:classes:SecureRemotePassword";
    private static final String TLS_AUTHN_CNTX_CLS = "urn:oasis:names:tc:SAML:2.0:ac:classes:TLSClient";
    private static final String PGP_AUTHN_CNTX_CLS = "urn:oasis:names:tc:SAML:2.0:ac:classes:PGP";
    private static final String SPKI_AUTHN_CNTX_CLS = "urn:oasis:names:tc:SAML:2.0:ac:classes:SPKI";
    private static final String DIG_SIGN_AUTHN_CNTX_CLS = "urn:oasis:names:tc:SAML:2.0:ac:classes:XMLDSig";
    private static final String[] VALID_AUTHN_CNTX_CLS_ARRAY = {INTERNET_AUTHN_CNTX_CLS,
            INTERNET_SECRET_AUTHN_CNTX_CLS, SECRET_AUTHN_CNTX_CLS, SECRET_TRANS_AUTHN_CNTX_CLS, KERBEROS_AUTHN_CNTX_CLS,
            PREVIOUS_AUTHN_CNTX_CLS, REMOTE_AUTHN_CNTX_CLS, TLS_AUTHN_CNTX_CLS, X509_AUTHN_CNTX_CLS, PGP_AUTHN_CNTX_CLS,
            SPKI_AUTHN_CNTX_CLS, DIG_SIGN_AUTHN_CNTX_CLS, UNSPECIFIED_AUTHN_CNTX_CLS};
    private static final List<String> VALID_AUTHN_CNTX_CLS_LIST = Collections
        .unmodifiableList(Arrays.asList(VALID_AUTHN_CNTX_CLS_ARRAY));

    static boolean isValidNameidFormat(final String format) {
        return VALID_NAME_LIST.contains(format.trim());
    }

    static boolean isValidAuthnCntxCls(final String value) {
        return VALID_AUTHN_CNTX_CLS_LIST.contains(value.trim());
    }

    static boolean isValidIssuerFormat(String format) {
        return VALID_NAME_LIST.contains(format);
    }

    public abstract Element build(CallbackProperties properties, String gatewayAlias);

}
