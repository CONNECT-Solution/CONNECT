/* 'Magic' date parsing, by Simon Willison (6th October 2003)
   http://simon.incutio.com/archive/2003/10/06/betterDateInput

Modifications By Tanny O'Haley
24 Feb 2006 Changed order of setting date items from day, month, year
		to year, month, day. When a user put in "31 jan" and the 
		current month did not end in 31 the date first be put to the
		next month then the entered date. If today was 27 Feb and
		the user put in 31 jan the date first be changed to 3 Mar,
		then the month would be set to Jan. The date displayed
		would be 3 Jan instead of 31 Jan. By changing the order
		the date is set correctly.
27 Feb 2006 Added code to display the dates in formats other than mm/dd/yyyy.
03 Jul 2006 Added code to automatically add magicDate behavior to input fields 
		with a className that includes the word dateparse.
*/

/* Finds the index of the first occurence of item in the array, or -1 if not found */
Array.prototype.indexOf = function(item) {
	for (var i = 0; i < this.length; i++) {
		if (this[i] == item) {
			return i;
		}
	}
	return -1;
};
/* Returns an array of items judged 'true' by the passed in test function */
Array.prototype.filter = function(test) {
	var matches = [];
	for (var i = 0; i < this.length; i++) {
		if (test(this[i])) {
			matches[matches.length] = this[i];
		}
	}
	return matches;
};

var monthNames = "January February March April May June July August September October November December".split(" ");
var weekdayNames = "Sunday Monday Tuesday Wednesday Thursday Friday Saturday".split(" ");

/* Takes a string, returns the index of the month matching that string, throws
an error if 0 or more than 1 matches
*/
function parseMonth(month) {
	var matches = monthNames.filter(function(item) { 
		return new RegExp("^" + month, "i").test(item);
	});

	if (matches.length == 0) {
		throw new Error("Invalid month string");
	}
	if (matches.length > 1) {
		throw new Error("Ambiguous month");
	}
	return monthNames.indexOf(matches[0]);
}
/* Same as parseMonth but for days of the week */
function parseWeekday(weekday) {
	var matches = weekdayNames.filter(function(item) {
		return new RegExp("^" + weekday, "i").test(item);
	});

	if (matches.length == 0) {
		throw new Error("Invalid day string");
	}
	if (matches.length > 1) {
		throw new Error("Ambiguous weekday");
	}
	return weekdayNames.indexOf(matches[0]);
}

