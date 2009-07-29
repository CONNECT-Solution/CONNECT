/*
*****************************************************************************************************
Author : Lea Smart
Source : www.totallysmartit.com
Date : 7/3/2001
DHTML Calendar
Version 1.2

You are free to use this code if you retain this header.
You do not need to link to my site (be nice though!)

Amendments
22 Jan 2002; Added ns resize bug code; rewrote date functions into Date 'class';
			 Added support for yyyy-mm-dd date format
			 Added support for calendar beginning on any day
7th Feb 2002 Fixed day highlight when year wasn't current year bug
9th Jun 2002 Fixed bug with weekend colour
			 Amended the code for the date functions extensions.  Shortened addDays code considerably
*****************************************************************************************************
Changes by Tanny O'Haley
22 Feb 2006 Cleaned up table code for calendar.
23 Feb 2006 Heavily modified for modern cross browser support.
			- Got rid of all the NS4 stuff and replaced it with modern DOM calls. 
			Still use innerHTML (it's just faster). 
			
			- Added code to create the popup container with DOM calls. This allows 
			me to append the container to the body when I need to and not before. 
			Especially not putting it in the head section like the original version.
			
			- Changed most global variable names so that they won't conflict with 
			varibles that may be used by other programmers.
24 Feb 2006 Modified generated table to be web standards complient.
13 Mar 2006 Added click event to calendar container that stops the click event from
			bubbling. Also added logic to close the month and year menus.
			- Changed logic that calculates to see if the calendar will drop below the
			bottom of the screen.
			- Changed page onclick handler to not calculate if it's on the calendar,
			but to just close the calendar.
			- Removed preload images since images are called from the CSS.
19 Mar 2006 Synchronized dp_dateFormat variable so that any changes to the date format are reflected
			in the dateparser.js code.
31 Aug 2006 Changed getFirstDOM function to set the day to 1 first then set the month and finally the year.
			This fixed a problem with the current day moving the month up one.

*****************************************************************************************************
*/
var cp_timeoutDelay = 2000; 	// milliseconds, change this if you like, set to 0 for the calendar to never auto disappear
var cp_timeoutId = false;	// used by timeout auto hide functions
var cp_location = 1;		// 0 = At the side of the button. 1 = At the bottom of the input field.
var g_startDay = 0;		// 0=sunday, 1=monday

var g_Calendar;  		// global to hold the calendar reference, set by constructor
//var daysOfWeek_global = "";
//var months_global ="";
//Global variables as per the locale in German, Japanese...etc
var cal_prev_text_global="";
var cal_next_text_global = "";
var cal_today_text_global ="";
var cal_month_text_global= "";
var cal_year_text_global="";

// constructor for calendar class
function Calendar() {
	g_Calendar = this;

	// some constants needed throughout the program
	//this.daysOfWeek = "Sun Mon Tue Wed Thu Fri Sat".split(" ");
	//this.months = "January February March April May June July August September October November December".split(" ");

	this.daysInMonth = new Array(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31); 
	this.containerLayer = null;
	this.dayMask = [1,1,1,1,1,1,1];
	this.Holidays = null;
	this.showHolidays = true;
}

