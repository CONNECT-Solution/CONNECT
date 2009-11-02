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

package com.vangent.hieos.xtest.main

import org.apache.log4j.Logger;
import com.vangent.hieos.xtest.framework.TestConfig;

public class XTestDriverGroovy {
    def version = 'xx.yy'
    //controlling variables
    def scripts
    def mgmt
    def testdir
    def logDir
    def testNum
    def testPart
    def testSpec = []     // pre-parsed extended test specification (subdir and steps)
    def testSteps = []
    def testPlanSpecs = []
    def testDir
    def tokens
    def testCollection
    def testCollections = [] // only used during parsing to load next two vars
    def testCollectionName
    def testCollectionFile
    def verbose = false
    def verboseverbose = false
    def prepareOnly = false
    def siteName = null
    def siteConfig = null
    boolean secure = false
    def listingOnly = false
    def showLocationInformation = false
    def tcListingOnly = false
    def sep = File.separator
    def run = false
    def runForever = false
    def showErrors = false
    def showReadme = false
    def bargs
    def recursive
    def testPath

    static void main(args) {
        private final static Logger logger = Logger.getLogger(XTestDriverGroovy.class);
        logger.info("xtest started")
        XTestDriverGroovy xt = new XTestDriverGroovy()
        xt.run(args)
    }

    def error(msg) {
        println msg
        System.exit(-1)
    }

