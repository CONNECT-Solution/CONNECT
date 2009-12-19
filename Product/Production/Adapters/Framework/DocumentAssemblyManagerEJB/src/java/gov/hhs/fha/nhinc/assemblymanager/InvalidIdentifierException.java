/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.assemblymanager;

/**
 *
 * @author kim
 */
public class InvalidIdentifierException extends Exception {

   /**
    * Creates a new instance of <code>InvalidIdentifierException</code> without detail message.
    */
   public InvalidIdentifierException() {
      super();
   }

   /**
    * Constructs an instance of <code>InvalidIdentifierException</code> with the specified detail message.
    * @param msg the detail message.
    */
   public InvalidIdentifierException(String msg) {
      super(msg);
   }
}
