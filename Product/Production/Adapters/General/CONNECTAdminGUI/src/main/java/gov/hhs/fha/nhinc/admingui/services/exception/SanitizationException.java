/**
 * 
 */
package gov.hhs.fha.nhinc.admingui.services.exception;

/**
 * Exception throws when detects maliouos code in request header, cookies, and url path
 * @author mpnguyen
 *
 */
public class SanitizationException extends Exception{

    /**
     * 
     */
    private static final long serialVersionUID = 1743385749659136738L;
    /**
     * 
     */
    public SanitizationException() {
        super();
    }
    /**
     * @param message
     * @param cause
     */
    public SanitizationException(String message, Throwable cause) {
        super(message, cause);
       
    }
    /**
     * @param cause
     */
    public SanitizationException(Throwable cause) {
        super(cause);
    }
    /**
     * @param string
     */
    public SanitizationException(String message) {
        super(message);
    }

}
