/**
 * 
 */
package gov.hhs.fha.nhinc.callback.cxf;

import gov.hhs.fha.nhinc.callback.SamlConstants;
import gov.hhs.fha.nhinc.callback.openSAML.CertificateManager;
import gov.hhs.fha.nhinc.callback.openSAML.CertificateManagerImpl;
import gov.hhs.fha.nhinc.callback.openSAML.HOKSAMLAssertionBuilder;
import gov.hhs.fha.nhinc.callback.openSAML.OpenSAML2ComponentBuilder;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.CeType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlAuthzDecisionStatementEvidenceType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;

import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.PhaseInterceptorChain;
import org.apache.ws.security.saml.ext.SAMLCallback;
import org.apache.ws.security.saml.ext.bean.ActionBean;
import org.apache.ws.security.saml.ext.bean.AttributeBean;
import org.apache.ws.security.saml.ext.bean.AttributeStatementBean;
import org.apache.ws.security.saml.ext.bean.AuthDecisionStatementBean;
import org.apache.ws.security.saml.ext.bean.AuthDecisionStatementBean.Decision;
import org.apache.ws.security.saml.ext.bean.AuthenticationStatementBean;
import org.apache.ws.security.saml.ext.bean.ConditionsBean;
import org.apache.ws.security.saml.ext.bean.KeyInfoBean;
import org.apache.ws.security.saml.ext.bean.KeyInfoBean.CERT_IDENTIFIER;
import org.apache.ws.security.saml.ext.bean.SubjectBean;
import org.apache.ws.security.saml.ext.bean.SubjectLocalityBean;
import org.apache.ws.security.saml.ext.builder.SAML2Constants;
import org.joda.time.DateTime;
import org.joda.time.ReadableDuration;
import org.joda.time.Seconds;
import org.opensaml.common.SAMLVersion;
import org.opensaml.saml2.core.AttributeStatement;
import org.opensaml.saml2.core.Evidence;
import org.opensaml.xml.XMLObject;
import org.w3c.dom.Element;

import com.sun.xml.wss.saml.internal.saml20.jaxb20.DecisionType;

/**
 * @author mweaver
 * 
 */
public class CXFSAMLCallbackHandler implements CallbackHandler {

    private static Log log = LogFactory.getLog(CXFSAMLCallbackHandler.class);

    public static final String HOK_CONFIRM = "urn:oasis:names:tc:SAML:2.0:cm:holder-of-key";

