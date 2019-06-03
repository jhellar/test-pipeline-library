#!/usr/bin/env groovy

def call(List containers, Closure body) {
  if (containers.size() == 0) {
    body()
    return
  }

  def c = containers.pop()

  def env = ''
  c.env.each { env += " -e ${it}"}

  def network = c.containsKey('network') ? c.network : 'jenkins'

  def params = "--network ${network} --name ${c.name} ${env}"

  def networkExists = sh(script: "docker network inspect ${network}", returnStatus: true) == 0

  if (!networkExists) {
    sh "docker network create ${network}"

    try {
      docker.image(c.image).withRun(params) {
        call(containers, body)
      }
    } catch (e) {
      throw e
    } finally {
      sh "docker network rm ${network}"
    }
  } else {
    docker.image(c.image).withRun(params) {
      call(containers, body)
    }
  }
}
