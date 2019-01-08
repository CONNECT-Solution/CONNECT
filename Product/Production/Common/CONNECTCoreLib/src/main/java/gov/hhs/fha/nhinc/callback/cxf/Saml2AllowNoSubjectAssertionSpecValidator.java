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
package gov.hhs.fha.nhinc.callback.cxf;

import org.apache.commons.collections.CollectionUtils;
import org.apache.wss4j.common.ext.WSSecurityException;
import org.apache.wss4j.common.ext.WSSecurityException.ErrorCode;
import org.apache.wss4j.common.saml.SamlAssertionWrapper;
import org.opensaml.saml.saml2.core.Assertion;

/**
 * Checks {@link org.opensaml.saml2.core.Assertion} for Spec compliance. This validator relaxes the rules by not
 * checking for the existence of Subject when the Assertion contains AttributeStatements. This is required for
 * interoperability with previous CONNECT gateways.
 */
public class Saml2AllowNoSubjectAssertionSpecValidator extends Saml2ExchangeAuthFrameworkValidator {

    /**
     * Instantiates a new saml2 allow no subject assertion spec validator.
     */
    public Saml2AllowNoSubjectAssertionSpecValidator() {
        super();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * gov.hhs.fha.nhinc.callback.cxf.Saml2ExchangeAuthFrameworkValidator#validateAssertion(org.apache.wss4j.common.saml
     * .SamlAssertionWrapper)
     */
    @Override
    protected void validateAssertion(final SamlAssertionWrapper samlAssertion) throws WSSecurityException {
        validateSubject(samlAssertion.getSaml2());
    }

    /**
     * Checks that the Subject element is present when required, but does not check for the existence of Subject when
     * the Assertion contains AttributeStatements.
     *
     * @param assertion the assertion
     * @throws ValidationException the validation exception
     */

    protected void validateSubject(final Assertion assertion) throws WSSecurityException {
        if (CollectionUtils.isEmpty(assertion.getStatements())) {
            assertAttAuthzStatements(assertion);
        }
        if (CollectionUtils.isNotEmpty(assertion.getAuthnStatements()) && assertion.getSubject() == null) {
            throw new WSSecurityException(ErrorCode.FAILURE, "Assertions containing AuthnStatements require a Subject");
        }
        if (CollectionUtils.isNotEmpty(assertion.getAuthzDecisionStatements()) && assertion.getSubject() == null) {
            throw new WSSecurityException(ErrorCode.FAILURE,
                "Assertions containing AuthzDecisionStatements require a Subject");
        }

        validateSubject(assertion.getSubject());

    }

    protected static void assertAttAuthzStatements(final Assertion assertion) throws WSSecurityException {
        if (CollectionUtils.isEmpty(assertion.getAttributeStatements())
            && CollectionUtils.isEmpty(assertion.getAuthnStatements())) {
            assertAuthzDecisionStatements(assertion);
        }
    }

    protected static void assertAuthzDecisionStatements(final Assertion assertion) throws WSSecurityException {
        if (CollectionUtils.isEmpty(assertion.getAuthzDecisionStatements())
            && assertion.getSubject() == null) {
            throw new WSSecurityException(ErrorCode.FAILURE, "Subject is required when Statements are absent");
        }
    }

}
