package com.ugm.kaskita.ui.main

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oratakashi.viewbinding.core.binding.recyclerview.ViewHolder
import com.oratakashi.viewbinding.core.binding.recyclerview.viewBinding
import com.ugm.kaskita.data.DataHistory
import com.ugm.kaskita.databinding.ListHistoryBinding
import com.ugm.kaskita.utils.Logs.formatRupiah

class MainAdapter : RecyclerView.Adapter<ViewHolder<ListHistoryBinding>>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder<ListHistoryBinding> = viewBinding(parent)

    override fun onBindViewHolder(holder: ViewHolder<ListHistoryBinding>, position: Int) {
        val dataHistory: DataHistory = data[position]
        with(holder.binding) {
            tvTitle.text = dataHistory.name
            tvDate.text = dataHistory.tanggal
            tvTotal.text = dataHistory.total.formatRupiah()
        }
    }

    override fun getItemCount(): Int = data.size

    @SuppressLint("NotifyDataSetChanged")
    fun submitData(data: List<DataHistory>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    private val data: MutableList<DataHistory> = ArrayList()
}