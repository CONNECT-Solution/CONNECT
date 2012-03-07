/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
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
package gov.hhs.fha.nhinc.adapter.commondatalayer.mappers;

import java.util.List;
import org.hl7.v3.ADExplicit;
import org.hl7.v3.AdxpExplicitCity;
import org.hl7.v3.AdxpExplicitCountry;
import org.hl7.v3.AdxpExplicitPostalCode;
import org.hl7.v3.AdxpExplicitState;
import org.hl7.v3.AdxpExplicitStreetAddressLine;
import org.hl7.v3.TELExplicit;

/**
 * 
 * @author kim
 */
public class StaticUtil {

    private static org.hl7.v3.ObjectFactory factory = new org.hl7.v3.ObjectFactory();

    public static ADExplicit createAddress(String streetName, String city, String state, String zip, String country) {
        ADExplicit addr = new ADExplicit();

        addr.getUse().add("HP");

        List addrlist = addr.getContent();
        if (streetName != null && streetName.length() > 0) {
            AdxpExplicitStreetAddressLine streetAddrLine = new AdxpExplicitStreetAddressLine();
            streetAddrLine.setContent(streetName);
            addrlist.add(factory.createADExplicitStreetAddressLine(streetAddrLine));
        }

        if (city != null && city.length() > 0) {
            AdxpExplicitCity aCity = new AdxpExplicitCity();
            aCity.setContent(city);
            addrlist.add(factory.createADExplicitCity(aCity));
        }

        if (state != null && state.length() > 0) {
            AdxpExplicitState aState = new AdxpExplicitState();
            aState.setContent(state);
            addrlist.add(factory.createADExplicitState(aState));
        }

        if (zip != null && zip.length() > 0) {
            AdxpExplicitPostalCode postalCode = new AdxpExplicitPostalCode();
            postalCode.setContent(zip);
            addrlist.add(factory.createADExplicitPostalCode(postalCode));
        }

        if (country != null && country.length() > 0) {
            AdxpExplicitCountry aCountry = new AdxpExplicitCountry();
            aCountry.setContent(country);
            addrlist.add(factory.createADExplicitCountry(aCountry));
        }

        return addr;
    }

    public static TELExplicit createTelecom(String phoneNumber, String type) {
        TELExplicit telecom = new TELExplicit();
        telecom.getUse().add(type);
        telecom.setValue(phoneNumber);

        return telecom;
    }
}
