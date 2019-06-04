#!/usr/bin/env groovy

def call(List containers, String network = 'jenkins', Closure body) {
  def containerIds = []

  try {
    containers.each {
      def c = it

      def name = c.containsKey('name') ? "--name ${c.name}" : ''

      def env = ''
      c.env.each { env += " -e ${it}"}

      def ports = ''
      c.ports.each { ports += " -p ${it}"}

      def params = "-d ${name} --network ${network} ${ports} ${env} ${c.image}"
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
  }
}