Calendar.prototype.show = function(e, 
	                               target, 
								   dateFormat,
								   daysOfWeek_global,
								   months_global,
								   cal_prev_text,
								   cal_next_text,
								   cal_today_text,
								   cal_month_text,
								   cal_year_text,
								   dateFrom, dateTo, menuYearFrom, menuYearTo){
 cal_prev_text_global=cal_prev_text;
 cal_next_text_global = cal_next_text;
 cal_today_text_global =cal_today_text;
 cal_month_text_global= cal_month_text;
 cal_year_text_global=cal_year_text;
	
	dateFormat = dateFormat.toLowerCase();
 	
 	this.daysOfWeek = daysOfWeek_global.split(" ");
	this.months = months_global.split(" ");
    
	if(!document.getElementById) {
		alert("This browser is not suppored for a calendar popup.");
		return;
	}
///alert(target);
	if (!document.getElementById(target)) {
		alert("Error: input field \"" + target + "\" does not exist.");
		return;
	}

	this.containerLayer = document.getElementById('calcontainer');
	// if calendar popup container does not exist, create it.
	if(!this.containerLayer) {
		this.containerLayer = document.createElement('div');
		this.containerLayer.id = 'calcontainer';
		this.containerLayer.onmouseout=calendarTimeout;
		this.containerLayer.onmouseover=calendarClearTimeout;
		this.containerLayer.onclick=this.handleClick;
		document.body.insertBefore(this.containerLayer,document.body.childNodes[0]);
	}

	// If the container is visible, hide it and return. This is the second click on the button.
	 if (this.isVisible()) {
		this.hide();
		return;
	}

	calendarClearTimeout();

	// Get the source element that the user clicked on so that we can calculate the position
	// of the calendar.
	var elSrc;
	if (!e){
		e = window.event;
	}
	if (e.target) {
		elSrc = e.target;
	} else if (e.srcElement) {
		elSrc = e.srcElement;
	}

	// Don't allow the click event to go to any other element.
	e.cancelBubble = true;
	if (e.stopPropagation){
		e.stopPropagation();
	}

	// find the x/y position of the element that the user clicked on or the element that
	// will receive the date..
	var x, y, obj;
	
	x = y = 0;
	if (cp_location == 1) {
		// Use the element that will receive the date.
		obj = document.getElementById(target);
	} else {
		// Use the element that the user clicked on.
		obj = elSrc;
	}

	// Find the x/y position of the element.
	if(obj.offsetParent) {
		while (obj) {
			x += obj.offsetLeft;
			y += obj.offsetTop;
			obj = obj.offsetParent;
		}
	} else {
		x = obj.x;
		y = obj.y;
	}
	
	if (cp_location == 1) {
		// Get the scroll offset of the window.
		var yScroll;
		if (self.pageYOffset) // all except Explorer
		{
			yScroll = self.pageYOffset;
		}
		else if (document.documentElement && document.documentElement.scrollTop) // Explorer 6 Strict
		{
			yScroll = document.documentElement.scrollTop;
		}
		else if (document.body) // all other Explorers
		{
			yScroll = document.body.scrollTop;
		}

		this.containerLayer.style.left = x + "px";
		// if the calendar goes below the bottom of the page, pop the calendar up instead of down.
		if ((y+document.getElementById(target).offsetHeight+1+this.containerLayer.offsetHeight) > this.getWindowHeight() + yScroll) {
			this.containerLayer.style.top = ((y-this.containerLayer.offsetHeight)-1) + "px";
		} else {
			this.containerLayer.style.top = (y+document.getElementById(target).offsetHeight+1) + "px";
		}
	} else {
		// Position the calender to the right of the clicked element and aligned with the top of
		// the clicked element.
		this.containerLayer.style.left = (x+elSrc.offsetWidth+1) + "px";
		this.containerLayer.style.top = y + "px";
	}

	// calendar can restrict choices between 2 dates, if however no restrictions
	// are made, let them choose any date between 1900 and 3000
	this.dateFrom = dateFrom || new Date(1900,0,1);
	this.dateFromDay = this.padZero(this.dateFrom.getDate());
	this.dateFromMonth = this.padZero(this.dateFrom.getMonth());
	this.dateFromYear = this.dateFrom.getFullYear();
	this.dateTo = dateTo || new Date(3000,0,1);
	this.dateToDay = this.padZero(this.dateTo.getDate());
	this.dateToMonth = this.padZero(this.dateTo.getMonth());
	this.dateToYear = this.dateTo.getFullYear();

	this.dateToday = new Date();
	this.tYear = this.dateToday.getFullYear();
	this.tMonth = this.dateToday.getMonth();
	this.tDay = this.dateToday.getDate();

	// Is dateFormat provided?
	if (dateFormat){
		this.dateFormat = dateFormat;
	} else if (dp_dateFormat) {
		// if dp_dateFormat has been defined, use its format.
		this.dateFormat = dp_dateFormat;
	} else if (!this.dateFormat) {	// Has it been set previously?
		// If not, default date to US format.
		this.dateFormat = 'mm/dd/yyyy';
	}

	dp_dateFormat = this.dateFormat;	// Set the date parser format the same.

	// Get the date delimiter.
	if(this.dateFormat.indexOf('-') != -1) {
		this.dateDelim = '-';
	} else if(this.dateFormat.indexOf('/') != -1) {
		this.dateDelim = '/';
	} else if(this.dateFormat.indexOf('.') != -1) {
		this.dateDelim = '.';
	} else if(this.dateFormat.indexOf(' ') != -1) {
		this.dateDelim = ' ';
	} else {
		this.dateDelim = '/';
	}

	this.target = target;
	var tmp = document.getElementById(this.target);
	tmp.className=tmp.className.replace("error", "").replace("  ", " ");
	if (tmp && tmp.value && tmp.value.split(this.dateDelim).length==3){
		// There is a date in the target input field. Split the elements of the date into
		// an array.
		var atmp = tmp.value.split(this.dateDelim);
		var delim = eval('/\\' + this.dateDelim + '/g');
		switch (this.dateFormat.replace(delim,"")){
		case 'ddmmmyyyy':
		case 'dmmmyyyy':
			this.day = 1;
			this.year = this.oYear = parseInt(atmp[2],10);
			for (var i=0;i<this.months.length;i++){
				if (atmp[1].toLowerCase()==this.months[i].substr(0,3).toLowerCase()){
					this.month = this.oMonth = i;
					break;
				}
			}
			this.day = parseInt(atmp[0],10);
			break;

		case 'ddmmyyyy':
		case 'dmmyyyy':
		case 'dmyyyy':
			this.day = 1;
			this.year = this.oYear = parseInt(atmp[2],10);
			this.month = this.oMonth = parseInt(atmp[1]-1,10); 
			this.day = parseInt(atmp[0],10);
			break;

		case 'mmddyyyy':
		case 'mddyyyy':
		case 'mdyyyy':
			this.day = 1;
			this.year = this.oYear = parseInt(atmp[2],10);
			this.month = this.oMonth = parseInt(atmp[0]-1,10);
			this.day = parseInt(atmp[1],10);
			break;

		case 'yyyymmdd':
			this.day = 1;
			this.year = this.oYear = parseInt(atmp[0],10);
			this.month = this.oMonth = parseInt(atmp[1]-1,10);
			this.day = parseInt(atmp[2],10);
			break;
		}
	} else { // no date set, default to today
		var theDate = new Date();
		this.year = this.oYear = theDate.getFullYear();
		this.month = this.oMonth = theDate.getMonth();
		this.day = this.oDay = theDate.getDate();
	}
	
	// When to start the year menu.
	this.menuYearFrom = menuYearFrom || this.year - 3;
	// When to end the year menu.
	this.menuYearTo = menuYearTo || this.year + 3;
	var tmpCurYear = (new Date()).getFullYear();
	if(this.menuYearTo < tmpCurYear)
		this.menuYearTo = tmpCurYear;
	// Really is up to not to, so increment by one.
	this.menuYearTo++;
 	this.writeString(this.buildString(cal_prev_text_global,
								   cal_next_text_global,
								   cal_today_text_global,
								   cal_month_text_global,
								   cal_year_text_global
));

	this.containerLayer.style.visibility='visible';
};

