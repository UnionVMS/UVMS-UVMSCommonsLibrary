pipeline {
    agent any
	options {
	  buildDiscarder logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '', daysToKeepStr: '2', numToKeepStr: '5')
	  skipStagesAfterUnstable()
	}
	stages{
	   stage('Clone') {
			steps{
	        // Get some code from a GitHub repository
	        //git branch: 'dgmare-handover', url:'https://github.com/UnionVMS/UVMS-UVMSCommonsLibrary.git'
	        checkout scm
	   		}
	   }
	   
	   stage('Maven Build') {
	       steps{
	       		// Run the maven build
		      	withMaven(
		          // Maven installation declared in the Jenkins "Global Tool Configuration"
		          maven: 'M3',
		          // Maven settings.xml file defined with the Jenkins Config File Provider Plugin
		          // We recommend to define Maven settings.xml globally at the folder level using
		          // navigating to the folder configuration in the section "Pipeline Maven Configuration / Override global Maven configuration"
		          // or globally to the entire master navigating to  "Manage Jenkins / Global Tools Configuration"
		          mavenSettingsConfig: 'sword-settings',
		          mavenLocalRepo: '.repo') {
		          	sh 'mvn -Dclean deploy -DaltDeploymentRepository=swordnexus-repo-snapshot::default::http://nexus:8081/repository/maven-snapshots/'
		      	}
	      	}
	   	}
    }
}