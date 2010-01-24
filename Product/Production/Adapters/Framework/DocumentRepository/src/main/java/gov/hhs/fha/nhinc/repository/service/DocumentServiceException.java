package gov.hhs.fha.nhinc.repository.service;

/**
 * Exception class for DocumentService exceptions
 * 
 * @author Neil Webb
 */
public class DocumentServiceException extends Exception
{
    private static final long serialVersionUID = -4876112548123135498L;

    /**
     * Default constructor
     */
    public DocumentServiceException()
    {
        super();
    }

    /**
     * Create an exception with a message.
     *
     * @param message Exception message
     */
    public DocumentServiceException(String message)
    {
        super(message);
    }

    /**
     * Create an exception with a root cause.
     * 
     * @param cause Root cause
     */
    public DocumentServiceException(Throwable cause)
    {
        super(cause);
    }

    /**
     * Create an exception with a message and root cause.
     * 
     * @param message Exception message
     * @param cause Root cause
     */
    public DocumentServiceException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
