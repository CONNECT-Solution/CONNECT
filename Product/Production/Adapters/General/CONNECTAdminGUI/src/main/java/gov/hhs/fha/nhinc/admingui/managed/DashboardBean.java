package gov.hhs.fha.nhinc.admingui.managed;

/**
 *
 * @author sadusumilli / jasonasmith
 */
import gov.hhs.fha.nhinc.admingui.event.model.EventNwhinOrganization;
import gov.hhs.fha.nhinc.admingui.event.service.EventCountService;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@ManagedBean(name = "dashboardBean")
@SessionScoped
@Component
public class DashboardBean {

    @Autowired
    private EventCountService eventCountService;
    
    public List<EventNwhinOrganization> getTotalEvents() {
        eventCountService.setCounts();
        
        return eventCountService.getTotalOrganizations();
    }
}
