#!/usr/bin/env groovy

pipeline {
    agent {
     label 'android-worker'
    }

    environment {
            PIPELINE_JOBS_NAME = 'edx-app-android-pipeline'
            ANDROID_HOME = '/opt/android-sdk-linux'
            APK_PATH = 'OpenEdXMobile/build/outputs/apk/prod/debuggable'
    }       

    stages {
        stage('checkingout configs') {
            steps {
                sh 'mkdir -p edx-mobile-config'
                dir('edx-mobile-config'){
                    checkout([
                        $class: 'GitSCM', 
                        branches: [[name: '*/master']], 
                        doGenerateSubmoduleConfigurations: false, 
                        extensions: [], 
                        submoduleCfg: [], 
                        userRemoteConfigs: 
                        [[credentialsId: 'USER', url: 'https://github.com/edx/edx-mobile-config']]
                        ])
                }
            }
        }

        stage('create required file '){
           steps {
               writeFile file: './OpenEdXMobile/edx.properties', text: 'edx.dir = \'../edx-mobile-config/prod/\''  
               } 
        }
        stage('compiling edx-app-android') {
            steps {
                sh 'bash ./resources/compile_android.sh'
            }
        }
        stage('valdiate compiled app') {
            steps {
                sh 'bash ./resources/validate_builds.sh'
            }
        }
        stage('archive the build') {
            steps {
                archiveArtifacts artifacts: "$APK_PATH/*.apk", onlyIfSuccessful: true
            }
        }
    }
}