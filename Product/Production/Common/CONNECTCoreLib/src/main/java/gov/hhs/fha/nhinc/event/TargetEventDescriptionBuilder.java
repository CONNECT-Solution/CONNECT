/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.event;

import com.google.common.base.Optional;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.event.builder.TargetDescriptionExtractor;

/**
 *
 * @author jasonasmith
 */
public abstract class TargetEventDescriptionBuilder extends AssertionEventDescriptionBuilder {
   
    protected Optional<NhinTargetSystemType> target = Optional.absent();    
    private final TargetDescriptionExtractor targetExtractor = new TargetDescriptionExtractor();
    
    @Override
    public final void buildRespondingHCIDs() {
        if(target.isPresent()){
            setRespondingHCIDs(targetExtractor.getResponders(target.get()));
        }else {
            setLocalResponder();
        }
    }
    
    protected final void extractTarget(Object ... arguments){
        if(arguments != null){
            for(int i = 0; i < arguments.length; ++ i) {
                if(arguments[i] instanceof NhinTargetSystemType) {
                    target = Optional.of((NhinTargetSystemType) arguments[i]);
                    return;
                }
            }
        }      
        target = Optional.absent();
    }
}
