#!/usr/bin/env groovy

def call(List containers, Closure body) {
  def containerIds = []
  def networks = []

  try {
    containers.each {
      def c = it

      def name = c.containsKey('name') ? "--name ${c.name}" : ''

      def env = ''
      c.env.each { env += " -e ${it}"}

      def network = c.containsKey('network') ? c.network : 'jenkins'

      def networkExists = sh(
        script: "docker network inspect ${network}",
        returnStatus: true,
        returnStdout: true
      ) == 0

      if (!networkExists) {
        sh "docker network create ${network}"
        networks.push(network)
      }

      def params = "-d ${name} --network ${network} ${env} ${c.image}"
      def id = sh(script: "docker run ${params}", returnStdout: true).trim()
      containerIds.push(id)
    }

    body()
  } catch (e) {
    throw e
  } finally {
    containerIds.each {
      sh(script: "docker kill ${it}", returnStatus: true)
      sh(script: "docker rm ${it}", returnStatus: true)
    }
    
    networks.each {
      sh(script: "docker network rm ${it}", returnStatus: true)
    }
  }
}