    public CXFSAMLCallbackHandler() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.security.auth.callback.CallbackHandler#handle(javax.security.auth.callback.Callback[])
     */
    @Override
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        log.debug("CXFSAMLCallbackHandler.handle begin");
        for (Callback callback : callbacks) {
            if (callback instanceof SAMLCallback) {

                try {

                    Message message = PhaseInterceptorChain.getCurrentMessage();

                    Object obj = message.get("assertion");

                    AssertionType custAssertion = null;
                    if (obj != null) {
                        custAssertion = (AssertionType) obj;
                    }

                    SAMLCallback oSAMLCallback = (SAMLCallback) callback;

                    oSAMLCallback.setSamlVersion(SAMLVersion.VERSION_20);

                    SubjectBean subjectBean = getSubjectBean(custAssertion);

                    ConditionsBean conditionsBean = new ConditionsBean();
                    int validityPeriod = 3600;
                    conditionsBean.setTokenPeriodMinutes(validityPeriod / 60);
                    oSAMLCallback.setConditions(conditionsBean);

                    oSAMLCallback.setSubject(subjectBean);

                    oSAMLCallback.setAuthenticationStatementData(getAuthenticationStatementData(custAssertion));

                    oSAMLCallback.setAttributeStatementData(getAttributeStatementData(custAssertion));

                    String issuer = "CN=SAML User,OU=SU,O=SAML User,L=Los Angeles,ST=CA,C=US";
                    if (custAssertion.getSamlIssuer() != null) {
                        issuer = custAssertion.getSamlIssuer().getIssuer();
                    }

                    oSAMLCallback.setAuthDecisionStatementData(getAuthDecisionStatementData(custAssertion, issuer,
                            validityPeriod));
                    oSAMLCallback.setIssuer(issuer);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        log.debug("CXFSAMLCallbackHandler.handle end");
    }

    /**
     * @param custAssertion
     * @return
     */
    private SubjectBean getSubjectBean(AssertionType custAssertion) {
        SubjectBean subj = new SubjectBean();
        subj.setSubjectConfirmationMethod(SAML2Constants.CONF_HOLDER_KEY);
        subj.setKeyInfo(getKeyInfoBean());
        subj.setSubjectNameIDFormat("urn:oasis:names:tc:SAML:1.1:nameid-format:X509SubjectName");
        subj.setSubjectName("UID=" + custAssertion.getUserInfo().getUserName());
        return subj;
    }

    protected final KeyInfoBean getKeyInfoBean() {

        KeyInfoBean keyInfo = new KeyInfoBean();
        CertificateManager cm = CertificateManagerImpl.getInstance();

        X509Certificate certificate = null;
        try {
            certificate = cm.getDefaultCertificate();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        keyInfo.setPublicKey(certificate.getPublicKey());
        keyInfo.setCertIdentifer(CERT_IDENTIFIER.KEY_VALUE);

        return keyInfo;
    }

    /**
     * @param assertionIn
     * @param issuer
     * @param validityPeriod
     * @return
     */
    private List<AuthDecisionStatementBean> getAuthDecisionStatementData(AssertionType assertionIn, String issuer,
            int validityPeriod) {

        if (assertionIn.getSamlAuthzDecisionStatement() == null) {
            return null;
        }

        List<AuthDecisionStatementBean> authDecisionStatementBeans = new ArrayList<AuthDecisionStatementBean>();
        AuthDecisionStatementBean authDecisionStatementBean = new AuthDecisionStatementBean();

        if (assertionIn.getSamlAuthzDecisionStatement().getDecision().equals(DecisionType.PERMIT)) {
            authDecisionStatementBean.setDecision(Decision.PERMIT);
        } else if (assertionIn.getSamlAuthzDecisionStatement().getDecision().equals(DecisionType.DENY)) {
            authDecisionStatementBean.setDecision(Decision.DENY);
        } else if (assertionIn.getSamlAuthzDecisionStatement().getDecision().equals(DecisionType.INDETERMINATE)) {
            authDecisionStatementBean.setDecision(Decision.INDETERMINATE);
        } else {
            authDecisionStatementBean.setDecision(Decision.PERMIT);
        }

        authDecisionStatementBean.setResource(assertionIn.getSamlAuthzDecisionStatement().getResource());

        // Build Action
        List<ActionBean> actionBeans = new ArrayList<ActionBean>();
        ActionBean actionBean = new ActionBean();
        actionBean.setActionNamespace("urn:oasis:names:tc:SAML:1.0:action:rwedc");
        actionBean.setContents("Execute");
        actionBeans.add(actionBean);
        authDecisionStatementBean.setActions(actionBeans);

        SamlAuthzDecisionStatementEvidenceType evidenceType = assertionIn.getSamlAuthzDecisionStatement().getEvidence();

        String evAssertionID = evidenceType.getAssertion().getId();
        DateTime issueInstant = new DateTime(evidenceType.getAssertion().getIssueInstant());
        String format = evidenceType.getAssertion().getIssuerFormat();
        List<AttributeStatement> statements = HOKSAMLAssertionBuilder.createEvidenceStatements(assertionIn
                .getSamlAuthzDecisionStatement().getEvidence().getAssertion().getAccessConsentPolicy(), assertionIn
                .getSamlAuthzDecisionStatement().getEvidence().getAssertion().getInstanceAccessConsentPolicy());
        DateTime beginValidTime = new DateTime();
        DateTime endValidTime = new DateTime();
        endValidTime.plus(Seconds.seconds(validityPeriod));

        // Build Evidence
        // assertionIn.get
        Evidence evidence = HOKSAMLAssertionBuilder.buildEvidence(evAssertionID, issueInstant, format, beginValidTime,
                endValidTime, issuer, statements);
        //

        authDecisionStatementBean.setEvidence(evidence);

        authDecisionStatementBeans.add(authDecisionStatementBean);
        return authDecisionStatementBeans;
    }

    /**
     * @return
     */
    private List<AttributeStatementBean> getAttributeStatementData(AssertionType assertionIn) {
        UserType userInfo = assertionIn.getUserInfo();
        ArrayList<AttributeBean> samlAttributes = new ArrayList<AttributeBean>();

        samlAttributes.add(new AttributeBean(null, SamlConstants.PATIENT_ID_ATTR, assertionIn.getUniquePatientId()));

        samlAttributes.add(new AttributeBean(null, SamlConstants.USER_ORG_ATTR, Arrays.asList(userInfo.getOrg()
                .getName())));

        samlAttributes.add(new AttributeBean(null, SamlConstants.USER_ORG_ID_ATTR, Arrays.asList(userInfo.getOrg()
                .getHomeCommunityId())));

        samlAttributes.add(new AttributeBean(null, SamlConstants.HOME_COM_ID_ATTR, Arrays.asList(assertionIn
                .getHomeCommunity().getHomeCommunityId())));

        samlAttributes.add(getUserRoleAttribute(userInfo));

        // NPI

        samlAttributes.add(getPurposeOfUseAttribute(assertionIn.getPurposeOfDisclosureCoded()));

        AttributeStatementBean attributeStatementBean = new AttributeStatementBean();
        attributeStatementBean.setSamlAttributes(samlAttributes);

        List<AttributeStatementBean> attributeStatements = new ArrayList<AttributeStatementBean>();
        attributeStatements.add(attributeStatementBean);
        return attributeStatements;
    }

    /**
     * @param purposeOfDisclosureCoded
     * @return
     */
    private AttributeBean getPurposeOfUseAttribute(CeType purposeOfDisclosureCoded) {
        AttributeBean attributeBean = new AttributeBean();

        attributeBean.setQualifiedName(SamlConstants.PURPOSE_ROLE_ATTR);

        List<XMLObject> attributeValues = new ArrayList<XMLObject>();

        attributeValues.add(OpenSAML2ComponentBuilder.getInstance().createHL7Attribute(
                purposeOfDisclosureCoded.getCode(), purposeOfDisclosureCoded.getCodeSystem(),
                purposeOfDisclosureCoded.getCodeSystemName(), purposeOfDisclosureCoded.getDisplayName()));
        attributeBean.setCustomAttributeValues(attributeValues);
        return attributeBean;
    }

    private AttributeBean getUserRoleAttribute(UserType userInfo) {
        AttributeBean attributeBean = new AttributeBean();

        attributeBean.setQualifiedName(SamlConstants.USER_ROLE_ATTR);

        List<XMLObject> attributeValues = new ArrayList<XMLObject>();

        attributeValues.add(OpenSAML2ComponentBuilder.getInstance().createHL7Attribute(
                userInfo.getRoleCoded().getCode(), userInfo.getRoleCoded().getCodeSystem(),
                userInfo.getRoleCoded().getCodeSystemName(), userInfo.getRoleCoded().getDisplayName()));

        attributeBean.setCustomAttributeValues(attributeValues);
        return attributeBean;

    }

    /**
     * @return
     */
    private List<AuthenticationStatementBean> getAuthenticationStatementData(AssertionType assertionIn) {
        List<AuthenticationStatementBean> authenticationStatementBeans = new ArrayList<AuthenticationStatementBean>();
        AuthenticationStatementBean authBean = new AuthenticationStatementBean();
        authBean.setAuthenticationMethod(assertionIn.getSamlAuthnStatement().getAuthContextClassRef());

        String authInstant = assertionIn.getSamlAuthnStatement().getAuthInstant();
        DateTime authInstantDateTime = new DateTime(authInstant);

        authBean.setAuthenticationInstant(authInstantDateTime);
        authBean.setSessionIndex(assertionIn.getSamlAuthnStatement().getSessionIndex());

        SubjectLocalityBean subjectLocalityBean = null;
        // Subject Locality is optional
        if (assertionIn.getSamlAuthnStatement().getSubjectLocalityAddress() != null) {
            subjectLocalityBean = new SubjectLocalityBean();
            subjectLocalityBean.setIpAddress(assertionIn.getSamlAuthnStatement().getSubjectLocalityAddress());
        }

        if (assertionIn.getSamlAuthnStatement().getSubjectLocalityDNSName() != null) {
            if (subjectLocalityBean == null) {
                subjectLocalityBean = new SubjectLocalityBean();
            }
            subjectLocalityBean.setDnsAddress(assertionIn.getSamlAuthnStatement().getSubjectLocalityDNSName());
        }

        if (subjectLocalityBean != null) {
            authBean.setSubjectLocality(subjectLocalityBean);
        }

        authenticationStatementBeans.add(authBean);

        return authenticationStatementBeans;
    }
}
