/**
 * 
 */
package gov.hhs.fha.nhinc.callback.cxf;

import java.util.Collection;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.cxf.helpers.CastUtils;
import org.apache.cxf.message.Message;
import org.apache.cxf.security.transport.TLSSessionInfo;
import org.apache.cxf.ws.policy.AssertionInfo;
import org.apache.cxf.ws.policy.AssertionInfoMap;
import org.apache.cxf.ws.security.policy.SP12Constants;
import org.apache.cxf.ws.security.policy.SPConstants;
import org.apache.cxf.ws.security.policy.model.SamlToken;
import org.apache.cxf.ws.security.policy.model.SupportingToken;
import org.apache.cxf.ws.security.policy.model.Token;
import org.apache.cxf.ws.security.wss4j.policyvalidators.AbstractTokenPolicyValidator;
import org.apache.cxf.ws.security.wss4j.policyvalidators.SupportingTokenPolicyValidator;
import org.apache.ws.security.WSDataRef;
import org.apache.ws.security.WSSecurityEngine;
import org.apache.ws.security.WSSecurityEngineResult;
import org.w3c.dom.Element;

/**
 * Validate an EndorsingSupportingToken policy. 
 */
public class CONNECTEndorsingTokenPolicyValidator extends AbstractTokenPolicyValidator implements SupportingTokenPolicyValidator /*extends AbstractSupportingTokenPolicyValidator*/ {
    
    private Message message;
    private List<WSSecurityEngineResult> results;
    private List<WSSecurityEngineResult> signedResults;
    private List<WSSecurityEngineResult> encryptedResults;
    private List<WSSecurityEngineResult> utResults;
    private List<WSSecurityEngineResult> samlResults;
    private Element timestamp;
    private boolean signed;
    private boolean encrypted;
    private boolean derived;
    private boolean endorsed;
    
    public CONNECTEndorsingTokenPolicyValidator() {
        setEndorsed(true);
    }
    
    /**
     * Set the list of SAMLToken results
     */
    public void setSAMLTokenResults(List<WSSecurityEngineResult> samlResultsList) {
        samlResults = samlResultsList;
    }
    
    /**
     * Set the Timestamp element
     */
    public void setTimestampElement(Element timestampElement) {
        timestamp = timestampElement;
    }
    
    public void setMessage(Message msg) {
        message = msg;
    }
    
    public void setResults(List<WSSecurityEngineResult> results) {
        this.results = results;
    }
    
    public void setSignedResults(List<WSSecurityEngineResult> signedResults) {
        this.signedResults = signedResults;
    }
    
    public void setEncryptedResults(List<WSSecurityEngineResult> encryptedResults) {
        this.encryptedResults = encryptedResults;
    }
    
    public void setSigned(boolean signed) {
        this.signed = signed;
    }
    
    public void setEncrypted(boolean encrypted) {
        this.encrypted = encrypted;
    }
    
    public void setDerived(boolean derived) {
        this.derived = derived;
    }
    
    public void setEndorsed(boolean endorsed) {
        this.endorsed = endorsed;
    }
    
    
    public boolean validatePolicy(
        AssertionInfoMap aim, 
        Message message,
        List<WSSecurityEngineResult> results,
        List<WSSecurityEngineResult> signedResults,
        List<WSSecurityEngineResult> encryptedResults
    ) {
        Collection<AssertionInfo> ais = aim.get(SP12Constants.ENDORSING_SUPPORTING_TOKENS);
        if (ais == null || ais.isEmpty()) {                       
            return true;
        }
        
        setMessage(message);
        setResults(results);
        setSignedResults(signedResults);
        setEncryptedResults(encryptedResults);

        for (AssertionInfo ai : ais) {
            SupportingToken binding = (SupportingToken)ai.getAssertion();
            if (SPConstants.SupportTokenType.SUPPORTING_TOKEN_ENDORSING != binding.getTokenType()) {
                continue;
            }
            ai.setAsserted(true);

            List<Token> tokens = binding.getTokens();
            for (Token token : tokens) {
                if (!isTokenRequired(token, message)) {
                    continue;
                }
                
                boolean derived = token.isDerivedKeys();
                setDerived(derived);
                boolean processingFailed = false;
                if (token instanceof SamlToken) {
                    if (!processSAMLTokens()) {
                        processingFailed = true;
                    }
                } else {
                    processingFailed = true;
                }
                
                if (processingFailed) {
                    ai.setNotAsserted(
                        "The received token does not match the endorsing supporting token requirement"
                    );
                    return false;
                }
            }
        }
        
        return true;
    }
    
