package com.github.koinsingle.sample.dao

import com.github.koinsingle.annotation.Single

@Single
class Dao : Daoable {

    override fun function() = "DAO"
}