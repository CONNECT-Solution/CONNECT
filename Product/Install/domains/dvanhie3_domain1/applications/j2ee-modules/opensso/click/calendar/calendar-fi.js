// ** I18N

// Calendar FI language (Finnish, Suomi)
// Authors: Markus Sipil‰ <markus.sipila@iki.fi>
// Encoding: UTF-8
// Distributed under the same terms as the calendar itself.

// K‰‰nnˆksiss‰ huomioitu kotoistushankkeen suoritukset
// Lis‰tietoja: http://www.csc.fi/sivut/kotoistus

// For translators: please use UTF-8 if possible.  We strongly believe that
// Unicode is the answer to a real internationalized world.  Also please
// include your contact information in the header, as can be seen above.

// full day names
Calendar._DN = new Array
("sunnuntai",
 "maanantai",
 "tiistai",
 "keskiviikko",
 "torstai",
 "perjantai",
 "lauantai",
 "sunnuntai");

// Please note that the following array of short day names (and the same goes
// for short month names, _SMN) isn't absolutely necessary.  We give it here
// for exemplification on how one can customize the short day names, but if
// they are simply the first N letters of the full name you can simply say:
//
//   Calendar._SDN_len = N; // short day name length
//   Calendar._SMN_len = N; // short month name length
//
// If N = 3 then this is not needed either since we assume a value of 3 if not
// present, to be compatible with translation files that were written before
// this feature.

// short day names
Calendar._SDN = new Array
("su",
 "ma",
 "ti",
 "ke",
 "to",
 "pe",
 "la",
 "su");

// First day of the week. "0" means display Sunday first, "1" means display
// Monday first, etc.
Calendar._FD = 1;

// full month names
Calendar._MN = new Array
("tammikuu",
 "helmikuu",
 "maaliskuu",
 "huhtikuu",
 "toukokuu",
 "kes‰kuu",
 "hein‰kuu",
 "elokuu",
 "syyskuu",
 "lokakuu",
 "marraskuu",
 "joulukuu");

// short month names
Calendar._SMN = new Array
("tammi",
 "helmi",
 "maalis",
 "huhti",
 "touko",
 "kes‰",
 "hein‰",
 "elo",
 "syys",
 "loka",
 "marras",
 "joulu");

// tooltips
Calendar._TT = {};
Calendar._TT["INFO"] = "Tietoja kalenterista";

Calendar._TT["ABOUT"] =
"DHTML Date/Time Selector\n" +
"(c) dynarch.com 2002-2005 / Author: Mihai Bazon\n" + // don't translate this this ;-)
"Suomennos: Markus Sipil‰\n" +
"Uusin versio osoitteessa: http://www.dynarch.com/projects/calendar/\n" +
"Julkaistu GNU LGPL lisenssill‰.  Lis‰tietoja osoitteessa http://gnu.org/licenses/lgpl.html" +
"\n\n" +
"P‰iv‰m‰‰r‰n valinta:\n" +
"- K‰yt‰ \xab, \xbb -painikkeita valitaksesi vuoden\n" +
"- K‰yt‰ " + String.fromCharCode(0x2039) + ", " + String.fromCharCode(0x203a) + " -painikkeita valitaksesi kuukauden\n" +
"- Pid‰ hiiren nappia pohjassa nopeuttaaksesi valintaa";
Calendar._TT["ABOUT_TIME"] = "\n\n" +
"Kellonajan valinta:\n" +
"- Klikkaa kellonajan numeroita lis‰t‰ksesi aikaa\n" +
"- Klikkattaessa shift-n‰pp‰imen pohjassapit‰minen v‰hent‰‰ aikaa\n" +
"- Klikkaa ja liikuta hiirt‰ nopeuttaaksesi valintaa";

Calendar._TT["PREV_YEAR"] = "Ed. vuosi / vuosivalikko";
Calendar._TT["PREV_MONTH"] = "Ed. kk / kk-valikko";
Calendar._TT["GO_TODAY"] = "Siirry t‰h‰n p‰iv‰‰n";
Calendar._TT["NEXT_MONTH"] = "Seur. kk / kk-valikko";
Calendar._TT["NEXT_YEAR"] = "Seur. vuosi / vuosivalikko";
Calendar._TT["SEL_DATE"] = "Valitse p‰iv‰m‰‰r‰";
Calendar._TT["DRAG_TO_MOVE"] = "Siirr‰ kalenterin paikkaa";
Calendar._TT["PART_TODAY"] = " (t‰n‰‰n)";

// the following is to inform that "%s" is to be the first day of week
// %s will be replaced with the day name.
Calendar._TT["DAY_FIRST"] = "N‰yt‰ %s ensimm‰isen‰";

// This may be locale-dependent.  It specifies the week-end days, as an array
// of comma-separated numbers.  The numbers are from 0 to 6: 0 means Sunday, 1
// means Monday, etc.
Calendar._TT["WEEKEND"] = "0,6";

Calendar._TT["CLOSE"] = "Sulje";
Calendar._TT["TODAY"] = "T‰n‰‰n";
Calendar._TT["TIME_PART"] = "(Shift-)Klikkaa tai liikuta hiirt‰";

// date formats
Calendar._TT["DEF_DATE_FORMAT"] = "%d.%m.%Y";
Calendar._TT["TT_DATE_FORMAT"] = "%d.%m.%Y";

Calendar._TT["WK"] = "vko";
Calendar._TT["TIME"] = "Kellonaika:";
