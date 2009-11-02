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
 * Hl7Race.java
 *
 * Created on August 15, 2005, 10:16 AM
 *
 */

package com.vangent.hieos.adt.db;

/**
 *
 * @author mccaffrey
 */
public class Hl7Race {
    
    private String parent = null;   //  uuid link to record -- not the patient's biological parent                                    
    private String race = null;
    
    /**
     *
     */
    public Hl7Race() {}
    
    /** Creates a new instance of Hl7Race
     * @param parent
     */
    public Hl7Race(String parent) {
        this.setParent(parent);
    }
    
    /**
     *
     * @param parent
     * @param race
     */
    public Hl7Race(String parent, String race) {
        this.setParent(parent);
        this.setRace(race);
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

    /**
     *
     * @return
     */
    public String getRace() {
        return race;
    }

    /**
     *
     * @param race
     */
    public void setRace(String race) {
        this.race = race;
    }
}
