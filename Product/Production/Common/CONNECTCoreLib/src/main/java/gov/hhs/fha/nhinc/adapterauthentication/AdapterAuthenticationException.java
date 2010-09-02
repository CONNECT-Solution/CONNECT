/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.adapterauthentication;

/**
 * This exception is thrown when an error occurs within the AdapterAuthentication.
 */
public class AdapterAuthenticationException extends Exception
{   
    /**
     * Default constructor.
     */
    public AdapterAuthenticationException()
    {
        super();
    }
    
    /**
     * Constructor with an envloping exception.
     * 
     * @param ex  The exception that caused this one.
     */
    public AdapterAuthenticationException(Exception ex)
    {
        super(ex);
    }

    /**
     * Constructor with the given exception and message.
     * 
     * @param message The message to place in the exception.
     * @param ex The exception that triggered this one.
     */
    public AdapterAuthenticationException(String message, Exception ex)
    {
        super(message, ex);
    }

    /**
     * Constructor with a given message.
     * 
     * @param message The message for the exception.
     */
    public AdapterAuthenticationException(String message)
    {
        super(message);
    }
    
}
