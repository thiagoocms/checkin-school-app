package com.nassau.checkinschool.model.classroom

import com.nassau.checkinschool.model.user.UserDTO
import java.io.Serializable

data class ClassRoomDTO(
    var id: Int? = null,
    var name: String? = null,
    var user: UserDTO? = null,
    var startDate: Long? = null,
    var endDate: Long? = null,
    var period: String? = null,
    var shift: String? = null
) : Serializable
