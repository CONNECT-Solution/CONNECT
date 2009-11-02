var attemptNumber = 0 ;

function chgPassword ( oldPassword , newPassword ) {

    // Obtain an XMLHttpRequest instance
    var req = newXMLHttpRequest();

    // Set the handler function to receive callback notifications
    // from the request object
    var handlerFunction = getReadyStateHandler(req, parseResultChgPass );
    req.onreadystatechange = handlerFunction;
  
    // Open an HTTP POST connection to the shopping cart servlet.
    // Third parameter specifies request is asynchronous.
    req.open("POST", "../AuthenticationServlet", true);

    // Specify that the body of the request contains form data
    req.setRequestHeader("Content-Type",
        "application/x-www-form-urlencoded");

  
    var parameterToSend ="password="+  oldPassword + "&chgPassword="+  newPassword ;
                      
    req.send( parameterToSend  );

}

function parseResultChgPass( resultXML ) 
{
    var messageResult  = resultXML.getElementsByTagName("response")[0] ;
    var isAuthenticated = messageResult.getAttribute("isAuthenticated");
    var isChanged      = messageResult.getAttribute("isChanged");

    if (  isChanged  == "true" )
    {
        $('errorMsg').innerHTML = "<p>Password changed correctly</p>" ;
        isAdmin = true ;
	   	    
        window.location.href = "../index.html" ;
    }
    else
    {
        $('errorMsg').innerHTML = "<p style='color:red;'>Password not changed <br />" + +"</p>" ;
        isAdmin = false ;
        attemptNumber++ ;
    }
}
function getAuthentication ( password ) {

    // Obtain an XMLHttpRequest instance
    var req = newXMLHttpRequest();

    // Set the handler function to receive callback notifications
    // from the request object
    var handlerFunction = getReadyStateHandler(req, parseResultAuth );
    req.onreadystatechange = handlerFunction;
  
    // Open an HTTP POST connection to the shopping cart servlet.
    // Third parameter specifies request is asynchronous.
    req.open("POST", "../AuthenticationServlet", true);

    // Specify that the body of the request contains form data
    req.setRequestHeader("Content-Type",
        "application/x-www-form-urlencoded");

  
    var parameterToSend ="password="+  password ;
                      
    req.send( parameterToSend  );

}
function getIsAdmin ( ) 
{

    // Obtain an XMLHttpRequest instance
    var req = newXMLHttpRequest();

    // Set the handler function to receive callback notifications
    // from the request object
    var handlerFunction = getReadyStateHandler(req, parseIsAdmin );
    req.onreadystatechange = handlerFunction;
  
    // Open an HTTP POST connection to the shopping cart servlet.
    // Third parameter specifies request is asynchronous.
    req.open("POST", "../AuthenticationServlet" , false );

    // Specify that the body of the request contains form data
    req.setRequestHeader("Content-Type",
        "application/x-www-form-urlencoded");
  
    var parameterToSend ="isAdmin=get" ;
                      
    req.send( parameterToSend  );

}
function logout ()
{
    // Obtain an XMLHttpRequest instance
    var req = newXMLHttpRequest();

    // Set the handler function to receive callback notifications
    // from the request object
    var handlerFunction = getReadyStateHandler(req, parselogOut );
    req.onreadystatechange = handlerFunction;
  
    // Open an HTTP POST connection to the shopping cart servlet.
    // Third parameter specifies request is asynchronous.
    req.open("POST", "AuthenticationServlet", true);

    // Specify that the body of the request contains form data
    req.setRequestHeader("Content-Type",
        "application/x-www-form-urlencoded");
  
    var parameterToSend ="logout=yes" ;
                      
    req.send( parameterToSend  );
}
function parselogOut(resultXML ) 
{
    window.location.href =  "../index.html" ;
}

function parseIsAdmin(resultXML ) 
{

  
    var messageResult 	=  resultXML.getElementsByTagName("response")[0] ;
    var isAuthenticated = messageResult.getAttribute("isAuthenticated");
    if (  isAuthenticated == "true" )
    {
        isAdmin = true ;
    }
    else
    {
        isAdmin = false ;
    }
    var sysType = messageResult.getAttribute("systemType");
	  
}
  
function parseResultAuth(resultXML ) 
{

    if ( attemptNumber < 3 )
    {
        var messageResult 	=  resultXML.getElementsByTagName("response")[0] ;
        var isAuthenticated = messageResult.getAttribute("isAuthenticated");
        if (  isAuthenticated == "true" )
        {
            document.getElementById('errorMsg').innerHTML = "<p>Authenticated as Admin</p>" ;
            isAdmin = true ;
	    
            window.location.href = '../index.html';
        }
        else
        {
            document.getElementById('errorMsg').innerHTML = "<p style='color:red;'>Incorrect password</p>" ;
            isAdmin = false ;
            attemptNumber++ ;
        }
    }
    else
    {
        window.location.href = "../index.html" ;
    }
}