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

class ClassroomFormViewModel : ViewModel() {

    var finish: () -> Unit = {}
    var error: (error: String?) -> Unit = {}
    var updateClassRoom: (classroom: ClassRoomDTO) -> Unit = {}
    var updateQrCode: (base64: String) -> Unit = {}

    private var list = MutableLiveData<ArrayList<ClassRoomDTO>>()
    private val classRoomService by lazy {
        RetrofitConfig.instance.create(ClassRoomService::class.java)
    }

    fun save (id: Int? = null, classRoomDTO: ClassRoomDTO) {
        if (id == null) {
            create(classRoomDTO)
        } else {
            update(id, classRoomDTO)
        }

    }

    fun generateQrCode(id: Int) {
        val call = classRoomService.generate(id)
        call.enqueue(object  : Callback<ClassRoomDTO>{
            override fun onResponse(call: Call<ClassRoomDTO>, response: Response<ClassRoomDTO>) {
                when(response.code()) {
                    200 -> updateQrCode(response.body()!!.qrCode!!)
                    else -> error(response.errorBody()?.string())
                }
            }

            override fun onFailure(call: Call<ClassRoomDTO>, t: Throwable) {

            }
        })
    }

    private fun create(classRoomDTO: ClassRoomDTO) {
        val call = classRoomService.create(classRoomDTO)
        call.enqueue(object  : Callback<ClassRoomDTO>{
            override fun onResponse(call: Call<ClassRoomDTO>, response: Response<ClassRoomDTO>) {
                when(response.code()) {
                    201, 200 -> finish()
                    else -> error(response.errorBody()?.string())
                }
            }

            override fun onFailure(call: Call<ClassRoomDTO>, t: Throwable) {

            }
        })
    }

    private fun update(id: Int, classRoomDTO: ClassRoomDTO) {
        classRoomDTO.id = id
        val call = classRoomService.update(id, classRoomDTO)
        call.enqueue(object  : Callback<ClassRoomDTO>{
            override fun onResponse(call: Call<ClassRoomDTO>, response: Response<ClassRoomDTO>) {
                when(response.code()) {
                    200 -> updateClassRoom(response.body()!!)
                    else -> error(response.errorBody()?.string())
                }
            }

            override fun onFailure(call: Call<ClassRoomDTO>, t: Throwable) {

            }
        })
    }
}