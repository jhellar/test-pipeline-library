#!/usr/bin/env groovy

import DockerNetwork

def call(String image, Closure body) {
  def network = ''
  if (DockerNetwork.current != '') {
    nework = "--network ${DockerNetwork.current}"
  }

  docker
    .image(image)
    .inside("-u root:root ${network}") {
      body()
  }
}
