package gov.hhs.fha.nhinc.callback.openSAML;

public class SAMLComponentBuilderException extends Exception {

    public SAMLComponentBuilderException(String message) {
        super(message);
    }

    public SAMLComponentBuilderException(Throwable throwable) {
    	super(throwable);
    }

    public SAMLComponentBuilderException(String message, Throwable throwable) {
    	super(message, throwable);
    }
}