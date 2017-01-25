package gov.hhs.fha.nhinc.callback.openSAML;

public class SAMLAssertionBuilderException extends Exception {

    public SAMLAssertionBuilderException(String message) {
        super(message);
    }

    public SAMLAssertionBuilderException(Throwable throwable) {
    	super(throwable);
    }

    public SAMLAssertionBuilderException(String message, Throwable throwable) {
    	super(message, throwable);
    }
}