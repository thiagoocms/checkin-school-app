package com.nassau.checkinschool.viewModel

import androidx.lifecycle.ViewModel
import com.example.desafio.config.RetrofitConfig
import com.nassau.checkinschool.model.user.UserDTO
import com.nassau.checkinschool.service.UserService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {

    var startHome: (user: UserDTO) -> Unit = {}
    var errorLogin: (error: String?) -> Unit = {}
    private val userService by lazy {
        RetrofitConfig.instance.create(UserService::class.java)
    }

    fun login(login: String? = null, password: String? = null) {
        var user = UserDTO(
            login = login,
            password = password
        )
        val call = userService.login(user)
        call.enqueue(object : Callback<UserDTO> {
            override fun onResponse(call: Call<UserDTO>, response: Response<UserDTO>) {
                when (response.code()) {
                    200 -> {
                        user = response.body()!!
                        startHome(user)
                    }

                    else -> errorLogin(response.errorBody()?.string())
                }
            }

            override fun onFailure(call: Call<UserDTO>, t: Throwable) {

            }

        })
    }

}