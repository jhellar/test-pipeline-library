#!/usr/bin/env groovy

def call(String network = 'jenkins', Closure body) {
  try {
    sh "docker network create ${network}"
    body()
  } catch (e) {
    throw e
  } finally {
    sh(script: "docker network rm ${network}", returnStatus: true)
  }
}
