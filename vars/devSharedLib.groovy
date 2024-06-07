def BuildDev1(Map config = [:]) {
    withCredentials([string(credentialsId: 'gituser', variable: 'username')], [string(credentialsId: 'gitpassword', variable: 'password')]) {
    docker.withRegistry('https://018028332614.dkr.ecr.us-east-1.amazonaws.com', 'ecr:us-east-1:fpg-prod-ecr-creds') {
        def dockerfile = "${config.Dockerfile}"
        def customImage = docker.build("dev1:${config.tag}", "-f ./dev1/${dockerfile} ./dev1/")
        customImage.push()
    }
    }
}

def BuildDev2(Map config = [:]) {
    docker.withRegistry('https://018028332614.dkr.ecr.us-east-1.amazonaws.com', 'ecr:us-east-1:fpg-prod-ecr-creds') {
        def dockerfile = "${config.Dockerfile}"
        def customImage = docker.build("dev2:${config.tag}", "-f ./dev2/${dockerfile} ./dev2/")
        customImage.push()
    }
}

def BuildDev3(Map config = [:]) {
    docker.withRegistry('https://018028332614.dkr.ecr.us-east-1.amazonaws.com', 'ecr:us-east-1:fpg-prod-ecr-creds') {
        def dockerfile = "${config.Dockerfile}"
        def customImage = docker.build("dev3:${config.tag}", "-f ./dev3/${dockerfile} ./dev3/")
        customImage.push()
    }
}

/*CD Methods*/
def Deployclone(Map config = [:]) {
    withCredentials([string(credentialsId: 'jenkins-change-set', variable: 'value')]) {
        git credentialsId: 'jenkins-change-set', url: "${value}", branch: "${config.hek=helmBranch}"
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
        sh """
        
        export KUBECONFIG=$KUBECRED
        sed "s+helmrelease-branch+"${config.helmBranch}"+g" helmcharts/${config.ENV}/dev1/helmrelease.yaml > helmcharts/${config.ENV}/dev1/helmrelease1.yaml
        kubectl apply -f helmcharts/${config.ENV}/dev1/helmrelease1.yaml
        sed -i 's/tag:.*/tag: ${config.tag}/g' helmcharts/${config.ENV}/dev1/values.yaml
        
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
        sed "s+helmrelease-branch+"${config.helmBranch}"+g" helmcharts/${config.ENV}/dev2/helmrelease.yaml > helmcharts/${config.ENV}/dev2/helmrelease1.yaml
        kubectl apply -f helmcharts/${config.ENV}/dev2/helmrelease1.yaml
        sed -i 's/tag:.*/tag: ${config.tag}/g' helmcharts/${config.ENV}/dev2/values.yaml
        
        """
    }
}

def DeployDev3(Map config = [:]) {
    
    withCredentials([
        file(credentialsId: "${config.configId}", variable: 'KUBECRED'),
        gitUsernamePassword(credentialsId: 'jenkins-change-set', gitToolName: 'Default')
    ]) {
        sh """
        
        export KUBECONFIG=$KUBECRED
        sed "s+helmrelease-branch+"${config.helmBranch}"+g" helmcharts/${config.ENV}/dev3/helmrelease.yaml > helmcharts/${config.ENV}/dev3/helmrelease1.yaml
        kubectl apply -f helmcharts/${config.ENV}/dev3/helmrelease1.yaml
        sed -i 's/tag:.*/tag: ${config.tag}/g' helmcharts/${config.ENV}/dev3/values.yaml
        
        """
    }
}
