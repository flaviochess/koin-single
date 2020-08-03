package com.github.koinsingle.sample.service

import com.github.koinsingle.annotation.Single
import com.github.koinsingle.sample.dao.Daoable
import com.github.koinsingle.sample.dao.OtherDao
import com.github.koinsingle.sample.service.interfaces.Serviceable

@Single(named = "firstService")
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

@Single(named = "secondService")
class Service2(
        private val dao: Daoable,
        private val otherDao: OtherDao
): Serviceable {
    override fun function() = "Service + [${dao.function()}] + [${otherDao.function()}]"
}