Calendar.prototype.buildString = function(cal_prev_text,
								   cal_next_text,
								   cal_today_text,
								   cal_month_text,
								   cal_year_text
){
 
	var tmpStr = '';
	
	// See if there are holidays for this month. Save the holidays to a temporary array
	// so that you only have to look at holidays for this month when building the calendar.
	// This is to improve performance.
	var aHolidays = new Array();
	if(this.Holidays) {
		for(var h = 0; h < this.Holidays.length; h++) {
			if(this.Holidays[h].date.getFullYear() == this.year && this.Holidays[h].date.getMonth() == this.month) {
				aHolidays[aHolidays.length] = this.Holidays[h];
			}
		}
	}
	
	if (document.all&&document.getElementById && !window.Opera){
		// Add a shim to hide select items for drop down menus.
		 if (navigator.appVersion.substr(22,3)!="5.0"){
		 	tmpStr = '<iframe src="blank.html" scrolling="no" frameborder="0" style="width: ' + this.containerLayer.offsetWidth + 'px; height: ' + this.containerLayer.offsetHeight + 'px; z-index: -1; position: absolute; filter: progid:DXImageTransform.Microsoft.Alpha(style=0,opacity=0);"></iframe>';
		 }
	}
    tmpStr = '<iframe src="blank.html" scrolling="no" frameborder="-1" style="width: ' + this.containerLayer.offsetWidth + 'px; height: ' + this.containerLayer.offsetHeight + 'px;z-index: -1; position: absolute;"></iframe>';
	tmpStr += '<table cellspacing="0">';
	tmpStr += '<thead><tr><td colspan="7"><ul>\n'+
			'<li id="cp_prevMonth"><a href="javascript:g_Calendar.changeMonth(-1)" title="'+cal_prev_text+' '+cal_month_text+'"><span>&lt;</span></a></li>\n'+
			'<li id="cp_monthMenu">' + this.months[this.month] + '\n' +
			'<ul id="cp_months"><li><a href="javascript:g_Calendar.clickMonth(0)">January</a></li><li><a href="javascript:g_Calendar.clickMonth(1)">February</a></li><li><a href="javascript:g_Calendar.clickMonth(2)">March</a></li><li><a href="javascript:g_Calendar.clickMonth(3)">April</a></li><li><a href="javascript:g_Calendar.clickMonth(4)">May</a></li><li><a href="javascript:g_Calendar.clickMonth(5)">June</a></li><li><a href="javascript:g_Calendar.clickMonth(6)">July</a></li><li><a href="javascript:g_Calendar.clickMonth(7)">August</a></li><li><a href="javascript:g_Calendar.clickMonth(8)">September</a></li><li><a href="javascript:g_Calendar.clickMonth(9)">October</a></li><li><a href="javascript:g_Calendar.clickMonth(10)">November</a></li><li><a href="javascript:g_Calendar.clickMonth(11)">December</a></li></ul></li>\n' +
			'<li id="cp_nextMonth"><a href="javascript:g_Calendar.changeMonth(1)" title="'+cal_next_text+' '+cal_month_text+'"><span>&gt;</span></a></li>\n' +
			'<li id="cp_prevYear"><a href="javascript:g_Calendar.changeYear(-1)" title="'+cal_prev_text+' '+cal_year_text+'"><span>&lt;</span></a></li>\n' +
			'<li id="cp_yearMenu">' + this.year + '\n';

	if(this.menuYearTo - this.menuYearFrom > 7){
		// I hate to do this with an inline style. I just couldn't get it to work with a class.
		tmpStr += '<ul id="cp_years" style="overflow: auto; overflow-x: hidden; overflow-y: auto;">';
	}else{
		tmpStr += '<ul id="cp_years">';
	}

	// Make sure that the year menu goes to at least the currently selected year.
	var menuYearTo = this.menuYearTo;
	if(menuYearTo <= this.year)
		menuYearTo = this.year + 1;

	// Create the year menu.
	for (var y = this.menuYearFrom; y < menuYearTo; y++) {
		tmpStr += '<li><a href="javascript:g_Calendar.clickYear(' + y + ')">' + y + '</a></li>';
	}

	tmpStr += '</ul></li>\n<li id="cp_nextYear"><a href="javascript:g_Calendar.changeYear(1)" title="'+cal_next_text+' '+cal_year_text+'"><span>&gt;</span></a></li>\n</ul></td></tr>';

	var iCount = 1;

	var iFirstDOM = (7+this.getFirstDOM()-g_startDay)%7; // to prevent calling it in a loop

	var iDaysInMonth = this.getDaysInMonth(); // to prevent calling it in a loop

	tmpStr += '<tr>';
	for (var i=0;i<7;i++){
		tmpStr += '<th>' + this.daysOfWeek[(g_startDay+i)%7] + '</th>';
	}

	var dtToday = new Date();
         

	tmpStr += '</tr></thead><tfoot><tr><td colspan="7">'+cal_today_text+ '<a href="javascript:g_Calendar.clickToday()" >' + (/^d/.test(this.dateFormat)?dtToday.getDate() + ' ' + this.months[dtToday.getMonth()] + ' ':this.months[dtToday.getMonth()] + ' ' + dtToday.getDate() + ', ') + dtToday.getFullYear()+'</a></td></tr></tfoot><tbody>';
	var tmpFrom = parseInt('' + this.dateFromYear + this.dateFromMonth + this.dateFromDay,10);
	var tmpTo = parseInt('' + this.dateToYear + this.dateToMonth + this.dateToDay,10);
	var tmpCompare;
	for (var j=1;j<=6;j++){
		tmpStr += '<tr>';
		for (i=1;i<=7;i++){
			// Variable that determines if the day is clickable.
			var bShowDay = true;
			// If the day is a holiday, set the title to show a description of the holiday.
			var sTitle = '';
			// Look to see if this day is a holiday.
			for(h = 0; h < aHolidays.length; h++) {
				if(aHolidays[h].date.getDate() == iCount) {
					if(!this.showHolidays)
						bShowDay = false;
					sTitle = aHolidays[h].desc;
				}
			}

			tmpStr += '<td ';
			if ( (7*(j-1) + i)>=iFirstDOM+1  && iCount <= iDaysInMonth){
				if (iCount==this.day && this.year==this.oYear && this.month==this.oMonth){
					tmpStr += 'class="calHighlightColor"';
				} else if (i==7-g_startDay || i==((7-g_startDay)%7)+1) {
						tmpStr += 'class="calWeekend"';
				}
				tmpStr += '>';
				
				if(this.dayMask[i-1] == 0) {
					bShowDay = false;
				} else {
					/* could create a date object here and compare that but probably more efficient to convert to a number
					and compare number as numbers are primitives */
					tmpCompare = parseInt('' + this.year + this.padZero(this.month) + this.padZero(iCount),10);
					if (!(tmpCompare >= tmpFrom && tmpCompare <= tmpTo)) {
						bShowDay = false;
					}
				}
				
				if(bShowDay) {
					if (iCount==this.tDay && this.year==this.tYear && this.month==this.tMonth) {
						tmpStr += '<a class="today"' + (sTitle == ''?'':' title="' + sTitle + '"') + ' href="javascript: g_Calendar.clickDay(' + iCount + ');"><strong>' + iCount + '</strong></a>';
					} else {
						tmpStr += '<a' + (sTitle == ''?'':' title="' + sTitle + '"') + ' href="javascript: g_Calendar.clickDay(' + iCount + ');">' + iCount + '</a>';
					}
				} else {
					if (iCount==this.tDay && this.year==this.tYear && this.month==this.tMonth) {
						tmpStr += '<span' + (sTitle == ''?'':' title="' + sTitle + '"') + ' class="disabled today"><strong>' + iCount + '</strong></span>';
					} else {
						tmpStr += '<span' + (sTitle == ''?'':' title="' + sTitle + '"') + ' class="disabled">' + iCount + '</span>';
					}
				}
				iCount++;
			} else {
				if  (i==7-g_startDay || i==((7-g_startDay)%7)+1) {
					tmpStr += 'class="calWeekend"';
				}
				tmpStr += (sTitle == ''?'':' title="' + sTitle + '"') + '>&nbsp;';
			}
			tmpStr += '</td>';
		}
		tmpStr += '</tr>';
	}
	tmpStr += '</tbody>';
	tmpStr += '</table>';

// debugging
//	document.getElementById("tarea").value = tmpStr;
//	alert(tmpStr);

	return tmpStr;
};

