/**
 * 
 */
package gov.hhs.fha.nhinc.callback.cxf;

import gov.hhs.fha.nhinc.callback.SamlConstants;
import gov.hhs.fha.nhinc.callback.openSAML.CertificateManager;
import gov.hhs.fha.nhinc.callback.openSAML.CertificateManagerImpl;
import gov.hhs.fha.nhinc.callback.openSAML.OpenSAML2ComponentBuilder;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;

import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.PhaseInterceptorChain;
import org.apache.ws.security.saml.ext.SAMLCallback;
import org.apache.ws.security.saml.ext.bean.AttributeBean;
import org.apache.ws.security.saml.ext.bean.AttributeStatementBean;
import org.apache.ws.security.saml.ext.bean.AuthDecisionStatementBean;
import org.apache.ws.security.saml.ext.bean.AuthenticationStatementBean;
import org.apache.ws.security.saml.ext.bean.ConditionsBean;
import org.apache.ws.security.saml.ext.bean.KeyInfoBean;
import org.apache.ws.security.saml.ext.bean.KeyInfoBean.CERT_IDENTIFIER;
import org.apache.ws.security.saml.ext.bean.SubjectBean;
import org.apache.ws.security.saml.ext.builder.SAML2Constants;
import org.opensaml.common.SAMLVersion;
import org.opensaml.xml.XMLObject;
import org.w3c.dom.Element;

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

                Element assertionElement;
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

                    oSAMLCallback.setAuthenticationStatementData(getAuthenticationStatementData());

                    oSAMLCallback.setAttributeStatementData(getAttributeStatementData(custAssertion));

                    String issuer = "default issuer name";
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
     * @param custAssertion
     * @param issuer
     * @param validityPeriod
     * @return
     */
    private List<AuthDecisionStatementBean> getAuthDecisionStatementData(AssertionType custAssertion, String issuer,
            int validityPeriod) {
        List<AuthDecisionStatementBean> authDecisionStatementBeans = new ArrayList<AuthDecisionStatementBean>();
        AuthDecisionStatementBean authDecisionStatementBean = new AuthDecisionStatementBean();

        return authDecisionStatementBeans;
    }

    /**
     * @return
     */
    private List<AttributeStatementBean> getAttributeStatementData(AssertionType assertionIn) {
        UserType userInfo = assertionIn.getUserInfo();
        ArrayList<AttributeBean> samlAttributes = new ArrayList<AttributeBean>();

        samlAttributes.add(getUserRoleAttribute(userInfo));
        AttributeStatementBean attributeStatementBean = new AttributeStatementBean();
        attributeStatementBean.setSamlAttributes(samlAttributes);

        List<AttributeStatementBean> attributeStatements = new ArrayList<AttributeStatementBean>();
        attributeStatements.add(attributeStatementBean);
        return attributeStatements;
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
    private List<AuthenticationStatementBean> getAuthenticationStatementData() {
        return new ArrayList<AuthenticationStatementBean>();
    }
}
