/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.assemblymanager.builder;

/**
 *
 * @author kim
 */
public class DocumentBuilderException extends Exception {

   /**
    * Creates a new instance of <code>DocumentBuilderException</code> without detail message.
    */
   public DocumentBuilderException() {
      super();
   }

   /**
    * Constructs an instance of <code>DocumentBuilderException</code> with the specified detail message.
    * @param msg the detail message.
    */
   public DocumentBuilderException(String msg) {
      super(msg);
   }
}
