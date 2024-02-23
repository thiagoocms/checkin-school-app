package com.nassau.checkinschool.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import com.nassau.checkinschool.databinding.ActivityHomeBinding
import com.nassau.checkinschool.model.Message
import com.nassau.checkinschool.model.classroom.ClassRoomDTO
import com.nassau.checkinschool.model.user.UserDTO
import com.nassau.checkinschool.model.user.UserProfileEnum
import com.nassau.checkinschool.util.ALoadingDialog
import com.nassau.checkinschool.util.JsonParse
import com.nassau.checkinschool.viewModel.HomeViewModel
import org.joda.time.DateTime
import java.text.SimpleDateFormat

class HomeActivity : AppCompatActivity() {

    private var list: ArrayList<ClassRoomDTO> = ArrayList()
    private lateinit var cardCheckinAdapter: CardCheckinAdapter
    private lateinit var aLoadingDialog: ALoadingDialog
    private var userDTO: UserDTO? = null
    private var setAdapter = true
    private val binding by lazy {
        ActivityHomeBinding.inflate(layoutInflater)
    }
    private val viewModel: HomeViewModel by lazy {
        ViewModelProvider(this)[HomeViewModel::class.java]
    }

    private var qrCodeScannerLauncher: ActivityResultLauncher<ScanOptions> =
        registerForActivityResult( ScanContract()) { result ->
            aLoadingDialog.show()
                if (result?.contents != null) {
                    val scannedData = JsonParse.fromJson(result.contents, ClassRoomDTO::class.java) as ClassRoomDTO
                    viewModel.checkin(userDTO?.id!!, scannedData.id!!)
                } else {
                    aLoadingDialog.cancel()
                    Toast.makeText(this@HomeActivity, "Não foi possivel ler o QR code", Toast.LENGTH_LONG).show()
                }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observer()
        initView()
        setContentView(binding.root)

    }

    override fun onResume() {
        super.onResume()
        userDTO?.id?.let {
            aLoadingDialog.show()
            viewModel.loadItems(it)
        }
    }
    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    private fun initView() {
        userDTO = intent.getSerializableExtra("user") as UserDTO
        viewModel.startCheck = {
            aLoadingDialog.cancel()
            startActivity(Intent(this@HomeActivity, CheckActivity::class.java))
        }
        viewModel.error = {
            val messageError = JsonParse.fromJson(it, Message::class.java) as Message?
            if (messageError != null) {
                MaterialAlertDialogBuilder(this@HomeActivity)
                    .setTitle("Aviso!")
                    .setMessage(messageError.message)
                    .setCancelable(false)
                    .setPositiveButton("ok") { _, _ -> }
                    .create().show()
                aLoadingDialog.cancel();
            }
        }
        val df = SimpleDateFormat("dd LLLL yyyy HH:mm")
        aLoadingDialog = ALoadingDialog(this@HomeActivity)
        userDTO?.name.let {
            binding.txtName.text = "Olá, " + it
        }
        binding.txtDate.text = df.format(DateTime.now().millis).toString()
        binding.btnCheckin.setOnClickListener {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.CAMERA),
                    100
                );
            }
            val options = ScanOptions()
            options.setPrompt("Leia o QR code para fazer Check-In")
            options.setCameraId(0)
            options.setDesiredBarcodeFormats(ScanOptions.QR_CODE)
            options.setBeepEnabled(false)
            options.setBarcodeImageEnabled(true)

            // Inicie o scanner usando o lançador do resultado da atividade
            qrCodeScannerLauncher.launch(options)
        }
        if (userDTO?.profile == UserProfileEnum.STUDENT) {
            binding.btnTeacherHome.visibility = View.GONE
        } else {
            binding.btnTeacherHome.setOnClickListener {
                var intent = Intent(this@HomeActivity, TeacherHomeActivity::class.java)
                intent.putExtra("user", userDTO)
                startActivity(intent)
            }
        }
    }

    private fun setAdapter() {
        cardCheckinAdapter = CardCheckinAdapter(list)
        binding.rvListCard.adapter = cardCheckinAdapter

        binding.rvListCard.layoutAnimation = LayoutAnimationController(
            AnimationUtils.loadAnimation(
                this@HomeActivity,
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
                cardCheckinAdapter.update(it)
            }
            aLoadingDialog.cancel()
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
    }
}