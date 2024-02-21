package com.nassau.checkinschool.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.nassau.checkinschool.databinding.ActivityLoginBinding
import com.nassau.checkinschool.model.Message
import com.nassau.checkinschool.model.user.UserDTO
import com.nassau.checkinschool.util.ALoadingDialog
import com.nassau.checkinschool.util.JsonParse
import com.nassau.checkinschool.viewModel.LoginViewModel

class LoginActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    private val viewModel: LoginViewModel by lazy {
        ViewModelProvider(this)[LoginViewModel::class.java]
    }
    private lateinit var aLoadingDialog: ALoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        aLoadingDialog = ALoadingDialog(this@LoginActivity)
        startViewModel()
        initView()
        setContentView(binding.root)
    }

    private fun initView() {
        binding.txtRegister.setOnClickListener {
            var intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            intent.putExtra("user", UserDTO(login = binding.edtLogin.text.toString(),
                                                    password = binding.edtPassword.text.toString() ))
            startActivity(intent)
        }
        binding.btnLogin.setOnClickListener {
            aLoadingDialog.show()
            viewModel.login(binding.edtLogin.text.toString(), binding.edtPassword.text.toString())
        }
    }

    private fun startViewModel() {
        viewModel.startHome = {
            var intent = Intent(this@LoginActivity, HomeActivity::class.java)
            intent.putExtra("user", it)
            startActivity(intent)
            finish()
        }
        viewModel.errorLogin = {
            var messageError = JsonParse.fromJson(it, Message::class.java) as Message?
            if (messageError != null) {
                val builder = MaterialAlertDialogBuilder(this@LoginActivity)
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