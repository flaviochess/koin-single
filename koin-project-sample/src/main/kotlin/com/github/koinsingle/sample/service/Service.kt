package com.github.koinsingle.sample.service

import com.github.koinsingle.sample.dao.Dao
import com.github.koinsingle.sample.dao.OtherDao

class Service(
        private val dao: Dao,
        private val otherDao: OtherDao
) {

    fun function() = "Service + [${dao.function()}] + [${otherDao.function()}]"
}