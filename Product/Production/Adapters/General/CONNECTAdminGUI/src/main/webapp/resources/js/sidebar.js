// JavaScript Document

$(document).ready(function() {
	
	if ($("#sidebar-main").length) {
		var stringColor = $('div[class*="tabs-ovr-"]').attr('class'); // GET THE FULL STRING OF THE CLASSNAMES CONTAINING THE COLOR CLASS
		var whichColor = stringColor.substring(stringColor.indexOf("tabs-ovr-")+9); // GET SUBSTRING OF JUST THE COLOR
		var movethis = $('.sidebar div[class*=sidebar-'+whichColor+']'); // FIND ID OF SIDEBAR MENU'S CONTAINING DIV THAT HAS THE ACTIVE COLOR
		
		var indexTab = $('ul.ui-tabs-nav li.ui-tabs-selected').index(); // GET THE INDEX NUMBER OF THE SELECTED TAB
		var showActivebar = $('.sidebar div[class*=sidebar-'+whichColor+'] li').eq(indexTab); // FIND SIDEBAR LI TO MATCH SELECTED TAB
		$('.sidebar li').not(showActivebar).removeClass('active'); // REMOVE ACTIVE CLASS OF OTHER LINK LI'S AS NEEDED
		$(showActivebar).addClass('active'); // ADD ACTIVE CLASS TO NEWLY SELECTED LINK LI
		
		$('div.ui-panelmenu').prepend(movethis); // MOVE SIDEBAR MENU SECTION CONTAINING ACTIVE LINK TO TOP OF SIDEBAR
		$('.sidebar div[class*=sidebar-'+whichColor+'] .ui-panelmenu-content').css("display","block"); // OPEN THE MENU BLOCK WITH THE ACTIVE LI
		$('.sidebar div[class*=sidebar-'+whichColor+'] .ui-panelmenu-header .ui-icon-triangle-1-e').removeClass("ui-icon-triangle-1-e").addClass("ui-icon-triangle-1-s"); // EXPAND THE TRIANGLE
    }
    else {
        return false;
    };
	
	if (window.attachEvent && !window.addEventListener) { // CHECK FOR OLDER BROWSER
		$('.sidebar-scroll').perfectScrollbar({ // SET PERFECT-SCROLLBAR TO SCROLL FASTER FOR OLDER INTERNET EXPLORER
			wheelSpeed:80,
			suppressScrollX:true
		});
	} else {
		$('.sidebar-scroll').perfectScrollbar({ // DEFAULT PERFECT-SCROLLBAR
			suppressScrollX:true
		});
	};
	
	// == BEGIN: AFTER TAB CLICKED, MODIFY ACTIVE STATE OF SIDEBAR MENU BASED ON WHICH TAB == //
	$('ul.ui-tabs-nav li').click(function () {
		var stringColor = $('div[class*="tabs-ovr-"]').attr('class'); // GET THE FULL STRING OF THE CLASSNAMES CONTAINING THE COLOR CLASS
		var whichColor = stringColor.substring(stringColor.indexOf("tabs-ovr-")+9); // GET SUBSTRING OF JUST THE COLOR
		var indexTab = $(this).index(); // GET THE INDEX NUMBER OF THE SELECTED TAB
		var showActivebar = $('.sidebar div[class*=sidebar-'+whichColor+'] li').eq(indexTab); // FIND SIDEBAR LI TO MATCH SELECTED TAB
		$('.sidebar li').not(showActivebar).removeClass('active'); // REMOVE ACTIVE CLASS OF OTHER LINK LI'S AS NEEDED
		$(showActivebar).addClass('active'); // ADD ACTIVE CLASS TO NEWLY SELECTED LINK LI
		// == BEGIN: FOR INTERNET EXPLORER AND OLDER, REDRAW SIDEBAR MENU TO MAKE SURE THE BUTTONS ARE VISUALLY UPDATED
		if (window.attachEvent && !window.addEventListener) { // CHECK FOR OLDER BROWSER
			var tempHolder = $('.sidebar div[class*=sidebar-'+whichColor+']').html(); // GET THE HTML
			$('.sidebar div[class*=sidebar-'+whichColor+']').html(tempHolder); // REDRAW THE HTML
		}
		// == END: FOR INTERNET EXPLORER AND OLDER, REDRAW SIDEBAR MENU TO MAKE SURE THE BUTTONS ARE VISUALLY UPDATED
	});
	// == END: AFTER TAB CLICKED CLICKED, MODIFY ACTIVE STATE OF SIDEBAR MENU BASED ON WHICH TAB == //
	
	// == BEGIN: AFTER SIDEBAR LINK CLICKED, MODIFY ACTIVE STATE OF SIDEBAR MENU LINK == //
	$('.sidebar .ui-menu-list li').click(function () {
		$('.sidebar li').not(this).removeClass('active'); // REMOVE ACTIVE CLASS OF OTHER LINK LI'S AS NEEDED
		$(this).addClass('active'); // ADD ACTIVE CLASS TO NEWLY SELECTED LINK LI
	});
	// == END: AFTER SIDEBAR LINK, MODIFY ACTIVE STATE OF SIDEBAR MENU LINK == //
	
}); // CLOSE THE DOCUMENT.READY