    /**
     * Process SAML Tokens. Only signed results are supported.
     */
    protected boolean processSAMLTokens() {
        if (samlResults.isEmpty()) {
            return false;
        }
        
        if (signed && !areTokensSigned(samlResults)) {
            return false;
        }
        if (encrypted && !areTokensEncrypted(samlResults)) {
            return false;
        }
        if (endorsed && !checkEndorsed(samlResults)) {
            return false;
        }
        return true;
    }
    
    private boolean isTLSInUse() {
        // See whether TLS is in use or not
        TLSSessionInfo tlsInfo = message.get(TLSSessionInfo.class);
        if (tlsInfo != null) {
            return true;
        }
        return false;
    }    

    /**
     * Return true if a list of tokens were signed, false otherwise.
     */
    private boolean areTokensSigned(List<WSSecurityEngineResult> tokens) {
        if (!isTLSInUse()) {
            for (WSSecurityEngineResult wser : tokens) {
                Element tokenElement = (Element)wser.get(WSSecurityEngineResult.TAG_TOKEN_ELEMENT);
                if (!isTokenSigned(tokenElement)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    /**
     * Return true if a list of tokens were encrypted, false otherwise.
     */
    private boolean areTokensEncrypted(List<WSSecurityEngineResult> tokens) {
        if (!isTLSInUse()) {
            for (WSSecurityEngineResult wser : tokens) {
                Element tokenElement = (Element)wser.get(WSSecurityEngineResult.TAG_TOKEN_ELEMENT);
                if (!isTokenEncrypted(tokenElement)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Check the endorsing supporting token policy. If we're using the Transport Binding then
     * check that the Timestamp is signed. Otherwise, check that the signature is signed.
     * @return true if the endorsed supporting token policy is correct
     */
    private boolean checkEndorsed(List<WSSecurityEngineResult> tokenResults) {
        if (isTLSInUse()) {
            return checkTimestampIsSigned(tokenResults);
        }
        return checkSignatureIsSigned(tokenResults);
    }

    /**
     * Return true if a token was signed, false otherwise.
     */
    private boolean isTokenSigned(Element token) {
        for (WSSecurityEngineResult signedResult : signedResults) {
            List<WSDataRef> dataRefs = 
                CastUtils.cast((List<?>)signedResult.get(WSSecurityEngineResult.TAG_DATA_REF_URIS));
            for (WSDataRef dataRef : dataRefs) {
                if (token == dataRef.getProtectedElement()) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Return true if a token was encrypted, false otherwise.
     */
    private boolean isTokenEncrypted(Element token) {
        for (WSSecurityEngineResult signedResult : encryptedResults) {
            List<WSDataRef> dataRefs = 
                CastUtils.cast((List<?>)signedResult.get(WSSecurityEngineResult.TAG_DATA_REF_URIS));
            if (dataRefs == null) {
                return false;
            }
            for (WSDataRef dataRef : dataRefs) {
                if (token == dataRef.getProtectedElement()) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Return true if the Timestamp is signed by one of the token results
     * @param tokenResults A list of WSSecurityEngineResults corresponding to tokens
     * @return true if the Timestamp is signed
     */
    private boolean checkTimestampIsSigned(List<WSSecurityEngineResult> tokenResults) {
        for (WSSecurityEngineResult signedResult : signedResults) {
            List<WSDataRef> sl =
                CastUtils.cast((List<?>)signedResult.get(
                    WSSecurityEngineResult.TAG_DATA_REF_URIS
                ));
            if (sl != null) {
                for (WSDataRef dataRef : sl) {
                    if (timestamp == dataRef.getProtectedElement()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    /**
     * Return true if the Signature is itself signed by one of the token results
     * @param tokenResults A list of WSSecurityEngineResults corresponding to tokens
     * @return true if the Signature is itself signed
     */
    private boolean checkSignatureIsSigned(List<WSSecurityEngineResult> tokenResults) {
        for (WSSecurityEngineResult signedResult : signedResults) {
            List<WSDataRef> sl =
                CastUtils.cast((List<?>)signedResult.get(
                    WSSecurityEngineResult.TAG_DATA_REF_URIS
                ));
            if (sl != null && sl.size() == 1) {
                for (WSDataRef dataRef : sl) {
                    QName signedQName = dataRef.getName();
                    if (WSSecurityEngine.SIGNATURE.equals(signedQName)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /* (non-Javadoc)
     * @see org.apache.cxf.ws.security.wss4j.policyvalidators.SupportingTokenPolicyValidator#setUsernameTokenResults(java.util.List, boolean)
     */
    @Override
    public void setUsernameTokenResults(List<WSSecurityEngineResult> utResultsList, boolean valUsernameToken) {
        // TODO Auto-generated method stub
        
    }
    
}