Calendar.prototype.setDateFormat = function(dateFormat){
	// Default date to US format.
	this.dateFormat = dateFormat || 'm/d/yyyy';
	dp_dateFormat = this.dateFormat;
};

Calendar.prototype.setDayMask = function(mask){
	var j = this.dayMask.length > mask.length?mask.length:this.dayMask.length;
	for(var i = 0; i < j; i++)
		this.dayMask[i] = mask[i];
	return this.dayMask;
};

Calendar.prototype.setHolidays = function(obj, bShow){
	this.Holidays = obj;
	this.showHolidays = bShow;

	return obj;
};

Calendar.prototype.getFirstDOM = function() {
	var thedate = new Date();
	
	thedate.setDate(1);
	thedate.setMonth(this.month);
	thedate.setFullYear(this.year);

	return thedate.getDay();
};

Calendar.prototype.getDaysInMonth = function (){
	// If not February return from the daysInMonth array.
	if (this.month!=1){
		return this.daysInMonth[this.month];
	}

	// Is it a leap year?
	if (this.isLeapYear(this.year)){
		return 29;
	}

	return 28;
};

Calendar.prototype.isLeapYear = function(year) {
	if (year%4==0 && ((year%100!=0) || (year%400==0))) {
		return true;
	} else {
		return false;
	}
};

