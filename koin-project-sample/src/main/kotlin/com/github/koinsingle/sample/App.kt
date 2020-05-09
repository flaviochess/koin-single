package com.github.koinsingle.sample

import com.github.koinsingle.sample.config.ServerConfig

class App

fun main(args: Array<String>) {

    ServerConfig().setup().start(7000)

}
