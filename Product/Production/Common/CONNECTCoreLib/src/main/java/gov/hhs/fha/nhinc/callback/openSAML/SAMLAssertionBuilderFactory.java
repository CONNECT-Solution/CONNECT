/**
 * 
 */
package gov.hhs.fha.nhinc.callback.openSAML;

/**
 * @author bhumphrey
 *
 */
public interface SAMLAssertionBuilderFactory {

	public abstract SAMLAssertionBuilder getBuilder(final String confirmationMethod);

}