Calendar.prototype.selectChange = function(el) {
	this.month = el.selectedIndex;
	this.writeString(this.buildString(cal_prev_text_global,
								   cal_next_text_global,
								   cal_today_text_global,
								   cal_month_text_global,
								   cal_year_text_global
));
};

Calendar.prototype.inputChange = function(el) {
	var tmp = el;
	if (tmp.value >=1900 && tmp.value <=2100) {
		this.year = tmp.value;
	this.writeString(this.buildString(cal_prev_text_global,
								   cal_next_text_global,
								   cal_today_text_global,
								   cal_month_text_global,
								   cal_year_text_global
));
	} else {
		tmp.value = this.year;
	}
};

Calendar.prototype.changeYear = function(incr) {
	if (incr==1){
		this.year++;
	} else {
		this.year--;
	}

	this.writeString(this.buildString(cal_prev_text_global,
								   cal_next_text_global,
								   cal_today_text_global,
								   cal_month_text_global,
								   cal_year_text_global
));
};

Calendar.prototype.changeMonth = function(incr){ 
	if (this.month==11 && incr==1) {
		this.month = 0;
		this.year++;
	} else if (this.month==0 && incr==-1) {
			this.month = 11;
			this.year--;
	} else if (incr==1) {
		this.month++;
	} else {
		this.month--;
	}

	this.writeString(this.buildString(cal_prev_text_global,
								   cal_next_text_global,
								   cal_today_text_global,
								   cal_month_text_global,
								   cal_year_text_global
));
};

