package com.nassau.checkinschool.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.nassau.checkinschool.R
import com.nassau.checkinschool.databinding.ActivityRegisterBinding
import com.nassau.checkinschool.model.Message
import com.nassau.checkinschool.model.user.UserDTO
import com.nassau.checkinschool.model.user.UserProfileEnum
import com.nassau.checkinschool.util.ALoadingDialog
import com.nassau.checkinschool.util.JsonParse
import com.nassau.checkinschool.viewModel.LoginViewModel
import com.nassau.checkinschool.viewModel.RegisterViewModel

class RegisterActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityRegisterBinding.inflate(layoutInflater)
    }
    private val viewModel: RegisterViewModel by lazy {
        ViewModelProvider(this)[RegisterViewModel::class.java]
    }
    private lateinit var aLoadingDialog: ALoadingDialog
    private var userDTO: UserDTO? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        startViewModel()
        setContentView(binding.root)
    }

    private fun initView() {
        aLoadingDialog = ALoadingDialog(this@RegisterActivity)
        binding.imgBack.setOnClickListener {
            finish()
        }
        userDTO = intent.getSerializableExtra("user") as UserDTO
        binding.edtLogin.setText(userDTO?.login)
        binding.edtPassword.setText(userDTO?.password)
        binding.rdGroupProfile.check(binding.rdBtnStudent.id)
        binding.btnRegister.setOnClickListener {
            aLoadingDialog.show()
            val user = UserDTO(
                name = binding.edtName.text.toString(),
                login = binding.edtLogin.text.toString(),
                password = binding.edtPassword.text.toString(),
                documentNumber = binding.edtDocument.text.toString(),
                documentType = "CPF",
                profile = if (binding.rdGroupProfile.checkedRadioButtonId == binding.rdBtnStudent.id){
                    UserProfileEnum.STUDENT
                }else{
                    UserProfileEnum.TEACHER
                }

            )
            viewModel.create(user)
        }
    }

    private fun startViewModel() {
        viewModel.startHome = {
            val intent = Intent(this@RegisterActivity, HomeActivity::class.java)
            intent.putExtra("user", it)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            aLoadingDialog.dismiss()
            startActivity(intent)
            finish()
        }
        viewModel.errorLogin = {
            var messageError = JsonParse.fromJson(it, Message::class.java) as Message?
            if (messageError != null) {
                val builder = MaterialAlertDialogBuilder(this@RegisterActivity)
                builder.setTitle("Aviso!")
                builder.setMessage(messageError.message)
                builder.setCancelable(false)
                builder.setPositiveButton("ok") { _, _ -> }
                builder.create().show()
            }
            aLoadingDialog.cancel();
        }
    }
}