/* Array of objects, each has 're', a regular expression and 'handler', a 
function for creating a date from something that matches the regular 
expression. Handlers may throw errors if string is unparseable. 
*/
var dateParsePatterns = [
	// Today
	{   re: /^tod/i,
		handler: function() { 
			return new Date();
		} 
	},
	// Tomorrow
	{   re: /^tom/i,
		handler: function() {
			var d = new Date(); 
			d.setDate(d.getDate() + 1); 
			return d;
		}
	},
	// Yesterday
	{   re: /^yes/i,
		handler: function() {
			var d = new Date();
			d.setDate(d.getDate() - 1);
			return d;
		}
	},
	// 4th
	{   re: /^(\d{1,2})(st|nd|rd|th)?$/i, 
		handler: function(bits) {
			var d = new Date();
			d.setDate(parseInt(bits[1], 10));
			return d;
		}
	},
	// 4th Jan
	{   re: /^(\d{1,2})(?:st|nd|rd|th)? (\w+)$/i, 
		handler: function(bits) {
			var d = new Date();
			d.setDate(1);
			d.setMonth(parseMonth(bits[2]));
			d.setDate(parseInt(bits[1], 10));
			return d;
		}
	},
	// 4th Jan 2003
	{   re: /^(\d{1,2})(?:st|nd|rd|th)? (\w+),? (\d{4})$/i,
		handler: function(bits) {
			var d = new Date();
			d.setDate(1);
			d.setYear(bits[3]);
			d.setMonth(parseMonth(bits[2]));
			d.setDate(parseInt(bits[1], 10));
			return d;
		}
	},
	// Jan 4th
	{   re: /^(\w+) (\d{1,2})(?:st|nd|rd|th)?$/i, 
		handler: function(bits) {
			var d = new Date();
			d.setDate(1);
			d.setMonth(parseMonth(bits[1]));
			d.setDate(parseInt(bits[2], 10));
			return d;
		}
	},
	// Jan 4th 2003
	{   re: /^(\w+) (\d{1,2})(?:st|nd|rd|th)?,? (\d{4})$/i,
		handler: function(bits) {
			var d = new Date();
			d.setDate(1);
			d.setYear(bits[3]);
			d.setMonth(parseMonth(bits[1]));
			d.setDate(parseInt(bits[2], 10));
			return d;
		}
	},
	// next Tuesday - this is suspect due to weird meaning of "next"
	{   re: /^ne(?:xt)* (\w+)$/i,
		handler: function(bits) {
			var d = new Date();
			var wd = d.getDay();
			var nwd = parseWeekday(bits[1]);
			var addDays = nwd - wd;

			// It can't be before today or this week.
			if (nwd <= wd || (addDays + wd < 7) ) {
				addDays += 7;
			}
			
			// Next is not tomorrow.
			if (1 == addDays) {
				addDays += 7;
			}

			d.setDate(d.getDate() + addDays);
			return d;
		}
	},
	// last Tuesday
	{   re: /^la(?:st)* (\w+)$/i,
		handler: function(bits) {
			var d = new Date();
			var wd = d.getDay();
			var nwd = parseWeekday(bits[1]);

			// determine the number of days to subtract to get last weekday
			var addDays = (-1 * (wd + 7 - nwd)) % 7;
			// above calculate 0 if weekdays are the same so we have to change this to 7
			if (0 == addDays) {
				addDays = -7;
			} else if (-1 == addDays) {	// Last is not yesterday.
				addDays -= 7;
			}

			// adjust date and return
			d.setDate(d.getDate() + addDays);
			return d;
			}
	},
	// this coming Tuesday
	{   re: /^th(?:is)* (\w+)$/i,
		handler: function(bits) {
			var d = new Date();
			var wd = d.getDay();
			var nwd = parseWeekday(bits[1]);
			var addDays = nwd - wd;

			// It can't be before today.
			if (nwd <= wd) {
				addDays += 7;
			}
			
			d.setDate(d.getDate() + addDays);
			return d;
		}
	},
	// first Tuesday
	{   re: /^fir(?:st)* (\w+)$/i,
		handler: function(bits) {
			var d = new Date();
			d.setDate(1);
			var day = d.getDay();
			var newDay = parseWeekday(bits[1]);
			if(day == newDay) {
				return d;
			}
			
			var addDays = newDay - day;
			if (newDay < day) {
				addDays += 7;
			}
			d.setDate(d.getDate() + addDays);
			return d;
		}
	},
	// second Tuesday
	{   re: /^sec(?:ond)* (\w+)$/i,
		handler: function(bits) {
			var d = new Date();
			d.setDate(1);
			var day = d.getDay();
			var newDay = parseWeekday(bits[1]);
			var addDays = newDay - day;
			if (newDay < day) {
				addDays += 7;
			}
			addDays += 7;
			d.setDate(d.getDate() + addDays);
			return d;
		}
	},
	// third Tuesday
	{   re: /^thi(?:rd)* (\w+)$/i,
		handler: function(bits) {
			var d = new Date();
			d.setDate(1);
			var day = d.getDay();
			var newDay = parseWeekday(bits[1]);
			var addDays = newDay - day;
			if (newDay < day) {
				addDays += 7;
			}
			addDays += 14;
			d.setDate(d.getDate() + addDays);
			return d;
		}
	},
	// fourth Tuesday
	{   re: /^fo(?:urth)* (\w+)$/i,
		handler: function(bits) {
			var d = new Date();
			d.setDate(1);
			var day = d.getDay();
			var newDay = parseWeekday(bits[1]);
			var addDays = newDay - day;
			if (newDay < day) {
				addDays += 7;
			}
			addDays += 21;
			d.setDate(d.getDate() + addDays);
			return d;
		}
	},
	// fifth Tuesday
	{   re: /^fi(?:fth)* (\w+)$/i,
		handler: function(bits) {
			var d = new Date();
			d.setDate(1);
			var day = d.getDay();
			var newDay = parseWeekday(bits[1]);
			var addDays = newDay - day;
			if (newDay < day) {
				addDays += 7;
			}
			addDays += 28;
			d.setDate(d.getDate() + addDays);
			return d;
		}
	},
	// mm/dd/yyyy (American style)
	{   re: /(\d{1,2})\/(\d{1,2})\/(\d{4})/,
		handler: function(bits) {
			var d = new Date();
			d.setDate(1);
			if(!dp_dateFormat || dp_dateFormat.substr(0,1) == 'm') {
				d.setYear(bits[3]);
				d.setMonth(parseInt(bits[1], 10) - 1); // Because months indexed from 0
				d.setDate(parseInt(bits[2], 10));
			} else {
				d.setYear(bits[3]);
				d.setMonth(parseInt(bits[2], 10) - 1); // Because months indexed from 0
				d.setDate(parseInt(bits[1], 10));
			}
			return d;
		}
	},
	// mm-dd-yyyy (American style) or dd-mm-yyyy
	{   re: /(\d{1,2})-(\d{1,2})-(\d{4})/,
		handler: function(bits) {
			var d = new Date();
			d.setDate(1);
			if(!dp_dateFormat || dp_dateFormat.substr(0,1) == 'm') {
				d.setYear(bits[3]);
				d.setMonth(parseInt(bits[1], 10) - 1); // Because months indexed from 0
				d.setDate(parseInt(bits[2], 10));
			} else {
				d.setYear(bits[3]);
				d.setMonth(parseInt(bits[2], 10) - 1); // Because months indexed from 0
				d.setDate(parseInt(bits[1], 10));
			}
			return d;
		}
	},
	// mm.dd.yyyy (American style) or dd.mm.yyyy
	{   re: /(\d{1,2})\.(\d{1,2})\.(\d{4})/,
		handler: function(bits) {
			var d = new Date();
			d.setDate(1);
			if(!dp_dateFormat || dp_dateFormat.substr(0,1) == 'm') {
				d.setYear(bits[3]);
				d.setMonth(parseInt(bits[1], 10) - 1); // Because months indexed from 0
				d.setDate(parseInt(bits[2], 10));
			} else {
				d.setYear(bits[3]);
				d.setMonth(parseInt(bits[2], 10) - 1); // Because months indexed from 0
				d.setDate(parseInt(bits[1], 10));
			}
			return d;
		}
	},
	// yyyy-mm-dd (ISO style)
	{   re: /(\d{4})-(\d{1,2})-(\d{1,2})/,
		handler: function(bits) {
			var d = new Date();
			d.setDate(1);
			d.setYear(parseInt(bits[1],10));
			d.setMonth(parseInt(bits[2], 10) - 1);
			d.setDate(parseInt(bits[3], 10));
			return d;
		}
	}
];

