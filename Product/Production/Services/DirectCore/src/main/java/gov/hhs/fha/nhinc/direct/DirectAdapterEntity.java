/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.direct;

import gov.hhs.fha.nhinc.proxy.ComponentProxyFactory;

/**
 *
 * @author svalluripalli
 */
public class DirectAdapterEntity {
    private static final String BEAN_NAME_RECEIVER = "directReceiver";
    private static final String BEAN_NAME_SENDER = "directSender";
    protected static final String CONFIG_FILE_NAME = "direct.appcontext.xml";    
    
    /**
     * @return a {@link DirectReceiver} from the factory.
     */
    public DirectReceiver getDirectReceiver() {
        return new ComponentProxyFactory(CONFIG_FILE_NAME).getInstance(BEAN_NAME_RECEIVER, DirectReceiver.class);
    }

    /**
     * @return a {@link DirectSender} from the factory.
     */
    public DirectSender getDirectSender() {
        return new ComponentProxyFactory(CONFIG_FILE_NAME).getInstance(BEAN_NAME_SENDER, DirectSender.class);
    }
}
