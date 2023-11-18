package com.nassau.checkinschool.model.checkin

import com.nassau.checkinschool.model.classroom.ClassRoomDTO

import com.nassau.checkinschool.model.user.UserDTO




data class CheckinDTO(val id: Long? = null,
                      val user: UserDTO? = null,
                      val classRoom: ClassRoomDTO? = null) {

}