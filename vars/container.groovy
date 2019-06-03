#!/usr/bin/env groovy

def call(String image, String network = 'jenkins', Closure body) {
  try {
    def networkExists = sh(
      script: "docker network inspect ${network}",
      returnStatus: true,
      returnStdout: true
    ) == 0

    if (!networkExists) {
      sh "docker network create ${network}"
    }

    docker
      .image(image)
      .inside("-u root:root --network ${network}") {
        body()
    }
  } catch (e) {
    throw e
  } finally {
    if (!networkExists) {
      sh(script: "docker network rm ${it}", returnStatus: true)
    }
  }
}
