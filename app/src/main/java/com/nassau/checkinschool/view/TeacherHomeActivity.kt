package com.nassau.checkinschool.view

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.lifecycle.ViewModelProvider
import com.example.desafio.listener.OnClickListener
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

    private lateinit var aLoadingDialog: ALoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        observer()
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        userDTO?.id?.let {
            aLoadingDialog.show()
            viewModel.loadItems(it)
        }
    }
    private fun initView() {
        aLoadingDialog = ALoadingDialog(this@TeacherHomeActivity)
        userDTO = intent.getSerializableExtra("user") as UserDTO
        binding.imgBack.setOnClickListener {
            finish()
        }
        val df = SimpleDateFormat("dd LLLL yyyy")
        binding.txtDate.text = df.format(DateTime.now().millis).toString()
        binding.btnCreate.setOnClickListener{
            var intent = Intent(this@TeacherHomeActivity, ClassroomFormActivity::class.java)
            intent.putExtra("user", userDTO)
            startActivity(intent)
        }
    }

    private fun setAdapter() {
        cardClassroomAdapter = CardClassroomAdapter(list)
        cardClassroomAdapter.setOnItemClickListener(object : OnClickListener<ClassRoomDTO> {
            override fun onClick(value: ClassRoomDTO, position: Int) {
                var intent = Intent(this@TeacherHomeActivity, ClassroomFormActivity::class.java)
                intent.putExtra("classroom", value)
                intent.putExtra("user", userDTO)
                startActivity(intent)
            }
        })
        binding.rvListCard.adapter = cardClassroomAdapter
    }

    private fun observer() {
        viewModel.getList().observe(this) { it ->
            list = it
            if (setAdapter) {
                setAdapter()
                setAdapter = false
                binding.rvListCard.layoutAnimation = LayoutAnimationController(
                    AnimationUtils.loadAnimation(
                        this@TeacherHomeActivity,
                        com.google.android.material.R.anim.abc_fade_in
                    )
                )
            } else {
                cardClassroomAdapter.update(list)
            }
            aLoadingDialog.dismiss()
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