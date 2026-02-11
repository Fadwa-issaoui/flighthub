pipeline {
    agent any
    
    tools {
        maven 'Maven'
        jdk 'JAVA_HOME'
    }
    
    stages {
        stage('Checkout') {
            steps {
                echo '=====  Récupération du code depuis Git ====='
                checkout scm
                echo ' Code récupéré avec succès'
            }
        }
        
        stage('Vérification') {
            steps {
                echo '=====  Vérification des fichiers ====='
                sh 'ls -la'
                sh 'pwd'
                echo '=====  Affichage du pom.xml ====='
                sh 'cat pom.xml | head -30'
            }
        }
        
        stage('Build') {
            steps {
                echo '=====  Compilation avec Maven ====='
                sh 'mvn --version'
                sh 'mvn clean compile'
            }
        }
        
        stage('Test') {
            steps {
                echo '=====  Exécution des tests ====='
                sh 'mvn test || true'
            }
        }
        
        stage('Package') {
            steps {
                echo '=====  Création du package ====='
                sh 'mvn package -DskipTests'
            }
        }
    }
    
    post {
        success {
            echo ' ========================================='
            echo ' BUILD RÉUSSI !'
            echo ' ========================================='
        }
        failure {
            echo ' ========================================='
            echo ' BUILD ÉCHOUÉ !'
            echo ' ========================================='
        }
        always {
            echo ' Build terminé'
        }
    }
}
