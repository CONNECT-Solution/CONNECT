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
package com.vangent.hieos.adt.verify;

import com.vangent.hieos.adt.db.AdtJdbcConnection;
import com.vangent.hieos.xutil.exception.XdsInternalException;

import java.sql.SQLException;
import org.apache.log4j.Logger;


/**
 * 
 * @author thumbe
 */
public class Verify {
    private final static Logger logger = Logger.getLogger(Verify.class);

    /**
     *
     */
    public Verify() {
    }

    /**
     *
     * @param patientId
     * @return
     * @throws java.sql.SQLException
     * @throws com.vangent.hieos.xutil.exception.XdsInternalException
     */
    public boolean isValid(String patientId) throws XdsInternalException {
        boolean isv = false;
        AdtJdbcConnection con = null;
        try {
            con = new AdtJdbcConnection();
            isv = con.doesIdExist(patientId);
        } catch (XdsInternalException e) {
            logger.error("Could not verify patient id", e);
            throw e;
        } catch (SQLException e) {
            logger.error("Could not verify patient id", e);
            throw new XdsInternalException("Failure validating patient id: " + e.getMessage());
        } finally {
            if (con != null) {
                con.closeConnection();
            }
        }
        return isv;
    }
}
