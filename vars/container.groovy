#!/usr/bin/env groovy

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
