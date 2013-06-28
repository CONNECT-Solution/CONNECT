import sys

# variables
username=sys.argv[1]
password=sys.argv[2]

adminUrl=sys.argv[3]  
applicationName=sys.argv[4] 
serverName=sys.argv[5]  
applactionEARFileName=sys.argv[6]


# connect to the server
connect(username, password, adminUrl)

# move to the managed server
serverConfig()
cd('Servers\server1')
ls()
# Deploy application
deploy(applicationName, applactionEARFileName, targets='server1',block='true')

