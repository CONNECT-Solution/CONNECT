readme.txt - XDS Test Kit

This directory contains several key directories:

testdata - contains test scripts for initializing Registry, Repository,
Receiving Gateway with test data

tests - scripts for individual tests

examples - scripts acting as examples of operations that a Document Source
must perform

The directory structure of each test script is:

TESTNUM
	- readme.txt - secondary documentation
	- testplan.xml - test script
	- other support files referenced by testplan.xml

Some tests have multiple sub-tests which are represented by directories 
within the test directory.  When this pattern is used, there is no 
testplan.xml in the top test directory and instead there is a file
index.idx which is used by the xdstest tool to order the running of the
sub-tests.  When sub-tests exist, the directory structure looks like:

TESTNUM
	- readme.txt - secondary documentation
	- index.idx  -  order that sub-tests must be performed in (used by
xdstest tool)
	+ submit (name is arbitrary - submit here is only an example)
		- testplan.xml
		- other support files referenced by testplan.xml
	
