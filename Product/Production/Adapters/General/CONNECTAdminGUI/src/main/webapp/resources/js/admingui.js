// JavaScript Document

window.prefix = "tab_"; // USING PREFIX VAR TO PREVENT BROWSER AUTOMATIC SCROLL TO THE TARGET ELEMENT

// FUNCTION TO SHOW THE CORRECT TABBED CONTENT IF NAVIGATING FROM DIFFERENT PAGE
$(document).ready(function() {
	var url = window.location.hash;
	if (url != "") {
		$('#nav-section-tabs a[href='+url.replace(prefix,"")+']').tab('show'); // REMOVE PREFIX and CHANGE TO CORRECT TAB
		SidebarNavStyle(url);
		var movethis = $('a[href$='+url+']').closest('div[id^="sidenav-"]'); // FIND ID OF SIDEBAR MENU'S CONTAINING DIV THAT MATCHES SELECTED TAB
		$(movethis).insertAfter("h1.page-header"); // MOVE SIDEBAR MENU SECTION MATCHING SELECTED TAB TO TOP OF SIDEBAR
	}
	else {
		var movethis = $('.nav-sidebar li.active').closest('div[id^="sidenav-"]'); // FIND ID OF SIDEBAR MENU'S CONTAINING DIV THAT HAS THE ACTIVE LINK
		$(movethis).insertAfter("h1.page-header"); // MOVE SIDEBAR MENU SECTION CONTAINING ACTIVE LINK TO TOP OF SIDEBAR
	};
//}); // ORIGINAL CLOSE THE DOCUMENT.READY

	// FUNCTION FOR SIDEBAR NAV - TOGGLE ACTIVE CLASS and CHANGE SELECTED TAB IN MAIN CONTENT AREA
	$('.sidebar li').click(function () {
		var showActivebar = $(this); // STORE ID OF SELECTED SIDEBAR LINK LI
		$('.sidebar li').not(showActivebar).removeClass('active'); // REMOVE ACTIVE CLASS OF OTHER LINK LI'S AS NEEDED
		$(showActivebar).addClass('active'); // ADD ACTIVE CLASS TO NEWLY SELECTED LINK LI
	
		var urlFull = $(this).find('a').attr('href'); // CATCH HREF FROM SIDEBAR LINK
		var url = urlFull.substring(urlFull.indexOf("#")); // GET JUST THE URL STRING STARTING AT THE HASH MARK
		if (url.match('#')) {
			$('.nav-section-tabs a[href='+url.replace(prefix,"")+']').tab('show') ; // REMOVE PREFIX and CHANGE TAB TO MATCH SELECTED SIDEBAR LINK
		}
	});
	
	// FUNCTION FOR TABBED NAV IN MAIN CONTENT AREA - CHANGE SELECTED SIDEBAR LINK
	$('.nav-section-tabs a').click(function () {
		var urlTest = $(this).attr('href').replace("#","#tab_"); // CATCH HREF FROM SELECTED TAB and ADD PREFIX TO FIND CORRECT SIDEBAR LINK
		if (urlTest.indexOf('domain-') >= 0) {
			var url = "#tab_domains";
		}
		else {
			var url = urlTest;
		}
		SidebarNavStyle(url);
	});
	
	// FUNCTION FOR DROPDOWN NAV IN UTILITY BAR - CHANGE SELECTED SIDEBAR LINK and TAB
	$('.nav-utility .dropdown-menu a').click(function () {
		var url = $(this).attr('href'); // CATCH HREF FROM SELECTED DROPDOWN ITEM
		SidebarNavStyle(url);
		$('.nav-section-tabs a[href='+url.replace(prefix,"")+']').tab('show') ; // REMOVE PREFIX and CHANGE TAB TO MATCH SELECTED DROPDOWN ITEM
	});
	
	// SUB-ROUTINE FOR SETTING ACTIVE CLASS ON SIDEBAR NAV
	function SidebarNavStyle(url) {
		var showActivebar = $('.sidebar a[href$='+url+']'); // FIND SIDEBAR LINK TO MATCH SELECTED TAB
		$('.sidebar li').not(showActivebar).removeClass('active'); // REMOVE ACTIVE CLASS OF OTHER LINK LI'S AS NEEDED
		$(showActivebar).parent().addClass('active'); // ADD ACTIVE CLASS TO NEWLY SELECTED LINK LI
	}
	
	// FUNCTION TO EXPAND/CONTRACT SIDEBAR NAV SECTIONS
	$('.sidebar h2.section-header').click(function () {
		var targetMenu = $(this).next('ul').attr('id');
		var activeLink = $(this).next('ul').children('.active').attr('id');
	
		$(this).find('i').toggleClass('glyphicon-plus glyphicon-minus');
		$(this).next('ul').slideToggle(400);
		if ($(targetMenu).is(":visible"))
			$(activeLink).addClass("active");
	});

	// FUNCTION TO ACTIVATE TABLE SORTING ABILITY
	$(function(){
		$("table").tablesorter();
	});

}); // CLOSE THE DOCUMENT.READY