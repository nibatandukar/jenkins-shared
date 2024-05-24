def BuildDev1(Map config = [:]) {
    docker.withRegistry('https://366751107728.dkr.ecr.us-west-2.amazonaws.com', 'ecr:us-west-2:jenkins-ecr-chnageset') {
        def dockerfile = "${config.Dockerfile}"
        def customImage = docker.build("jenkins-change-set:${config.tag}", "-f ./dev1/${dockerfile} ./dev1/")
        customImage.push()
    }
}

def BuildDev2(Map config = [:]) {
    docker.withRegistry('https://366751107728.dkr.ecr.us-west-2.amazonaws.com', 'ecr:us-west-2:jenkins-ecr-chnageset') {
        def dockerfile = "${config.Dockerfile}"
        def customImage = docker.build("jenkins-change-set:${config.tag}", "-f ./dev2/${dockerfile} ./dev2/")
        customImage.push()
    }
}

def BuildDev3(Map config = [:]) {
    docker.withRegistry('https://366751107728.dkr.ecr.us-west-2.amazonaws.com', 'ecr:us-west-2:jenkins-ecr-chnageset') {
        def dockerfile = "${config.Dockerfile}"
        def customImage = docker.build("jenkins-change-set:${config.tag}", "-f ./dev3/${dockerfile} ./dev3/")
        customImage.push()
    }
}