    def run(in_args) {
        List args = in_args

        testdir = null
        try { testdir = System.getenv('HIEOSxTestDir') } catch (Exception e) {}
        if (testdir == null) {
            error('Environment variable HIEOSxTestDir is not set')
        }
	
        mgmt = testdir
        scripts = testdir + "/scripts"
        try { logDir  = System.getenv('HIEOSxLogDir') } catch (Exception e) {}

        bassert(logDir != null, 'Environment variable HIEOSxLogDir is not set')
        bassert(new File(testdir).exists(),"HIEOSxTestDir directory ${testdir} does not exist")
        bassert(new File(scripts).exists(),"Scripts directory ${scripts} does not exist")
        bassert(new File(scripts + "/collections"),"Scripts directory ${testdir} is not really the test directory, no collections subdirectory exists")
        bassert(new File(logDir).exists(),"HIEOSxLogDir directory ${logDir} does not exist")

        version = "1.0"
        XTestDriver.version = version
        if (args.size() > 0) {
            bargs = (List)  args.reverse() // bargs = bacwards args (works as stack)
            // option processing
            while (bhas()) {
                def option = bpop('')
                switch (option) {
                    case '--forever' :
                    case '-F':
                    runForever = true
                    break;
                    case '--site' :
                    case '-s' :
                    siteName = bpop('--site expects a site name')
                    optAssert(!siteName.startsWith('-'), "--site expects a site name")
                    break;
                    case '--readme' :
                    case '-R' :
                    showReadme = true
                    break
                    case '--secure' :
                    case '-S' :
                    secure = true
                    break;
                    case '--testcollection' :
                    case '-tc' :
                    testCollection = bpop('--testcollection expects a path')
                    optAssert(!testCollection.startsWith('-'), "--testcollection expects a test collection name")
                    testCollections << testCollection
                    while (bhas() && !bpeak().startsWith('-')) {
                        testSpec << bpop('oops')
                    }
                    break;
                    case '--logDir' :
                    case '-L' :
                    logDir = bpop('--logDir expects a path')
                    optAssert(!logDir.startsWith('-'), "--logDir expects a path")
                    break;
                    case '--test' :
                    case '-t' :
                    testNum = bpop('--testnum expects a test number')
                    optAssert(!testNum.startsWith('-'), "--testnum expects a test number")
                    while (bhas() && !bpeak().startsWith('-')) {
                        testSpec << bpop('oops')
                    }
                    parseTestSpec(testNum, scripts, null)
                    testNum = null
                    break;
                    case '--scripts' :
                    case '-T' :
                    scripts = bpop('-scripts expects a path')
                    optAssert(!scripts.startsWith('-'), "--scripts expects a path")
                    break;
                    case '--logdir' :
                    case '-ld' :
                    logDir = bpop('--logdir expects a path')
                    optAssert(!logdir.startsWith('-'), "--logdir expects a path")
                    break;
                    case '-h' :
                    case '--help' :
                    usage('')
                    case '-v' :
                    case '--verbose' :
                    verbose = true
                    break
                    case '-err' :
                    case '--errors' :
                    showErrors = true
                    break
                    case '-loc' :
                    showLocationInformation = true
                    break
                    case '-vv' :
                    case '--verboseverbose' :
                    verboseverbose = true
                    verbose = true
                    TestConfig.verbose = true
                    break
                    case '-ls' :
                    case '--listing' :
                    listingOnly = true
                    recursive = false
                    break
                    case '-lsc' :
                    tcListingOnly = true
                    recursive = false
                    break
                    case '-run' :
                    run = true
                    break
                    case '-P' :
                    case '--prepare' :
                    prepare = true
                    break
                    case '-V' :
                    case '--version' :
                    println "xtest version ${version}"
                    System.exit(0)
                    default:
                    if (option.startsWith("-D")) {
                        def parts = option.split("=");
                        def pname = parts[0]
                        pname = pname.substring(2)
                        def pvalue = parts[1]
                        println "Setting ${pname} to ${pvalue}"
                        System.getProperties().put(pname, pvalue)
                    } else {
                        usage("Unknown option ${option}")
                    }
                }
            }
        }
        else {
            usage("No options entered")
        }
        /*
        def tlsConfigured = false
        try { tlsConfigured = System.getenv('tlsConfigured') } catch (Exception e) {}
        if (secure && ! tlsConfigured)
        error('-S option specified but TLS is not configured')
         */
       
        //
        // option validation and extended configuration variable initialization
        //
	
        bassert(new File(scripts).isDirectory(), "Test scripts not found at ${scripts}, it is not a directory")
        bassert(new File(scripts + File.separatorChar + 'tests').isDirectory(),
			"Test scripts not found at ${scripts}, no 'tests' sub-directory found (${scripts + File.separatorChar + 'tests'})")
	
        //
        // parse sites
        //
        def sites = new XmlSlurper().parse(mgmt + File.separatorChar + 'actors.xml')
        bassert(sites.site.size()>0,"Could not load actor configuration from ${mgmt + File.separatorChar + 'actors.xml'}")
		
        if (!siteName) {
            // no site is offered on command line, maybe default is declared in actors.xml
            siteName = sites.defaultsite.text()
        }
        bassert(siteName, "No site specified on command line and no default present in actors.xml")
	
        TestConfig.target = siteName
        TestConfig.siteXPath = "site[@name='${siteName}']"   // preceeding double slash is supplied later
		
        def siteConfig = sites.site.grep{it.@name == siteName}[0]
		
        bassert(siteConfig, "Could not load site configuration from ${mgmt + File.separatorChar + 'actors.xml'} for site ${siteName}")
	
        HashMap endpoints = new HashMap();
        HashMap secureEndpoints = new HashMap();
        HashMap repositories = new HashMap();
        HashMap secureRepositories = new HashMap();
        HashMap xRepositories = new HashMap();
        HashMap xSecRepositories = new HashMap();
	
        if (verbose) {
            if (secure) {
                println "Secure transaction"
            } else {
                println "Unsecure transaction"
            }
        }
        siteConfig.transaction.each {
            def isSecure = it.@secure
            def name = it.@name
            if (name == 'xcr') {
                if (isSecure == "1") {
                    xSecRepositories.put((it.@home).toString(), it.toString())
                } else {
                    xRepositories.put((it.@home).toString(), it.toString())
                }
            } else {
                if (isSecure == "1") {
                    secureEndpoints.put((it.@name).toString(), it.toString())
                }
                else {
                    endpoints.put((it.@name).toString(), it.toString())
                }
            }
        }
	
        siteConfig.repository.each {
            def isSecure = it.@secure
            if (isSecure == "1")  { // BHT FIX: fixed to use @uid for HashMap as key.
                secureRepositories.put((it.@uid).toString(), it.toString())
            } else {
                repositories.put((it.@uid).toString(), it.toString())
            }
        }
	
        if (verboseverbose) {
            println "site config is ${endpoints}\nsecure site config is ${secureEndpoints}"
        }
        TestConfig.endpoints = endpoints;
        TestConfig.secure_endpoints = secureEndpoints;
        TestConfig.repositories = repositories;
        TestConfig.secureRepositories = secureRepositories;
        TestConfig.xRepositories = xRepositories;
        TestConfig.xSecRepositories = xSecRepositories;
        TestConfig.secure = secure;
        if (siteConfig.PidAllocateEndpoint) {
            TestConfig.pid_allocate_endpoint = siteConfig.PidAllocateEndpoint
        }

        //
        // Main loop
        //
        if (listingOnly && !testNum) {
            showTestListing(scripts)
            System.exit(-1)
        }
	
        if (tcListingOnly && !testNum) {
            showTcListing(scripts)
            System.exit(-1)
        }
	

        //
        // parse test specification
        //
        def testCollDir = 'collections'
        def testCollExt = '.tc'
        if (testCollections.size == 0 && testNum != null) {
            parseTestSpec(testNum, scripts, testSpec)
        } else {
            testCollections.each { testCollection ->
                if (! new File(testCollection).exists()) {
                    // could be part of scripts
                    if (new File(scripts + File.separatorChar + testCollDir + File.separatorChar + testCollection).exists()) {
                        testCollection = scripts + File.separatorChar + testCollDir + File.separatorChar + testCollection
                    } else if (new File(scripts + File.separatorChar + testCollDir + File.separatorChar + testCollection + testCollExt).exists()) {
                        testCollection = scripts + File.separatorChar + testCollDir + File.separatorChar + testCollection + testCollExt
                    } else {
                        println "testCollection ${testCollection} not found"
                        System.exit(-1)
                    }
                }
                if (verboseverbose) {
                    println "Reading test collection ${testCollection}"
                }
                new File(testCollection).eachLine { line ->
                    def comment= line.indexOf('#')
                    if (comment != -1) {
                        line = line.substring(0, comment)
                    }
                    tokens = tokenize(line)
                    if (tokens.size() > 0) {
                        def thisTestNum = tokens[0]
                        tokens = tokens[1..<tokens.size()]
                        parseTestSpec(thisTestNum, scripts, tokens)
                    }
                }
            }
        }
		
        if (listingOnly && testNum) {
            if (!showReadme) {
                showDesc(testNum, scripts)
            }
            showTestListing(scripts, testPlanSpecs)
            if (showReadme) {
                showReadme(testNum, scripts)
            }
            System.exit(-1)
        }
	
        if (showReadme && testNum) {
            showReadme(testNum, scripts)
            System.exit(-1)
        }
        while (true)
        {
            if (run) {
                runTests()
            }
            if (showErrors) {
                runShowErrors()
            }
            if (runForever == false) {
                break;
            }
        }
        System.exit(0)
    }

