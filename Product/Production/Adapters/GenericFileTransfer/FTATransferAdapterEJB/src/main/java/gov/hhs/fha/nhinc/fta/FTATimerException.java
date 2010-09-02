/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.fta;

/**
 * This exception is thrown when an error occurs accessing properties.
 *
 * @author Les Westberg
 */
public class FTATimerException extends Exception
{
    private static final long serialVersionUID = -4399592211810514874L;

    /**
     * Default constructor.
     */
    public FTATimerException()
    {
        super();
    }

    /**
     * Constructor with an envloping exception.
     *
     * @param e  The exception that caused this one.
     */
    public FTATimerException(Exception e)
    {
        super(e);
    }

    /**
     * Constructor with the given exception and message.
     *
     * @param sMessage The message to place in the exception.
     * @param e The exception that triggered this one.
     */
    public FTATimerException(String sMessage, Exception e)
    {
        super(sMessage, e);
    }

    /**
     * Constructor with a given message.
     *
     * @param sMessage The message for the exception.
     */
    public FTATimerException(String sMessage)
    {
        super(sMessage);
    }

}
