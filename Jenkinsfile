pipeline {
    agent any

    tools {
        maven 'Maven'
        jdk 'JAVA_HOME'
    }

    stages {
        stage('Checkout') {
            steps {
                echo '===== Récupération du code depuis GitHub ====='
                checkout scm
                echo 'Code récupéré avec succès'
            }
        }

        stage('Build') {
            steps {
                echo '===== Compilation du code avec Maven ====='
                sh 'mvn clean compile'
                echo 'Compilation terminée'
            }
        }
    }

    post {
        success {
            echo '===== BUILD RÉUSSI ====='
        }
        failure {
            echo '===== BUILD ÉCHOUÉ ====='
        }
        always {
            echo 'Build terminé'
        }
    }
}
