package com.nassau.checkinschool.service

import com.nassau.checkinschool.model.Page
import com.nassau.checkinschool.model.classroom.ClassRoomDTO
import com.nassau.checkinschool.model.user.UserDTO
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ClassRoomService {

    @GET("api/v1/classrooms")
    fun list(@Query("userId") userId: Int): Call<Page<ClassRoomDTO>>

    @POST("api/v1/classrooms")
    fun create(@Body classRoomDTO: ClassRoomDTO): Call<ClassRoomDTO>

    @PUT("api/v1/classrooms/{id}")
    fun update(@Path("id") id: Int, @Body classRoomDTO: ClassRoomDTO): Call<ClassRoomDTO>

    @GET("api/v1/classrooms/{id}")
    fun byId(@Path("id") id: Int): Call<ClassRoomDTO>

    @GET("api/v1/classrooms/{id}/generate")
    fun generate(@Path("id") id: Int): Call<ClassRoomDTO>

    @GET("api/v1/classrooms/{id}/check")
    fun getUsers(@Path("id") id: Int): Call<ArrayList<UserDTO>>
}