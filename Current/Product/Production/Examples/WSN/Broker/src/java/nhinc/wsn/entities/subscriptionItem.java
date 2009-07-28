package nhinc.wsn.entities;

import java.util.ArrayList;
import java.util.List;
import javax.xml.ws.wsaddressing.W3CEndpointReference;
import nhinc.wsn.entities.filters.IFilterCheck;
import org.oasis_open.docs.wsn.b_2.Subscribe;

/**
 *
 * @author rayj
 */
public class subscriptionItem {

    public Subscribe subscribe;    //todo: use the one specified in the schema, not this one
    public W3CEndpointReference subscriptionReference;
    public List<IFilterCheck> Filters = new ArrayList<IFilterCheck>();
    private String rawSubscribe;
    private String serializedSubscribe;

    public void setRawSubscribe(String rawSubscribe)
    {
        this.rawSubscribe = rawSubscribe;
    }

    public String getRawSubscribe()
    {
        return rawSubscribe;
    }

    public void setSerializedSubscribe(String serializedSubscribe)
    {
        this.serializedSubscribe = serializedSubscribe;
    }

    public String getSerializedSubscribe()
    {
        return serializedSubscribe;
    }
    
    
}
