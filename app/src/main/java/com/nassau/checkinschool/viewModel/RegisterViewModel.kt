package com.nassau.checkinschool.viewModel

import androidx.lifecycle.ViewModel
import com.example.desafio.config.RetrofitConfig
import com.nassau.checkinschool.model.user.UserDTO
import com.nassau.checkinschool.service.UserService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel() {

    var startHome: (user: UserDTO) -> Unit = {}
    var errorLogin: (error: String?) -> Unit = {}
    private val userService by lazy {
        RetrofitConfig.instance.create(UserService::class.java)
    }

    fun create(user: UserDTO) {
        val call = userService.create(user)
        call.enqueue(object : Callback<UserDTO> {
            override fun onResponse(call: Call<UserDTO>, response: Response<UserDTO>) {
                when (response.code()) {
                    200, 201 -> {
                        var userDTO = response.body()!!
                        startHome(userDTO)
                    }

                    else -> errorLogin(response.errorBody()?.string())
                }
            }
            override fun onFailure(call: Call<UserDTO>, t: Throwable) {

            }

        })
    }
}