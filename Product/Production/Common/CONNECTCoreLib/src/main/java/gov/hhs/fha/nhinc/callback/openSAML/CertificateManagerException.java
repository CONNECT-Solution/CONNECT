package gov.hhs.fha.nhinc.callback.openSAML;

public class CertificateManagerException extends Exception {

    public CertificateManagerException(String message) {
        super(message);
    }

    public CertificateManagerException(Throwable throwable) {
    	super(throwable);
    }

    public CertificateManagerException(String message, Throwable throwable) {
    	super(message, throwable);
    }
}