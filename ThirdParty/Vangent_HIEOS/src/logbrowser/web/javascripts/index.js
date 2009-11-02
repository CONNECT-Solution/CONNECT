		var host ;
		var user  ;
		var pass  ;


		var date1 = "0" ;
		var date2 = "0" ;
		var date  = "0" ;
		
		var currentPage  = 0      ;
		var nbByPage = 50         ;
		
	    var ptrFuncGetMessageArray         = getMessageArray        ;
	    var ptrFuncGetDate                 = getDate                ;
	    var ptrFuncGetMessage              = getMessage             ;
        var ptrFuncGetCompanyList          = getCompanyList         ;
        var ptrFuncGetTestList             = getTestList            ;  
	    var isAdmin                        = null                   ;
 
function init()
{

   $('CenterPanel').style.left  = '48%'   ;
   getIsAdmin() ;
}
function init_()
{        
          Element.hide('filterTab'); 
		  Element.hide('displayTab'); 
		  
        if ( isAdmin )
         {
           $('status').innerHTML = "<p style='color:red;'>Connected as admin &nbsp;&nbsp;&nbsp; <div id='options'><a href='#' onclick='logout();'>Logout</a><br/>"+
           "<a href='admin/pwd.html'>Change Password</a><br /><a href='admin/fastDelete.html'>Fast Delete Messages</a></div> </p>" ;
           ptrFuncGetCompanyList() ;
         }
         else
         {
         
            $('ipLabel').style.display      = "none" ;
            $('ip').style.display           = "none" ;
            $('company').style.display      = "none" ;
            $('company').value              = ""     ;
            $('cancelCompany').style.display= "none" ; 
         }

         
          performAdvancedSearch(  ) ;

		
		if (!isIE3Mac && is.gecko)
		{
	 
		}
		else if  (!isIE3Mac && is.nav4)
		{
		  alert ( "This application has not been tested on this browser!"); 
		}
		else if (!isIE3Mac && is.ie5up)
		{
	
		    var screenW = screen.width;
            var screenH = screen.height;
            
            $('Panel').style.height = (screenH - 250 )+"px" ;
            $('CenterPanel').style.height = (screenH - 300 )+"px" ;
		}
		else if (!isIE3Mac && (is.nav3 || is.opera))
		{
		  alert ( "This application has not been tested on this browser!"); 
		}
		else 
		{
		   alert ( "This application has not been tested on this browser!"); 
		}
		
		  
		  Element.show('filterTab'); 
		  Element.show('displayTab'); 
		  
		  hidePreferenceTab('filterTab','tab2');
		  hidePreferenceTab('displayTab','tab3');
		  
		  ptrFuncGetDate () ;
		  ptrFuncGetTestList( ipOfClient ) ;
}
	    
function changeDateSearchType( i )
{
 if ( i==1 )
 {
  date1 = "0" ;
  date2 = "0" ;
  $('date1').value = "0" ;
  $('date2').value = "0" ;
  date = $('date').value ; 

 }
 else if ( i == 2 )
 {
  date  = "0" ;
  $('date').value = "0" ;
  date1 =$('date1').value ;
  date2 =$('date2').value;
 }
}

function reinitFields ()
{
  $('company').value = "" ;
  $('ip').value      = "" ;
  $('pass').value    = "" ;
  $('test').value    = "" ;
  
  currentPage  = 0        ;	
  $('nbByPage').value = nbByPage ;	
  $('date').value = "0"  ; date  = "0" ; 
  $('date1').value = "0" ; date1 = "0" ;
  $('date2').value = "0" ; date2 = "0" ;
  		
}


// Removes leading whitespaces
function LTrim( value ) {
	
	var re = /\s*((\S+\s*)*)/;
	return value.replace(re, "$1");
	
}

// Removes ending whitespaces
function RTrim( value ) {
	
	var re = /((\s*\S+)*)\s*/;
	return value.replace(re, "$1");
	
}

// Removes leading and ending whitespaces
function trim( value ) {
	
	return LTrim(RTrim(value));
	
}

	    