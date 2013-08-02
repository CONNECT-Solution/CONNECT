/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
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

import org.opensaml.saml2.core.Assertion;
import org.opensaml.xml.validation.ValidationException;

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

    /**
     * Checks that the Subject element is present when required, but does not check for the existence of Subject when
     * the Assertion contains AttributeStatements.
     *
     * @param assertion the assertion
     * @throws ValidationException the validation exception
     */
    @Override
    protected void validateSubject(Assertion assertion) throws ValidationException {
        if ((assertion.getStatements() == null || assertion.getStatements().size() == 0)
                && (assertion.getAuthnStatements() == null || assertion.getAuthnStatements().size() == 0)
                && (assertion.getAttributeStatements() == null || assertion.getAttributeStatements().size() == 0)
                && (assertion.getAuthzDecisionStatements() == null || assertion.getAuthzDecisionStatements().size() == 0)
                && assertion.getSubject() == null) {
            throw new ValidationException("Subject is required when Statements are absent");
        }

        if (assertion.getAuthnStatements().size() > 0 && assertion.getSubject() == null) {
            throw new ValidationException("Assertions containing AuthnStatements require a Subject");
        }
        if (assertion.getAuthzDecisionStatements().size() > 0 && assertion.getSubject() == null) {
            throw new ValidationException("Assertions containing AuthzDecisionStatements require a Subject");
        }
        
        if (assertion.getSubject() != null) {
            validateSubject(assertion.getSubject());
        }
    }
}