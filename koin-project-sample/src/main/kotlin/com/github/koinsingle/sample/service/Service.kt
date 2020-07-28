package com.github.koinsingle.sample.service

import com.github.koinsingle.annotation.Single
import com.github.koinsingle.sample.dao.Daoable
import com.github.koinsingle.sample.dao.OtherDao
import com.github.koinsingle.sample.service.interfaces.Serviceable

@Single(named = "secondary")
class Service(
        private val dao: Daoable,
        private val otherDao: OtherDao
): Serviceable {

//    TODO: for evolution, two constructors
//    var msg: String = ""
//
//    constructor(
//            dao: Dao,
//            otherDao: OtherDao,
//            @Property msg: String //creates other object instead of string
//    ) : this(dao, otherDao) {
//        this.msg = msg
//    }

    override fun function() = "Service + [${dao.function()}] + [${otherDao.function()}]"
}
