deployALL="/xenvs/interop_test/ldev11/DEPLOY_ALL" #all g1 and g0
deployGZ="/xenvs/interop_test/ldev11/DEPLOY_GZ"  # only g0 
deployPQR="/xenvs/interop_test/ldev11/DEPLOY_PQR" #PD DQ and DR
deployHIEM="/xenvs/interop_test/ldev11/DEPLOY_HIEM" #HIEM
deployAS="/xenvs/interop_test/ldev11/DEPLOY_AS"  #AD and DS
restore="/nhin/NHINC/deploy.application.list.xml.BACKUP"
run="/xenvs/interop_test/ldev11/NODE_RUN"

while true; do
	#PlaceHolder	

while true; do
	sleep 10
	if [ -f $deployALL ]
		then	
		#INSERT PREP FOR HUB TO NODE TESTS
		
		#TEST RUN
		pushd .
		cd /nhin/NHINC
		ant -f deploy.xml deploy.glassfish.stop.server

		ant -f deploy.xml delete.common.jars

		ant -f deploy.xml deploy.shared.libs.to.glassfish

		ant -f deploy.xml deploy.glassfish.start.server

		ant -f deploy.xml deploy.wars.to.glassfish

		ant -f deploy.xml deploy.ejbs.to.glassfish
		popd
		
		/nhin/interop_setup/reset_interop_node.sh verify	
		sleep 30
		#TEST RUN
		
		rm -f $deployALL 
		
		echo "" > /xenvs/interop_test/ldev11/READY
		#break
	
	elif [ -f $deployGZ ]
		then	
		#INSERT PREP FOR HUB TO NODE TESTS
		
		#TEST RUN
		pushd .
		
		cd /nhin/NHINC
		ant -f deploy.xml undeploy 
		mv -f deploy.application.list.xml $restore
		cp -f /nhin/interop_setup/deploy.application.list.justG0.xml deploy.application.list.xml

		ant -f deploy.xml deploy.glassfish.stop.server

		ant -f deploy.xml delete.common.jars

		ant -f deploy.xml deploy.shared.libs.to.glassfish

		ant -f deploy.xml deploy.glassfish.start.server

		ant -f deploy.xml deploy.wars.to.glassfish

		ant -f deploy.xml deploy.ejbs.to.glassfish
		cd /nhin/NHINC
		popd
		
		/nhin/interop_setup/reset_interop_node.sh verify	
		sleep 30
		#TEST RUN
		
		rm -f $deployGZ 
		
		echo "" > /xenvs/interop_test/ldev11/READY
		#break
	
	elif [ -f $deployPQR ]
		then	
		#INSERT PREP FOR HUB TO NODE TESTS
		
		#TEST RUN
		pushd .
		
		cd /nhin/NHINC
		ant -f deploy.xml undeploy
		cp -f /nhin/interop_setup/deploy.application.list.DQ_DR_PD.xml deploy.application.list.xml
		
		ant -f deploy.xml deploy.glassfish.stop.server

		ant -f deploy.xml delete.common.jars

		ant -f deploy.xml deploy.shared.libs.to.glassfish

		ant -f deploy.xml deploy.glassfish.start.server

		ant -f deploy.xml deploy.wars.to.glassfish

		ant -f deploy.xml deploy.ejbs.to.glassfish
		popd
		
		/nhin/interop_setup/reset_interop_node.sh verify	
		sleep 30
		#TEST RUN
		
		rm -f $deployPQR
		
		echo "" > /xenvs/interop_test/ldev11/READY
		#break
	
	elif [ -f $deployHIEM ]
		then	
		#INSERT PREP FOR HUB TO NODE TESTS
		
		#TEST RUN
		pushd .
		
		cd /nhin/NHINC
		ant -f deploy.xml undeploy 
		cp -f /nhin/interop_setup/deploy.application.list.HIEM.xml deploy.application.list.xml
		ant -f deploy.xml deploy.glassfish.stop.server

		ant -f deploy.xml delete.common.jars

		ant -f deploy.xml deploy.shared.libs.to.glassfish

		ant -f deploy.xml deploy.glassfish.start.server

		ant -f deploy.xml deploy.wars.to.glassfish

		ant -f deploy.xml deploy.ejbs.to.glassfish
		popd
		
		/nhin/interop_setup/reset_interop_node.sh verify	
		sleep 30
		#TEST RUN
		
		rm -f $deployHIEM
		
		echo "" > /xenvs/interop_test/ldev11/READY
		break
	
	elif [ -f $deployAS ]
		then	
		#INSERT PREP FOR HUB TO NODE TESTS
		
		#TEST RUN
		pushd .
		
		cd /nhin/NHINC
		ant -f deploy.xml undeploy 
		cp -f /nhin/interop_setup/deploy.application.list.AD_DS.xml deploy.application.list.xml
		ant -f deploy.xml deploy.glassfish.stop.server

		ant -f deploy.xml delete.common.jars

		ant -f deploy.xml deploy.shared.libs.to.glassfish

		ant -f deploy.xml deploy.glassfish.start.server

		ant -f deploy.xml deploy.wars.to.glassfish

		ant -f deploy.xml deploy.ejbs.to.glassfish
		popd
		
		/nhin/interop_setup/reset_interop_node.sh verify	
		sleep 30
		#TEST RUN
		
		rm -f $deployAS
		
		echo "" > /xenvs/interop_test/ldev11/READY
		#break
	fi
done

while true; do
	sleep 10
	if [ -f $run ]; then
	
		#INSERT CALL TO RUN NODE TO HUB TESTS
		if [ -f  $restore ]
			then
			
			mv -f $restore /nhin/NHINC/deploy.application.list.xml
			
		fi
		
		#TEST RUN
		/nhin/glassfish/bin/asadmin stop-domain domain1
		sleep 30
		#TEST RUN
		
		rm -f $run 
		
		echo "" > /xenvs/interop_test/ldev11/NODE_FINISHED
		break
	fi
done

done

