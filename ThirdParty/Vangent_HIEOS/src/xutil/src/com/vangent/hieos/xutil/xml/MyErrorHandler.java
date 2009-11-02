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
 * ErrorHandler.java
 *
 * Created on September 27, 2005, 9:10 AM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.vangent.hieos.xutil.xml;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

/**
 *
 * @author bill
 */
public class MyErrorHandler implements ErrorHandler {
    StringBuffer errors;
    String schemaFile = "";
    
    /** Creates a new instance of ErrorHandler */
    public MyErrorHandler() {
        errors = new StringBuffer();
    }

    public void setSchemaFile(String file) {
	schemaFile = file;
    }
    
    public String getErrors() {
        return errors.toString();
    }
    
    public void error(SAXParseException exception) {
        errors.append("\nError: " + exception.getMessage() + "\n" +
		      "Schema location is " + schemaFile);
    }
    
    public void fatalError(SAXParseException exception) {
        errors.append("\nFatal Error: " + exception.getMessage());
    }
    
    public void warning(SAXParseException exception) {
        errors.append("\nWarning: " + exception.getMessage());
    }
}
