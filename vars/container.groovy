#!/usr/bin/env groovy

def call(String image, String network = 'jenkins', Closure body) {
  docker
    .image(image)
    .inside("-u root:root --network ${network}") {
      body()
  }
}
