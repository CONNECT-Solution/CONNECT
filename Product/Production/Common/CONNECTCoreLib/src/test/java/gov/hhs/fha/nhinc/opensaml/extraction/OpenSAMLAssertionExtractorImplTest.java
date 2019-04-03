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
package gov.hhs.fha.nhinc.opensaml.extraction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.CeType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlAuthnStatementType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlAuthzDecisionStatementEvidenceAssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlAuthzDecisionStatementEvidenceConditionsType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlAuthzDecisionStatementEvidenceType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlAuthzDecisionStatementType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlIssuerType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlSignatureKeyInfoType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlSignatureType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlSubjectConfirmationType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.wss4j.common.saml.OpenSAMLUtil;
import org.junit.Test;
import org.opensaml.saml.saml2.core.Assertion;
import org.opensaml.saml.saml2.core.Evidence;
import org.opensaml.saml.saml2.core.SubjectConfirmation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author dharley
 *
 */
public class OpenSAMLAssertionExtractorImplTest {

    private static final String EMPTY_STRING = "";

    private final OpenSAMLAssertionExtractorImpl openSAMLAssertionExtractorImpl = new OpenSAMLAssertionExtractorImpl();

    static {
        OpenSAMLUtil.initSamlEngine();
    }

    /**
     * When the SAML file is null, assertion extracted will be null.
     *
     * @throws Exception on error.
     */
    @Test
    public void testNullAssertionElement() throws Exception {
        assertNull(openSAMLAssertionExtractorImpl.extractSAMLAssertion(null));
    }

    /**
     * Tests SAML Assertion populated with all possible Assertion elements and
     * attributes, verify they are populated.
     *
     * @throws Exception on error.
     */
    @Test
    public void testCompleteSamlAssertion() throws Exception {

        AssertionType assertionType = openSAMLAssertionExtractorImpl
            .extractSAMLAssertion(getElementForSamlFile(getTestFilePath("complete_saml.xml")));
        assertNotNull(assertionType);

        verifyHomeCommunity(assertionType.getHomeCommunity(), "2.16.840.1.113883.3.424", null);
        verifyIssuer(assertionType.getSamlIssuer());
        verifyDecisionStatement(assertionType.getSamlAuthzDecisionStatement());
        verifyUser(assertionType.getUserInfo());
        verifyAuthnStatement(assertionType.getSamlAuthnStatement());
        verifyUniquePatientId(assertionType.getUniquePatientId());
        verifyCeType(assertionType.getPurposeOfDisclosureCoded(), "OPERATIONS", "2.16.840.1.113883.3.18.7.1",
            "nhin-purpose", "Healthcare Operations");
        verifySignature(assertionType.getSamlSignature());
        verifySubject(assertionType.getSamlSubjectConfirmations());
    }

    /**
     * @param samlSubjects
     */
    private void verifySubject(List<SamlSubjectConfirmationType> samlSubjects) {
        assertEquals("subject should have HOK,SV,Bearer methods", 3, samlSubjects.size());
        for (SamlSubjectConfirmationType subject : samlSubjects) {
            if (SubjectConfirmation.METHOD_SENDER_VOUCHES.equalsIgnoreCase(subject.getMethod())) {
                assertEquals("tester", subject.getInResponseTo());
                assertEquals("127.0.0.1", subject.getAddress());
            }
        }
    }

    /**
     * Tests the SAML Assertion extraction ensuring that the correct assertion
     * is retrieved even if the element is not first in the security element and
     * even if there are descendants that have assertions.
     *
     * @throws Exception on error
     */
    @Test
    public void testDifferentOrderedAssertion() throws Exception {

        AssertionType assertionType = openSAMLAssertionExtractorImpl
            .extractSAMLAssertion(getElementForSamlFile(getTestFilePath("saml_header_diff_order.xml")));
        assertNotNull(assertionType);

        assertEquals("CN=SAML User,OU=SU,O=SAML User,L=Los Angeles,ST=CA,C=US", assertionType.getSamlIssuer()
            .getIssuer());
        assertEquals("urn:oasis:names:tc:SAML:1.1:nameid-format:X509SubjectName", assertionType.getSamlIssuer()
            .getIssuerFormat());
    }

    @Test
    public void evidenceWithoutAssertions() throws Exception {
        Element element = getElementWitoutEvidenceAssertions();
        AssertionType assertionType = openSAMLAssertionExtractorImpl.extractSAMLAssertion(element);
        SamlAuthzDecisionStatementEvidenceType extractedEvidence = assertionType.getSamlAuthzDecisionStatement()
            .getEvidence();
        assertNotNull(extractedEvidence);
        assertNull(extractedEvidence.getAssertion());
    }

    @Test
    public void testSamlAssertionValuesMissing() throws Exception {

        AssertionType assertionType = openSAMLAssertionExtractorImpl
            .extractSAMLAssertion(getElementForSamlFile(getTestFilePath("saml_missingValues.xml")));
        assertNotNull(assertionType);

        verifyHomeCommunity(assertionType.getHomeCommunity(), EMPTY_STRING, null);
        verifyIssuer(assertionType.getSamlIssuer());
        verifyDecisionStatement(assertionType.getSamlAuthzDecisionStatement());
        assertEquals(assertionType.getUserInfo().getPersonName().getFamilyName(), EMPTY_STRING);
        assertEquals(assertionType.getUserInfo().getRoleCoded().getCode(), EMPTY_STRING);
        verifyAuthnStatement(assertionType.getSamlAuthnStatement());
        assertEquals(assertionType.getPurposeOfDisclosureCoded().getCode(), EMPTY_STRING);
        verifySignature(assertionType.getSamlSignature());
    }

