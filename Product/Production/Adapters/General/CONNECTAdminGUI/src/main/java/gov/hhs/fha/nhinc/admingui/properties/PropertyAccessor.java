/*******************************************************************************
 * Copyright 2013 The California Health and Human Services Agency (CHHS). All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"), you may not use this file except in compliance with the License. You may obtain a copy of the License at: http://www.apache.org/licenses/LICENSE-2.0.
 * Unless required by applicable law or agreed to in writing, content (including but not limited to software, documentation, information, and all other works distributed under the License) is distributed on an "AS IS" BASIS, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE CONTENT OR THE USE OR OTHER DEALINGS IN THE CONTENT. IN NO EVENT SHALL CHHS HAVE ANY OBLIGATION TO PROVIDE SUPPORT, UPDATES, MODIFICATIONS, AND/OR UPGRADES FOR CONTENT. See the License for the specific language governing permissions and limitations under the License.
 * This publication/product was made possible by Award Number 90HT0029 from Office of the National Coordinator for Health Information Technology (ONC), U.S. Department of Health and Human Services. Its contents are solely the responsibility of the authors and do not necessarily represent the official views of ONC or the State of California.
 ******************************************************************************/
package gov.hhs.fha.nhinc.admingui.properties;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import java.io.File;

import org.apache.log4j.Logger;

public class PropertyAccessor {

    private static final PropertyAccessor propertyAccessor = new PropertyAccessor();
    private static final String PROP_FILE = "portal.properties";
    private static final Logger LOG = Logger.getLogger(PropertyAccessor.class);
    private PropertiesConfiguration properties;

    // Singleton
    private PropertyAccessor() {
        configurePropertyAccessor();
    }

    private void configurePropertyAccessor() {
        try {
            String path = PropertyAccessor.class.getClassLoader().getResource(PROP_FILE).getPath();
            properties = new PropertiesConfiguration(new File(path));
            properties.setReloadingStrategy(new FileChangedReloadingStrategy());
        } catch (Exception e) {
            LOG.error("Unable to load properties.", e);
            System.exit(-1);
        }
    }

    public static PropertyAccessor getInstance() {
        return propertyAccessor;
    }

    public String getProperty(String key) {
        return properties.getString(key);
    }
}
