package com.nassau.checkinschool.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.lifecycle.ViewModelProvider
import com.nassau.checkinschool.databinding.ActivityTeacherHomeBinding
import com.nassau.checkinschool.model.classroom.ClassRoomDTO
import com.nassau.checkinschool.model.user.UserDTO
import com.nassau.checkinschool.util.ALoadingDialog
import com.nassau.checkinschool.viewModel.TeacherHomeViewModel
import org.joda.time.DateTime
import java.text.SimpleDateFormat

class TeacherHomeActivity : AppCompatActivity() {

    private var list: ArrayList<ClassRoomDTO> = ArrayList()
    private lateinit var cardClassroomAdapter: CardClassroomAdapter
    private val binding by lazy {
        ActivityTeacherHomeBinding.inflate(layoutInflater)
    }
    private val viewModel: TeacherHomeViewModel by lazy {
        ViewModelProvider(this)[TeacherHomeViewModel::class.java]
    }
    private var setAdapter = true
    private var userDTO: UserDTO? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        observer()
        setContentView(binding.root)
    }
    private fun initView() {
        userDTO = intent.getSerializableExtra("user") as UserDTO
        userDTO?.id?.let {
            viewModel.loadItems(it)
//            mock()
        }
        binding.imgBack.setOnClickListener {
            finish()
        }
        val df = SimpleDateFormat("dd LLLL yyyy")
        binding.txtDate.text = df.format(DateTime.now().millis).toString()
    }

    private fun setAdapter() {
        cardClassroomAdapter = CardClassroomAdapter(list)
        binding.rvListCard.adapter = cardClassroomAdapter

        binding.rvListCard.layoutAnimation = LayoutAnimationController(
            AnimationUtils.loadAnimation(
                this@TeacherHomeActivity,
                com.google.android.material.R.anim.abc_fade_in
            )
        )
    }

    private fun observer() {
        viewModel.getList().observe(this) { it ->
            list = it
            if (setAdapter) {
                setAdapter()
                setAdapter = false
            } else {
                cardClassroomAdapter.update(it)
            }


        }
    }
    private fun mock() {
        list = arrayListOf(
            ClassRoomDTO(
                0,
                "Matematica",
                UserDTO(name = "Arianne Maria Vasconcelos"),
                1699571115354,
                1699571115354,
                "ewwew",
                "Manhã"
            ),
            ClassRoomDTO(
                0,
                "Matematica",
                UserDTO(name = "Arianne Maria Vasconcelos"),
                1699571115354,
                1699571115354,
                "ewwew",
                "Manhã"
            ),
            ClassRoomDTO(
                0,
                "Matematica",
                UserDTO(name = "Arianne Maria Vasconcelos"),
                1699571115354,
                1699571115354,
                "ewwew",
                "Manhã"
            ),
            ClassRoomDTO(
                0,
                "Matematica",
                UserDTO(name = "Arianne Maria Vasconcelos"),
                1699571115354,
                1699571115354,
                "ewwew",
                "Manhã"
            ),
            ClassRoomDTO(
                0,
                "Matematica",
                UserDTO(name = "Arianne Maria Vasconcelos"),
                1699571115354,
                1699571115354,
                "ewwew",
                "Manhã"
            ),
            ClassRoomDTO(
                0,
                "Matematica",
                UserDTO(name = "Arianne Maria Vasconcelos"),
                1699571115354,
                1699571115354,
                "ewwew",
                "Manhã"
            )
        )
        viewModel.setList(list)
    }
}