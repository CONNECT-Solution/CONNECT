package gov.hhs.fha.nhinc.properties;

public interface IPropertyAcessor {

	public abstract String getProperty(String sPropertyName)
			throws PropertyAccessException;

}
