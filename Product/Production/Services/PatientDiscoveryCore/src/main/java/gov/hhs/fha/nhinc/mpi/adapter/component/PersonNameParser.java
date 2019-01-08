/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
 * All rights reserved.
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
package gov.hhs.fha.nhinc.mpi.adapter.component;

import gov.hhs.fha.nhinc.mpilib.PersonName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author rayj
 */
public class PersonNameParser {

    private static final Logger LOG = LoggerFactory.getLogger(PersonNameParser.class);

    /**
     *An enum for possible combinations of name order.
     *The only combinations are FIRST_LAST and LAST_FIRST. No other ordering is allowed.
     *
     */
    private enum NameOrder {

        FIRST_LAST, LAST_FIRST
    }

    /**
     * Method parses a name from a String, and returns the name as a PersonName object.
     * @param name the name to transform into a PersonName object.
     * @return name as a PersonName object
     */
    public static PersonName splitName(String name) {
        LOG.trace("Begin PersonNameParser.SplitName(String)");
        PersonName personName;
        if (name == null || name.contentEquals("")) {
            personName = new PersonName();
        } else {
            personName = splitNameByDelimiter(name, ",", NameOrder.LAST_FIRST);
            if (personName == null) {
                personName = splitNameByDelimiter(name, " ", NameOrder.FIRST_LAST);
            }

            if (personName == null) {
                    personName = new PersonName();
                    personName.setLastName(name);
            }
        }
        LOG.trace("End PersonNameParser.SplitName(String)");
        return personName;
    }

    private static PersonName splitNameByDelimiter(String name, String delimiter, NameOrder order) {
        LOG.trace("Begin PersonNameParser.SplitNameByDelimiter(String,String,nameorder)");
        PersonName personName = null;
        if (name.contains(delimiter)) {
            String[] nameparts = name.split(delimiter);
            if (nameparts.length == 0 || nameparts.length == 1) {
                personName = null;
            } else if (nameparts.length == 2) {
                personName = new PersonName();

                if (order == NameOrder.FIRST_LAST) {
                    personName.setFirstName(nameparts[0]);
                    personName.setLastName(nameparts[1]);
                } else if (order == NameOrder.LAST_FIRST) {
                    personName.setLastName(nameparts[0]);
                    personName.setFirstName(nameparts[1]);
                }

            } else if (nameparts.length > 2) {
                personName = new PersonName();
                personName.setLastName(nameparts[0]);
                personName.setFirstName(nameparts[1]);
            }

        } else {
            personName = null;
        }
        LOG.trace("End PersonNameParser.SplitNameByDelimiter(String,String,nameorder)");
        return personName;
    }

    /**
     * Method to take a PersonName object and return it as a String.
     * @param personName the personName object to serialize
     * @return the PersonName as a String.
     */
    public static String serializeName(PersonName personName) {
        LOG.trace("Begin PersonNameParser.SerializeName(PersonName)");
        String serializedName;
        if (personName == null) {
            serializedName = "";
        } else if (personName.getFirstName().contentEquals("")) {
            serializedName = personName.getLastName();
        } else if (personName.getLastName().contentEquals("")) {
            serializedName = personName.getFirstName();
        } else {
            serializedName = personName.getLastName() + "," + personName.getFirstName();
        }
        LOG.trace("End PersonNameParser.SerializeName(PersonName)");
        return serializedName;
    }
}
