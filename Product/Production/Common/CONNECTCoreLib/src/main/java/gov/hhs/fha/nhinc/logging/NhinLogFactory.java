package gov.hhs.fha.nhinc.logging;

/**
 *
 * @author zmelnick
 */
public class NhinLogFactory {

    
    static NhinLog getLog(Class<?> aClass) {
        return new NhinLogImpl(aClass);

    }








}
