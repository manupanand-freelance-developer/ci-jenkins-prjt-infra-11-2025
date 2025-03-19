def call(){
    node(){ 
        stage('Code checkout'){
            sh 'find . |grep "^./" |xargs rm -rf'
            if(env.TAG_NAME==~".*"){
                env.gitbrname= "refs/tags/${env.TAG_NAME}"
            }else{
                env.gitbrname= "${env.BRANCH_NAME}"
            }
            checkout poll: false,scm:[
                $class: 'GitSCM',
                userRemoteConfigs:[
                    [
                        url: "https://github.com/manupanand-freelance-developer/${env.appName}"
                    ]
                ],
                branches: [
                    [
                        name: gitbrname
                    ]
                ]
            ]
        }
        if(env.TAG_NAME){
            stage('Docker build'){
                // print 'Docker build'
                sh "docker build -t manupanand/${env.appName}:${env.TAG_NAME} ."
            }
            stage('Docker Push'){
                sh 'docker login '
                sh "docker push manupanand/${env.appName}:${env.TAG_NAME}" //aws ecr need IAM permission
                //regisrty-amazonEC2containerRegistryPowerUser
            }
            stage('Deploy to dev'){
                sh "gh workflow run pipeline.yaml -f appVersion=${env.TAG_NAME}"  -f appName=${env.appname}" // on repo
            }
        }else{ //this stage we will enable later
            // stage('Compile'){ // only need for go and java | scripting languages doenst require it.
            //     if(appType='maven'){
            //         sh 'mvn compile'
            //     }
            //     else{
            //         print 'compile not required'
            //     }
            // }
            if(BRANCH_NAME != "main"){

                stage('Unit Test Case'){
                    if(appType=='nodejs'){
                        // sh 'npm run test'
                        print 'Unit test #npm run test'
                    }
                     if(appType=='python'){
                        // sh 'npm run test'
                        print 'Unit test #python -m unittest'
                    }
                    if(appType=='maven'){
                        // sh 'npm run test'
                        print 'Unit test #mvn test'
                    }
                    
                }
                stage('Integration Test Case'){
                     if(appType=='nodejs'){
                        // sh 'npm run test'
                        print 'Integration Test #npm run integration-test'
                    }
                     if(appType=='python'){
                        // sh 'npm run test'
                        print 'Integration Test #python -m unittest discover project/tests/integration'
                    }
                    if(appType=='maven'){
                        // sh 'npm run test'
                        print 'Integration Test #mvn inetgration-test'
                    }
                    
                }
                stage('RegressionTest Case'){
                    if(appType=='nodejs'){
                        // sh 'npm run test'
                        print 'Integration Test #npm run integration-test'
                    }
                     if(appType=='python'){
                        // sh 'npm run test'
                        print 'Integration Test #python -m unittest discover project/tests/integration'
                    }
                    if(appType=='maven'){
                        // sh 'npm run test'
                        print 'Integration Test #mvn inetgration-test'
                    }
                }
            }
        
        }
    
    
    }
}