/*
 * Copyright (c) 2011, Conemaugh Valley Memorial Hospital
 *
 * This source is subject to the Conemaugh public license.  Please see the
 * license.txt file for more information.
 *
 * All other rights reserved.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
 * CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT
 * SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapter.commondatalayer.mappers;
/*
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.hl7.v3.ProviderDetailsPRPMIN306010UV1RequestType;
import org.hl7.v3.ProviderDetailsPRPMIN306011UV01ResponseType;
*/
/**
 *
 * @author A22387
 */
/*
public class StaticFindProvidersQuery {

    private static Log logger = LogFactory.getLog(StaticFindProvidersQuery.class);

    public static ProviderDetailsPRPMIN306011UV01ResponseType createFindProvidersResponse(ProviderDetailsPRPMIN306010UV1RequestType request)
    {
        logger.debug("Creating Static Find Providers Data");

        ProviderDetailsPRPMIN306011UV01ResponseType response = new ProviderDetailsPRPMIN306011UV01ResponseType();

        // Get Provider OID from the request
        String receiverOID = request.getReceiverOID();

        // Get Provider ID from the request
        String reqProviderID = request.getQuery().getControlActProcess().getQueryByParameterPayload().getValue().getProviderID().get(0).getValue().getExtension();
        
        logger.debug("Retrieving Emulated Data File for : Provider ID: " + reqProviderID + ", receiverOID: " + receiverOID);
        response = StaticUtil.createFindProvidersResponse(reqProviderID, receiverOID);

        return response;
    }
}*/