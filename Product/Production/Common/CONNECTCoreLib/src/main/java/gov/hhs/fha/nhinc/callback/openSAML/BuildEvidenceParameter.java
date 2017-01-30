/*
 * Copyright (c) 2009-2016, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.callback.openSAML;

import java.util.List;
import org.joda.time.DateTime;
import org.opensaml.saml.saml2.core.AttributeStatement;
import org.opensaml.saml.saml2.core.Subject;

public class BuildEvidenceParameter {

    private CallbackProperties properties;
    private String evAssertionID;
    private DateTime issueInstant = null;
    private String format = null;
    private DateTime beginValidTime = null;
    private DateTime endValidTime = null;
    private String issuer = null;
    private List<AttributeStatement> statements;
    private Subject subject;

    public BuildEvidenceParameter(String evAssertionID, String issuer, CallbackProperties properties,
        List<AttributeStatement> statements, Subject subject) {
        this.evAssertionID = evAssertionID;
        this.properties = properties;
        this.statements = statements;
        this.subject = subject;
        this.issuer = issuer;
        setProperties(properties);
    }

    public CallbackProperties getProperties() {
        return properties;
    }

    public void setProperties(CallbackProperties properties) {
        issueInstant = properties.getEvidenceInstant();
        setIssueInstant(issueInstant);
        format = properties.getEvidenceIssuerFormat();
        setFormat(format);
        beginValidTime = properties.getEvidenceConditionNotBefore();
        setBeginValidTime(beginValidTime);
        endValidTime = properties.getEvidenceConditionNotAfter();
        setEndValidTime(endValidTime);
        this.properties = properties;
    }

    public String getEvAssertionID() {
        return evAssertionID;
    }

    public void setEvAssertionID(String evAssertionID) {
        this.evAssertionID = evAssertionID;
    }

    public DateTime getIssueInstant() {
        return issueInstant;
    }

    public void setIssueInstant(DateTime issueInstant) {
        this.issueInstant = issueInstant;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public DateTime getBeginValidTime() {
        return beginValidTime;
    }

    public void setBeginValidTime(DateTime beginValidTime) {
        this.beginValidTime = beginValidTime;
    }

    public DateTime getEndValidTime() {
        return endValidTime;
    }

    public void setEndValidTime(DateTime endValidTime) {
        this.endValidTime = endValidTime;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public List<AttributeStatement> getStatements() {
        return statements;
    }

    public void setStatements(List<AttributeStatement> statements) {
        this.statements = statements;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }
}
