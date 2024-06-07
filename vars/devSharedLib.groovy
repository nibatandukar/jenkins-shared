def BuildDev1(Map config = [:]) {
    withCredentials([string(credentialsId: 'gituser', variable: 'username'), string(credentialsId: 'gitpassword', variable: 'password')]) {
    docker.withRegistry('https://018028332614.dkr.ecr.us-east-1.amazonaws.com', 'ecr:us-east-1:fpg-prod-ecr-creds') {
        def dockerfile = "${config.Dockerfile}"
        def customImage = docker.build("jenkins-change-set:dev1-${config.tag}", "--build-arg username=$username --build-arg password=$password -f ./dev1/${dockerfile} ./dev1/")
        customImage.push()
    }
    }
}


def BuildDev2(Map config = [:]) {
    docker.withRegistry('https://018028332614.dkr.ecr.us-east-1.amazonaws.com', 'ecr:us-east-1:fpg-prod-ecr-creds') {
        def dockerfile = "${config.Dockerfile}"
        def customImage = docker.build("jenkins-change-set:${config.tag}", "-f ./dev2/${dockerfile} ./dev2/")
        customImage.push()
    }
}

def BuildDev3(Map config = [:]) {
    docker.withRegistry('https://018028332614.dkr.ecr.us-east-1.amazonaws.com', 'ecr:us-east-1:fpg-prod-ecr-creds') {
        def dockerfile = "${config.Dockerfile}"
        def customImage = docker.build("jenkins-change-set:${config.tag}", "-f ./dev3/${dockerfile} ./dev3/")
        customImage.push()
    }
}

/*CD Methods*/
def Deployclone(Map config = [:]) {
    withCredentials([string(credentialsId: 'jenkins-changeset-github-url', variable: 'value')]) {
        git credentialsId: 'jenkins-change-set', url: "${value}", branch: "${config.helmBranch}"
    }
    withCredentials([
        file(credentialsId: "${config.configId}", variable: 'KUBECRED'),
        gitUsernamePassword(credentialsId: 'jenkins-change-set', gitToolName: 'Default')
    ]) {
        sh """
        git config --global user.name "jenkins"
        git config --global --list
        echo "Cloning the repo!!!!!!!!!!!!!!!"
        """
    }
}


def DeployDev1(Map config = [:]) {
    
    withCredentials([
        file(credentialsId: "${config.configId}", variable: 'KUBECRED'),
        gitUsernamePassword(credentialsId: 'jenkins-change-set', gitToolName: 'Default')
    ]) {
        
        // sh """
        //  export KUBECONFIG=$KUBECRED
        //  sed "s+helmrelease-branch+"${config.helmBranch}"+g" helmcharts/${config.ENV}/dev1/helmrelease.yaml > helmcharts/${config.ENV}/dev1/helmrelease1.yaml *
        //  kubectl apply -f helmcharts/${config.ENV}/dev1/helmrelease1.yaml
        //  sed -i 's/tag:.*/tag: ${config.tag}/g' helmcharts/${config.ENV}/dev1/values.yaml
        // """
            sh """
        
        export KUBECONFIG=$KUBECRED
        cd helmcharts/${config.ENV}
        helm delete dev1
        helm install dev1 dev1
        
        """
    }
}


def DeployDev2(Map config = [:]) {
    
    withCredentials([
        file(credentialsId: "${config.configId}", variable: 'KUBECRED'),
        gitUsernamePassword(credentialsId: 'jenkins-change-set', gitToolName: 'Default')
    ]) {
        sh """
        
        export KUBECONFIG=$KUBECRED
        cd helmcharts/${config.ENV}
        helm delete dev2
        helm install dev2 dev2
        """
        //     sh """
        
        // export KUBECONFIG=$KUBECRED
        // // sed "s+helmrelease-branch+"${config.helmBranch}"+g" helmcharts/${config.ENV}/dev2/helmrelease.yaml > helmcharts/${config.ENV}/dev2/helmrelease1.yaml
        // // kubectl apply -f helmcharts/${config.ENV}/dev2/helmrelease1.yaml
        // // sed -i 's/tag:.*/tag: ${config.tag}/g' helmcharts/${config.ENV}/dev2/values.yaml
        
        // """
    }
}

def DeployDev3(Map config = [:]) {
    
    withCredentials([
        file(credentialsId: "${config.configId}", variable: 'KUBECRED'),
        gitUsernamePassword(credentialsId: 'jenkins-change-set', gitToolName: 'Default')
    ]) {
        // sh """
        // export KUBECONFIG=$KUBECRED
        // // sed "s+helmrelease-branch+"${config.helmBranch}"+g" helmcharts/${config.ENV}/dev3/helmrelease.yaml > helmcharts/${config.ENV}/dev3/helmrelease1.yaml
        // // kubectl apply -f helmcharts/${config.ENV}/dev3/helmrelease1.yaml
        // // sed -i 's/tag:.*/tag: ${config.tag}/g' helmcharts/${config.ENV}/dev3/values.yaml
        
        // """
        
        sh """
        export KUBECONFIG=$KUBECRED
        wget https://get.helm.sh/helm-v3.15.0-linux-arm64.tar.gz
        tar -zxvf helm-v3.15.0-linux-amd64.tar.gz
        mv linux-amd64/helm /usr/local/bin/helm
        
        cd helmcharts/${config.ENV}
        helm delete dev3
        helm install dev3 dev3
        """
    }
}
