package com.github.koinsingle.sample.config

import com.github.koinsingle.sample.controller.Controller
import com.github.koinsingle.sample.controller.OtherController
import com.github.koinsingle.sample.dao.Dao
import com.github.koinsingle.sample.dao.OtherDao
import com.github.koinsingle.sample.service.OtherService
import com.github.koinsingle.sample.service.Service
import org.koin.dsl.module

val manualGeneratedModule = module {

    single { Controller(get(), get()) }
    single { OtherController(get(), get()) }

    single { Service(get(), get()) }
    single { OtherService(get(), get()) }

    single { Dao() }
    single { OtherDao() }

    single { Router(get(), get()) }
}