    def runTests()
    {
        long testRunStartTime = System.currentTimeMillis();  // Start time of test run.
        if (mgmt.charAt(mgmt.size()-1) != '/') {
            mgmt = mgmt + '/'
        }
        TestConfig.testmgmt_dir = mgmt
        bassert(logDir, '''-ld <logDir> must be used on command line or HIEOSxLogDir environment
				variable must be set to point to the directory that should hold log files.
				''')
        if (testSteps != ':all') {
            testSteps.each { XTestDriver.only_steps.add(it) }
        }
        int numTestSpecs = 0;
        testPlanSpecs.each {
            ++numTestSpecs
            def testPlanDir = mkDirName(it)
            def logDirSpec = it.clone()
            logDirSpec[0] = logDir
            mkDir(logDirSpec).mkdirs()
            TestConfig.log_dir = mkDirName(logDirSpec) + sep
            println "${it[1..<it.size]}"
            def ok = XTestDriver.runTest(testPlanDir)
            println ((ok) ? "...Pass" : "...Fail")
        }
        long testRunStopTime = System.currentTimeMillis();  // Stop time of test run.
        long testRunElapsedTime = testRunStopTime - testRunStartTime;
        println "\n\n---------------------------------  Test Summary  ------------------------------\n"
        println "\t Number of test specs: ${numTestSpecs}"
        println "\t Elapsed time: ${testRunElapsedTime/1000.0} seconds"
    }

