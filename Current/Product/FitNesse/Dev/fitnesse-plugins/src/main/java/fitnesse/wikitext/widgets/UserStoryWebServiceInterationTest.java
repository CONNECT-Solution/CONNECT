package fitnesse.wikitext.widgets;

import static org.junit.Assert.*;

import java.util.Properties;

import org.junit.Test;
import org.codehaus.xfire.client.Client;
import org.codehaus.xfire.util.dom.DOMOutHandler;
import org.codehaus.xfire.security.wss4j.WSS4JOutHandler;
import org.apache.ws.security.handler.WSHandlerConstants;
import org.apache.ws.security.WSConstants;

import com.targetprocess.integration.PasswordHandler;
import com.targetprocess.integration.userstory.UserStoryDTO;
import com.targetprocess.integration.userstory.UserStoryServiceClient;
import com.targetprocess.integration.userstory.UserStoryServiceSoap;


public class UserStoryWebServiceInterationTest {

	@Test
	public void obtainTheUserDescriptionForUserStory864(){
		UserStoryServiceSoap testSubject = getUserStoryService();
		UserStoryDTO actualUserStory = (UserStoryDTO)testSubject.getByID(864);
		assertEquals("Address Iteration 6 Bugs and Enhancements", actualUserStory.getName());
		assertEquals("<p>- Address past and current iteration critical bug fixes and enhancements coming out of iteration system testing activties </p>\n", actualUserStory.getDescription());
	}
	
	private final UserStoryServiceClient userStoryServiceClient = new UserStoryServiceClient();
    private UserStoryServiceSoap userStoryService;
	
	public synchronized UserStoryServiceSoap getUserStoryService() {
        if (userStoryService == null) {
            userStoryService = userStoryServiceClient.getUserStoryServiceSoap("http://nodalexchange.tpondemand.com/Services/UserStoryService.asmx");
            configureSecurity(userStoryService);
        }
        return userStoryService;
    }
	
	protected void configureSecurity(Object service) {
        Client client = Client.getInstance(service);
        client.addOutHandler(new DOMOutHandler());
        Properties properties = new Properties();
        properties.setProperty(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN);
        properties.setProperty(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_TEXT);
        properties.setProperty(WSHandlerConstants.USER, "flowersj");
        properties.setProperty(WSHandlerConstants.PW_CALLBACK_CLASS, PasswordHandler.class.getName());
        client.addOutHandler(new WSS4JOutHandler(properties));
    }
	
}
