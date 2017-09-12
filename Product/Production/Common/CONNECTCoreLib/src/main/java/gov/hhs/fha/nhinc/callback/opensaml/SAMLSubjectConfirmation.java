/**
 *
 */
package gov.hhs.fha.nhinc.callback.opensaml;

import org.apache.commons.lang.builder.HashCodeBuilder;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.wss4j.common.saml.bean.SubjectConfirmationDataBean;

/**
 * @author mpnguyen
 *
 */
public class SAMLSubjectConfirmation extends SubjectConfirmationDataBean {
    private String method;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

}
