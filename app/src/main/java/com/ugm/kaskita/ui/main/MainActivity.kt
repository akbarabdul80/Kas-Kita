package com.ugm.kaskita.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.oratakashi.viewbinding.core.binding.activity.viewBinding
import com.oratakashi.viewbinding.core.tools.gone
import com.oratakashi.viewbinding.core.tools.visible
import com.ugm.kaskita.R
import com.ugm.kaskita.data.DataHistory
import com.ugm.kaskita.databinding.ActivityMainBinding
import com.ugm.kaskita.utils.Converter.formatRupiah
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by viewBinding()
    private lateinit var database: DatabaseReference
    private val dataHistory: MutableList<DataHistory> = ArrayList()
    private val adapter: MainAdapter by lazy {
        MainAdapter()
    }

    val TAG = "Main"

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        database = Firebase.database.reference


        with(binding) {

            when (SimpleDateFormat("HH").format(Date()).toInt()) {
                in 0..11 -> {
                    tvDay.text = "Selamat Pagi,"
                }
                in 12..14 -> {
                    tvDay.text = "Selamat Siang,"
                }
                in 15..17 -> {
                    tvDay.text = "Selamat Sore,"
                }
                else -> {
                    tvDay.text = "Selamat Malam,"
                }
            }

            binding
            rvHistory.also {
                it.layoutManager = LinearLayoutManager(this@MainActivity)
                it.adapter = adapter
            }

            swDashboard.setOnRefreshListener {
                setupListener()
                swDashboard.isRefreshing = false
            }
        }

        setupListener()
    }

    private fun setupListener() {

        database.child("history").addValueEventListener(
            object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    dataHistory.clear()
                    dataSnapshot.children.forEach {
                        dataHistory.add(it.getValue(DataHistory::class.java)!!)
                    }

                    var tmpMasuk = 0
                    var tmpKeluar = 0
                    dataHistory.forEach {
                        if (it.type == "in") {
                            tmpMasuk += it.total.toInt()
                        } else {
                            tmpKeluar += it.total.toInt()
                        }
                    }

                    binding.shDashboard.visible()
                    binding.shDashboard.startShimmer()
                    binding.rvHistory.gone()
                    binding.avKas.visible()
                    binding.tvKas.gone()

                    binding.avIn.visible()
                    binding.avOut.visible()
                    binding.avIn.show()
                    binding.avOut.show()

                    binding.tvMasuk.gone()
                    binding.tvKeluar.gone()

                    binding.llNull.gone()
                    Handler(Looper.getMainLooper()).postDelayed(
                        {
                            binding.shDashboard.gone()
                            binding.shDashboard.stopShimmer()
                            binding.rvHistory.visible()
                            binding.avKas.gone()
                            binding.tvKas.visible()
                            binding.tvKeluar.text = tmpKeluar.formatRupiah()
                            binding.tvMasuk.text = tmpMasuk.formatRupiah()

                            binding.tvMasuk.visible()
                            binding.tvKeluar.visible()

                            binding.tvKas.text = (tmpMasuk - tmpKeluar).toString().formatRupiah()

                            binding.avIn.gone()
                            binding.avOut.gone()
                            binding.avIn.hide()
                            binding.avOut.hide()

                            if (dataHistory.size == 0) {
                                binding.llNull.visible()
                            } else {
                                adapter.submitData(dataHistory)
                            }
                        }, 1000L
                    )

                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Getting Post failed, log a message
                    Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                }
            }
        )

    }
}