/*
 * This code is subject to the HIEOS License, Version 1.0
 *
 * Copyright(c) 2008-2009 Vangent, Inc.  All rights reserved.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * Hl7Address.java
 *
 * Created on August 5, 2005, 11:28 AM
 *
 */

package com.vangent.hieos.adt.db;

/**
 *
 * @author mccaffrey
 */
public class Hl7Address {
   
    private String parent = null;   //  uuid link to record... used in database but may not be used in actuality in java object
    
    private String streetAddress = null;
    private String otherDesignation = null;
    private String city = null;
    private String stateOrProvince = null;
    private String zipCode = null;
    private String country = null;
    private String countyOrParish = null;
    
    /**
     *
     */
    public Hl7Address() {}
        
    
    /**
     *
     * @param parent
     */
    public Hl7Address(String parent) {
        this.setParent(parent);
        
    }
    
    /** Creates a new instance of Hl7Address
     * @param parent
     * @param country
     * @param otherDesignation
     * @param city
     * @param streetAddress
     * @param stateOrProvince
     * @param countyOrParish
     * @param zipCode
     */
    public Hl7Address(String parent, String streetAddress, String otherDesignation, String city, String stateOrProvince,
            String zipCode, String country, String countyOrParish) {
        this.setParent(parent);
        this.setStreetAddress(streetAddress);
        this.setOtherDesignation(otherDesignation);
        this.setCity(city);
        this.setStateOrProvince(stateOrProvince);
        this.setZipCode(zipCode);
        this.setCountry(country);
        this.setCountyOrParish(countyOrParish);
    }

    /**
     *
     * @return
     */
    public String getStreetAddress() {
        return streetAddress;
    }

    /**
     *
     * @param streetAddress
     */
    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    /**
     *
     * @return
     */
    public String getOtherDesignation() {
        return otherDesignation;
    }

    /**
     *
     * @param otherDesignation
     */
    public void setOtherDesignation(String otherDesignation) {
        this.otherDesignation = otherDesignation;
    }

    /**
     *
     * @return
     */
    public String getCity() {
        return city;
    }

    /**
     *
     * @param city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     *
     * @return
     */
    public String getStateOrProvince() {
        return stateOrProvince;
    }

    /**
     *
     * @param stateOrProvince
     */
    public void setStateOrProvince(String stateOrProvince) {
        this.stateOrProvince = stateOrProvince;
    }

    /**
     *
     * @return
     */
    public String getZipCode() {
        return zipCode;
    }

    /**
     *
     * @param zipCode
     */
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    /**
     *
     * @return
     */
    public String getCountry() {
        return country;
    }

    /**
     *
     * @param country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     *
     * @return
     */
    public String getCountyOrParish() {
        return countyOrParish;
    }

    /**
     *
     * @param countyOrParish
     */
    public void setCountyOrParish(String countyOrParish) {
        this.countyOrParish = countyOrParish;
    }
    
    // not complete
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.getStreetAddress() + '\n');
        sb.append(this.getCity() + " " + this.getStateOrProvince() + this.getZipCode() + '\n');
        sb.append(this.getCountry() + '\n');
        return sb.toString();
    }

    /**
     *
     * @return
     */
    public String getParent() {
        return parent;
    }

    /**
     *
     * @param parent
     */
    public void setParent(String parent) {
        this.parent = parent;
    }
    
}
