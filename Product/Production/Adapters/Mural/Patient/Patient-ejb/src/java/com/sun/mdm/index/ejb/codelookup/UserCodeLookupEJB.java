/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2003-2007 Sun Microsystems, Inc. All Rights Reserved.
 *
 * The contents of this file are subject to the terms of the Common 
 * Development and Distribution License ("CDDL")(the "License"). You 
 * may not use this file except in compliance with the License.
 *
 * You can obtain a copy of the License at
 * https://open-dm-mi.dev.java.net/cddl.html
 * or open-dm-mi/bootstrap/legal/license.txt. See the License for the 
 * specific language governing permissions and limitations under the  
 * License.  
 *
 * When distributing the Covered Code, include this CDDL Header Notice 
 * in each file and include the License file at
 * open-dm-mi/bootstrap/legal/license.txt.
 * If applicable, add the following below this CDDL Header, with the 
 * fields enclosed by brackets [] replaced by your own identifying 
 * information: "Portions Copyrighted [year] [name of copyright owner]"
 */
package com.sun.mdm.index.ejb.codelookup;

import java.util.Map;
import javax.annotation.Resource;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import com.sun.mdm.index.codelookup.CodeLookupException;
import com.sun.mdm.index.codelookup.UserCodeRegistry;


@Stateless(mappedName="ejb/PatientUserCodeLookup")
@Remote(UserCodeLookupRemote.class)
@Resource(name="jdbc/BBEDataSource", 
          type=javax.sql.DataSource.class,
          mappedName="jdbc/PatientDataSource" )
public class UserCodeLookupEJB implements UserCodeLookupRemote {


    /** Creates new UserCodeLookupEJB */
    public UserCodeLookupEJB() {
    }
    
    /** See UserCodeLookup
     * @throws CodeLookupException See UserCodeLookup
     * @return See UserCodeLookup
     */    
    public Map getAllCodes() throws CodeLookupException {
        Map codeMap = UserCodeRegistry.getInstance().getCodeMap();
        return codeMap;
    }
    
    /** See CodeLookup
     * @param module See UserCodeLookup
     * @throws CodeLookupException See UserCodeLookup
     * @return See UserCodeLookup
     */    
    public Map getCodesByModule(String module) throws CodeLookupException {
        Map codeMap = UserCodeRegistry.getInstance().getCodeMapByModule(module);
        return codeMap;
    }

}