    def runShowErrors()
    {
        println "\n\n---------------------------------  Error Summary  ------------------------------\n"
        testPlanSpecs.each {
            def logDirSpec = it.clone()
            logDirSpec[0] = logDir
            bassert (mkFile(logDirSpec,'log.xml').exists(), "Cannot display log file ${mkFile(logDirSpec,'log.xml')}, probably specified -err option without -run option and this test has not been run before")
            def log = new XmlParser().parse(mkFile(logDirSpec,'log.xml'))
            def fatalErrorNode = log.FatalError[0]
            if (fatalErrorNode) {
                println "${logDirSpec[2..logDirSpec.size-1]} ***************"
                println "\tEndpoint   : ${logEndpoint(log).text()}"
                println "\tFatalError (step ${TestConfig.currentStep}) : ${firstNLines(fatalErrorNode.text(), 8)}"
            }
            def failedSteps = log.TestStep.grep { it.@status == 'Fail' }
            if (failedSteps.size > 0) {
                println "${logDirSpec[2..logDirSpec.size-1]} ***************"
            }
            failedSteps.each { step ->
                def trans = step.find { it.name() =~ /Transaction/ }
                println "\tstep ****** : ${step.attribute('id')}"
                println "\ttransaction : ${trans.name()}"
                println "\tEndpoint    : ${logEndpoint(trans).text()}"
                step.Error.each { err ->
                    println "\tError is    : ${firstNLines(err.text(),8).replaceAll('\n', '\n\t')}"
                }
                try {
                    def regErrList = trans.Result[0]
                    .find { it.name() =~ /RegistryResponse/ || it.name() =~ /AdhocQueryResponse/ }
                    .find { it.name() =~ /RegistryErrorList/ }
                    if (regErrList == null) regErrList = trans.Result[0]
                    .find { it.name() =~ /RetrieveDocumentSetResponse/  }
                    .find { it.name() =~ /RegistryResponse/  }
                    .find { it.name() =~ /RegistryErrorList/ }
                    regErrList.each { regErr ->
                        println "\tDetails : ${regErr.attribute('errorCode')} : " + findNL(regErr.attribute('codeContext')) + "${regErr.attribute('codeContext').replaceAll('\n', '\n\t')}"
                        if (showLocationInformation) {
                            println "\tLocation : ${regErr.attribute('location').replaceAll('\n', '\n\t')}"
                        }
                    }
                }
                catch (Exception e) {}
            }
        }
    }

    def findNL(str) {
        if (str.indexOf('\n') == -1) {
            return ""
        }
        return "\n\t"
    }
	
    def logEndpoint(log) {
        if (log.RegistryEndpoint)
        {
            return log.RegistryEndpoint
        }
        return log.Endpoint
    }
	
    def showReadme(testNum, scripts) {
        def testSpec = findTest(scripts, testNum)
        mkFile(testSpec, 'readme.txt').eachLine {
            println it
        }
    }
	
    def showDesc(testNum, scripts) {
        def testSpec = findTest(scripts, testNum)
        try {
            mkFile(testSpec, 'readme.txt').eachLine {
                println it
                throw new Exception('foo')
            }
        } catch (Exception e) {
        }
    }
	
    def showTestListing(scripts) {
        ['testdata', 'examples', 'tests', 'development', 'selftest'].each { section ->
            println "======================  ${section}  =============================="
            mkDir([scripts, section]).eachDir { test ->
                def dirName = test.name
                if (isTestDir([scripts, section, test.name]) || hasTestDir([scripts, section, test.name])) {
                    def firstLine = getTestDescription(test)
                    println "${dirName}\t${firstLine}"
                }
            }
        }
    }

    def getTestDescription(File testdir) {
        def firstLine = null
		testdir.eachFileMatch(~/readme.txt/) { file ->
			try {
				file.eachLine { line ->
					firstLine = line
					throw new Exception('bo')
				}
			} catch (Exception e) {}
		}
		return firstLine
    }


    def showTcListing(scripts) {
        mkDir([scripts, 'collections']).eachFile { file ->
            if (file.name.endsWith('.tc')) {
                def str = file.name.substring(0,file.name.indexOf('.tc'))
                def indx = str.lastIndexOf('/')
                if (indx != -1) {
                    str = str.substring(indx+1)
                }
                println str
            }
        }
    }
	
