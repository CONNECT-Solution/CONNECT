/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
//
// Non-Export Controlled Information
//
//####################################################################
//## The MIT License
//##
//## Copyright (c) 2010 Harris Corporation
//##
//## Permission is hereby granted, free of charge, to any person
//## obtaining a copy of this software and associated documentation
//## files (the "Software"), to deal in the Software without
//## restriction, including without limitation the rights to use,
//## copy, modify, merge, publish, distribute, sublicense, and/or sell
//## copies of the Software, and to permit persons to whom the
//## Software is furnished to do so, subject to the following conditions:
//##
//## The above copyright notice and this permission notice shall be
//## included in all copies or substantial portions of the Software.
//##
//## THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
//## EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
//## OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
//## NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
//## HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
//## WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
//## FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
//## OTHER DEALINGS IN THE SOFTWARE.
//##
//####################################################################
//********************************************************************
// FILE: HandlerPrototype.javas
//
// Copyright (C) 2010 Harris Corporation. All rights reserved.
//
// CLASSIFICATION: Unclassified
//
// DESCRIPTION: HandlerPrototype.java 
//              
//
// LIMITATIONS: None
//
// SOFTWARE HISTORY:
//
//> Feb 24, 2010 PTR#  - R. Robinson
// Initial Coding.
//<
//********************************************************************
package gov.hhs.fha.nhinc.lift.proxy.server;

import java.io.IOException;

import gov.hhs.fha.nhinc.lift.proxy.util.ProtocolWrapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class HandlerPrototype implements Cloneable, Runnable {

    protected Log log = null;
    private ProtocolWrapper wrapper;

    public HandlerPrototype() {
        log = createLogger();
    }

    protected Log createLogger() {
        if (log == null) {
            log = LogFactory.getLog(getClass());
        }
        return log;
    }

    /**
     * Template method that first makes a subclass implementation handshake
     * with the socket and then create a new instance of the object to
     * return to the caller of clone.
     * @param socket
     * @return
     * @throws IOException
     */
    public final HandlerPrototype clone(ProtocolWrapper wrapper) throws IOException {
        HandlerPrototype inst = createInstance(wrapper);
        boolean success = inst.performHandshake();

        if (success) {
            return inst;
        } else {
            return null;
        }
    }

    /**
     * Returns a new instance of this object and uses the given socket.
     * @param socket
     * @return
     */
    protected abstract HandlerPrototype createInstance(ProtocolWrapper wrapper);

    /**
     * Implementation specific, must know how to handshake with the client.
     * This method has nothing to do with SSL handshaking, it is in place for
     * any additional handshaking between a proxy client and a proxy server.
     *
     * NOTE: may want to consider moving this to be handled by the within
     * the Handler somewhere rather than in the Server.  Current, I think this
     * functionality is Server implementation specific but that thinking may be
     * wrong.
     * @param socket
     * @return
     * @throws IOException
     */
    protected abstract boolean performHandshake() throws IOException;

    protected ProtocolWrapper getWrapper() {
        return wrapper;
    }

    protected void setWrapper(ProtocolWrapper wrapper) {
        this.wrapper = wrapper;
    }

    public void sendLine(String mess) throws IOException {

        this.getWrapper().sendLine(mess);
        log.debug("Sending message: " + mess);
    }

    /**
     * Helper method in place to read line from the output stream of the
     * socket.
     * @return
     * @throws IOException
     */
    public String readLine() throws IOException {
        String mess = this.getWrapper().readLine();
        log.debug("Received message: " + mess);
        return mess;
    }
}
