package com.nassau.checkinschool.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.desafio.config.RetrofitConfig
import com.nassau.checkinschool.model.Page
import com.nassau.checkinschool.model.checkin.CheckinDTO
import com.nassau.checkinschool.model.classroom.ClassRoomDTO
import com.nassau.checkinschool.model.user.UserDTO
import com.nassau.checkinschool.service.ClassRoomService
import com.nassau.checkinschool.service.UserService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TeacherHomeViewModel : ViewModel() {

    private var list = MutableLiveData<ArrayList<ClassRoomDTO>>()
    private val classRoomService by lazy {
        RetrofitConfig.instance.create(ClassRoomService::class.java)
    }

    fun getList(): LiveData<ArrayList<ClassRoomDTO>> {
        return list
    }
    fun setList(items: ArrayList<ClassRoomDTO>) {
        list.value = items
    }

    fun loadItems(id: Int) {
        val call = classRoomService.list(id)
        call.enqueue(object : Callback<Page<ClassRoomDTO>> {
            override fun onResponse(call: Call<Page<ClassRoomDTO>>, response: Response<Page<ClassRoomDTO>>) {
                when (response.code()) {
                    200 -> list.value = response.body()?.content!!
                }
            }

            override fun onFailure(call: Call<Page<ClassRoomDTO>>, t: Throwable) {
            }
        })
    }
}