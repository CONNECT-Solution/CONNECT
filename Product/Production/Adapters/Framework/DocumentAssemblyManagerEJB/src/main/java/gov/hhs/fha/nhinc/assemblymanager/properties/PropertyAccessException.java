/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * Copyright (c) 2011, Conemaugh Valley Memorial Hospital
 *
 * This source is subject to the Conemaugh public license.  Please see the
 * license.txt file for more information.
 *
 * All other rights reserved.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
 * CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT
 * SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.assemblymanager.properties;

/**
 * This exception is thrown when an error occurs accessing properties.
 * 
 * @author Les Westberg
 */
public class PropertyAccessException extends Exception
{
    /**
     * Default constructor.
     */
    public PropertyAccessException()
    {
        super();
    }
    
    /**
     * Constructor with an envloping exception.
     * 
     * @param e  The exception that caused this one.
     */
    public PropertyAccessException(Exception e)
    {
        super(e);
    }

    /**
     * Constructor with the given exception and message.
     * 
     * @param sMessage The message to place in the exception.
     * @param e The exception that triggered this one.
     */
    public PropertyAccessException(String sMessage, Exception e)
    {
        super(sMessage, e);
    }

    /**
     * Constructor with a given message.
     * 
     * @param sMessage The message for the exception.
     */
    public PropertyAccessException(String sMessage)
    {
        super(sMessage);
    }
    
}
