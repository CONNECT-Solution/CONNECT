The Interoperability Lab software is checked in here for safety purposes.

The code is for internal use only is provided "as is" and is not warranted or supported outside
of the CONNECT development/test teams.

That said, a couple of things about the lab to be considerered:

There are 6 servers with the following names, operating systems and CONNECT versions:
WDEV11 - Windows XP - CONNECT 3.3.0.15 (or current) HUB SERVEER
LDEV11 - Linux - CONNECT 3.3.0.15 (or current) NODE SERVER
WTST11 - Windows XP - CONNECT 3.2.1.9 NODE SERVER
SDEV11 - Solaris - CONNECT 3.1.0.455 NODE SERVER
STST11 - Solaris - CONNECT 2.4.7 NODE SERVER
LTST11 - Linux - CONNECT 2.4.8 NODE SERVER

The Windows and Unix servers share a common drive.  It is mapped as the F:
drive in Windows and /xenvs on Unix.

On the common drive, the following structures exist:
<commondrive>-interop_test - contains a subdirectory for each of the servers above to act as the 
messaging drive for the interop test harness.

<commondrive>-interop_lab_results - contains a subdirectory for each server above with 
the exception of LDEV11 that never tests back to the hub to house the interop test results files.  
For the WDEV11 (hub) server there is a subdirectory for each of the nodes (WTST11, LDEV11, SDEV11, 
STST11, LTST11). Each of the above subdirectories has a warmup subsubdirectory.

To execute the interop tests, on the hub execute the following:

C:\interop_harness\run_interop_harness from a DOS prompt or scheduled task.

At the end of the test runs, the results will be written to C:\interop_setup\interop_Sum.htm which 
can also be moved to an Apache server directory for hosting/remote access.


