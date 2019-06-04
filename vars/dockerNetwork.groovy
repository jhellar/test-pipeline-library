#!/usr/bin/env groovy

currentDockerNetwork = ''

def call(String network = 'jenkins', Closure body) {
  def previous = currentDockerNetwork

  try {
    currentDockerNetwork = network
    sh "docker network create ${network}"
    body()
  } catch (e) {
    throw e
  } finally {
    sh(script: "docker network rm ${network}", returnStatus: true)
    currentDockerNetwork = previous
  }
}
