/**
 *
 */
package gov.hhs.fha.nhinc.callback.openSAML;

/**
 * @author bhumphrey
 *
 */
public interface SAMLAssertionBuilderFactory {

    public static final String HOK_ASSERTION_TYPE = "HOK-Assertion";
    public static final String SV_ASSERTION_TYPE = "SV-Assertion";

    /**
     * @param confirmationMethod the confirmation method
     * @return the SAML Assertion Builder
     */
    SAMLAssertionBuilder getBuilder(final String confirmationMethod);

}