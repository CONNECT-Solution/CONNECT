releaseVersion = args[0]
branchPath = args[1]
username = args[2]
password = args[3]
releaseCandidateWorkingCopy = args[4]
repoURL = args[5]
archiveDirectory = args[6]; //"c:/archives"
deploymentQueueDirectory = args[7]  //c:/current-tst

///////THINGS THAT ARE GOING TO RARELY CHANGE
svnAntInstallationDir = new File(".", "svnant-1.3.1/").getAbsolutePath()
branchURL = repoURL + branchPath
tagsURL = repoURL + "/tags"
TAGGING_MESSAGE = "Tagging a release candidate"
///////////////////////////////////////

def mostRecentReleaseCandidateNumber(existingTagNames, releaseVersion) {
    def rcNumbers = []
    for(def tagName in existingTagNames) {
        def matcher = (tagName =~ /${releaseVersion}.([0-9]+)/)
        if(matcher.size() > 0) {
            rcNumbers.add(matcher[0][1])
        }
    }
    
    if(rcNumbers.size()>0) {
        return rcNumbers.max()
    }
    else {
        return 0
    }
}


def antFactory(svnAntInstallationDir) {
   def ant = new AntBuilder(); 
   
   ant.path(id:"path.svnant") {
       pathelement(location:svnAntInstallationDir+"/lib/svnant.jar")
       pathelement(location:svnAntInstallationDir+"/lib/svnClientAdapter.jar")
       pathelement(location:svnAntInstallationDir+"/lib/svnkit.jar")
       pathelement(location:svnAntInstallationDir+"/lib/svnjavahl.jar")
       pathelement(location:svnAntInstallationDir+"/lib/ganymed.jar")
   }

   ant.taskdef(resource:"org/tigris/subversion/svnant/svnantlib.xml",
               classpathref:"path.svnant")
 
   return ant
}

def retrieveTagNames(ant, tagsURL) {
    ant.project.properties.tag_listing = ""
    
    ant.svn(username: username,
            password: password) {
        list(url: tagsURL,
             property: "tag_listing")
    }
    
    return ant.project.properties.tag_listing.split(',')
}

def makeTag(ant, branchURL, tagsURL, username, password) {
    def existingTagNames = retrieveTagNames(ant, tagsURL)
    def newRCNumber = mostRecentReleaseCandidateNumber(existingTagNames, releaseVersion).toInteger() + 1  
    def newTagURL = tagsURL + "/" + releaseVersion + "." + newRCNumber 
    
    println "Making a new tag at ${newTagURL}"
    
    ant.svn(username: username,
            password: password) {
        copy(srcUrl: branchURL,
             destUrl: newTagURL,
             message: TAGGING_MESSAGE)
    }
    return [url: newTagURL, 
            release: releaseVersion +"." +newRCNumber]
}

def checkoutTag(ant, newTagURL, username, password) {
    ant.delete(dir: releaseCandidateWorkingCopy)

    ant.mkdir(dir: releaseCandidateWorkingCopy)

    println "About to checkout on ${releaseCandidateWorkingCopy}"
    
    ant.svn(username: username,
            password: password, 
            logFile: "STDOUT") {
        checkout(url: newTagURL,
                 destPath: releaseCandidateWorkingCopy)
    }
}


def dist(directoryToBuildFrom, releasedVersionName) {
    
    def cmd = 'cmd /c "cd '+ directoryToBuildFrom.getAbsolutePath() +' & ant.bat -Dreleased.version.name='+releasedVersionName+' dist"'
    //println cmd

    def proc = cmd.execute()
    proc.consumeProcessOutput(System.out, System.err) 
    proc.waitFor()
}


def archiveTag(ant, releasedVersionName) {

    def targetDir = new File(archiveDirectory, releasedVersionName)
    ant.mkdir(dir: targetDir.getAbsolutePath())

    println "Archiving distributions at ${targetDir}"

    ant.copy(todir: targetDir.getAbsolutePath()) {
        fileset(dir: releaseCandidateWorkingCopy + "/target",
                includes: "*.zip")
    }

    ////////to the queue directory for the deployment project to poll and pick up

    ant.copy(todir: deploymentQueueDirectory) {
        fileset(dir: releaseCandidateWorkingCopy + "/target",
                includes: "*.zip")
    }    
}

/////


ant = antFactory(svnAntInstallationDir)

tagTuple = makeTag(ant, 
                   branchURL, 
                   tagsURL, 
                   username, 
                   password)

checkoutTag(ant, 
            tagTuple.url,
            username, 
            password)
          
            
//dist("c:\\release_candidate_working_copy\\Product")            
dist(new File(releaseCandidateWorkingCopy, "Product"), tagTuple.release)

archiveTag(ant, tagTuple.release)
//////////////////






