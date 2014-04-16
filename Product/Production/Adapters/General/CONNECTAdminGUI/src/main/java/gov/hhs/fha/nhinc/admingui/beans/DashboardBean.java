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
	
	@Deprecated
        public List<Service> getDashboardDetails1() {
            
                List<Service> services = new ArrayList<Service>();
                
                Service s1 = new Service();
                s1.setServicename("PL");
		s1.setInboudvalue(100);
		s1.setOutboundvalue(200);
                s1.setInoutboudvalue(s1.getInboudvalue() + s1.getOutboundvalue());
                
		
		Service s2 = new Service();
                s2.setServicename("PD");
		s2.setInboudvalue(100);
		s2.setOutboundvalue(200);
                s2.setInoutboudvalue(s2.getInboudvalue() + s2.getOutboundvalue());
		
		Service s3 = new Service();
                s3.setServicename("DQ");
		s3.setInboudvalue(100);
		s3.setOutboundvalue(200);
                s3.setInoutboudvalue(s3.getInboudvalue() + s3.getOutboundvalue());
                
                Service s4 = new Service();
		s4.setServicename("DR");
		s4.setInboudvalue(400);
		s4.setOutboundvalue(500); 
                s4.setInoutboudvalue(s4.getInboudvalue() + s4.getOutboundvalue());
                
		services.add(s1);
		services.add(s2);
		services.add(s3);
                services.add(s4);
                
               
		return services;
	}
        
        @Deprecated
        public List<Organization> getDashboardDetails2() {
            
                List<Organization> organizationList = new ArrayList<Organization>();
                
		Service[] services = new Service[4];
                
                Organization o1 = new Organization();
                o1.setHcidName("Gatesay 1001");
                Organization o2 = new Organization();
                o2.setHcidName("Gatesay 1002");
                
                Service s1 = new Service();
                s1.setServicename("PL");
		s1.setInboudvalue(100);
		s1.setOutboundvalue(200);
                
		
		Service s2 = new Service();
                s2.setServicename("PD");
		s2.setInboudvalue(100);
		s2.setOutboundvalue(200);
		
		Service s3 = new Service();
                s3.setServicename("DQ");
		s3.setInboudvalue(100);
		s3.setOutboundvalue(200);
                
                Service s4 = new Service();
		s4.setServicename("DR");
		s4.setInboudvalue(400);
		s4.setOutboundvalue(500);
		
		services[0] = s1;
                services[1] = s2;
                services[2] = s3;
                services[3] = s4;
		
                
                o1.setServices(services);
                o2.setServices(services);
                
                organizationList.add(o1);
                organizationList.add(o2);
                
                
		
		return organizationList;
	}
        
        public List<Organization> getDashboardDetails() {
            
                List<Organization> organizationList = new ArrayList<Organization>();
                		               
                Organization o1 = new Organization();
                o1.setHcidName("HCID-1001");
                Organization o2 = new Organization();
                o2.setHcidName("HCID-1002");
                
                Service s1 = new Service();
                s1.setServicename("PD");
		s1.setInboudvalue(100);
		s1.setOutboundvalue(200);
                s1.setInoutboudvalue(s1.getInboudvalue() + s1.getOutboundvalue());
                
		
		Service s2 = new Service();
                s2.setServicename("DQ");
		s2.setInboudvalue(100);
		s2.setOutboundvalue(200);
                s2.setInoutboudvalue(s2.getInboudvalue() + s2.getOutboundvalue());
		
		Service s3 = new Service();
                s3.setServicename("DR");
		s3.setInboudvalue(100);
		s3.setOutboundvalue(200);
                s3.setInoutboudvalue(s3.getInboudvalue() + s3.getOutboundvalue());
                
                Service s4 = new Service();
		s4.setServicename("DS");
		s4.setInboudvalue(400);
		s4.setOutboundvalue(500);
                s4.setInoutboudvalue(s4.getInboudvalue() + s4.getOutboundvalue());
                
                Service s5 = new Service();
		s5.setServicename("AD");
		s5.setInboudvalue(400);
		s5.setOutboundvalue(500);
                s5.setInoutboudvalue(s5.getInboudvalue() + s5.getOutboundvalue());
		
                
                o1.setService1Name("PD");
                o1.setService1InOutBoundValue(s1.getInoutboudvalue());
                o1.setService2Name("DQ");
                o1.setService2InOutBoundValue(s2.getInoutboudvalue());
                o1.setService3Name("DR");
                o1.setService3InOutBoundValue(s3.getInoutboudvalue());
                o1.setService4Name("DS");
                o1.setService4InOutBoundValue(s4.getInoutboudvalue());
                o1.setService5Name("AD");
                o1.setService5InOutBoundValue(s5.getInoutboudvalue());
                o1.setTotalInOutBoundValue(s1.getInoutboudvalue() + s2.getInoutboudvalue() + s3.getInoutboudvalue() + s4.getInoutboudvalue()+ s5.getInoutboudvalue());
                
                o2.setService1Name("PD");
                o2.setService1InOutBoundValue(s1.getInoutboudvalue());
                o2.setService2Name("DQ");
                o2.setService2InOutBoundValue(s2.getInoutboudvalue());
                o2.setService3Name("DR");
                o2.setService3InOutBoundValue(s3.getInoutboudvalue());
                o2.setService4Name("DS");
                o2.setService4InOutBoundValue(s4.getInoutboudvalue());
                o2.setService5Name("AD");
                o2.setService5InOutBoundValue(s5.getInoutboudvalue());
                o2.setTotalInOutBoundValue(s1.getInoutboudvalue() + s2.getInoutboudvalue() + s3.getInoutboudvalue() + s4.getInoutboudvalue()+ s5.getInoutboudvalue());
                
                o1.setService1(s1);
                o1.setService1(s2);
                o1.setService1(s3);
                o1.setService1(s4);
                o1.setService1(s5);
                
                o2.setService1(s1);
                o2.setService1(s2);
                o2.setService1(s3);
                o2.setService1(s4);  
                o2.setService1(s5);
                
                organizationList.add(o1);
                organizationList.add(o2);
		return organizationList;
	}
}