Calendar.prototype.clickDay = function(day){
	var el = document.getElementById(this.target);

	if(el){
		el.value = this.formatDateAsString(day,this.month,this.year);
		if(magicDate) {
			magicDate(el);
		}
		el.focus();
	}

	this.hide();
};

Calendar.prototype.clickMonth = function(month){
	this.month = month;
	this.writeString(this.buildString(cal_prev_text_global,
								   cal_next_text_global,
								   cal_today_text_global,
								   cal_month_text_global,
								   cal_year_text_global
));
};

Calendar.prototype.clickYear = function(year){
	this.year = year;
	this.writeString(this.buildString(cal_prev_text_global,
								   cal_next_text_global,
								   cal_today_text_global,
								   cal_month_text_global,
								   cal_year_text_global
));
};

Calendar.prototype.clickToday = function(){
	var theDate = new Date();
	this.year = theDate.getFullYear();
	this.month = theDate.getMonth();
	this.writeString(this.buildString(cal_prev_text_global,
								   cal_next_text_global,
								   cal_today_text_global,
								   cal_month_text_global,
								   cal_year_text_global
));

	g_Calendar.clickDay(theDate.getDate());
 };

Calendar.prototype.formatDateAsString = function(day, month, year){
	var delim = eval('/\\' + this.dateDelim + '/g');
	switch (this.dateFormat.replace(delim,"")){
	case 'ddmmmyyyy':
		return	dp_padZero(day) + '-' +  monthNames[month].substr(0,3) + '-' + year;
	//case 'ddmmmyyyy':
	//	return this.padZero(day) + this.dateDelim + this.months[month].substr(0,3) + this.dateDelim + year;
	case 'dmmmyyyy':
		return day + this.dateDelim + this.months[month].substr(0,3) + this.dateDelim + year;
	case 'ddmmyyyy':
		return this.padZero(day) + this.dateDelim + this.padZero(month+1) + this.dateDelim + year;
	case 'dmmyyyy':
		return day + this.dateDelim + this.padZero(month+1) + this.dateDelim + year;
	case 'mmddyyyy':
		return this.padZero((month+1)) + this.dateDelim + this.padZero(day) + this.dateDelim + year;
	case 'mddyyyy':
		return (month+1) + this.dateDelim + this.padZero(day) + this.dateDelim + year;
	case 'mdyyyy':
		return (month+1) + this.dateDelim + day + this.dateDelim + year;
	case 'yyyymmdd':
		return year + this.dateDelim + this.padZero(month+1) + this.dateDelim + this.padZero(day);
	default:
		return (month+1) + this.dateDelim + this.padZero(day) + this.dateDelim + year;
	}
};

