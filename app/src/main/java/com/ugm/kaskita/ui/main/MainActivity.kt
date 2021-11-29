package com.ugm.kaskita.ui.main

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

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by viewBinding()
    private lateinit var database: DatabaseReference
    private val dataHistory: MutableList<DataHistory> = ArrayList()
    private val adapter: MainAdapter by lazy {
        MainAdapter()
    }

    val TAG = "Main"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        database = Firebase.database.reference
        with(binding) {

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

                    Handler(Looper.getMainLooper()).postDelayed(
                        {
                            binding.shDashboard.gone()
                            binding.shDashboard.stopShimmer()
                            binding.rvHistory.visible()
                            binding.avKas.gone()
                            binding.tvKas.visible()
                            binding.tvKeluar.text = tmpKeluar.formatRupiah()
                            binding.tvMasuk.text = tmpMasuk.formatRupiah()
                            binding.tvKas.text = (tmpMasuk - tmpKeluar).toString().formatRupiah()

                            adapter.submitData(dataHistory)
                        }, 1000L
                    )

                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Getting Post failed, log a message
                    Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                }
            }
        )

//        database.child("saldo").addValueEventListener(
//            object : ValueEventListener {
//                override fun onDataChange(dataSnapshot: DataSnapshot) {
//                    // Get Post object and use the values to update the UI
//                    val post = dataSnapshot.value
//
//                    binding.avKas.visible()
//                    binding.tvKas.gone()
//
//                    Handler(Looper.getMainLooper()).postDelayed(
//                        {
//                            binding.avKas.gone()
//                            binding.tvKas.visible()
//                            binding.tvKas.text = post.toString().formatRupiah()
//                        }, 1000L
//                    )
//
//                }
//
//                override fun onCancelled(databaseError: DatabaseError) {
//                    // Getting Post failed, log a message
//                    Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
//                }
//            }
//        )




    }
}