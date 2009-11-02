
function gup( name )
{
  var regexS = "[\\?&]"+name+"=([^&#]*)";
  var regex = new RegExp( regexS );
  var tmpURL = window.location.href;
  var results = regex.exec( tmpURL );
  if( results == null )
    return "";
  else
    return results[1];
}



function Connection()
{
	this.user = "";
	this.passwd = "";
	this.dbhost = "";
}
var connection = new Connection(); // Create new connection object
var timeref = null; // Holds reference to the setInterval used in the auto-update


function showHide(id ) // Shows or hides an element based on the passed id, used for the expandable forms
{
	if ( $(id).style.display == "none" ||
	$(id).style.display == "" )
	{
		$(id).style.display = "block";
	}
	else
	{
		$(id).style.display = "none";
	}

}
function hidePreferenceTab( id , idButton  ) 
{
  var obj = $(id) ;
  var obj2 = $(idButton) ;
   
  if ( obj.offsetLeft == 0 )
  {
  obj.style.left     =  (obj.offsetLeft - obj2.offsetLeft  )+ 'px';
  }
}
function showHidePreferenceTab( id , idButton  ) // Shows or hides an element based on the passed id, used for the expandable forms
{
    var obj = $(id) ;
    var obj2 = $(idButton) ;
 

  if ( obj.offsetLeft >= 0 )
    {
       obj.style.left     =  (obj.offsetLeft - obj2.offsetLeft  )+ 'px';
    }
    else if ( obj.offsetLeft < 0 )
    {
       obj.style.left = '0px';
    }
}

function showTab(id)  // hides all tabs and then shows the tab with the passed id
{
   currentTab= id ;
 
	if ( gup('target')=="" )
	{
		 
			var tabs = tablesName ; // defined in GetMessageNew 
	 
	}
	else
	{
		var target = gup('target') ;
 
			var tabs = tablesName ;  
	}
	for ( var i = 0; i < tabs.length; i++ )
	{
		if ( $(tabs[i])!=null )
		{
		$(tabs[i]).style.display = "none";
		$(tabs[i]+'tab').style.background = "white";
		}
	}
	$(id).style.display = "inline";
	$(id+'tab').style.background = "#DEF";
}
function panelOnLeft()
	 {
	
	 if ( $('CenterPanel').style.left < '52%' )
	 {
	   // alert ( "case 1");
	  $('LeftPanel').style.display = 'none' ;
	  $('LeftPanel').style.left    = '-100%';
	  $('LeftPanel').style.width   = '0px'  ;
	  $('LeftPanel').style.zIndex  =  0     ;
	  $('LeftArr').style.display   = 'none' ; 
	  $('CenterPanel').style.left  = '0%'   ;
	  $('RightPanel').style.left   = '4%'   ;
	  $('RightPanel').style.width  = '95%'  ;
	  $('RightPanel').style.zIndex =  1     ; 
	
	  }
	  else
	  {
	   // alert ( "case 2");
	  $('RightPanel').style.display = 'block' ;
	  $('RightArr').style.display   = 'block' ;
	  $('RightPanel').style.left    = '50%'   ; 
	  $('RightPanel').style.width   = '49%'   ; 
	  $('RightPanel').style.zIndex  =  0      ; 
	  $('CenterPanel').style.left   = '48%'   ;
	  $('LeftPanel').style.left     = '2%'    ;
	  $('LeftPanel').style.width    = '46%'   ;
	  $('LeftPanel').style.zIndex   =  0      ;	  
	  }
 }
	 
function panelOnRight()
{
	  if ( $('CenterPanel').style.left < '48%' )
	  {
	 //  alert ( "case 3 > " + $('CenterPanel').style.left );
	  $('LeftPanel').style.display = 'block' ;
	  $('LeftPanel').style.width   = '46%'   ;
	  $('LeftPanel').style.left    = '2%'    ;
	  $('LeftPanel').style.zIndex  =   0     ;
	  $('LeftArr').style.display   = 'block' ; 
	  $('CenterPanel').style.left  = '48%'   ;
	  $('RightPanel').style.left   = '50%'   ;
	  $('RightPanel').style.width  = '49%'   ;
	  $('RightPanel').style.zIndex =    0    ;
	 
	  }
	  else 
	  {
	   //alert ( "case 4 >" + $('CenterPanel').style.left );
	  $('RightPanel').style.display = 'none' ;
	  $('RightPanel').style.left    = '100%' ;
	  $('RightPanel').style.width   = '0px'  ;
	  $('RightArr').style.display   = 'none' ;
	  $('RightPanel').style.zIndex  =   0    ; 
	  $('CenterPanel').style.left   = '98%'  ;
	  $('LeftPanel').style.left     = '2%'   ;
	  $('LeftPanel').style.width    = '96%'  ;
	  $('LeftPanel').style.zIndex   =   1    ;
	  }
}
function showWindowFixed(id)
{ 
	var win = document.getElementById(id); 
	if (!document.getElementById(id + "_child")) 
	{ 
		win.innerHTML = "<div style='overflow:auto;' id='" + id + "_child'>" + win.innerHTML + "</div>"; 
	} 
		win.style.visibility = "visible"; 
		win.style.display = "inline";
}


