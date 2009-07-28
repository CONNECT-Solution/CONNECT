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

package com.sun.mdm.index.ops;

import java.util.HashMap;

import com.sun.mdm.index.ops.PatientDB;
import com.sun.mdm.index.ops.PatientSBRDB;
import com.sun.mdm.index.ops.AliasDB;
import com.sun.mdm.index.ops.AliasSBRDB;
import com.sun.mdm.index.ops.AddressDB;
import com.sun.mdm.index.ops.AddressSBRDB;
import com.sun.mdm.index.ops.PhoneDB;
import com.sun.mdm.index.ops.PhoneSBRDB;
import com.sun.mdm.index.ops.exception.*;

public final class OPSInitHelper
    implements OPSLoad
{
    public OPSInitHelper() {}
    
    public HashMap loadOPS()
        throws OPSException
    {
        HashMap ret = new HashMap();
        
        PatientDB PatientDB_handle = new PatientDB();
        ret.put("PatientDB", PatientDB_handle);
        PatientSBRDB PatientSBRDB_handle = new PatientSBRDB();
        ret.put("PatientSBRDB", PatientSBRDB_handle);
        AliasDB AliasDB_handle = new AliasDB();
        ret.put("AliasDB", AliasDB_handle);
        AliasSBRDB AliasSBRDB_handle = new AliasSBRDB();
        ret.put("AliasSBRDB", AliasSBRDB_handle);
        AddressDB AddressDB_handle = new AddressDB();
        ret.put("AddressDB", AddressDB_handle);
        AddressSBRDB AddressSBRDB_handle = new AddressSBRDB();
        ret.put("AddressSBRDB", AddressSBRDB_handle);
        PhoneDB PhoneDB_handle = new PhoneDB();
        ret.put("PhoneDB", PhoneDB_handle);
        PhoneSBRDB PhoneSBRDB_handle = new PhoneSBRDB();
        ret.put("PhoneSBRDB", PhoneSBRDB_handle);
        
        return ret;
    }
}