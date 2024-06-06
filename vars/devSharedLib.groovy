def BuildDev1(Map config = [:]) {
    docker.withRegistry('https://018028332614.dkr.ecr.us-east-1.amazonaws.com', 'ecr:us-east-1:fpg-prod-ecr-creds') {
        def dockerfile = "${config.Dockerfile}"
        def customImage = docker.build("jenkins-change-set:${config.tag}", "-f ./dev1/${dockerfile} ./dev1/")
        customImage.push()
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

