package com.nassau.checkinschool.view

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Base64
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.nassau.checkinschool.R
import com.nassau.checkinschool.databinding.ActivityClassroomFormBinding
import com.nassau.checkinschool.databinding.QrCodeLayoutBinding
import com.nassau.checkinschool.model.Message
import com.nassau.checkinschool.model.classroom.ClassRoomDTO
import com.nassau.checkinschool.model.user.UserDTO
import com.nassau.checkinschool.util.ALoadingDialog
import com.nassau.checkinschool.util.JsonParse
import com.nassau.checkinschool.viewModel.ClassroomFormViewModel
import kotlinx.coroutines.CoroutineStart
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import java.text.SimpleDateFormat
import java.util.Calendar
import kotlin.io.encoding.ExperimentalEncodingApi


class ClassroomFormActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityClassroomFormBinding.inflate(layoutInflater)
    }
    private val viewModel: ClassroomFormViewModel by lazy {
        ViewModelProvider(this)[ClassroomFormViewModel::class.java]
    }
    private lateinit var aLoadingDialog: ALoadingDialog

    private var classRoomDTO: ClassRoomDTO = ClassRoomDTO()

    private var userDTO: UserDTO? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        setContentView(binding.root)
    }

    @OptIn(ExperimentalEncodingApi::class)
    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private fun initView() {

        aLoadingDialog = ALoadingDialog(this@ClassroomFormActivity)

        binding.imgBack.setOnClickListener {
            finish()
        }
        userDTO = intent.getSerializableExtra("user") as UserDTO
        var classroom = intent.getSerializableExtra("classroom") as ClassRoomDTO?
        classroom?.let { it ->
            it.qrCode.let { value ->
                binding.imgViewQrCode.setOnClickListener {
                    val decodedBytes: ByteArray = Base64.decode(classRoomDTO.qrCode,
                        Base64.DEFAULT
                    )
                    val view = QrCodeLayoutBinding.inflate(LayoutInflater.from(this@ClassroomFormActivity))
                    val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                    if (bitmap != null) {
                        view.imgQrCode.setImageBitmap(bitmap)
                    }
                    val dialog = Dialog(this@ClassroomFormActivity)
                    dialog.setContentView(view.root)
                    dialog.show()
                }
                if (value != null) {
                    binding.imgViewQrCode.visibility = android.view.View.VISIBLE
                } else {
                        binding.imgCreateQrCode.visibility = android.view.View.VISIBLE
                        binding.imgCreateQrCode.setOnClickListener { view ->
                            aLoadingDialog.show()
                            viewModel.generateQrCode(it.id!!)
                        }
                    }
            }
            val df = SimpleDateFormat("dd/MM/yyyy")
            val endDate = DateTime(it.endDate)
            val startDate = DateTime(it.startDate)
            classRoomDTO = it
            binding.edtName.setText(it.name)
            binding.edtShift.setText(it.shift)
            binding.edtPeriod.setText(it.period)
            binding.edtDate.setText(df.format(startDate.toDate()))
            binding.edtStart.setText("${formatTen(startDate.hourOfDay)}:${formatTen(startDate.minuteOfHour)}")
            binding.edtEnd.setText("${formatTen(endDate.hourOfDay)}:${formatTen(endDate.minuteOfHour)}")
        }

        binding.edtDate.setOnClickListener {
            showDatePickerDialog()
        }
        binding.edtEnd.setOnClickListener {
            showTimePickerDialog(false)
        }
        binding.edtStart.setOnClickListener {
            showTimePickerDialog(true)
        }
        binding.btnSave.setOnClickListener {

            aLoadingDialog.show()
            if (binding.edtDate.text.toString() == ""
                || binding.edtStart.text.toString() == ""
                || binding.edtEnd.text.toString() == "") {
                val builder = MaterialAlertDialogBuilder(this@ClassroomFormActivity)
                builder.setTitle("Aviso!")
                builder.setMessage("todos os campos são obrigatorios")
                builder.setCancelable(false)
                builder.setPositiveButton("ok") { _, _ -> }
                builder.create().show()
                return@setOnClickListener
            }

            val dateFormatter: DateTimeFormatter = DateTimeFormat.forPattern("dd/MM/yyyy")
            val date: DateTime = dateFormatter.parseDateTime(binding.edtDate.text.toString())

            val timeFormatter: DateTimeFormatter = DateTimeFormat.forPattern("HH:mm")
            val start: DateTime = timeFormatter.parseDateTime(binding.edtStart.text.toString())
            val end: DateTime = timeFormatter.parseDateTime(binding.edtEnd.text.toString())
            val startDate = date.withTime(start.hourOfDay, start.minuteOfHour, start.secondOfMinute, 0).millis
            val endDate = date.withTime(end.hourOfDay, end.minuteOfHour, end.secondOfMinute, 0).millis
            classRoomDTO.name = binding.edtName.text.toString()
            classRoomDTO.user = userDTO
            classRoomDTO.period = binding.edtPeriod.text.toString()
            classRoomDTO.shift = binding.edtShift.text.toString()
            classRoomDTO.name = binding.edtName.text.toString()
            classRoomDTO.startDate = startDate
            classRoomDTO.endDate = endDate
            viewModel.save(classRoomDTO.id, classRoomDTO)
        }
        viewModel.error = {
            var messageError = JsonParse.fromJson(it, Message::class.java) as Message?
            if (messageError != null) {
                val builder = MaterialAlertDialogBuilder(this@ClassroomFormActivity)
                builder.setTitle("Aviso!")
                builder.setMessage(messageError.message)
                builder.setCancelable(false)
                builder.setPositiveButton("ok") { _, _ -> }
                builder.create().show()
            }
            aLoadingDialog.cancel();
        }
        viewModel.finish = {
            aLoadingDialog.cancel();
            finish()
        }
        viewModel.updateClassRoom = {
            aLoadingDialog.cancel();
            val snackBar = Snackbar.make(
                binding.root,
                "Sala de aula atualizada com sucesso",
                Snackbar.LENGTH_LONG
            )
            snackBar.show()
        }
        viewModel.updateQrCode = {
            classRoomDTO.qrCode = it
            binding.imgCreateQrCode.visibility = android.view.View.GONE
            binding.imgViewQrCode.visibility = android.view.View.VISIBLE
            aLoadingDialog.dismiss()
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this,
            { _: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                var monthView = if (monthOfYear + 1 < 10) {
                    "0${monthOfYear + 1}"
                } else {
                    "${monthOfYear + 1}"
                }

                val selectedDate = "${if(dayOfMonth < 10) {
                    "0${dayOfMonth}"
                } else {
                    dayOfMonth
                }}/$monthView/$year"
               binding.edtDate.setText(selectedDate)
            }, year, month, dayOfMonth
        )

        datePickerDialog.show()
    }

    private fun showTimePickerDialog(start: Boolean) {

        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this,
            { _, selectedHour, selectedMinute ->

                val selectedTime = "${formatTen(selectedHour)}:${formatTen(selectedMinute)}"
                if (start) {
                    binding.edtStart.setText(selectedTime)
                } else {
                    binding.edtEnd.setText(selectedTime)
                }
            },
            hour,
            minute,
            true
        )
        timePickerDialog.show()
    }

    private fun formatTen(value: Int): String {
       return if(value < 10) {
            "0${value}"
        } else {
            "$value"
        }
    }

}