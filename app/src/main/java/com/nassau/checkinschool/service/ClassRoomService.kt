package com.nassau.checkinschool.service

import com.nassau.checkinschool.model.Page
import com.nassau.checkinschool.model.classroom.ClassRoomDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ClassRoomService {

    @GET("api/v1/classrooms")
    fun list(@Query("userId") userId: Int): Call<Page<ClassRoomDTO>>
}