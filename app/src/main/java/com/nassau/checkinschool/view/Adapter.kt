package com.nassau.checkinschool.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nassau.checkinschool.databinding.CardCheckinBinding
import com.nassau.checkinschool.model.classroom.ClassRoomDTO
import org.joda.time.DateTime
import org.joda.time.Hours
import java.text.SimpleDateFormat

class Adapter(private var data: ArrayList<ClassRoomDTO>) :
    RecyclerView.Adapter<Adapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CardCheckinBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    inner class ViewHolder(private val binding: CardCheckinBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: ClassRoomDTO) {
            val df = SimpleDateFormat("dd LLLL yyyy HH:mm")
            binding.txtName.text = item.name + "  -  "
            binding.txtShift.text = item.shift
            binding.txtUsername.text = " " + item.user?.name
            binding.txtStartDate.text = " " + df.format(item.startDate).toString()
            binding.txtInterval.text = " " + Hours.hoursBetween(
                DateTime(item.startDate),
                DateTime(item.endDate)
            ).hours.toString() + " hrs"
        }
    }

    fun addItems(newItems: ArrayList<ClassRoomDTO>) {
        val sizeCurrent = data.size
        data.addAll(newItems)
        notifyItemRangeInserted(sizeCurrent, newItems.size)
    }

    fun update(items: ArrayList<ClassRoomDTO>) {
        val sizeCurrent = data.size
        data = items
        notifyItemRangeInserted(sizeCurrent, items.size)
    }

}