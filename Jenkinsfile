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
                echo ' Code récupéré avec succès'
            }
        }
        
        stage('Build') {
            steps {
                echo '===== Compilation du code avec Maven ====='
                sh 'mvn clean compile'
                echo ' Compilation terminée'
            }
        }
        
        stage('Test') {
            steps {
                echo '===== Exécution des tests ====='
                sh 'mvn test'
            }
        }
        
        stage('Package') {
            steps {
                echo '===== Création du fichier JAR ====='
                sh 'mvn package -DskipTests'
                echo ' Package créé dans target/'
            }
        }
    }
    
    post {
        success {
            echo '========================================='
            echo ' BUILD RÉUSSI !'
            echo 'Fichier JAR disponible dans target/'
            echo '========================================='
        }
        failure {
            echo '========================================='
            echo ' BUILD ÉCHOUÉ !'
            echo '========================================='
        }
        always {
            echo 'Build terminé'
        }
    }
}