    private Element getElementWitoutEvidenceAssertions() throws Exception {
        Element element = getElementForSamlFile(getTestFilePath("complete_saml.xml"));
        Assertion assertion = (Assertion) OpenSAMLUtil.fromDom(element);
        Evidence evidence = assertion.getAuthzDecisionStatements().get(0).getEvidence();
        List<Assertion> assertions = evidence.getAssertions();
        assertions.clear();
        return OpenSAMLUtil.toDom(assertion, null);
    }

    /**
     * Gets the file path to the test resource.
     *
     * @param filename the name of the file to retrieve
     * @return
     */
    private String getTestFilePath(String filename) {
        // the first "/" is intentionally not using File.separator in order to get the absolute path to the file.
        return "/testing_saml" + File.separator + filename;
    }

    private void verifyIssuer(SamlIssuerType issuer) {
        assertEquals("CN=SAML User,OU=SU,O=SAML User,L=Los Angeles,ST=CA,C=US", issuer.getIssuer());
        assertEquals("urn:oasis:names:tc:SAML:1.1:nameid-format:X509SubjectName", issuer.getIssuerFormat());
    }

    private void verifyHomeCommunity(HomeCommunityType homeCommunity, String id, String name) {
        assertEquals(id, homeCommunity.getHomeCommunityId());
        assertEquals(name, homeCommunity.getName());
    }

    private void verifyUniquePatientId(List<String> uniquePatientId) {
        assertEquals(1, uniquePatientId.size());
        assertEquals("RI1.101.00043^^^&2.16.840.1.113883.3.424&ISO", uniquePatientId.get(0));
    }

    private void verifyAuthnStatement(SamlAuthnStatementType statement) {
        assertEquals("urn:oasis:names:tc:SAML:2.0:ac:classes:X509", statement.getAuthContextClassRef());
        assertEquals("2010-05-01T02:09:01.089Z", statement.getAuthInstant());
        assertEquals("123456", statement.getSessionIndex());
        assertEquals(null, statement.getSubjectLocalityAddress());
        assertEquals(null, statement.getSubjectLocalityDNSName());
    }

    private void verifyDecisionStatement(SamlAuthzDecisionStatementType decisionStatement) {

        assertNotNull(decisionStatement);

        // verify decision statement
        assertEquals("Permit", decisionStatement.getDecision());
        assertEquals("https://nhinri1c23.aegis.net:8181/NhinConnect/EntityPatientDiscoverySecured",
            decisionStatement.getResource());
        assertEquals("Execute", decisionStatement.getAction());

        // verify decision statement evidence
        SamlAuthzDecisionStatementEvidenceType evidence = decisionStatement.getEvidence();
        SamlAuthzDecisionStatementEvidenceAssertionType evidenceAssertion = evidence.getAssertion();
        assertEquals("759724ff-e9ce-4a7f-a55b-fc41ffe21a75", evidenceAssertion.getId());
        assertEquals("2010-05-01T02:09:01.104Z", evidenceAssertion.getIssueInstant());
        assertEquals("2.0", evidenceAssertion.getVersion());
        assertEquals("CN=SAML User,OU=SU,O=SAML User,L=Los Angeles,ST=CA,C=US", evidenceAssertion.getIssuer());
        assertEquals("urn:oasis:names:tc:SAML:1.1:nameid-format:X509SubjectName", evidenceAssertion.getIssuerFormat());

        SamlAuthzDecisionStatementEvidenceConditionsType evidenceConditions = evidenceAssertion.getConditions();
        assertEquals("2010-05-01T02:09:01.104Z", evidenceConditions.getNotBefore());
        assertEquals("2010-05-01T02:09:01.104Z", evidenceConditions.getNotOnOrAfter());

    }

    private void verifyUser(UserType user) {

        verifyHomeCommunity(user.getOrg(), "2.16.840.1.113883.3.424", "2.16.840.1.113883.3.424");

        PersonNameType personName = user.getPersonName();
        assertEquals("Testcase", personName.getFamilyName());
        assertEquals("Interop\n                IT Testcase", personName.getFullName());
        assertEquals("Interop", personName.getGivenName());
        assertEquals("IT", personName.getSecondNameOrInitials());
        assertNull(personName.getNameType());
        assertNull(personName.getPrefix());
        assertNull(personName.getSuffix());

        verifyCeType(user.getRoleCoded(), "46255001", "2.16.840.1.113883.6.96", "SNOMED_CT", "Pharmacist");
        assertEquals("UID=Scenario 45 PDR-5.7", user.getUserName());
    }

    private void verifyCeType(CeType ceType, String code, String codeSystem, String codeSystemName, String displayName) {
        assertEquals(code, ceType.getCode());
        assertEquals(codeSystem, ceType.getCodeSystem());
        assertEquals(codeSystemName, ceType.getCodeSystemName());
        assertEquals(displayName, ceType.getDisplayName());
    }

    private void verifySignature(SamlSignatureType signature) {
        assertNotNull(signature.getSignatureValue());
        SamlSignatureKeyInfoType keyInfo = signature.getKeyInfo();
        assertNotNull(keyInfo.getRsaKeyValueExponent());
        assertNotNull(keyInfo.getRsaKeyValueModulus());
    }

    private Element getElementForSamlFile(String samlFileName) throws Exception {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(getSamlFile(samlFileName));
        return document.getDocumentElement();
    }

    private File getSamlFile(String samlFileName) {
        URI uri = null;
        try {
            uri = this.getClass().getResource(samlFileName).toURI();
        } catch (URISyntaxException e) {
            fail("Could not build URI for filepath. " + e.getMessage());
        }
        return new File(uri);
    }

}