    def showTestListing(scripts, testPlanSpecs) {
        testPlanSpecs.each { testPlanSpec ->
            println testPlanSpec
            def tplan = new XmlSlurper().parse(mkFile(testPlanSpec,'testplan.xml'))
            tplan.TestStep.each { testStep ->
                println "\t${testStep.@id}"
            }
        }
    }
	
    def tokenize(str) {
		StringTokenizer st = new StringTokenizer(str)
		def results = []
		while (st.hasMoreTokens()) {
			results << st.nextToken()
		}
		return results
	}

    def firstNLines(string, n) {
        def startingAt = 0
        (1..n).each {
            if (startingAt != -1) {
                startingAt = string.indexOf('\n', startingAt + 1)
            }
        }
        if (startingAt == -1) { return string }
        return string.substring(0, startingAt)
    }

    def hasTestDir(dirSpec) {
        def found = false
        mkDir(dirSpec).eachDir { dir ->
            dir.eachFileMatch(~/testplan.xml/) { found = true }
        }
        return found
	
    }

    def isTestDir(dirSpec) {
        return mkFile(dirSpec, 'testplan.xml').exists()
    }

    // test specification
    // [ scripts, section, testNum, <subdir> ]

    def mkFile(testSpec, filename) {
        new File(mkFileName(testSpec, filename))
    }

    def mkFileName(testSpec, filename) {
        testSpec.join(sep) + sep + filename
    }

    def mkDir(testSpec) {
        new File(mkDirName(testSpec))
    }
		
    def mkDirName(testSpec) {
        testSpec.join(sep)
    }
		
    def parseTestSpec(testNum, scripts, inputTestSpec) {
        // inputTestSpec is not in testSpec format
        if (verboseverbose) {
            println "parseTestSpec: testNum is ${testNum}   inputTestSpec is ${inputTestSpec}"
            println "existing testSpecs are ${testPlanSpecs}"
        }
        if (testNum) {  // is -t option specified? if so its subparms are in testSpec
            def testSpec = findTest(scripts, testNum)   // identify section of test kit (test, examples, testdata etc.)
            bassert(testSpec, "Cannot find test ${testNum}")
		
            if (verboseverbose) {
                println "looking for ${mkFileName(testSpec,'testplan.xml')}"
            }
            if (mkFile(testSpec,'testplan.xml').exists()) {
                testPlanSpecs << testSpec
            }
            else if (inputTestSpec?.size() > 0) {
                while (inputTestSpec.size() > 0) {
                    def testEle = inputTestSpec[0]
                    inputTestSpec = inputTestSpec[1..<inputTestSpec.size()]
                    if (verboseverbose) {
                        println "looking in ${mkFileName(testSpec, testEle)}"
                    }
                    if (mkFile(testSpec, testEle).isDirectory()) {
                        // specified sub-test (like SOAP11 vs SOAP12)
                        testPath = testSpec +  [ testEle ]
                        if (mkFile(testPath, 'testplan.xml').exists()) {
                            testPlanSpecs << testPath
                        }
                    } else {
                        // must be a test step
                        testSteps << testEle
                    }
                }
            }
            else {
                // search testPathMaster for all test plans and add them to the list
                testPlanSpecs.addAll(findAllTestSpecs(testSpec))
            }
        } else {
            println "No testnum specified"
            System.exit(-1)
        }
        if (testSteps.size() == 0)
        testSteps = ':all'
	
        if (verbose) {
            println "scripts location is ${scripts}"
            println "testSpecs are ${testPlanSpecs}"
            println "testSteps are ${testSteps}"
        }

    }
  
    def findAllTestSpecs(dirSpec) {
        def testSpecs = []
        if (mkFile(dirSpec, 'testplan.xml').exists()) {
            testSpecs << dirSpec
        }
        if (mkFile(dirSpec, 'index.idx').exists()) { // file to control ordering of sub-tests
            mkFile(dirSpec, 'index.idx').eachLine {
                it = it.trim()
                testSpecs << dirSpec + [ it ]
            }
        } else {
            new File(dirSpec.join(sep)).eachDir { dir ->
                findAllTestSpecs(dirSpec + [ dir.name ]).each { testSpecs << it }
            }
        }
        return testSpecs
    }

