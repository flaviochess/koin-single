package com.github.koinsingle.sample.service

import com.github.koinsingle.annotation.Single
import com.github.koinsingle.sample.dao.Dao
import com.github.koinsingle.sample.dao.OtherDao

@Single
class Service(
        private val dao: Dao,
        private val otherDao: OtherDao
) {

    fun function() = "Service + [${dao.function()}] + [${otherDao.function()}]"
}