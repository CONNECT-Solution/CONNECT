/**
 * 
 */
package gov.hhs.fha.nhinc.callback.cxf;

import gov.hhs.fha.nhinc.callback.openSAML.CallbackMapProperties;
import gov.hhs.fha.nhinc.callback.openSAML.CertificateManager;
import gov.hhs.fha.nhinc.callback.openSAML.CertificateManagerImpl;
import gov.hhs.fha.nhinc.callback.openSAML.SAMLAssertionBuilder;
import gov.hhs.fha.nhinc.callback.openSAML.SAMLAssertionBuilderFactory;
import gov.hhs.fha.nhinc.callback.openSAML.SAMLAssertionBuilderFactoryImpl;

import java.io.IOException;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ws.security.saml.ext.SAMLCallback;
import org.apache.ws.security.saml.ext.bean.AttributeStatementBean;
import org.apache.ws.security.saml.ext.bean.AuthDecisionStatementBean;
import org.apache.ws.security.saml.ext.bean.AuthenticationStatementBean;
import org.apache.ws.security.saml.ext.bean.ConditionsBean;
import org.apache.ws.security.saml.ext.bean.KeyInfoBean;
import org.apache.ws.security.saml.ext.bean.KeyInfoBean.CERT_IDENTIFIER;
import org.apache.ws.security.saml.ext.bean.SubjectBean;
import org.apache.ws.security.saml.ext.builder.SAML2Constants;
import org.opensaml.common.SAMLVersion;
import org.w3c.dom.Element;

/**
 * @author mweaver
 * 
 */
public class CXFSAMLCallbackHandler implements CallbackHandler {

    private static Log log = LogFactory.getLog(CXFSAMLCallbackHandler.class);

    public static final String HOK_CONFIRM = "urn:oasis:names:tc:SAML:2.0:cm:holder-of-key";
    private SAMLAssertionBuilderFactory assertionBuilderFactory;

    public CXFSAMLCallbackHandler() {
        assertionBuilderFactory = new SAMLAssertionBuilderFactoryImpl();
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
                    SAMLCallback oSAMLCallback = (SAMLCallback) callback;

                    oSAMLCallback.setSamlVersion(SAMLVersion.VERSION_20);

                    SubjectBean subjectBean = getSubjectBean();

                    ConditionsBean conditionsBean = new ConditionsBean();
                    int validityPeriod = 3600;
                    conditionsBean.setTokenPeriodMinutes(validityPeriod / 60);
                    oSAMLCallback.setConditions(conditionsBean);

                    oSAMLCallback.setSubject(subjectBean);

                    oSAMLCallback.setAuthenticationStatementData(getAuthenticationStatementData());

                    oSAMLCallback.setAttributeStatementData(getAttributeStatementData());

                    String issuer = "default issuer name";
                    Object custAssertion = null;
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
     * @return
     */
    private SubjectBean getSubjectBean() {
        SubjectBean subj = new SubjectBean();
        subj.setSubjectConfirmationMethod(SAML2Constants.CONF_HOLDER_KEY);
        subj.setKeyInfo(getKeyInfoBean());
        subj.setSubjectName("CN=Default SAML User,OU=SU,O=SAML Org,L=Fairfax,ST=VA,C=US");
        subj.setSubjectNameIDFormat("urn:oasis:names:tc:SAML:1.1:nameid-format:X509SubjectName");
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
    private List<AuthDecisionStatementBean> getAuthDecisionStatementData(Object custAssertion, String issuer,
            int validityPeriod) {
        return new ArrayList<AuthDecisionStatementBean>();
    }

    /**
     * @return
     */
    private List<AttributeStatementBean> getAttributeStatementData() {
        return new ArrayList<AttributeStatementBean>();
    }

    /**
     * @return
     */
    private List<AuthenticationStatementBean> getAuthenticationStatementData() {
        return new ArrayList<AuthenticationStatementBean>();
    }
}
