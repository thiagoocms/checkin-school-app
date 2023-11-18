package com.nassau.checkinschool.model.user

import java.io.Serializable

data class UserDTO(
    var id: Int? = null,
    var name: String? = null,
    var documentNumber: String? = null,
    var documentType: String? = null,
    var login: String? = null,
    var password: String? = null,
    var profile: String? = null
) : Serializable {}