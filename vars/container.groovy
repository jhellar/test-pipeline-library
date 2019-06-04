#!/usr/bin/env groovy

def call(String image, String name = '', String network = 'jenkins', Closure body) {
  def nameParam = name == '' ? '' : "--name ${name}"
  docker
    .image(image)
    .inside("-u root:root --network ${network} ${nameParam}") {
      body()
  }
}
