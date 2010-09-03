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
