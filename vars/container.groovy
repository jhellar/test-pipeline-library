#!/usr/bin/env groovy

def call(String image, Closure body) {
  def network = ''
  if (currentDockerNetwork != '') {
    nework = "--network ${currentDockerNetwork}"
  }

  docker
    .image(image)
    .inside("-u root:root ${network}") {
      body()
  }
}