function parseDateString(s) {
	for (var i = 0; i < dateParsePatterns.length; i++) {
		var re = dateParsePatterns[i].re;
		var handler = dateParsePatterns[i].handler;
		var bits = re.exec(s);
		if (bits) {
			return handler(bits);
		}
	}
	throw new Error("Invalid date string");
}

var dp_dateFormat;

function dp_DateString(d) {
	var dateDelim, delim;

	dp_dateFormat = dp_dateFormat || 'm/d/yyyy';
	if(dp_dateFormat.indexOf('/') != -1) {
		delim = eval('/\\//g');
		dateDelim = '/';
	} else if(dp_dateFormat.indexOf('-') != -1) {
		delim = eval('/\\-/g');
		dateDelim = '-';
	} else if(dp_dateFormat.indexOf('.') != -1) {
		delim = eval('/\\./g');
		dateDelim = '.';
	} else {
		delim = eval('/\\ /g');
		dateDelim = ' ';
	}

	var month, day, year;
	
	month = d.getMonth();
	day = d.getDate();
	year = d.getFullYear();

	switch (dp_dateFormat.replace(delim,"")){
	case 'ddmmmyyyy':
		return	dp_padZero(day) + '-' + dateDelim + '-' + monthNames[month].substr(0,3) + '-' + dateDelim + year;
	//case 'ddmmmyyyy':
	//	return dp_padZero(day) + dateDelim + monthNames[month].substr(0,3) + dateDelim + year;
	case 'dmmmyyyy':
		return day + dateDelim + monthNames[month].substr(0,3) + dateDelim + year;
	case 'ddmmyyyy':
		return dp_padZero(day) + dateDelim + dp_padZero(month+1) + dateDelim + year;
	case 'dmmyyyy':
		return day + dateDelim + dp_padZero(month+1) + dateDelim + year;
	case 'mmddyyyy':
		return dp_padZero((month+1)) + dateDelim + dp_padZero(day) + dateDelim + year;
	case 'mddyyyy':
		return (month+1) + dateDelim + dp_padZero(day) + dateDelim + year;
	case 'mdyyyy':
		return (month+1) + dateDelim + day + dateDelim + year;
	case 'yyyymmdd':
		return year + dateDelim + dp_padZero(month+1) + dateDelim + dp_padZero(day);
	default:
		return (month+1) + dateDelim + day + dateDelim + year;
	}
}

