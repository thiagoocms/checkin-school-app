package com.nassau.checkinschool.service

import com.nassau.checkinschool.model.checkin.CheckinDTO
import com.nassau.checkinschool.model.classroom.ClassRoomDTO
import com.nassau.checkinschool.model.user.UserDTO
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


interface UserService {

    @POST("api/v1/users/login")
    fun login(@Body userDTO: UserDTO): Call<UserDTO>

    @POST("api/v1/users")
    fun create(@Body userDTO: UserDTO): Call<UserDTO>

    @GET("api/v1/users/{id}/classrooms")
    fun listClassrooms(@Path("id") id:Int): Call<ArrayList<ClassRoomDTO>>

    @PUT("api/v1/users/{id}/check/{classRoomId}")
    fun checkIn(@Path("id") id:Int, @Path("classRoomId") classRoomId:Int): Call<CheckinDTO>
}