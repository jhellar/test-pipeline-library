#!/usr/bin/env groovy

withContainers([
  network: 'jenkins'
  containers: [
    image: 'node',
    env: []
  ]
])

def call(List containers, Closure body) {
  if (containers.size() == 0) {
    body()
  }

  def c = containers.pop()

  def env = ''
  c.env.each { env += " -e ${it}"}

  def network = c.containsKey('network') ? c.network : 'jenkins'

  def networkExists = sh(script: "docker netowork inspect ${network}", returnStatus: true) == 0

  if (!networkExists) {
    sh "docker network create ${network}"
  }

  docker
    .image(c.image)
    .withRun("--network ${network} --name ${c.name} ${env}") {
      call(containers, body)
  }

  if (!networkExists) {
    sh "docker network rm ${network}"
  }
}
