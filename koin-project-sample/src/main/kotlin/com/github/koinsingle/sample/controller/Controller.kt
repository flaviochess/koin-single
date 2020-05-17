package com.github.koinsingle.sample.controller

import com.github.koinsingle.annotation.Single
import com.github.koinsingle.sample.service.OtherService
import com.github.koinsingle.sample.service.Service

@Single
class Controller(
        private val service: Service,
        private val otherService: OtherService
) {

    fun function() = "Controller + [${service.function()}] + [${otherService.function()}]"
}