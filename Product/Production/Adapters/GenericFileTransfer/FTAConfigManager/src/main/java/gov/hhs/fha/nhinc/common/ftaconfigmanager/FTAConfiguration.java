/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.common.ftaconfigmanager;
import java.util.List;

/**
 *
 * @author dunnek
 */
public class FTAConfiguration {
    private List<FTAChannel> inboundChannels = null;
    private List<FTAChannel> outboundChannels = null;

    public List<FTAChannel> getInboundChannels()
    {
        return inboundChannels;
    }
    public List<FTAChannel> getOutboundChannels()
    {
        return outboundChannels;
    }
    public void setInboundChannels(List<FTAChannel> value)
    {
        inboundChannels = value;
    }
    public void setOutboundChannels(List<FTAChannel> value)
    {
        outboundChannels = value;
    }

}
