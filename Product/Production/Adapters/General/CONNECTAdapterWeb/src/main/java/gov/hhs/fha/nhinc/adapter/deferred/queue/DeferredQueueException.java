/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package gov.hhs.fha.nhinc.adapter.deferred.queue;

import java.io.Serializable;

/**
 * This exception is thrown when an error occurs during deferred queue
 * processing.
 *
 * @author richard.ettema
 */
public class DeferredQueueException extends Exception implements Serializable {

    private static final long serialVersionUID = -1029384875629102934L;

    /**
     * Default constructor.
     */
    public DeferredQueueException() {
        super();
    }

    /**
     * Constructor with an envloping exception.
     *
     * @param e  The exception that caused this one.
     */
    public DeferredQueueException(Exception e) {
        super(e);
    }

    /**
     * Constructor with the given exception and message.
     *
     * @param sMessage The message to place in the exception.
     * @param e The exception that triggered this one.
     */
    public DeferredQueueException(String sMessage, Exception e) {
        super(sMessage, e);
    }

    /**
     * Constructor with a given message.
     *
     * @param sMessage The message for the exception.
     */
    public DeferredQueueException(String sMessage) {
        super(sMessage);
    }
}
