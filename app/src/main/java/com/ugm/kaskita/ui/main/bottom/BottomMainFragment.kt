package com.ugm.kaskita.ui.main.bottom

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.andrefrsousa.superbottomsheet.SuperBottomSheetFragment
import com.oratakashi.viewbinding.core.binding.fragment.viewBinding
import com.oratakashi.viewbinding.core.tools.onClick
import com.ugm.kaskita.R
import com.ugm.kaskita.data.DataSaldo
import com.ugm.kaskita.databinding.FragmentBottomMainBinding
import com.ugm.kaskita.utils.Validation.isEmpty
import com.ugm.kaskita.utils.Validation.itText

private const val TYPE = "type"

class BottomMainFragment : SuperBottomSheetFragment() {
    private var type: String? = null
    private val binding: FragmentBottomMainBinding by viewBinding()
    lateinit var parent: BottomSheet



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (type == "in") {
            binding.tvTitle.text = resources.getString(R.string.title_saldo_masuk)
            binding.tvSumber.text = resources.getString(R.string.title_sumber_pemasukan)
            binding.etSumber.hint = resources.getString(R.string.dec_sumbar_pemasukan)
        } else {
            binding.tvTitle.text = resources.getString(R.string.title_saldo_keluar)
            binding.tvSumber.text = resources.getString(R.string.title_sumber_pengeluaran)
            binding.etSumber.hint = resources.getString(R.string.dec_sumbar_pengeluaran)
        }

        binding.btnSubmit.onClick {
            when {
                binding.etSumber.isEmpty() -> {
                    binding.etSumber.error = resources.getString(R.string.title_error_et)
                }

                binding.etNominal.isEmpty() -> {
                    binding.etNominal.error = resources.getString(R.string.title_error_et)
                }

                else -> {
                    parent.onSubmit(
                        DataSaldo(
                            binding.etSumber.itText(),
                            binding.etNominal.itText().toLong(),
                            type
                        )
                    )
                    dismiss()
                }
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        parent.onDismiss()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        parent = context as BottomSheet
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            type = it.getString(TYPE)
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun getExpandedHeight(): Int {
        return -2
    }

    override fun isSheetAlwaysExpanded(): Boolean {
        return true
    }

    companion object {
        @JvmStatic
        fun newInstance(type: String) =
            BottomMainFragment().apply {
                arguments = Bundle().apply {
                    putString(TYPE, type)
                }
            }
    }

    interface BottomSheet {
        fun onDismiss()
        fun onSubmit(data: DataSaldo)
    }
}