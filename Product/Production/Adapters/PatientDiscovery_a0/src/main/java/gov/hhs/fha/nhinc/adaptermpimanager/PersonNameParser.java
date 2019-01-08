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
package gov.hhs.fha.nhinc.adaptermpimanager;

import gov.hhs.fha.nhinc.mpilib.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mflynn02
 */
public class PersonNameParser {
    private static final Logger LOG = LoggerFactory.getLogger(PersonNameParser.class);

    private enum nameorder {

        first_last, last_first
    }

    public static PersonName SplitName(String name) {
        LOG.debug("Begin PersonNameParser.SplitName(String)");
        PersonName personname;
        if (name == null || name.contentEquals("")) {
            personname = new PersonName();
        } else {
            personname = SplitNameByDelimiter(name, ",", nameorder.last_first);
            if (personname == null) {
                personname = SplitNameByDelimiter(name, " ", nameorder.first_last);
            }

            if (personname == null) {
                {
                    personname = new PersonName();
                    personname.setLastName(name);
                }
            }
        }
        LOG.debug("End PersonNameParser.SplitName(String)");
        return personname;
    }

    private static PersonName SplitNameByDelimiter(String name, String delimiter, nameorder order) {
        LOG.debug("Begin PersonNameParser.SplitNameByDelimiter(String,String,nameorder)");
        PersonName personname = null;
        if (name.contains(delimiter)) {
            String[] nameparts = name.split(delimiter);
            if (nameparts.length == 0 || nameparts.length == 1) {
                personname = null;
            } else if (nameparts.length == 2) {
                personname = new PersonName();

                if (order == nameorder.first_last) {
                    personname.setFirstName(nameparts[0]);
                    personname.setLastName(nameparts[1]);
                } else if (order == nameorder.last_first) {
                    personname.setLastName(nameparts[0]);
                    personname.setFirstName(nameparts[1]);
                }

            } else if (nameparts.length > 2) {
                personname = new PersonName();
                personname.setLastName(nameparts[0]);
                personname.setFirstName(nameparts[1]);
            }

        } else {
            personname = null;
        }
        LOG.debug("End PersonNameParser.SplitNameByDelimiter(String,String,nameorder)");
        return personname;
    }

    public static String SerializeName(PersonName personname) {
        LOG.debug("Begin PersonNameParser.SerializeName(PersonName)");
        String serializedname;
        if (personname == null) {
            serializedname = "";
        } else if (personname.getFirstName().contentEquals("")) {
            serializedname = personname.getLastName();
        } else if (personname.getLastName().contentEquals("")) {
            serializedname = personname.getFirstName();
        } else {
            serializedname = personname.getLastName() + "," + personname.getFirstName();
        }
        LOG.debug("End PersonNameParser.SerializeName(PersonName)");
        return serializedname;
    }

}
