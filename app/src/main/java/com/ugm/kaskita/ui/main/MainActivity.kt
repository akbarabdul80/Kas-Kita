package com.ugm.kaskita.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.oratakashi.viewbinding.core.binding.activity.viewBinding
import com.oratakashi.viewbinding.core.tools.gone
import com.oratakashi.viewbinding.core.tools.onClick
import com.oratakashi.viewbinding.core.tools.visible
import com.ugm.kaskita.R
import com.ugm.kaskita.data.DataSaldo
import com.ugm.kaskita.databinding.ActivityMainBinding
import com.ugm.kaskita.ui.main.bottom.BottomMainFragment
import com.ugm.kaskita.utils.Converter.formatRupiah
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), BottomMainFragment.BottomSheet {

    private val binding: ActivityMainBinding by viewBinding()
    private lateinit var database: DatabaseReference
    private val dataSaldo: MutableList<DataSaldo> = ArrayList()
    private val adapter: MainAdapter by lazy {
        MainAdapter()
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n", "WrongConstant")
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

            rvHistory.also {
                it.layoutManager =
                    LinearLayoutManager(this@MainActivity, LinearLayout.VERTICAL, true)
                it.adapter = adapter
            }

            swDashboard.setOnRefreshListener {
                setupListener()
                swDashboard.isRefreshing = false
            }

            llIn.onClick {
                val bottomSheetDialog: BottomMainFragment =
                    BottomMainFragment.newInstance("in")
                bottomSheetDialog.show(supportFragmentManager, "Bottom saldo")

                llIn.background =
                    ResourcesCompat.getDrawable(resources, R.drawable.rectangle_in, null)
            }

            llOut.onClick {
                val bottomSheetDialog: BottomMainFragment =
                    BottomMainFragment.newInstance("out")
                bottomSheetDialog.show(supportFragmentManager, "Bottom saldo")

                llOut.background =
                    ResourcesCompat.getDrawable(resources, R.drawable.rectangle_out, null)
            }
        }

        setupListener()
    }

    private fun setupListener() {

        database.child("history").addValueEventListener(
            object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    dataSaldo.clear()
                    dataSnapshot.children.forEach {
                        dataSaldo.add(it.getValue(DataSaldo::class.java)!!)
                    }

                    var tmpMasuk = 0
                    var tmpKeluar = 0
                    dataSaldo.forEach {
                        if (it.type == "in") {
                            tmpMasuk += it.total.toInt()
                        } else {
                            tmpKeluar += it.total.toInt()
                        }
                    }

                    with(binding){
                        shDashboard.visible()
                        shDashboard.startShimmer()
                        rvHistory.gone()
                        avKas.visible()
                        tvKas.gone()

                        avIn.visible()
                        avOut.visible()
                        avIn.show()
                        avOut.show()

                        tvMasuk.gone()
                        tvKeluar.gone()

                        llNull.gone()
                        Handler(Looper.getMainLooper()).postDelayed(
                            {
                                shDashboard.gone()
                                shDashboard.stopShimmer()
                                rvHistory.visible()
                                avKas.gone()
                                tvKas.visible()
                                tvKeluar.text = tmpKeluar.formatRupiah()
                                tvMasuk.text = tmpMasuk.formatRupiah()

                                tvMasuk.visible()
                                tvKeluar.visible()

                                tvKas.text = (tmpMasuk - tmpKeluar).toString().formatRupiah()

                                avIn.gone()
                                avOut.gone()
                                avIn.hide()
                                avOut.hide()

                                if (dataSaldo.size == 0) {
                                    llNull.visible()
                                }
                                adapter.submitData(dataSaldo)
                            }, 1000L
                        )
                    }


                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Getting Post failed, log a message
                    Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                }
            }
        )

    }

    override fun onDismiss() {
        binding.llIn.background =
            ResourcesCompat.getDrawable(resources, R.drawable.rectangle, null)
        binding.llOut.background =
            ResourcesCompat.getDrawable(resources, R.drawable.rectangle, null)
    }

    override fun onSubmit(data: DataSaldo) {
        database.child("history").push().setValue(data)
    }

    companion object {
        const val TAG = "Main"
    }
}