/**
 *
 */
package gov.hhs.fha.nhinc.callback.openSAML;

import org.joda.time.DateTime;
import org.opensaml.saml2.core.AuthnStatement;

/**
 * @author bhumphrey
 *
 */
interface SAMLCompontentBuilder {


    AuthnStatement createAuthenicationStatements(String cntxCls, String sessionIndex, DateTime authInstant,
            String inetAddr, String dnsName);


}
