package com.nassau.checkinschool.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.desafio.config.RetrofitConfig
import com.nassau.checkinschool.model.checkin.CheckinDTO
import com.nassau.checkinschool.model.classroom.ClassRoomDTO
import com.nassau.checkinschool.model.user.UserDTO
import com.nassau.checkinschool.service.UserService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    var startCheck: () -> Unit = {}
    var error: (message: String?) -> Unit = {}
    private var list = MutableLiveData<ArrayList<ClassRoomDTO>>()
    private val userService by lazy {
        RetrofitConfig.instance.create(UserService::class.java)
    }

    fun getList(): LiveData<ArrayList<ClassRoomDTO>> {
        return list
    }

    fun loadItems(id: Int) {
        val call = userService.listClassrooms(id)
        call.enqueue(object : Callback<ArrayList<ClassRoomDTO>> {
            override fun onResponse(call: Call<ArrayList<ClassRoomDTO>>, response: Response<ArrayList<ClassRoomDTO>>) {
                when (response.code()) {
                    200 -> list.value = response.body()!!
                }
            }

            override fun onFailure(call: Call<ArrayList<ClassRoomDTO>>, t: Throwable) {
            }
        })
    }

    fun checkin(id: Int, classRoomId: Int) {
        val call = userService.checkIn(id, classRoomId)
        call.enqueue(object : Callback<CheckinDTO> {
            override fun onResponse(call: Call<CheckinDTO>, response: Response<CheckinDTO>) {
                when (response.code()) {
                    200 -> startCheck()
                    else -> error(response.errorBody()?.string())
                }
            }

            override fun onFailure(call: Call<CheckinDTO>, t: Throwable) {
            }
        })
    }
}