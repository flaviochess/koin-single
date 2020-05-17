package com.github.koinsingle.sample.config

import com.github.koinsingle.annotation.Single
import com.github.koinsingle.sample.controller.Controller
import com.github.koinsingle.sample.controller.OtherController
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.get
import io.javalin.apibuilder.ApiBuilder.path

@Single
class Router(
        private val controller: Controller,
        private val otherController: OtherController
) {

    fun register(app: Javalin) {

        app.routes {

            path("path") {
                get("/") {ctx ->
                    ctx.result(controller.function())
                    ctx.status(200)
                }
            }

            get("/") { ctx ->
                ctx.result(otherController.function())
                ctx.status(200)
            }
        }
    }

}
