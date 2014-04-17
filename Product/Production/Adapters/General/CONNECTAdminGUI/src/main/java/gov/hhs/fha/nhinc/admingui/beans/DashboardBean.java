/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.admingui.beans;

/**
 *
 * @author sadusumilli
 */
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "dashboardBean") 
@SessionScoped
public class DashboardBean {
    
        public List<Organization> getDashboardDetails() {
            
                List<Organization> organizationList = new ArrayList<Organization>();
                
                Organization o1 = new Organization();
                o1.setHcidName("HCID-1001");
                Organization o2 = new Organization();
                o2.setHcidName("HCID-1002");               
               
                Service PDService = new Service();
                PDService.setServicename("PD");
		PDService.setInboundvalue(100);
		PDService.setOutboundvalue(200);
                PDService.setInoutboundvalue(PDService.getInboundvalue() + PDService.getOutboundvalue());
                		
		Service DQService = new Service();
                DQService.setServicename("DQ");
		DQService.setInboundvalue(200);
		DQService.setOutboundvalue(400);
                DQService.setInoutboundvalue(DQService.getInboundvalue() + DQService.getOutboundvalue());
		
		Service DRService = new Service();
                DRService.setServicename("DR");
		DRService.setInboundvalue(200);
		DRService.setOutboundvalue(100);
                DRService.setInoutboundvalue(DRService.getInboundvalue() + DRService.getOutboundvalue());
                
                Service DSService = new Service();
		DSService.setServicename("DS");
		DSService.setInboundvalue(500);
		DSService.setOutboundvalue(300);
                DSService.setInoutboundvalue(DSService.getInboundvalue() + DSService.getOutboundvalue());
                
                Service ADService = new Service();
		ADService.setServicename("AD");
		ADService.setInboundvalue(400);
		ADService.setOutboundvalue(100);
                ADService.setInoutboundvalue(ADService.getInboundvalue() + ADService.getOutboundvalue());
                
                Service DirectService = new Service();
		DirectService.setServicename("Direct");
		DirectService.setInboundvalue(300);
		DirectService.setOutboundvalue(800);
                DirectService.setInoutboundvalue(DirectService.getInboundvalue() + DirectService.getOutboundvalue());
             
                o1.setPDName("PD");
                o1.setPdInOutBoundValue(PDService.getInoutboundvalue());
                o1.setDQName("DQ");
                o1.setDqInOutBoundValue(DQService.getInoutboundvalue());
                o1.setDRName("DR");
                o1.setDrInOutBoundValue(DRService.getInoutboundvalue());
                o1.setDSName("DS");
                o1.setDsInOutBoundValue(DSService.getInoutboundvalue());
                o1.setADName("AD");
                o1.setAdInOutBoundValue(ADService.getInoutboundvalue());
                o1.setDirectName("Direct");
                o1.setDirectInOutBoundValue(DirectService.getInoutboundvalue());
                o1.setTotalInOutBoundValue(PDService.getInoutboundvalue() + DQService.getInoutboundvalue() + DRService.getInoutboundvalue() + DSService.getInoutboundvalue()+ ADService.getInoutboundvalue()+DirectService.getInoutboundvalue());
                
                o2.setPDName("PD");
                o2.setPdInOutBoundValue(PDService.getInoutboundvalue());
                o2.setDQName("DQ");
                o2.setDqInOutBoundValue(DQService.getInoutboundvalue());
                o2.setDRName("DR");
                o2.setDrInOutBoundValue(DRService.getInoutboundvalue());
                o2.setDSName("DS");
                o2.setDsInOutBoundValue(DSService.getInoutboundvalue());
                o2.setADName("AD");
                o2.setAdInOutBoundValue(ADService.getInoutboundvalue());
                o2.setDirectName("Direct");
                o2.setDirectInOutBoundValue(DirectService.getInoutboundvalue());
                o2.setTotalInOutBoundValue(PDService.getInoutboundvalue() + DQService.getInoutboundvalue() + DRService.getInoutboundvalue() + DSService.getInoutboundvalue()+ ADService.getInoutboundvalue()+DirectService.getInoutboundvalue());
                                 
                o1.setPD(PDService);
                o1.setDQ(DQService);
                o1.setDR(DRService);
                o1.setDS(DSService);
                o1.setAD(ADService);
                o1.setDirect(DirectService);
                
                o2.setPD(PDService);
                o2.setDQ(DQService);
                o2.setDR(DRService);
                o2.setDS(DSService);
                o2.setAD(ADService);
                o2.setDirect(DirectService); 
                
                organizationList.add(o1);
                organizationList.add(o2);
		return organizationList;
	}
}
