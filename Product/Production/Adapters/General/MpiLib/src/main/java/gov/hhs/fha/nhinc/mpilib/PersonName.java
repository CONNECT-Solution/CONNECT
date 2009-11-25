/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.mpilib;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author rayj
 */
public class PersonName implements java.io.Serializable {
    private static Log log = LogFactory.getLog(PersonName.class);
    private String lastName = "";
    private String firstName = "";
    private String middleName = "";
    
    public PersonName() {
        log.debug("PersonName Initialized");
    }
    public PersonName(String lastname , String firstname) {
        this.setLastName(lastname);
        this.setFirstName(firstname);
    }
    public String getMiddleName()
    {
        if (middleName == null) {
            middleName = "";
        }
        return middleName;
    }
    public String getMiddleInitial()
    {
        if (middleName == null) {
            middleName = "";
        }
        return middleName.substring(0, 0);       
    }
    
    public void setMiddleName(String middle)
    {
        middleName = middle;
    }
    public String getLastName() {
        if (lastName == null) {
            lastName = "";
        }
        return lastName;
    }

    public void setLastName(String LastName) {
        this.lastName = LastName;
    }

    public String getFirstName() {
        if (firstName == null) {
            firstName = "";
        }
        return firstName;
    }

    public void setFirstName(String FirstName) {
        this.firstName = FirstName;
    }
}