Calendar.prototype.writeString = function(str){
	this.containerLayer.innerHTML = str;
};

Calendar.prototype.hide = function(){
	calendarClearTimeout();
	this.containerLayer.style.visibility='hidden';
};

Calendar.prototype.isVisible = function() {
	return this.containerLayer.style.visibility=='visible' ? true : false;
};

Calendar.prototype.isHidden = function() {
	return this.containerLayer.style.visibility=='hidden' ? true : false;
};

Calendar.prototype.padZero = function(num) {
	return ((num <= 9) ? ("0" + num) : num);
};

Calendar.prototype.getWindowHeight = function() {
	var windowHeight=0;

	if (typeof(window.innerHeight)=='number') {
		windowHeight=window.innerHeight;
	} else if (document.documentElement && document.documentElement.clientHeight) {
		windowHeight=document.documentElement.clientHeight;
	} else if (document.body && document.body.clientHeight) {
		windowHeight=document.body.clientHeight;
	}

	return windowHeight;
};

Calendar.prototype.openMenu = function(e) {
	var el = document.getElementById(e);
	if(!el) {
		return;
	}

	if(el.className == "open") {
		el.className = "";
	} else {
		el.className = "open";
	}
};
Calendar.prototype.closeMenu = function(e) {
	var el = document.getElementById(e);
	if(!el) {
		return;
	}

	el.className = "";
};
Calendar.prototype.openMenuMonths = function() {
	this.closeMenu('cp_years');
	this.openMenu('cp_months');
};
Calendar.prototype.openMenuYears = function() {
	this.closeMenu('cp_months');
	this.openMenu('cp_years');
};
Calendar.prototype.closeMenus = function() {
	this.closeMenu('cp_years');
	this.closeMenu('cp_months');
};

Calendar.prototype.handleClick = function(e)
{
	if (!e){
		e = window.event;
	}
	if (e.target){
		elSrc = e.target;
	}else if (e.srcElement){
		elSrc = e.srcElement;
	}

	// Get the x/y position of the mouse.
	if (e.pageX || e.pageY) {
		x = e.pageX;
		y = e.pageY;
	} else if (e.clientX || e.clientY) {
		x = e.clientX + document.body.scrollLeft;
		y = e.clientY + document.body.scrollTop;
	}

	// For some reason this doesn't give me access to the container,
	// so I'm using the global variable instead.
	if(y > parseInt(g_Calendar.containerLayer.style.top,10) + 20) {
		g_Calendar.closeMenus();
	}

	e.cancelBubble = true;
	if (e.stopPropagation){
		e.stopPropagation();
	}
};
// End Calendar Class

// Utility functions
function calendarTimeout() {
	if(cp_timeoutDelay) {
		cp_timeoutId = setTimeout('g_Calendar.hide();',cp_timeoutDelay);
	}
}

function calendarClearTimeout() {
	if (cp_timeoutId) {
		clearTimeout(cp_timeoutId);
	}
}

function cp_handleDocumentClick(e){
	var elSrc, x, y;

	if(!g_Calendar.containerLayer){
		return;
	}

	if(g_Calendar.isHidden()) {
		return;
	}

	g_Calendar.hide();
        
}

Date.prototype.addDays = function(num){
	return new Date((num*(1000*60*60*24))+this.valueOf());
};

new Calendar(new Date());

window.document.onclick=cp_handleDocumentClick;
/*
window.onerror = function(msg,url,line){
	alert('******* an error has occurred ********' +
		'\n\nPlease check that' + 
		'\n\n- You have set the parameters correctly in the g_Calendar.show() method ' +
		'\n\nSee www.totallysmartit.com\\examples\\calendar\\simple.asp for examples' +
		'\n\n------------------------------------------------------' +
		'\nError details' +
		'\nText:' + msg + '\nurl:' + url + '\nline:' + line);
};
*/
