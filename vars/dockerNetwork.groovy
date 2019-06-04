#!/usr/bin/env groovy

class DockerNetwork {
   static String current = ''
}

def call(String network = 'jenkins', Closure body) {
  def previous = DockerNetwork.current

  try {
    DockerNetwork.current = network
    sh "docker network create ${network}"
    body()
  } catch (e) {
    throw e
  } finally {
    sh(script: "docker network rm ${network}", returnStatus: true)
    DockerNetwork.current = previous
  }
}
