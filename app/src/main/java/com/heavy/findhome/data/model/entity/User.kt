package com.heavy.findhome.data.model.entity

data class User(
    var email: String = "",
    var password: String = "",
    var username: String = "",
    var name: String = "",
    var lastname: String = "",
    var provider: String = "",
    var profilePhoto: String = ""
)
