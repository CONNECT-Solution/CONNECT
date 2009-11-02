/*
 * This code is subject to the HIEOS License, Version 1.0
 *
 * Copyright(c) 2008-2009 Vangent, Inc.  All rights reserved.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vangent.hieos.xtest.framework

toolkit = null
try { toolkit = System.getenv('HIEOSxTestDir') } catch (Exception e) {}
if (toolkit == null) error('Environment variable HIEOSxTestDir is not set')

logdir = null
try { logdir = System.getenv('HIEOSxLogDir') } catch (Exception e) {}
if (logdir == null) error('Environment variable HIEOSxLogDir is not set')

if ( ! new File(logdir).exists() )
	error("Log directory, ${logdir} as configured in environment variable HIEOSxLogDir does not exist")
if ( ! new File(logdir).isDirectory() )
	error("Log directory, ${logdir} as configured in environment variable HIEOSxLogDir exists but is not a directory")
if ( ! new File(logdir).canWrite() )
	error("Log directory, ${logdir} as configured in environment variable HIEOSxLogDir exists, is a directory, but is not writable")
	
println "\nHIEOSxLogDir is configured to ${logdir}\n"
	
	
def error(msg) {
	println msg
	System.exit(-1)
}

if (toolkit == null) 
	error("Environment variable HIEOSxTestDir must be exist and point to an installaton of the toolkit")

/*
if (!new File("${toolkit}/install.txt").exists())
	error("Environment variable XDSTOOLKIT exists but does not point to the toolkit")
*/
	
aa = new File("${toolkit}/assigning_authority.txt").text
aa = strip(aa)
first = aa.indexOf('&')
last = aa.lastIndexOf('&')
if (first != -1 && last != -1 && first != last) 
	aa = aa.substring(first+1, last)
change=getyn("Assigning Authority is ${aa}, change it?", false)
if (change) {
	aa = getline("New Value:")
	new File("${toolkit}/assigning_authority.txt").write("&${strip(aa)}&ISO")
}

aa = new File("${toolkit}/patientid_base.txt").text
aa = strip(aa)
first = aa.indexOf('&')
last = aa.lastIndexOf('&')
if (first != -1 && last != -1 && first != last) 
	aa = aa.substring(first+1, last)
change=getyn("Patient ID is ${aa}, change it?", false)
if (change) {
	aa = getline("New Value:")
	new File("${toolkit}/patientid_base.txt").write("&${strip(aa)}&ISO")
}

println "The current patient ID is " +
strip(new File("${toolkit}/patientid_base.txt").text) +
'^^^' +
strip(new File("${toolkit}/assigning_authority.txt").text)
println ''

uib = new File("${toolkit}/uniqueid_base.txt").text
uib = strip(uib)
change=getyn("Current Unique ID OID base is ${uib}, change it?", false)
if (change) {
	uib = getline("New Value:")
	uib = strip(uib)
	if (uib.length() == 0) 
		println "Aborting"
	if (uib[uib.length()-1] != '.')
		uib += '.'
	new File("${toolkit}/uniqueid_base.txt").write(uib)
}

uib = new File("${toolkit}/uniqueid_index.txt").text
uib = strip(uib)
change=getyn("Current Unique ID OID counter is ${uib}, reset it to zero?", false)
if (change) {
	uib = '0'
	new File("${toolkit}/uniqueid_index.txt").write(uib)
}

println "The next unique ID generated will be " +
strip(new File("${toolkit}/uniqueid_base.txt").text) +
strip(new File("${toolkit}/uniqueid_index.txt").text)
	
println "Done"

def strip(str) {
	while (str.length() > 0 && str[0] in [' ', '\t', '\n'])
		str = str.substring(1)
	while (str.length() > 0 && str[str.length()-1] in [' ', '\t', '\n'])
		str = str.substring(0,str.length()-1)
	return str
}

def getyn(msg, defaultValue) {
	def defaultStr = (defaultValue) ? ' : Yn :' : ' : yN '
	while (true) {
		def r = getline(msg + defaultStr)
		if (r.length() == 0) return defaultValue
		if (r.toLowerCase() == 'y') return true
		if (r.toLowerCase() == 'n') return false
		println "Answer with y or n or RETURN"
	}
}

def getline(msg) {
	println msg
	return readline()
}

def readline() {
	def line = ''
	while (true) {
		int i = System.in.read()
		char ch = i
		if (ch == '\n')
			return new String(line)
		line += ch
	}
}
