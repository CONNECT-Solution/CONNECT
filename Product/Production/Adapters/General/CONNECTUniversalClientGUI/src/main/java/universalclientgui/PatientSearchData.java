/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
 * All rights reserved. 
 * Copyright (c) 2011, Conemaugh Valley Memorial Hospital
 
 * This source is subject to the Conemaugh public license.  Please see the
 * license.txt file for more information.
 *
 * All other rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met: 
 *     * Redistributions of source code must retain the above 
 *       copyright notice, this list of conditions and the following disclaimer. 
 *     * Redistributions in binary form must reproduce the above copyright 
 *       notice, this list of conditions and the following disclaimer in the documentation 
 *       and/or other materials provided with the distribution. 
 *     * Neither the name of the United States Government nor the 
 *       names of its contributors may be used to endorse or promote products 
 *       derived from this software without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY 
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */
package universalclientgui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.JAXBElement;
import org.hl7.v3.EnExplicitFamily;
import org.hl7.v3.EnExplicitGiven;
import org.hl7.v3.PNExplicit;

public class PatientSearchData implements Serializable
{

    private ArrayList<Object> columns;
    private String assigningAuthorityID;

    public PatientSearchData()
    {
    }

    public PatientSearchData(List<Object> cols) {
            columns = new ArrayList<Object>(cols);
    }

    public ArrayList<Object> getColumns() {
        return columns;
    }

    public String getAssigningAuthorityID() {
        return assigningAuthorityID;
    }

    public void setAssigningAuthorityID(String assigningAuthorityID) {
        this.assigningAuthorityID = assigningAuthorityID;
    }

    public void setColumns(ArrayList<Object> columns) {
        this.columns = columns;
    }

    //patient names
    public Object getNames() {
        return columns.get(0);
    }

    public void setNames(Object col) {
        columns.set(0, col);
    }

    //patient Address
    public Object getAddress() {

        return columns.get(1);
    }

    public void setAddress(Object col) {

        columns.set(1,col);
    }

  /*  public Object getLastName() {
        return columns.get(0);
    }

    public void setLastName(Object col) {
        columns.set(0, col);
    }

    public Object getFirstName() {
        return columns.get(1);
    }

    public void setFirstName(Object col) {
        columns.set(1, col);
    }
*/
    public Object getPatientId() {
        return columns.get(2);
    }

    public void setPatientId(Object col) {
        columns.set(2, col);
    }

    public Object getSsn() {
        return columns.get(3);
    }

    public void setSsn(Object col) {
        columns.set(3, col);
    }

    public Object getDisplaySsn() {
        return columns.get(9);
    }

    public void setDisplaySsn(Object col) {
        columns.set(9, col);
    }

    public Object getDob() {
        return columns.get(4);
    }

    public void setDob(Object col) {
        columns.set(4, col);
    }

    public Object getGender() {
        return columns.get(5);
    }

    public void setGender(Object col) {
        columns.set(5, col);
    }

    //phone number
    public Object getPhoneNumbers() {

        return columns.get(6);
    }

    public void setPhoneNumbers(Object col)
    {
        columns.set(6, col);
    }

    public Object getLastName() {

       return columns.get(7);
    }

    public void setLastName(Object col) {

        columns.set(7, col);
    }

    public Object getFirstName() {

        return columns.get(8);
    }

    public void setFirstName(Object col) {
        columns.set(8, col);
    }

    //hold PRPAMT201301UV02Patient value 
    //public Object getPatientObj() {
         
     //   return columns.get(9);
    //}
    
    //public Object setPatientObj(Object col) {
        
    //    return columns.set(9, col);
   // }

}