function dp_padZero(n) {
	return ((n <= 9) ? ("0" + n) : n);
}

function magicDate(input, required) {
	if(!required && input.value == '') {
		return true;
	}
	var bRet = true;
	var messagespan = input.id + 'Msg';
	try {
		var d = parseDateString(input.value);
		
		input.value = dp_DateString(d);
		//input.className = '';
		// Human readable date
		var el = document.getElementById(messagespan);
		if(el) {
			el.firstChild.nodeValue = d.toDateString();
			//el.className = 'normal';
		} else {
			d.toDateString();
		}
	}
	catch (e) {
		//input.className = 'error';
		var message = e.message;

		// Fix for IE6 bug
		if (!message.length || message.indexOf('is null or not an object') > -1) {
			message = 'Invalid date string';
		}
		var el = document.getElementById(messagespan);
		if(el) {
			el.firstChild.nodeValue = message;
			//el.className = 'error';
		} else {
			//alert(message);
		}
		bRet = false;
	}
//	if(!bRet)
//		input.focus();
	return bRet;
}

// If there is an addEvent function add an event to add the dateparse functions to
// an input field with a className of dateparse. If you don't have an addEvent function,
// please use mine at http://tanny.ica.com, search for DOMContentLoaded.
try {
	if(addEvent){
		// If there is an addDOMLoadEvent function use it as it runs as soon as the
		// DOM (html) has loaded. This is faster than load (onload) since it doesn't
		// have to wait for images and other stuff (technical term).
		var sEventType;;
		try {
			if(addDOMLoadEvent)
				sEventType = "DOMContentLoaded";
		}
		catch(e) {
			sEventType = "load";
		}

		// Let's add an event to run the add date parse behavior function.
		addEvent(window, sEventType, function() {
			// Get an array of input elements.
			var els = document.getElementsByTagName("input");

			// Check each input element.
			for(var i = 0; i < els.length; i++) {
				// If the className has the word dateparse in it, add the behavior.
				if(/\bdateparse\b/.test(els[i].className)) {
					addEvent(els[i], "blur", function() { magicDate(this); });
					addEvent(els[i], "focus", function() { if (this.className != 'error') this.select(); });
				}
			}
		});
	}
}
catch(e) {
	// We could add the function to the onload event here, but the user might overright it
	// in body onload attribute. Therefore, we're not going to add the functionality.
}
