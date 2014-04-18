/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.direct;

import javax.jws.WebService;

/**
 *
 * @author svalluripalli
 */
@WebService
public interface DirectReceiverService {    
    public void receiveInbound(ConnectCustomMimeMessage message);
}
