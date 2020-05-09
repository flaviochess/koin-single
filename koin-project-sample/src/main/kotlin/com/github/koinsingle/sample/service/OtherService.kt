package com.github.koinsingle.sample.service

import com.github.koinsingle.sample.dao.Dao
import com.github.koinsingle.sample.dao.OtherDao

class OtherService(
        private val dao: Dao,
        private val otherDao: OtherDao
) {

    fun function() = "OtherService + [${dao.function()}] + [${otherDao.function()}]"
}