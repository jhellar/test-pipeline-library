#!/usr/bin/env groovy

def call(String registry, Closure body) {
  def original = sh(script: 'npm get registry', returnStdout: true).trim()

  try {
    sh "npm set registry ${registry}"
    body()
  } catch (e) {
    throw e
  } finally {
    sh "npm set registry ${original}"
  }
}