    def findTransaction(siteConfig, name, boolean secure) {
        def secureValue = (secure) ? '1' : '0'
        return siteConfig.transaction.find {
            it.@name == name && it.@secure == secureValue
        }
    }

    def findSite(actorConfig, siteName) {
        return actorConfig.site.find { site -> site["@name"] == siteName }
    }

    def findTest(scripts, testNum) {
        def section = ['tests', 'testdata', 'examples', 'internal', 'play', 'selftest', 'msw', 'development'].find { section ->
            new File(scripts + File.separatorChar + section + File.separatorChar + testNum).isDirectory()
        }
        if (section) {
            return [ scripts, section, testNum ]
        }
        return null
    }

    def bpeak() { haveArg('bpeak() failed - list empty'); return bargs[bargs.size()-1] }
    def peak() { return bargs[bargs.size()-1] }
    def bpop(msg) { haveArg(msg); return bargs.pop(); }
    void haveArg(msg) { if (bargs.size() == 0) { usage(msg) } }
    def bhas() { return bargs.size() > 0 }
    def bassert(test, message) { if (!test) { println("Error: ${message}"); System.exit(-1); } }
    def optAssert(test, message) { if (!test) { usage(message) } }

	
    def usage(msg) {
        def usageMsg = """
Usage:  xtest <options>
Options are:
  -h --help      
  	Display this help message

  -v --verbose
  	Verbose output - this is mostly for debuggin xdstest

  -t --test <test number> [<sub dir>] [<test step>]*
	Specify the test number to run.  The test specification will be taken
	out of the configured test scripts (configured by environment variable
	or command line option). Some tests have sub-directories which can be specified here. If
	<sub dir> is not specified then tests found in all sub directories will be run. 
	Zero or more test steps are specified.  If no test steps are specified then all test steps
	are run.

	This option may not be specified along with --testcollection.

  -tc --testcollection <test collection> [<test collection>*]
	Specify the test collection to run.  The specification of <test collection> can be
	either a path (starts with '/' chars) or a name (does not start with a '/' char). If 
	specified as a path it is an absolute path (starting with '/') to a test collection file.  
	If it is a name it is the name of a file in the directory scripts/collections. This tool will
    add .tc to the name to make it into a filename. A test 
	collection file is a plain text file with a test specified per line.  The following are 
	valid test specifications:

	12324           # run test 12324
	12325 SOAP11    # run test plan in SOAP11 part of test 12325
	11233 leafclass # run 'leafclass' test step in testplan of test 11233
	11225 SOAP11 leafclass # run 'leafclass' test step in testplan of the SOAP11 part of test 11233
	11233 leafclass err # run 'leafclass' and 'err' test steps in testplan of test 11233

	This option may not be specified along with --test.

  -err
    Print an error summary at the end.

  -loc
    Display error location information from the RegistryResponse in the error summary.

  -ls --list
    Lists content of scripts by test number displaying the documentation string
    at the top of the readme.txt file.

  -lsc
  	List test collections

  -s --site <site name>
	Specify the site to send to. The site name must be found in the actors configuration file
	(HIEOSxTestDir/actors.xml).  The test step within the test plan specifies which transaction
	is being initiated.  The actors.xml file, for each configured site, specifies the WS Endpoint
	to be used.

  -P --prepare
  	Prepare the input message, write the log file and exit.  No messages
  	are sent.  The log file can be inspected to see the message that
  	would have been sent.

  -S --secure
  	Use TLS.

  -V --version
  	Print version number of xtest tool

  -T --scripts <path to scripts>
	If not specified then value taken from HIEOSxTestDir environment variable.
	This is useful when working with more than one version of the testkit.

  -L --logDir <path to log directory>
	By default log files (log.xml) are written to the directory holding the 
	testplan (testplan.xml).  If the HIEOSxLogDir environment variable
	exists then it is used as the destination for log files.  This option
	can also be used to specify a log directory. If a log directory is used
	the log files are deposited into directories named for the test number.

  -F --forever
	Run tests in an endless loop for rudimentary stress testing.

  -run
  	Required to actually run the test. Just specifying the test with -t TESTNUM is not enough since
  	there are other functions on a test besides just running it.
"""
        if (msg) { println('Error: ' + msg) }
        println(usageMsg)
        System.exit(-1)
    }
}
