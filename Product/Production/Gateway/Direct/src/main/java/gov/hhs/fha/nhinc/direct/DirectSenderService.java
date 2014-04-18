/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.direct;

/**
 *
 * @author svalluripalli
 */
public interface DirectSenderService {
     public void SendoutMessage(ConnectCustomMimeMessage message);
}
