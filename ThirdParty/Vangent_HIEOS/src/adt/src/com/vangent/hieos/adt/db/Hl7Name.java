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
 * Hl7Name.java
 *
 * Created on August 4, 2005, 4:42 PM
 *
 */

package com.vangent.hieos.adt.db;

/**
 *
 * @author mccaffrey
 */
public class Hl7Name {
    
    private String parent = null;   //  uuid link to record -- not the patient's biological parent                                    
    private String familyName = null;
    private String givenName = null;
    private String secondAndFurtherName = null;
    private String suffix = null;
    private String prefix = null;
    private String degree = null;
    
    /**
     * Creates a new instance of Hl7Name 
     */
    
    public Hl7Name() {
        
    }
    
    /**
     *
     * @param parent
     */
    public Hl7Name(String parent) {
        this.setParent(parent);
        
    }

    /**
     *
     * @param parent
     * @param familyName
     * @param givenName
     * @param secondAndFurtherName
     * @param suffix
     * @param prefix
     * @param degree
     */
    public Hl7Name(String parent, String familyName, String givenName, String secondAndFurtherName,
            String suffix, String prefix, String degree) {
        this.setParent(parent);
        this.setFamilyName(familyName);
        this.setGivenName(givenName);
        this.setSecondAndFurtherName(secondAndFurtherName);
        this.setSuffix(suffix);
        this.setPrefix(prefix);
        this.setDegree(degree);
    }

    /**
     *
     * @return
     */
    public String getFamilyName() {
        return familyName;
    }

    /**
     *
     * @param familyName
     */
    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    /**
     *
     * @return
     */
    public String getGivenName() {
        return givenName;
    }

    /**
     *
     * @param givenName
     */
    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    /**
     *
     * @return
     */
    public String getSecondAndFurtherName() {
        return secondAndFurtherName;
    }

    /**
     *
     * @param secondAndFurtherName
     */
    public void setSecondAndFurtherName(String secondAndFurtherName) {
        this.secondAndFurtherName = secondAndFurtherName;
    }

    /**
     *
     * @return
     */
    public String getSuffix() {
        return suffix;
    }

    /**
     *
     * @param suffix
     */
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    /**
     *
     * @return
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     *
     * @param prefix
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     *
     * @return
     */
    public String getDegree() {
        return degree;
    }

    /**
     *
     * @param degree
     */
    public void setDegree(String degree) {
        this.degree = degree;
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
