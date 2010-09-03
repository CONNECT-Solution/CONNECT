/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.assemblymanager;

/**
 *
 * @author kim
 */
public class InvalidTypeException extends Exception {

   /**
    * Creates a new instance of <code>InvalidTypeException</code> without detail message.
    */
   public InvalidTypeException() {
      super();
   }

   /**
    * Constructs an instance of <code>InvalidTypeException</code> with the specified detail message.
    * @param msg the detail message.
    */
   public InvalidTypeException(String msg) {
      super(msg);
   }
}
