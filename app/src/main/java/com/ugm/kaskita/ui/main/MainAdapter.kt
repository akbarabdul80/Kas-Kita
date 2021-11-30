package com.ugm.kaskita.ui.main

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.oratakashi.viewbinding.core.binding.recyclerview.ViewHolder
import com.oratakashi.viewbinding.core.binding.recyclerview.viewBinding
import com.ugm.kaskita.R
import com.ugm.kaskita.data.DataSaldo
import com.ugm.kaskita.databinding.ListHistoryBinding
import com.ugm.kaskita.utils.Logs.formatRupiah

class MainAdapter : RecyclerView.Adapter<ViewHolder<ListHistoryBinding>>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder<ListHistoryBinding> = viewBinding(parent)

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder<ListHistoryBinding>, position: Int) {
        val dataSaldo: DataSaldo = data[position]
        with(holder.binding) {
            tvTitle.text = dataSaldo.name
            tvDate.text = dataSaldo.tanggal

            if (dataSaldo.type == "in") {
                tvTotal.text = "+" + dataSaldo.total.formatRupiah()
                tvTotal.setTextColor(ContextCompat.getColor(root.context, R.color.plus))
                imgType.setImageResource(R.drawable.ic_masuk)
            } else {
                tvTotal.text = "-" + dataSaldo.total.formatRupiah()
                tvTotal.setTextColor(ContextCompat.getColor(root.context, R.color.min))
                imgType.setImageResource(R.drawable.ic_keluar)
            }

        }
    }

    override fun getItemCount(): Int = data.size

    @SuppressLint("NotifyDataSetChanged")
    fun submitData(data: List<DataSaldo>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    private val data: MutableList<DataSaldo> = ArrayList()
}