#!/usr/bin/env groovy

pipeline {
    agent {
     label 'android-worker'
    }

    environment {
            PIPELINE_JOBS_NAME = 'edx-app-android-pipeline'
            ANDROID_HOME = '/opt/android-sdk-linux'
            EDX_PROPERTIES = './OpenEdXMobile/edx.properties'
            APK_PATH = 'OpenEdXMobile/build/outputs/apk/prod/debuggable'
    }       

    stages {
        
        stage('create required file '){
           steps {
               touch file: '$EDX_PROPERTIES'
               writeFile file: '$EDX_PROPERTIES', text: 'edx.dir = \'../../edx-mobile-config/prod/\''     
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